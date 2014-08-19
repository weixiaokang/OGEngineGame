package com.orange.block.entity;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.orange.entity.group.EntityGroup;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.primitive.Rectangle;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.entity.sprite.ButtonSprite;
import com.orange.entity.sprite.ButtonSprite.OnClickListener;
import com.orange.entity.text.Text;
import com.orange.res.FontRes;
import com.orange.util.color.Color;
import com.orange.block.control.TimerTool;
import com.orange.block.res.Res;
import com.orange.block.scene.GameScene;
import com.orange.block.util.ConstantUtil;
import com.orange.block.util.SharedUtil;

/**
 * 失败界面
 * 
 * @author lch
 * 
 */
public class FailGroup extends EntityGroup {

	private GameScene mGameScene;
	private Rectangle bgRect;

	private AnimatedSprite titelSprite;
	private AnimatedSprite modelSprite;

	private ButtonSprite btn_back; // 返回按钮
	private ButtonSprite btn_again;// 重来按钮

	private Text txt_big;
	private Text txt_small;

	public FailGroup(float pWidth, float pHeight, GameScene pGameScene) {
		super(pWidth, pHeight, pGameScene);
		this.mGameScene = pGameScene;
		initView();
	}

	private void initView() {
		// 背景
		bgRect = new Rectangle(0, 0, this.getWidth(), this.getHeight(),
				this.getVertexBufferObjectManager());
		this.attachChild(bgRect);
		bgRect.setColor(Color.RED);
		// 标题 “别踩白块儿”
		titelSprite = new AnimatedSprite(0, 10, Res.GAME_TITEL,
				this.getVertexBufferObjectManager());
		titelSprite.setRightPositionX(this.getRightX() - 10);
		this.attachChild(titelSprite);

		// 模式 “经典模式”
		modelSprite = new AnimatedSprite(0, 150, Res.GAME_MODEL,
				this.getVertexBufferObjectManager());
		modelSprite.setCentrePositionX(this.getCentreX());
		this.attachChild(modelSprite);

		// 大字
		txt_big = new Text(0, modelSprite.getBottomY() + 130,
				FontRes.getFont(ConstantUtil.FONT_NAME_RESULT), "", 15,
				this.getVertexBufferObjectManager());
		this.attachChild(txt_big);
		txt_big.setScale(1.9f);
		// 小字
		txt_small = new Text(0, txt_big.getBottomY() + 30,
				FontRes.getFont(ConstantUtil.FONT_NAME_RESULT), "", 15,
				this.getVertexBufferObjectManager());
		this.attachChild(txt_small);
		txt_small.setScale(0.7f);
		// 返回按钮
		btn_back = new ButtonSprite(80, 0, Res.BTN_BACK,
				this.getVertexBufferObjectManager());
		btn_back.setBottomPositionY(this.getBottomY() - 50);
		btn_back.setIgnoreTouch(false);
		this.attachChild(btn_back);
		btn_back.setOnClickListener(onClickListener);
		// 重来按钮
		btn_again = new ButtonSprite(0, 0, Res.BTN_AGAIN,
				this.getVertexBufferObjectManager());
		btn_again.setRightPositionX(this.getRightX() - 80);
		btn_again.setBottomPositionY(this.getBottomY() - 50);
		btn_again.setIgnoreTouch(false);
		this.attachChild(btn_again);
		btn_again.setOnClickListener(onClickListener);
		
		resetView();
	}
	
	

	public void showView() {
		setBtnEnable(true);
		this.setVisible(true);
		updateBigTxt("失败了！");

		if (SharedUtil.getRecord(getActivity()) > 0) {
			TimerTool mTimerTool = mGameScene.getmTimerTool();
			updateSmallTxt("最佳: "
					+ mTimerTool.millisToTimer(SharedUtil
							.getRecord(getActivity())));
		}
		
		ScaleModifier scaleModifier = new ScaleModifier(0.2f, 0.0f, 1.0f);
		this.registerEntityModifier(scaleModifier);

	}

	private void updateBigTxt(String pText) {
		txt_big.setText(pText);
		txt_big.setCentrePositionX(this.getCentreX());
	}

	private void updateSmallTxt(String pText) {
		txt_small.setText(pText);
		txt_small.setCentrePositionX(this.getCentreX());
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
				float pTouchAreaLocalY) {
			if (btn_back == pButtonSprite) {
				showDialog();
			} else if (btn_again == pButtonSprite) {
				resetView();
				mGameScene.resetGame();
			}

		}

	};
	
	private void resetView(){
		this.setScale(0);
		this.setVisible(false);
		setBtnEnable(false);
	}

	private void setBtnEnable(boolean isEnable) {
		btn_back.setEnabled(isEnable);
		btn_again.setEnabled(isEnable);
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
}
