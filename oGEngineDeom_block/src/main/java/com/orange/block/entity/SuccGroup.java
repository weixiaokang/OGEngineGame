package com.orange.block.entity;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.orange.entity.group.EntityGroup;
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
 * 成功界面
 * 
 * @author lch
 * 
 */
public class SuccGroup extends EntityGroup {

	private GameScene mGameScene;
	private Rectangle bgRect;

	private AnimatedSprite titelSprite;
	private AnimatedSprite modelSprite;

	private ButtonSprite btn_back; // 返回按钮
	private ButtonSprite btn_again;// 重来按钮

	private Text txt_big;
	private Text txt_small;

	public SuccGroup(float pWidth, float pHeight, GameScene pGameScene) {
		super(pWidth, pHeight, pGameScene);
		initView();
		this.mGameScene = pGameScene;
	}

	private void initView() {
		// 背景
		bgRect = new Rectangle(0, 0, this.getWidth(), this.getHeight(),
				this.getVertexBufferObjectManager());
		this.attachChild(bgRect);
		bgRect.setColor(Color.GREEN);
		// 标题 “别踩白块儿”
		titelSprite = new AnimatedSprite(0, 10, Res.GAME_TITEL,
				this.getVertexBufferObjectManager());
		titelSprite.setRightPositionX(this.getRightX() - 10);
		this.attachChild(titelSprite);
		titelSprite.setVisible(false);

		// 模式 “经典模式”
		modelSprite = new AnimatedSprite(0, 150, Res.GAME_MODEL,
				this.getVertexBufferObjectManager());
		modelSprite.setCentrePositionX(this.getCentreX());
		this.attachChild(modelSprite);
		modelSprite.setVisible(false);

		// 大字
		txt_big = new Text(0, modelSprite.getBottomY() + 100,
				FontRes.getFont(ConstantUtil.FONT_NAME_RESULT), "", 15,
				this.getVertexBufferObjectManager());
		this.attachChild(txt_big);
		txt_big.setScale(1.5f);
		txt_big.setVisible(false);
		// 小字
		txt_small = new Text(0, txt_big.getBottomY() + 10,
				FontRes.getFont(ConstantUtil.FONT_NAME_RESULT), "", 15,
				this.getVertexBufferObjectManager());
		this.attachChild(txt_small);
		txt_small.setScale(0.7f);
		txt_small.setVisible(false);
		// 返回按钮
		btn_back = new ButtonSprite(80, 0, Res.BTN_BACK,
				this.getVertexBufferObjectManager());
		btn_back.setBottomPositionY(this.getBottomY() - 50);
		btn_back.setIgnoreTouch(false);
		this.attachChild(btn_back);
		btn_back.setOnClickListener(onClickListener);
		btn_back.setVisible(false);
		// 重来按钮
		btn_again = new ButtonSprite(0, 0, Res.BTN_AGAIN,
				this.getVertexBufferObjectManager());
		btn_again.setRightPositionX(this.getRightX() - 80);
		btn_again.setBottomPositionY(this.getBottomY() - 50);
		btn_again.setIgnoreTouch(false);
		this.attachChild(btn_again);
		btn_again.setOnClickListener(onClickListener);
		btn_again.setVisible(false);
	}

	public void showItems(boolean pVisible) {
		titelSprite.setVisible(pVisible);
		modelSprite.setVisible(pVisible);
		txt_big.setVisible(pVisible);
		txt_small.setVisible(pVisible);
		btn_back.setVisible(pVisible);
		btn_again.setVisible(pVisible);

		if (pVisible) {
			TimerTool mTimerTool = mGameScene.getmTimerTool();
			updateBigTxt(mTimerTool.millisToTimer(mTimerTool
					.getMillisPass()));

			long mMillisPass = mTimerTool.getMillisPass();

			if (mMillisPass < SharedUtil.getRecord(getActivity())
					|| SharedUtil.getRecord(getActivity()) == 0) {
				// 新记录
				updateSmallTxt("新记录");
				SharedUtil.setRecord(getActivity(), mMillisPass);

			} else {

				updateSmallTxt("最佳: "
						+ mTimerTool.millisToTimer(SharedUtil
								.getRecord(getActivity())));
			}
		}

	}

	public void updateBigTxt(String pText) {
		txt_big.setText(pText);
		txt_big.setCentrePositionX(this.getCentreX());
	}

	public void updateSmallTxt(String pText) {
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
				mGameScene.resetGame();
			}

		}
	};

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
