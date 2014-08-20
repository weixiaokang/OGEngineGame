package com.example.ogenginedemo_plane.scene;

import android.util.Log;

import com.orange.content.SceneBundle;
import com.orange.engine.handler.IUpdateHandler;
import com.orange.entity.scene.Scene;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.input.touch.TouchEvent;
import com.orange.input.touch.detector.ScrollDetector;
import com.orange.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import com.orange.input.touch.detector.SurfaceScrollDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.orange.engine.handler.timer.ITimerCallback;
import com.orange.engine.handler.timer.TimerHandler;
import com.orange.entity.sprite.AnimatedSprite.IAnimationListener;
import com.orange.entity.sprite.ButtonSprite;
import com.orange.entity.sprite.ButtonSprite.OnClickListener;
import com.orange.entity.text.Text;
import com.orange.res.FontRes;
import com.orange.res.MusicRes;
import com.orange.res.SoundRes;
import com.example.ogenginedemo_plane.Res.Res;
import com.example.ogenginedemo_plane.entity.BoomBulletPackage;
import com.example.ogenginedemo_plane.entity.Enemy;
import com.example.ogenginedemo_plane.util.ConstantUtil;
import com.example.ogenginedemo_plane.entity.OverLayer;
import com.example.ogenginedemo_plane.entity.Bullet;

public class GameScene extends Scene implements IScrollDetectorListener{

	//敌机列表
	private List<Enemy> EnemyList;
	//子弹列表
	private List<Bullet> BulletList;
	//弹药包列表
	private List<BoomBulletPackage> packageList;
	//当前分数
	private int currScore;
	//当前得分文本
	private Text tCurrScore;
	//暂停按钮
	private ButtonSprite pause;
	//使用炸弹按钮
	private ButtonSprite useBomb;
	//当前炸弹数
	private int currBombNum;
	//当前炸弹数文本
	private Text tCurrBombNum;
			
	private TimerHandler mTimerHandler_enemy;
	
	private TimerHandler mTimerHandler_bullet_creat;
	
	private TimerHandler mTimerHandler_bullet_change;
	
	private TimerHandler mTimerHandler_package_creat;
	
	//敌机生成时间
	private float pTimerSeconds_enemy = 2.0f;
	//子弹生成时间
	private float pTimerSeconds_bullet_create = 0.2f;
	//强效子弹持续时间
	private float pTimerSeconds_bullet_change = 10.0f;
	//弹药包生成时间
	private float pTimerSeconds_package_creat = 20.0f;
	
	//按概率生成敌机
	private int enemyChance; 

	// 游戏结束层
	private OverLayer mOverLayer;
	//子弹种类
	private int bullet_type=0;
	
	//我方飞机
	AnimatedSprite myPlane;
	
	//第一背景
	AnimatedSprite game_bg_1;
	//第二背景
	AnimatedSprite game_bg_2;
	
	SurfaceScrollDetector mScrollDetector;

	@Override
	public void onSceneCreate(SceneBundle bundle) {
		super.onSceneCreate(bundle);
		this.setIgnoreTouch(false);
		mScrollDetector = new SurfaceScrollDetector(this);
				
		this.setWidth(this.getCameraWidth());
		this.setHeight(this.getCameraHeight());
		
		this.EnemyList = new ArrayList<Enemy>();
		this.BulletList =new ArrayList<Bullet>();
		this.packageList=new ArrayList<BoomBulletPackage>();		
		
		initTimerHander();
		initView();
		this.registerUpdateHandler(updateHandler);
		this.registerUpdateHandler(mTimerHandler_bullet_creat);
		this.registerUpdateHandler(mTimerHandler_package_creat);
		this.registerUpdateHandler(mTimerHandler_enemy);
	}
	
	@Override
	public void onSceneResume() {
		super.onSceneResume();
		//LogUtil.d("GameScene----onSceneResume："+getEngine().isRunning());
	}
	
	@Override
	public void onScenePause() {
		super.onScenePause();
		//LogUtil.d("GameScene----onScenePause");
		
	}
	
	@Override
	public void onSceneDestroy() {
		super.onSceneDestroy();
		//LogUtil.d("GameScene----onSceneDestroy");
	}

	private void initView() {
		
		game_bg_1 = new AnimatedSprite(0, 0, Res.BG,
		getVertexBufferObjectManager());
		game_bg_1.setRightPositionX(this.getRightX());
		game_bg_1.setBottomPositionY(this.getBottomY());
		this.attachChild(game_bg_1);
		
		game_bg_2 = new AnimatedSprite(0, 0, Res.BG,
		getVertexBufferObjectManager());
		game_bg_2.setRightPositionX(this.getRightX());
		game_bg_2.setBottomPositionY(this.getTopY()+10);
		this.attachChild(game_bg_2);
		
		myPlane = new AnimatedSprite(200, 500, Res.PLANE,
				getVertexBufferObjectManager());
		this.attachChild(myPlane);

		tCurrScore = new Text(0, 20, FontRes.getFont(ConstantUtil.FONT_SCORE_NUM), "0", 9, getVertexBufferObjectManager());
		tCurrScore.setCentrePositionX(this.getCentreX());
		tCurrScore.setColor(0,0,0);
		currScore=0;
		this.attachChild(tCurrScore);
		
		tCurrBombNum = new Text(100,0,FontRes.getFont(ConstantUtil.FONT_SCORE_NUM), "0", 9, getVertexBufferObjectManager());
		tCurrBombNum.setBottomPositionY(this.getBottomY()-20);
		currBombNum=0;
		this.attachChild(tCurrBombNum);
		
		pause=new ButtonSprite(0, 0, Res.STOP, getVertexBufferObjectManager());
		pause.setTopPositionY(this.getTopY()+20);
		pause.setIgnoreTouch(false);
		pause.setOnClickListener(onClickListener);
		this.attachChild(pause);
		
		useBomb=new ButtonSprite(0,0,Res.BT_BOMB,getVertexBufferObjectManager());
		useBomb.setBottomPositionY(this.getBottomY()-20);
		useBomb.setIgnoreTouch(false);
		useBomb.setOnClickListener(onClickListener);
		this.attachChild(useBomb);

		mOverLayer = new OverLayer(this.getCameraWidth(), this.getCameraHeight(), this);
		
		MusicRes.playMusic(ConstantUtil.SOUND_GAME_MUSIC, true);
	
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
				@Override
				public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
						float pTouchAreaLocalY) {
					if (pButtonSprite == pause) {
						// 点击了帮助按钮
						showDialog();
					}
					else if(pButtonSprite == useBomb) {
						if(currBombNum >0) {
							currBombNum--;
							tCurrBombNum.setText(currBombNum+"");
							usebomb();
						}						
					}
					else
						;		
				}
			};
			
			
	private void usebomb() {
		SoundRes.playSound(ConstantUtil.SOUND_USE_BOMB);
		Enemy enemySprite_boom = null;
		for(int i=0;i<EnemyList.size();i++) {
			enemySprite_boom = EnemyList.get(i);
			PlaneBoom(enemySprite_boom,2);		
			updateScore(enemySprite_boom.getScore());
			EnemyList.remove(i);	
			i--;//下一架飞机已经移动到列表的第i个位置
		}
	}		

	private void initTimerHander() {

		// TODO Auto-generated method stub
		mTimerHandler_enemy = new TimerHandler(pTimerSeconds_enemy, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						Log.i("tag", "initTimerHander_enemy");
						enemyChance =(int)(Math.random()*50);
						int pX = new Random().nextInt(480);
						if (0 <=enemyChance && enemyChance <20)
							createEnemy((int) (pX%(431)+25), 0);
						else if (20 <=enemyChance && enemyChance <40)
							createEnemy((int) (pX%(411)+35), 1);
						else
							createEnemy((int) (pX%(396)+85), 2);
					}
				});
		mTimerHandler_bullet_creat = new TimerHandler(pTimerSeconds_bullet_create, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						Log.i("tag", "initTimerHander_bullet");						
						createBullet();
					}	
				});
		
		mTimerHandler_bullet_change = new TimerHandler(pTimerSeconds_bullet_change, false,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						bullet_type=0;												
					}	
				});
		
		mTimerHandler_package_creat = new TimerHandler(pTimerSeconds_package_creat, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						creatPackage();												
					}					
				});
				
	}
	
	float bg_speed = 120;
	private void bgmove(float pSecondsElapsed) {
		game_bg_1.setPositionY(game_bg_1.getY()+bg_speed*pSecondsElapsed);
		game_bg_2.setPositionY(game_bg_2.getY()+bg_speed*pSecondsElapsed);
		if(game_bg_1.getTopY()>=this.getBottomY()) {
			game_bg_1.setBottomPositionY(this.getBottomY());
			game_bg_2.setBottomPositionY(this.getTopY()+10);
		}
	}
	
	/**
	 * 生成弹药包
	 * 
	 */
	
	private void creatPackage() {
						// TODO Auto-generated method stub
		BoomBulletPackage boombulletpackage=null;
		switch ((int)(Math.random()*2)) {
		case 0:
			boombulletpackage = new BoomBulletPackage((int)(Math.random()*(ConstantUtil.DESIRED_SIZE)), 0, Res.BULLET, 0, getVertexBufferObjectManager());
			break;
		case 1:
			boombulletpackage = new BoomBulletPackage((int)(Math.random()*(ConstantUtil.DESIRED_SIZE)), 0, Res.BOMB, 1, getVertexBufferObjectManager());
			break;
		}	
		packageList.add(boombulletpackage);
		this.attachChild(boombulletpackage);
	}	
	
	/**
	 * 生成子弹
	 * 
	 */
	
	private void createBullet() {
						// TODO Auto-generated method stub
		Bullet bullet =null;
		switch (bullet_type) {
			case 0:
				bullet = new Bullet(myPlane.getCentreX(),myPlane.getY(),Res.BULLET1,0,getVertexBufferObjectManager());
				break;
			case 1:
				bullet = new Bullet(myPlane.getCentreX(),myPlane.getY(),Res.BULLET2,1,getVertexBufferObjectManager());
				break;
		}		
		bullet.setPass(false);
		bullet.setHit(false);
		this.attachChild(bullet);
		SoundRes.playSound(ConstantUtil.SOUND_BULLET);
		BulletList.add(bullet);								
	}
	
	/**
	 * 随机生成一架敌机
	 * @param pX敌机的出现位置，type敌机的种类
	 */
	private void createEnemy(int pX,int type) {
		float pY = 0;
		Enemy enemy = null;
		switch (type) {
			case 0:
				enemy = new Enemy(pX, pY, Res.SENEMY, 0,
						getVertexBufferObjectManager());
				enemy.setType(0);
				enemy.setPass(false);
				break;
				
			case 1:
				enemy = new Enemy(pX, pY, Res.MENEMY, 1,
						getVertexBufferObjectManager());
				enemy.setType(1);
				enemy.setPass(false);
				break;
			case 2:
				enemy = new Enemy(pX, pY, Res.BENEMY, 2,
						getVertexBufferObjectManager());
				enemy.setType(2);
				enemy.setPass(false);
				break;
		}
				
		this.attachChild(enemy);
		
		EnemyList.add(enemy);	
		
	}

	public void restartGame() {

		SoundRes.playSound(ConstantUtil.SOUND_COUNTDOWN);
		this.registerUpdateHandler(mTimerHandler_bullet_creat);
		this.registerUpdateHandler(mTimerHandler_package_creat);
		this.registerUpdateHandler(mTimerHandler_enemy);
		
		myPlane.setVisible(true);;
		this.detachChild(mOverLayer);
		this.currScore = 0;
		updateScore(currScore);
		this.currBombNum = 0;
		this.bullet_type=0;
		tCurrBombNum.setText(currBombNum+"");
		this.myPlane.stopAnimation(0);		
		
		//清空原先敌机
		for (Enemy enemy : EnemyList) {
			enemy.detachSelf();
			enemy.dispose();
		}
		this.EnemyList.clear();				
		
		// 播放音效
		MusicRes.playMusic(ConstantUtil.SOUND_GAME_MUSIC, true);
	}
	
	private void updateScore(int currScore2) {
		// TODO Auto-generated method stub
		//转变为char类型
		currScore+=currScore2;
		tCurrScore.setText(currScore+"");
		tCurrScore.setCentrePositionX(this.getCentreX());			
	}
	
	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		mScrollDetector.onTouchEvent(pSceneTouchEvent);
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		myPlane.setPosition(myPlane.getX()+pDistanceX, myPlane.getY()+pDistanceY);
		if(myPlane.getRightX()>this.getWidth()) 
			myPlane.setRightPositionX(this.getWidth());
		if(myPlane.getLeftX()<0)
			myPlane.setLeftPositionX(0);
		if(myPlane.getTopY()<0)
			myPlane.setTopPositionY(0);
		if(myPlane.getBottomY()>this.getHeight())
			myPlane.setBottomPositionY(this.getHeight());		
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub
		
	}
	
	Enemy enemySprite;
	
	Bullet bulletSprite;
	
	BoomBulletPackage boombulletpackageSprite; 
	
	private IUpdateHandler updateHandler = new IUpdateHandler() {
		
		@Override
		public void reset() {
			
		}
		
		
		@Override
		public void onUpdate(float pSecondsElapsed) {
			/** 背景移动**/
			bgmove(pSecondsElapsed);
			
			/** 弹药包碰撞检测 **/
			for(int i=0;i<packageList.size();i++) {
				boombulletpackageSprite = packageList.get(i);
				if(myPlane.collidesWith(boombulletpackageSprite)) {
					switch(boombulletpackageSprite.getType()) {
						case 0:							
							SoundRes.playSound(ConstantUtil.SOUND_GET_DOUBLE_LASER);
							bullet_type=1;							
							mTimerHandler_bullet_change.reset();
							registerUpdateHandler(mTimerHandler_bullet_change);	
							break;
						case 1:
							SoundRes.playSound(ConstantUtil.SOUND_GET_BOMB);
							if (currBombNum < 3)
								currBombNum++;
							tCurrBombNum.setText(currBombNum+"");							
							break;
					}
					//删除吃掉的炸药包
					detachChild(boombulletpackageSprite);
					packageList.remove(i);
				}
			}
			/** 敌机与我机碰撞检测以及子弹与敌机的碰撞检测**/
			for(int i=0;i<EnemyList.size();i++) {
				enemySprite= EnemyList.get(i);
				if(!enemySprite.IsPass()) {
					if(myPlane.collidesWith(enemySprite)) {
						PlaneBoom(myPlane,1);
						PlaneBoom(enemySprite,2);													
						EnemyList.remove(i);
						enemySprite.PlaySound();	
						SoundRes.playSound(ConstantUtil.SOUND_GAME_OVER);
						
						unregisterUpdateHandler(mTimerHandler_enemy);
						unregisterUpdateHandler(mTimerHandler_package_creat);
						unregisterUpdateHandler(mTimerHandler_bullet_creat);	
						
						if(!mOverLayer.hasParent()){
							attachChild(mOverLayer);
						}						
						
						i--;//下一架敌机已经移动到列表第i个位置
						
						//已经碰撞的飞机不在做子弹碰撞检测与边界检测						
						continue;
					}
				}
				
				for (int j=0;j<BulletList.size();j++) {
					bulletSprite = BulletList.get(j);
					if(!bulletSprite.IsPass() && !bulletSprite.IsHit()) {
						if(bulletSprite.collidesWith(enemySprite)) {
							bulletSprite.setHit(true);
							detachChild(bulletSprite);
							bulletSprite.dispose();	
							BulletList.remove(j);
							j--;//下一颗子弹已经移动到第j个位置
							switch (bulletSprite.getType()) {
								case 0:
									enemySprite.setBlood(enemySprite.getBlood()-25);
									break;
								case 1:
									enemySprite.setBlood(enemySprite.getBlood()-50);
									break;
							}							
							if(enemySprite.getBlood()<=0) {
								updateScore(enemySprite.getScore());
								PlaneBoom(enemySprite,2);
								enemySprite.PlaySound();
								EnemyList.remove(i);
								i--;//下一架飞机已经移动到第i个位置
							}
						}
						
						//已经碰撞的子弹不在做边界检测
						if(bulletSprite.IsHit()) 
							continue;						
						//没有碰撞的子弹做碰撞检测
						if(bulletSprite.getBottomY()<0) {
							bulletSprite.setPass(true);
							detachChild(bulletSprite);
							bulletSprite.dispose();	
							BulletList.remove(j);
							j--;//下一颗子弹已经移动到第j个位置
						}
					}
				}
				
				//已经爆炸的飞机不在做边界检测
				if(enemySprite.getBlood()<=0)
					continue;
				
				// 将移出了下边镜头的敌方飞机删除
				if (enemySprite.getBottomY() > getCameraHeight()) {
					enemySprite.setPass(true);
					detachChild(enemySprite);
					enemySprite.dispose();	
					EnemyList.remove(i);
					i--;//下一架飞机已经移动到第i个位置
				}
			}
			
	}		
};
	
	private void PlaneBoom (final AnimatedSprite Plane,final int type) {
	
	Plane.animate(100, false, new IAnimationListener(){

		@Override
		public void onAnimationFinished(AnimatedSprite arg0) {
			// TODO Auto-generated method stub
			if(type==2) {
				//敌机爆炸
				detachChild(Plane);
				Plane.dispose();	
				Plane.setVisible(false);				
			}
			else {
				//我机爆炸
				Plane.setVisible(false);
			}
		}

		@Override
		public void onAnimationFrameChanged(AnimatedSprite arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationLoopFinished(AnimatedSprite arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStarted(AnimatedSprite arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}});		
	}

	//退出游戏提示框
	private void showDialog() {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				new AlertDialog.Builder(getActivity())
						.setTitle("退出游戏")
						.setMessage("是否要退出游戏！")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										SoundRes.playSound(ConstantUtil.SOUND_BUTTON);
										getActivity().finish();
										System.exit(0);
									}
								}).setNegativeButton("取消", null).show();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			showDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}


