package com.orange.block.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.view.KeyEvent;

import com.orange.content.SceneBundle;
import com.orange.entity.IEntity;
import com.orange.entity.modifier.ColorModifier;
import com.orange.entity.modifier.DelayModifier;
import com.orange.entity.modifier.IEntityModifier.IEntityModifierListener;
import com.orange.entity.modifier.LoopEntityModifier;
import com.orange.entity.modifier.MoveYModifier;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.modifier.SequenceEntityModifier;
import com.orange.entity.primitive.DrawMode;
import com.orange.entity.primitive.Mesh;
import com.orange.entity.scene.Scene;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.entity.text.Text;
import com.orange.res.FontRes;
import com.orange.util.color.Color;
import com.orange.util.modifier.IModifier;
import com.orange.block.control.TimerTool;
import com.orange.block.entity.Block;
import com.orange.block.entity.FailGroup;
import com.orange.block.entity.SuccGroup;
import com.orange.block.res.Res;
import com.orange.block.util.ConstantUtil;
import com.orange.block.util.LogUtil;

/**
 * 游戏场景
 * 
 * @author lch
 * 
 */
public class GameScene extends Scene {
	/** 块的宽 **/
	private float blockWidth = 0;
	/** 块的高 **/
	private float blockHight = 0;
	/** 用于装当前所有生成但未被删除的block **/
	private List<Block[]> blockList;
	/** 当前生成的块的行数 **/
	private int linesLength = 5;
	/** 游戏状态 **/
	private int gameStatus = ConstantUtil.GAME_START;
	/** 移动步数 **/
	private int moveNum = 0;
	/** 成功界面 **/
	private SuccGroup mSuccGroup;
	/** 失败界面 **/
	private FailGroup mFailGroup;
	/** 计时管理类 **/
	private TimerTool mTimerTool;

	/** 显示计时的Text **/
	private Text timerText;
	
	/**游戏提示语**/
	private AnimatedSprite game_tip;

	// 用于Z索引排序
	private static final int pZIndex_middle = 2;
	private static final int pZIndex_top = 3;

	public Text getTimerText() {
		return timerText;
	}

	public TimerTool getmTimerTool() {
		return mTimerTool;
	}

	@Override
	public void onSceneCreate(SceneBundle bundle) {
		super.onSceneCreate(bundle);
		// 镜头里显示的是4*4的块，所以用镜头宽的四分之一作为块宽
		blockWidth = this.getCameraWidth() / 4;
		blockHight = this.getCameraHeight() / 4;

		this.blockList = new ArrayList<Block[]>();
		mTimerTool = new TimerTool(this);
		initView();
	}

	private void initView() {
		// 初始化blocks
		initBlocks();

		timerText = new Text(getCameraCenterX(), 10,
				FontRes.getFont(ConstantUtil.FONT_NAME_TIMER), "00.000\"",
				"00:00.000\"".length(), getVertexBufferObjectManager());
		this.attachChild(timerText);
		timerText.setCentrePositionX(getCameraCenterX());
		timerText.setZIndex(pZIndex_top);

		mSuccGroup = new SuccGroup(getCameraWidth(), getCameraHeight(), this);
		mSuccGroup.setZIndex(pZIndex_middle);
		mSuccGroup.setVisible(false);
		this.attachChild(mSuccGroup);

		mFailGroup = new FailGroup(getCameraWidth(), getCameraHeight(), this);
		this.attachChild(mFailGroup);
		mFailGroup.setZIndex(pZIndex_middle);
		
		game_tip = new AnimatedSprite(0, 0, Res.GAME_TIP, getVertexBufferObjectManager());
		game_tip.setCentrePositionX(this.getCameraCenterX());
		game_tip.setBottomPositionY(this.getCameraBottomY()-60);
		this.attachChild(game_tip);
		game_tip.setZIndex(pZIndex_top);
		
	}

	/**
	 * 初始化blocks
	 */
	private void initBlocks() {
		Random mRandom = new Random();

		int blackIndex = 0;
		// 初始blocks,先创建4*5表格，使用时候再一行行增加
		for (int row = 0; row < linesLength; row++) {// 行
			// 一行blocks
			Block[] rowBolcks = new Block[4];
			// 随机一个黑块所在位置
			blackIndex = mRandom.nextInt(4);
			for (int column = 0; column < 4; column++) {// 列
				rowBolcks[column] = new Block(this, row, column, blockWidth,
						blockHight, blackIndex, getVertexBufferObjectManager());
				this.attachChild(rowBolcks[column]);
			}
			blockList.add(rowBolcks);
		}
	}

	/**
	 * 重来时，重置游戏相关
	 */
	public void resetGame() {
		gameStatus = ConstantUtil.GAME_START;
		linesLength = 5;
		moveNum = 0;
		mSuccGroup.showItems(false);
		timerText.setText("00.000\"");
		timerText.setVisible(true);
		mTimerTool.resetTimer();
		mSuccGroup.setVisible(false);
		game_tip.setVisible(true);
		deletBlocks();
		initBlocks();
		// 按Z索引排序一下上下层顺序
		this.sortChildren();
	}

	/**
	 * 创建添加新的一行
	 */
	private void createNewRowBolcks() {
		// 超出屏幕没用的部分移除掉
		if (blockList.size() > 5) {
			Block[] rowBolcks = blockList.get(0);
			for (Block block : rowBolcks) {
				block.detachSelf();
			}
			blockList.remove(0);
		}

		// 目前顶部的一行块的Y坐标
		float topBlocksY = blockList.get(blockList.size() - 1)[0].getY();

		// 一行块
		Block[] rowBolcks = new Block[4];
		int blackIndex = new Random().nextInt(4);
		for (int column = 0; column < 4; column++) {
			// 新创建 Block
			rowBolcks[column] = new Block(this, column, blockWidth, blockHight,
					blackIndex, getVertexBufferObjectManager());
			// 设置Y坐标
			rowBolcks[column].setBottomPositionY(topBlocksY - 1);
			// 设置已经是第几行
			rowBolcks[column].setRow(linesLength);
			this.attachChild(rowBolcks[column]);
		}
		blockList.add(rowBolcks);

		// 按Z索引排序一下上下层顺序
		this.sortChildren();

		// 当前生成的块的行数增加
		linesLength++;
	}

	/**
	 * 最后一个移动，游戏胜利
	 * 
	 * @param pBlock
	 */
	private void gameSucc(Block pBlock) {
		gameStatus = ConstantUtil.GAME_OVER;
		// 最后一个移动，游戏胜利
		moveDown(pBlock, 0);
		// 停止计时
		mTimerTool.stop();
		// 显示游戏胜利画面
		mSuccGroup.showItems(true);
		// 隐藏显示时间的Text
		timerText.setVisible(false);
	}

	/**
	 * 点击错误后弹出红色的失败界面,游戏失败
	 */
	private void gameFail() {
		gameStatus = ConstantUtil.GAME_OVER;
		// 游戏失败，停止计时
		mTimerTool.stop();
		// 隐藏显示时间的Text
		timerText.setVisible(false);
	}

	/**
	 * 游戏快到顶部时开始出现绿色的胜利界面
	 */
	private void showSuccGroup() {
		// 目前顶部的一行块的Y坐标
		float topBlocksY = blockList.get(blockList.size() - 1)[0].getY();
		mSuccGroup.setBottomPositionY(topBlocksY);
		mSuccGroup.setVisible(true);
	}

	/**
	 * 移除剩下的block，清空blockList
	 */
	private void deletBlocks() {
		for (Block[] rowBlocks : blockList) {
			for (Block block : rowBlocks) {
				this.detachChild(block);
			}
		}

		blockList.clear();
	}

	/**
	 * 点击到Block时进行的逻辑处理
	 * 
	 * @param pBlock
	 *            所点击的block
	 */
	public void touchBlock(Block pBlock) {

		if (gameStatus == ConstantUtil.GAME_START) {

			if (pBlock.getRow() == moveNum + 2) {// 表示是在底部往上数的倒数第三行
				// 判断是不是点击了该点击的黑块的上一格，如果是，我们也判定这是正确点击了，做出相应移动
				upBlockTouch(pBlock);
			} else if (pBlock.getRow() == moveNum + 1) {// 表示是在底部往上数的倒数第二行
				if (pBlock.getColorType() == ConstantUtil.COLOR_BLACK) {
					if (linesLength == moveNum + 2) {
						// 游戏胜利
						gameSucc(pBlock);
					} else {
						// 整体blocks下移
						moveDown(pBlock, 1);
					}

				} else if (pBlock.getColorType() == ConstantUtil.COLOR_WHITE) {
					// 误点了白块，游戏失败
					gameFail();
					// 失败时pBlock的一个闪红效果
					LoopEntityModifier loop = failAction();
					// 播放效果
					pBlock.registerEntityModifier(loop);
				}

			}

		}

	}

	/**
	 * 失败时pBlock的一个闪红效果
	 * @return
	 */
	private LoopEntityModifier failAction() {
		SequenceEntityModifier sequence = new SequenceEntityModifier(
				new ColorModifier(0.1f, Color.RED, Color.WHITE),
				new DelayModifier(0.07f), new ColorModifier(0.1f,
						Color.WHITE, Color.RED));
		LoopEntityModifier loop = new LoopEntityModifier(sequence,
				3, new IEntityModifierListener() {

					@Override
					public void onModifierStarted(
							IModifier<IEntity> pModifier,
							IEntity pItem) {
					}
					@Override
					public void onModifierFinished(
							IModifier<IEntity> pModifier,
							IEntity pItem) {
						//效果播放完毕，显示游戏失败界面
						mFailGroup.showView();
					}
				});
		return loop;
	}

	/**
	 * 判断是不是点击了该点击的黑块的上一格，如果是，我们也判定这是正确点击了，做出相应移动
	 * 
	 * @param pBlock
	 *            所被点击的块
	 */
	private void upBlockTouch(Block pBlock) {
		int touchColumn = pBlock.getColumn();
		for (Block[] blocks : blockList) {
			for (Block block : blocks) {
				if (block.getRow() == moveNum + 1
						&& block.getColorType() == ConstantUtil.COLOR_BLACK) {
					if (block.getColumn() == touchColumn) {
						// 整体blocks下移
						moveDown(block, 1);
					}
					return;
				}
			}

		}
	}

	/**
	 * 正确点击该点击的黑块，或者上一行的块，整体向下移动、创建新的一样块，改变黑块颜色
	 * 
	 * @param pBlock
	 * @param stepNum
	 *            一般为1，到最后出现绿色成功界面的最后一步为0
	 */
	private void moveDown(Block pBlock, int stepNum) {

		if(moveNum == 0){
			// 开始计时
			mTimerTool.start();
			// 隐藏提示文字
			game_tip.setVisible(false);
		}
		

		if (moveNum < ConstantUtil.LINES_LEN) {
			// 创建添加新的一行
			createNewRowBolcks();
		} else if (moveNum == ConstantUtil.LINES_LEN) {
			// 开始显示绿色胜利界面，即将胜利，但还没有胜利
			showSuccGroup();
		}

		// 被点击的黑块变灰
		pBlock.setColor(0.63f, 0.63f, 0.63f);
		pBlock.registerEntityModifier(new ScaleModifier(0.1f, 0.5f, 1.0f));

		// 移动步数加1
		moveNum++;
		// 需要移动的距离
		float moveDistance = this.getCameraHeight() - blockHight * stepNum
				- pBlock.getY();
		for (Block[] rowBlocks : blockList) {
			for (Block block : rowBlocks) {
				// 遍历，所有block向下移动指定距离
				moveToY(block, moveDistance);
			}
		}

		// 快到胜利出现绿色胜利界面时，胜利界面跟着移动
		if (mSuccGroup.isVisible()) {
			moveToY(mSuccGroup, moveDistance);
		}
	}

	/**
	 * 在Y轴方向上，由当前位置移动指定的距离
	 * 
	 * @param entity
	 *            要移动的实体
	 * @param moveDistance
	 *            需要移动的距离
	 */
	private void moveToY(IEntity entity, float moveDistance) {
		float pFromY = entity.getY();
		float pToY = pFromY + moveDistance;
		entity.registerEntityModifier(new MoveYModifier(0.1f, pFromY, pToY));
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.getActivity().finish();
			return true;
		}
		return false;
	}
	
	@Override
	public void onSceneResume() {
		super.onSceneResume();
		this.setIgnoreUpdate(false);
	}
	
	@Override
	public void onScenePause() {
		super.onScenePause();
		this.setIgnoreUpdate(true);
	}

	@Override
	public void onSceneDestroy() {
		super.onSceneDestroy();

		LogUtil.d("onSceneDestroy");
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
