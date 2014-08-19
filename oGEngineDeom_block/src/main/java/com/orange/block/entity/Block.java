package com.orange.block.entity;

import com.orange.entity.primitive.Rectangle;
import com.orange.input.touch.TouchEvent;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.util.color.Color;
import com.orange.block.scene.GameScene;
import com.orange.block.util.ConstantUtil;

/**
 * 单个块元素
 * 
 * @author lch
 * 
 */
public class Block extends Rectangle {
	// 游戏场景
	private GameScene mGameScene;
	// 此block的颜色类型，白色还是黑色？
	private int colorType;
	// block 所在的行
	private int row;
	// block 所在的列
	private int column;

	// ======================get&set========================
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public int getColorType() {
		return colorType;
	}
	// =====================================================

	/**
	 * 构造器1,初始化blocks时用到
	 * @param pGameScene 游戏场景
	 * @param row block所在的行
	 * @param column block所在的列
	 * @param pWidth block的宽
	 * @param pHeight block的高
	 * @param blackIndex 用来确定是否是黑块，如果blackIndex == column时设为黑块
	 * @param pVertexBufferObjectManager
	 */
	public Block(GameScene pGameScene, int row, int column, float pWidth,
			float pHeight, int blackIndex,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(column * pWidth, (3 - row) * pHeight, pWidth - 1, pHeight - 1,
				pVertexBufferObjectManager);
		this.mGameScene = pGameScene;
		this.row = row;
		this.column = column;
		if (row == 0) {
			// 第一行设置为黄块
			this.setColor(Color.YELLOW);
		} else {
			// 初始化block的颜色数据,是白块还是黑块?
			initBlockData(column, blackIndex);
		}
		// 设置可以相应触碰事件
		this.setIgnoreTouch(false);
	}

	/**
	 * 构造器2,新增blocks时用到
	 * @param pGameScene 游戏场景
	 * @param column block所在的列
	 * @param pWidth block的宽
	 * @param pHeight block的高
	 * @param blackIndex 来确定是否是黑块，如果blackIndex == column时设为黑块
	 * @param pVertexBufferObjectManager
	 */
	public Block(GameScene pGameScene, int column, float pWidth, float pHeight,
			int blackIndex, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(column * pWidth, 0, pWidth - 1, pHeight - 1,
				pVertexBufferObjectManager);
		this.mGameScene = pGameScene;
		this.column = column;
		// 初始化block的颜色数据,是白块还是黑块?
		initBlockData(column, blackIndex);
		// 设置可以相应触碰事件
		this.setIgnoreTouch(false);
	}
	
	/**
	 * 初始化block的颜色数据,是白块还是黑块?
	 * @param column
	 * @param blackIndex
	 */
	private void initBlockData(int column, int blackIndex) {
		if (blackIndex == column) {
			// 设置为黑块
			this.setColor(Color.BLACK);
			this.colorType = ConstantUtil.COLOR_BLACK;
		} else {
			// 设置为白块
			this.setColor(Color.WHITE);
			this.colorType = ConstantUtil.COLOR_WHITE;
		}
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		// 触碰事件监听
		if (pSceneTouchEvent.isActionDown()) {
			// 点击到Block时进行的逻辑处理
			mGameScene.touchBlock(this);
		}
		return true;
	}
}
