package com.orange.flappybird.entity;

import com.orange.engine.camera.Camera;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.opengl.util.GLState;
import com.orange.opengl.vbo.VertexBufferObjectManager;

/**
 * 可以循环滚动的地板类
 * 
 * @author lch
 * 
 */
public class FloorSprite extends AnimatedSprite {
	/** 视差值 **/
	private float mParallaxValue;
	/** 视差移动的因数 **/
	private float mParallaxFactor = 5;
	/** 每秒改变的视差值 **/
	private float mParallaxChangePerSecond;

	/**
	 * 设置每秒改变的视差值
	 * 
	 * @param mParallaxChangePerSecond
	 */
	public void setParallaxChangePerSecond(float mParallaxChangePerSecond) {
		this.mParallaxChangePerSecond = mParallaxChangePerSecond;
	}

	// ==============get&set===============
	public FloorSprite(float pX, float pY, String pTextureRegionName,
			float mParallaxChangePerSecond,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegionName, pVertexBufferObjectManager);
		this.mParallaxChangePerSecond = mParallaxChangePerSecond;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		// LogUtil.d("onManagedUpdate pSecondsElapsed-->"+pSecondsElapsed);
		this.mParallaxValue += this.mParallaxChangePerSecond * pSecondsElapsed;

	}

	@Override
	protected void onManagedDraw(GLState pGLState, Camera pCamera) {
		pGLState.pushModelViewGLMatrix();
		{
			final float cameraWidth = pCamera.getWidth();
			final float shapeWidthScaled = this.getWidthScaled();
			float baseOffset = (mParallaxValue * this.mParallaxFactor)
					% shapeWidthScaled;

			while (baseOffset > 0) {
				baseOffset -= shapeWidthScaled;
			}
			pGLState.translateModelViewGLMatrixf(baseOffset, 0, 0);
			float currentMaxX = baseOffset;

			do {
				super.onManagedDraw(pGLState, pCamera);
				pGLState.translateModelViewGLMatrixf(shapeWidthScaled, 0, 0);
				currentMaxX += shapeWidthScaled;
			} while (currentMaxX < cameraWidth);
		}
		pGLState.popModelViewGLMatrix();

	}

}
