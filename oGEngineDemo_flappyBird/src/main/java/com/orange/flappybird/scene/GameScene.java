package com.orange.flappybird.scene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.orange.content.SceneBundle;
import com.orange.engine.handler.IUpdateHandler;
import com.orange.engine.handler.timer.ITimerCallback;
import com.orange.engine.handler.timer.TimerHandler;
import com.orange.entity.scene.MatchScene;
import com.orange.entity.shape.IShape;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.entity.text.Text;
import com.orange.flappybird.entity.FloorSprite;
import com.orange.flappybird.entity.BarSprite;
import com.orange.flappybird.entity.BirdSprite;
import com.orange.flappybird.entity.OverLayer;
import com.orange.flappybird.res.Res;
import com.orange.flappybird.util.CollisionUtil;
import com.orange.flappybird.util.IConstant;
import com.orange.flappybird.util.LogUtil;
import com.orange.input.touch.TouchEvent;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.FontRes;
import com.orange.res.SoundRes;

public class GameScene extends MatchScene implements IConstant {
	private VertexBufferObjectManager pVertexBufferObjectManager;
	private BirdSprite birdSprite;
	private PhysicsWorld mPhysicsWorld;
	private FloorSprite mFloorSprite;

	private int game_state = STATE_READY;

	private List<BarSprite> barSprites;

	private TimerHandler mTimerHandler;
	private float pTimerSeconds = 2.0f;
	
	private AnimatedSprite tapTipSprite;
	private AnimatedSprite getReadySprite;
	
	private Text scoreText;
	private int score = 1003782;
	
	private OverLayer mOverLayer;// 游戏结束层

	@Override
	public void onSceneCreate(SceneBundle bundle) {
		super.onSceneCreate(bundle);
		pVertexBufferObjectManager = getVertexBufferObjectManager();
		barSprites = new ArrayList<BarSprite>();
		initTimerHander();
		initView();
		initPhysics();
	
		LogUtil.d("GameScene----onSceneCreate");
	}
	
	@Override
	public void onSceneResume() {
		super.onSceneResume();
		LogUtil.d("GameScene----onSceneResume："+getEngine().isRunning());
	}
	
	@Override
	public void onScenePause() {
		super.onScenePause();
		LogUtil.d("GameScene----onScenePause");
	}
	
	@Override
	public void onSceneDestroy() {
		super.onSceneDestroy();
		LogUtil.d("GameScene----onSceneDestroy");
	}

	private void initView() {
		AnimatedSprite game_bg = new AnimatedSprite(0, 0, Res.GAME_BG_DAY,
				pVertexBufferObjectManager);
		game_bg.setCentrePositionY(this.getCentreY());
		this.attachChild(game_bg);

		mFloorSprite = new FloorSprite(0, 0, Res.GAME_FLOOR, 0,
				pVertexBufferObjectManager);
		mFloorSprite.setBottomPositionY(this.getBottomY());
		mFloorSprite.setZIndex(pZIndex_top);
		this.attachChild(mFloorSprite);

		birdSprite = new BirdSprite(120, 370, pVertexBufferObjectManager);
		birdSprite.setZIndex(pZIndex_middle);
		birdSprite.refreshState(game_state);
		birdSprite.setScale(1.2f);
		this.attachChild(birdSprite);
		
		tapTipSprite = new AnimatedSprite(162, 349, Res.GAME_TAP_TIP, pVertexBufferObjectManager);
		tapTipSprite.setCentrePosition(this.getCentreX(), this.getCentreY());
		this.attachChild(tapTipSprite);
		getReadySprite = new AnimatedSprite(0, 182, Res.GAME_READY, pVertexBufferObjectManager);
		getReadySprite.setCentrePositionX(this.getCentreX());
		this.attachChild(getReadySprite);

		// 分数文本
		scoreText = new Text(0, 50, FontRes.getFont(FONT_SCORE), "123456789", 9, pVertexBufferObjectManager);
		updateScore(score);
		scoreText.setZIndex(pZIndex_middle);
		this.attachChild(scoreText);
		
		mOverLayer = new OverLayer(this.getCameraWidth(), this.getCameraHeight(), this);
		mOverLayer.setZIndex(pZIndex_top);
		
		sortChildren();
	}

	private void initTimerHander() {
		mTimerHandler = new TimerHandler(pTimerSeconds, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						createBars(480);

					}
				});
	}
	
	/**
	 * 更新分数
	 * @param score
	 */
	private void updateScore(int score){
		scoreText.setText(score+"");
		scoreText.setCentrePositionX(this.getCentreX());
		
		if(game_state == STATE_FLY){
			// 播放音效
			SoundRes.playSound(SOUND_POINT);
		}
	}

	/**
	 * 创建一对bars
	 * @param pX
	 */
	private void createBars(float pX) {
		float pY = -new Random().nextInt(300);

		BarSprite upSprite = new BarSprite(pX, pY, Res.BAR_UP, true,
				pVertexBufferObjectManager);
		BarSprite downSprite = new BarSprite(pX, upSprite.getBottomY() + 165,
				Res.BAR_DOWN, false, pVertexBufferObjectManager);
		this.attachChild(upSprite);
		this.attachChild(downSprite);
		barSprites.add(upSprite);
		barSprites.add(downSprite);

		this.sortChildren();

	}

	/**
	 * 初始化物理效果
	 */
	private void initPhysics() {
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, 50), false);
		// 参数 ：密度、弹力、摩擦力
		FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.0f, 0.0f);
		
		Body birdBody = PhysicsFactory.createBoxBody(mPhysicsWorld, birdSprite,
				BodyType.DynamicBody, FIXTURE_DEF);
		birdSprite.setBody(birdBody);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(birdSprite,
				birdBody, true, true));
	}

	/**
	 * 开始游戏
	 */
	private void gameStart() {
		this.game_state = STATE_FLY;
		
		this.tapTipSprite.setVisible(false);
		this.getReadySprite.setVisible(false);
		
		this.birdSprite.refreshState(game_state);
		this.registerUpdateHandler(mPhysicsWorld);
		this.registerUpdateHandler(checkHandler);
		this.registerUpdateHandler(mTimerHandler);
		
		this.mFloorSprite.setParallaxChangePerSecond(-30);
	}
	
	/**
	 * 重新开始游戏
	 */
	public void restartGame(){
		this.game_state = STATE_READY;
		this.birdSprite.refreshState(game_state);
		this.detachChild(mOverLayer);
		this.score = 0;
		updateScore(score);
		this.birdSprite.setCollisionBar(false);
		this.birdSprite.setCollisionFloor(false);
		
		for (BarSprite barSprite : barSprites) {
			barSprite.detachSelf();
			barSprite.dispose();
		}
		this.barSprites.clear();
		
		this.tapTipSprite.setVisible(true);
		this.getReadySprite.setVisible(true);
		
		initPhysics();
		
		// 播放音效
		SoundRes.playSound(SOUND_SWOOSING);
	}

	/**
	 * 游戏结束
	 */
	private void gameOver() {
		LogUtil.w("碰撞了，游戏失败");
		
		game_state = STATE_DIE;
		unregisterUpdateHandler(mPhysicsWorld);
		unregisterUpdateHandler(checkHandler);
		birdSprite.refreshState(game_state);
		mOverLayer.updateScore(score);
		this.attachChild(mOverLayer);
		// 移除小鸟物理效果
		PhysicsConnector mPhysicsConnector = mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByEntity(birdSprite);
		mPhysicsWorld.unregisterPhysicsConnector(mPhysicsConnector);
		mPhysicsWorld.destroyBody(mPhysicsConnector.getBody());
		
		// 播放音效
		SoundRes.playSound(SOUND_DIE);
	}
	
	/**
	 * 停止水管的生成和移动
	 */
	private void barOver() {
		unregisterUpdateHandler(mTimerHandler);
		this.mFloorSprite.setParallaxChangePerSecond(0);
		// 水管停止移动
		for (BarSprite barSprite : barSprites) {
			barSprite.stopMove();
		}
	}

	/**
	 * 不停检测用的 handler
	 */
	private IUpdateHandler checkHandler = new IUpdateHandler() {

		@Override
		public void reset() {

		}

		@Override
		public void onUpdate(float pSecondsElapsed) {
			// LogUtil.d("onUpdate--->"+pSecondsElapsed);
			if (game_state == STATE_FLY) {
				// 小鸟旋转
				rotateBird();
				if (!birdSprite.isCollisionBar() && isCollidesWithBar()) {
					birdSprite.setCollisionBar(true);
					// 停止水管的生成和移动
					barOver();
					
					// 播放音效
					SoundRes.playSound(SOUND_HIT);
					
				}
				
				// 小鸟是否碰撞到地板或者水管
//				if (birdSprite.collidesWith(mFloorSprite) && !birdSprite.isCollisionFloor()) {
				if ( !birdSprite.isCollisionFloor()&&isContact(birdSprite, mFloorSprite)) {
					birdSprite.setCollisionFloor(true);
					
					if(!birdSprite.isCollisionBar()){
						// 停止水管的生成和移动
						barOver();
						// 播放音效
						SoundRes.playSound(SOUND_HIT);
					}
					// 游戏结束
					gameOver();
				}

				// 迭代器遍历bar集合
				Iterator<BarSprite> iterator = barSprites.iterator();
				while (iterator.hasNext()) {
					BarSprite barSprite = iterator.next();
					// 检测小鸟是否通过水管
					if(barSprite.isUpBar() && !barSprite.isPass()){
						if(barSprite.getCentreX()<birdSprite.getX()){
							barSprite.setPass(true);
							score++;
							updateScore(score);
						}
						
					}
					// 将移出了左边镜头的水管清除掉
					if (barSprite.getRightX() < 0) {
						detachChild(barSprite);
						barSprite.dispose();
						iterator.remove();
					}
				}

			}

		}


	};
	
	/**
	 * 自定义碰撞检测
	 * @param shapeA
	 * @param shapeB
	 * @return
	 */
	private boolean isContact(IShape shapeA, IShape shapeB) {

		float x1 = shapeA.getX()+3;
		float y1 = shapeA.getY()+3;
		float w1 = shapeA.getWidthScaled()-6;
		float h1 = shapeA.getHeightScaled()-6;

		float x2 = shapeB.getX();
		float y2 = shapeB.getY();
		float w2 = shapeB.getWidthScaled();
		float h2 = shapeB.getHeightScaled();

		return CollisionUtil.IsRectCollision(x1, y1, w1, h1, x2, y2, w2, h2);
	}
	
	/**
	 * 小鸟角度旋转逻辑
	 */
	private void rotateBird(){
		float speed_y = birdSprite.getBody().getLinearVelocity().y;
		
		float pRotation  = Math.min(Math.max(-30, (speed_y*4f)-45), 90);
		
		birdSprite.getBody().setRotation(pRotation);
	}

	/**
	 * 小鸟是否跟水管碰撞了
	 * 
	 * @return
	 */
	private boolean isCollidesWithBar() {
		for (BarSprite barSprite : barSprites) {

//			if (birdSprite.collidesWith(barSprite)) {
			if (isContact(birdSprite, barSprite)) {

				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {

			if (game_state == STATE_READY) {
				gameStart();
			}
			if (!birdSprite.isCollisionFloor() && !birdSprite.isCollisionBar()) {
				
				if(birdSprite.getY()>0){
					// 不能飞过顶部
					birdSprite.getBody().setLinearVelocity(0, -15);
					// 播放音效
					SoundRes.playSound(SOUND_WING);
				}
			}
		}
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			this.getActivity().finish();
			LogUtil.d("GameScene onKeyDown");
		}
		return true;
	}
}
