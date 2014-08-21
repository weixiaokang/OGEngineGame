package com.orange.game2048.entity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;

import com.orange.entity.IEntity;
import com.orange.entity.group.EntityGroup;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.modifier.SequenceEntityModifier;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.game2048.res.Res;
import com.orange.game2048.scene.GameScene;
import com.orange.game2048.util.ConstantUtil;
import com.orange.game2048.util.LogUtil;
import com.orange.input.touch.TouchEvent;
import com.orange.res.SoundRes;
import com.orange.util.modifier.ease.EaseBounceInOut;

/**
 * 游戏主体部分界面及逻辑
 * 
 * @author lch
 * 
 */
public class GameGroup extends EntityGroup {

	private GameScene mGameScene;
	/** 手指滑动的最小响应距离 **/
	private final static int FLING_MIN_DISTANCE = 10;
	/** 卡片之间的间隔 **/
	private static final float INTERVAL = 15;
	/** 卡片行列数量 **/
	private final static int mCount = 4;
	/** 卡片尺寸 **/
	private final static float CARD_SIZE = 90;

	/** 卡片数组 **/
	private CardGroup[][] cardArrays = new CardGroup[4][4];

	/** 用于标记还有哪些空的位置 **/
	private List<Point> emptyPoints = new ArrayList<Point>();
	/** 随机生成的数字2 **/
	private final static int mSamllNum = 2;
	/** 随机生成的数字4 **/
	private final static int mBignum = 4;

	public GameGroup(GameScene pGameScene) {
		super(0, 0, 435, 435, pGameScene);
		// 设置可以监听触碰事件
		this.setIgnoreTouch(false);
		this.mGameScene = pGameScene;
		initView();
	}

	private void initView() {
		// 创建背景
		AnimatedSprite rectBg = new AnimatedSprite(0, 0, Res.GAME_RECT_BG,
				this.getVertexBufferObjectManager());
		this.attachChild(rectBg);
		// 创建 4*4 单元格 卡片
		for (int row = 0; row < mCount; row++) {
			for (int column = 0; column < mCount; column++) {
				cardArrays[row][column] = new CardGroup((column + 1) * INTERVAL
						+ column * CARD_SIZE, (row + 1) * INTERVAL + row
						* CARD_SIZE, getScene());
				this.attachChild(cardArrays[row][column]);

			}
		}
		// 在随机一处剩余的空白单元随机生成数字，2或者4
		addRandomNum();
		addRandomNum();
	}

	private boolean mGrabbed = false;
	private float startX, startY, offsetX, offsetY;

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		if (pSceneTouchEvent.isActionDown()) {
			mGrabbed = true;
			startX = pSceneTouchEvent.getX();
			startY = pSceneTouchEvent.getY();

		} else if (pSceneTouchEvent.isActionUp()) {
			if (mGrabbed) {
				mGrabbed = false;
				offsetX = pSceneTouchEvent.getX() - startX;
				offsetY = pSceneTouchEvent.getY() - startY;

				if (Math.abs(offsetX) > Math.abs(offsetY)) {
					if (offsetX < -FLING_MIN_DISTANCE) {
						// 向左滑
						toLeft();
					} else if (offsetX > FLING_MIN_DISTANCE) {
						// 向右滑
						toRight();
					}
				} else {
					if (offsetY < -FLING_MIN_DISTANCE) {
						// 向上滑
						toUp();
					} else if (offsetY > FLING_MIN_DISTANCE) {
						// 向下滑
						toDown();
					}
				}
			}

		}
		return true;
	}

	private void toLeft() {
		LogUtil.d("左移");
		SoundRes.playSound(ConstantUtil.SOUND_SELECT);
		boolean mMerge = false;

		for (int x = 0; x < mCount; x++) {
			for (int y = 0; y < mCount; y++) {

				for (int y1 = y + 1; y1 < mCount; y1++) {
					if (cardArrays[x][y1].getNumber() > 0) {
						if (cardArrays[x][y].getNumber() <= 0) {
							cardArrays[x][y].setNumber(cardArrays[x][y1]
									.getNumber());
							cardArrays[x][y1].setNumber(0);

							mMerge = true;
							y--;
						} else if (cardArrays[x][y].equals(cardArrays[x][y1])) {
							cardArrays[x][y].setNumber(cardArrays[x][y]
									.getNumber() * 2);
							mergeAction(cardArrays[x][y]);
							cardArrays[x][y1].setNumber(0);
							// 添加当前分数
							mGameScene.addCurrScore(cardArrays[x][y]
									.getNumber());
							mMerge = true;
						}
						break;
					}
				}
			}
		}

		if (mMerge) {
			addRandomNum();
			checkComplete();
		}
	}

	private void toRight() {
		LogUtil.d("右移");
		SoundRes.playSound(ConstantUtil.SOUND_SELECT);
		boolean mMerge = false;

		for (int x = 0; x < mCount; x++) {
			for (int y = mCount - 1; y >= 0; y--) {

				for (int y1 = y - 1; y1 >= 0; y1--) {
					if (cardArrays[x][y1].getNumber() > 0) {
						if (cardArrays[x][y].getNumber() <= 0) {

							cardArrays[x][y].setNumber(cardArrays[x][y1]
									.getNumber());
							cardArrays[x][y1].setNumber(0);

							mMerge = true;
							y++;
						} else if (cardArrays[x][y].equals(cardArrays[x][y1])) {
							cardArrays[x][y].setNumber(cardArrays[x][y]
									.getNumber() * 2);
							mergeAction(cardArrays[x][y]);
							cardArrays[x][y1].setNumber(0);
							mGameScene.addCurrScore(cardArrays[x][y]
									.getNumber());
							mMerge = true;
						}
						break;
					}
				}
			}
		}

		if (mMerge) {
			addRandomNum();
			checkComplete();
		}
	}

	private void toUp() {
		LogUtil.d("上移");
		SoundRes.playSound(ConstantUtil.SOUND_SELECT);
		boolean mMerge = false;
		for (int y = 0; y < mCount; y++) {
			for (int x = 0; x < mCount; x++) {

				for (int x1 = x + 1; x1 < mCount; x1++) {
					if (cardArrays[x1][y].getNumber() > 0) {
						if (cardArrays[x][y].getNumber() <= 0) {
							cardArrays[x][y].setNumber(cardArrays[x1][y]
									.getNumber());
							cardArrays[x1][y].setNumber(0);
							mMerge = true;
							x--;
						} else if (cardArrays[x][y].equals(cardArrays[x1][y])) {
							cardArrays[x][y].setNumber(cardArrays[x][y]
									.getNumber() * 2);
							mergeAction(cardArrays[x][y]);
							cardArrays[x1][y].setNumber(0);
							mGameScene.addCurrScore(cardArrays[x][y]
									.getNumber());
							mMerge = true;
						}
						break;
					}
				}
			}
		}

		if (mMerge) {
			addRandomNum();
			checkComplete();
		}
	}

	private void toDown() {
		LogUtil.d("下移");
		SoundRes.playSound(ConstantUtil.SOUND_SELECT);
		boolean mMerge = false;

		for (int y = 0; y < mCount; y++) {
			for (int x = mCount - 1; x >= 0; x--) {

				for (int x1 = x - 1; x1 >= 0; x1--) {
					if (cardArrays[x1][y].getNumber() > 0) {
						if (cardArrays[x][y].getNumber() <= 0) {
							cardArrays[x][y].setNumber(cardArrays[x1][y]
									.getNumber());
							cardArrays[x1][y].setNumber(0);
							mMerge = true;
							x++;
						} else if (cardArrays[x][y].equals(cardArrays[x1][y])) {
							cardArrays[x][y].setNumber(cardArrays[x][y]
									.getNumber() * 2);
							mergeAction(cardArrays[x][y]);
							cardArrays[x1][y].setNumber(0);
							mGameScene.addCurrScore(cardArrays[x][y]
									.getNumber());
							mMerge = true;
						}
						break;
					}
				}
			}
		}

		if (mMerge) {
			addRandomNum();
			checkComplete();
		}
	}

	/**
	 * 合并时的动作效果
	 * 
	 * @param entity
	 */
	private void mergeAction(IEntity entity) {
		ScaleModifier scaleModifier1 = new ScaleModifier(0.1f, 0.8f, 1.2f,
				EaseBounceInOut.getInstance());
		ScaleModifier scaleModifier2 = new ScaleModifier(0.1f, 1.2f, 1.0f);
		SequenceEntityModifier sequenceEntityModifier = new SequenceEntityModifier(
				scaleModifier1, scaleModifier2);
		entity.registerEntityModifier(sequenceEntityModifier);
	}

	/**
	 * 在随机一处剩余的空白单元随机生成数字，2或者4
	 */
	private void addRandomNum() {
		// 播放音效
		SoundRes.playSound(ConstantUtil.SOUND_SETPOS);
		emptyPoints.clear();
		for (int x = 0; x < mCount; x++) {
			for (int y = 0; y < mCount; y++) {
				if (cardArrays[x][y].getNumber() <= 0) {
					emptyPoints.add(new Point(x, y));
				}
			}
		}

		Point p = emptyPoints
				.remove((int) (Math.random() * emptyPoints.size()));
		cardArrays[p.x][p.y].setNumber(Math.random() > 0.1f ? mSamllNum
				: mBignum);
		// 生成卡片的一些动作效果
		cardArrays[p.x][p.y].registerEntityModifier(new ScaleModifier(0.2f,
				0.0f, 1.0f));
	}

	private void resetGame() {
		mGameScene.clearScore();
		for (int y = 0; y < mCount; y++) {
			for (int x = 0; x < mCount; x++) {
				cardArrays[x][y].setNumber(0);
			}
		}
		addRandomNum();
		addRandomNum();
	}

	/**
	 * 检测是否游戏结束
	 */
	private void checkComplete() {

		for (int i = 0; i < mCount; i++) {
			for (int j = 0; j < mCount; j++) {

				if (cardArrays[i][j].getNumber() == 2048) {
					// 拼出2048，胜利完成，弹出胜利对话框
					showDialog(true);
					return;
				}

			}

		}

		for (int y = 0; y < mCount; y++) {
			for (int x = 0; x < mCount; x++) {
				if (cardArrays[x][y].getNumber() <= 0
						|| (x > 0 && cardArrays[x][y]
								.equals(cardArrays[x - 1][y]))
						|| (x < mCount - 1 && cardArrays[x][y]
								.equals(cardArrays[x + 1][y]))
						|| (y > 0 && cardArrays[x][y]
								.equals(cardArrays[x][y - 1]))
						|| (y < mCount - 1 && cardArrays
								.equals(cardArrays[x][y + 1]))) {
					return;
				}
			}
		}
		// 全盘填满并且不能再合并时失败结束，弹出失败对话框
		showDialog(false);

	}

	String title = "游戏失败";
	String msg = "你并没有拼出2048，别灰心，继续努力哦，据说全世界只有3%的人能拼出2048，再来一局吧，看好你哟！";

	private void showDialog(boolean isWin) {

		if (isWin) {
			title = "游戏胜利";
			msg = "你拼出了2048，实在是太厉害了！小伙伴们都惊呆了！";
		}

		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// 游戏结束
				new AlertDialog.Builder(getActivity())
						.setTitle(title)
						.setMessage(msg)
						.setPositiveButton("再来一局",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										resetGame();

									}
								})
						.setNegativeButton("退出",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										getActivity().finish();
									}
								}).show();

			}
		});

	}
}
