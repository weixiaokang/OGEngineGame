package com.orange.game2048.scene;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.orange.content.SceneBundle;
import com.orange.entity.scene.Scene;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.entity.sprite.ButtonSprite;
import com.orange.entity.sprite.ButtonSprite.OnClickListener;
import com.orange.entity.text.Text;
import com.orange.game2048.entity.GameGroup;
import com.orange.game2048.entity.HelpLayer;
import com.orange.game2048.res.Res;
import com.orange.game2048.util.ConstantUtil;
import com.orange.game2048.util.SharedUtil;
import com.orange.input.touch.TouchEvent;
import com.orange.res.FontRes;
import com.orange.res.SoundRes;

/**
 * 游戏场景
 * 
 * @author lch
 * 
 */
public class GameScene extends Scene {
	// 游戏主体部分界面及逻辑
	private GameGroup mGameGroup;
	// 当前分数
	private int currScore = 0;
	// 最佳得分背景
	private AnimatedSprite bestScoreBg;
	// 最佳得分文本
	private Text tBestScore;
	// 当前得分背景
	private AnimatedSprite currScoreBg;
	// 当前分数文本
	private Text tCurrScore;
	// 游戏帮助按钮
	private ButtonSprite btnHelp;
	// 退出游戏按钮
	private ButtonSprite btnExit;

	// 帮助层
	private HelpLayer helpLayer;

	@Override
	public void onSceneCreate(SceneBundle bundle) {
		super.onSceneCreate(bundle);

		initView();
	}

	private void initView() {
		// 游戏背景
		AnimatedSprite game_bg = new AnimatedSprite(0, 0, Res.GAME_BG,
				getVertexBufferObjectManager());
		this.attachChild(game_bg);
		// 中间游戏主体部分
		mGameGroup = new GameGroup(this);
		// 设置改Group的中心位置在镜头的中心点上
		mGameGroup.setCentrePosition(this.getCameraCenterX(),
				this.getCameraCenterY());
		this.attachChild(mGameGroup);

		// 2048 LOGO
		AnimatedSprite game_logo = new AnimatedSprite(20, 20, Res.GAME_LOGO,
				getVertexBufferObjectManager());
		this.attachChild(game_logo);

		// 最佳得分背景
		bestScoreBg = new AnimatedSprite(0, 20, Res.GAME_SCORE_BG_BEST,
				getVertexBufferObjectManager());
		// 设置bestScoreBg右边x坐标的位置在镜头的右边减20的位置
		bestScoreBg.setRightPositionX(this.getCameraRightX() - 20);
		this.attachChild(bestScoreBg);
		// 最佳得分文本
		tBestScore = new Text(0, bestScoreBg.getY() + 50,
				FontRes.getFont(ConstantUtil.FONT_SCORE_NUM),
				SharedUtil.getBestScore(getActivity()) + "", 4,
				getVertexBufferObjectManager());
		// 设置 tBestScore 的X坐标上的中点在bestScoreBg的X坐标中点上
		tBestScore.setCentrePositionX(bestScoreBg.getCentreX());
		this.attachChild(tBestScore);

		// 当前得分背景
		currScoreBg = new AnimatedSprite(0, bestScoreBg.getY(),
				Res.GAME_SCORE_BG_NOW, getVertexBufferObjectManager());
		// 设置currScoreBg的右边X坐标点在bestScoreBg左边的X坐标减20的位置上
		currScoreBg.setRightPositionX(bestScoreBg.getLeftX() - 20);
		this.attachChild(currScoreBg);
		// 当前得分文本
		tCurrScore = new Text(0, currScoreBg.getY() + 50,
				FontRes.getFont(ConstantUtil.FONT_SCORE_NUM), "0", 4,
				getVertexBufferObjectManager());
		tCurrScore.setCentrePositionX(currScoreBg.getCentreX());
		this.attachChild(tCurrScore);

		// 帮助按钮
		btnHelp = new ButtonSprite(10, 0, Res.GAME_BTN_HELP,
				getVertexBufferObjectManager());
		btnHelp.setBottomPositionY(this.getCameraBottomY() - 20);
		btnHelp.setIgnoreTouch(false);
		btnHelp.setOnClickListener(onClickListener);
		this.attachChild(btnHelp);

		// 退出按钮
		btnExit = new ButtonSprite(0, btnHelp.getY(), Res.GAME_BTN_EXIT,
				getVertexBufferObjectManager());
		btnExit.setRightPositionX(this.getCameraRightX() - 10);
		btnExit.setIgnoreTouch(false);
		btnExit.setOnClickListener(onClickListener);
		this.attachChild(btnExit);

		// 帮助层
		helpLayer = new HelpLayer(this);

	}

	/**
	 * 更新最高纪录
	 * 
	 * @param pBestScore
	 */
	private void updateBestScore(int pBestScore) {
		tBestScore.setText(pBestScore + "");
		// 设置相对bestScoreBgX坐标居中
		tBestScore.setCentrePositionX(bestScoreBg.getCentreX());
	}

	/**
	 * 增加当前分数
	 * 
	 * @param pAddScore
	 *            所增加的分数
	 */
	public void addCurrScore(int pAddScore) {
		if (pAddScore != 0) {
			// 播放音效
			SoundRes.playSound(ConstantUtil.SOUND_MERGE);
		}
		currScore += pAddScore;
		tCurrScore.setText(currScore + "");
		tCurrScore.setCentrePositionX(currScoreBg.getCentreX());

		// 当前分数大于所保存的最佳分数时，更新最佳分数
		if (currScore > SharedUtil.getBestScore(getActivity())) {
			SharedUtil.setBestScore(getActivity(), currScore);
			updateBestScore(currScore);
		}
	}

	/**
	 * 清除当前分数
	 */
	public void clearScore() {
		currScore = 0;
		addCurrScore(0);
	}

	/**
	 * 按钮点击监听
	 */
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
				float pTouchAreaLocalY) {
			if (pButtonSprite == btnHelp) {
				// 点击了帮助按钮
				attachChild(helpLayer);
			} else if (pButtonSprite == btnExit) {
				// 点击了退出游戏按钮
				showDialog();
			}

		}
	};

	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		// 如果打开了帮助层，点击场景任意位置即把它关掉
		if (helpLayer.hasParent()) {
			helpLayer.detachSelf();
		}
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (helpLayer.hasParent()) {
				helpLayer.detachSelf();
			} else {
				showDialog();
			}
			return true;
		}
		return false;
	}

	/**
	 * 退出游戏确认对话框
	 */
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
										getActivity().finish();

									}
								}).setNegativeButton("取消", null).show();
			}
		});

	}

	// ================================================================
	/**
	 * 获取镜头的 右边 x 坐标
	 */
	public float getCameraRightX() {
		return this.getCameraWidth();
	}

	/**
	 * 获取镜头的 中点 x 坐标
	 * 
	 * @return
	 */
	public float getCameraCenterX() {
		return this.getCameraWidth() * 0.5f;
	}

	/**
	 * 获取镜头底部Y坐标
	 * 
	 * @return
	 */
	public float getCameraBottomY() {
		return this.getCameraHeight();
	}

	/**
	 * 获取镜头中心Y坐标
	 * 
	 * @return
	 */
	public float getCameraCenterY() {
		return this.getCameraHeight() * 0.5f;
	}
}
