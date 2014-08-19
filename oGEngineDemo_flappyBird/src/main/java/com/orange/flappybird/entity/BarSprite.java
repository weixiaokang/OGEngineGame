package com.orange.flappybird.entity;

import com.orange.engine.handler.physics.PhysicsHandler;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.opengl.vbo.VertexBufferObjectManager;

public class BarSprite extends AnimatedSprite {
	
	private PhysicsHandler mPhysicsHandler;
	
	// 是否为上端水管,这里以上水管来参考小鸟是否通过它
	private boolean  isUpBar;
	// 小鸟是否已经通过它
	private boolean isPass;
	
	
	public boolean isUpBar() {
		return isUpBar;
	}

	public void setUpBar(boolean isUpBar) {
		this.isUpBar = isUpBar;
	}


	public boolean isPass() {
		return isPass;
	}

	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}


	public BarSprite(float pX, float pY, String pTextureRegionName,boolean isUpBar,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegionName, pVertexBufferObjectManager);

		this.isUpBar = isUpBar;
		mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(mPhysicsHandler);
		// 设置移动方向和速度 
		mPhysicsHandler.setVelocityX(-150);
	}
	
	/**
	 * 停止移动
	 */
	public void stopMove(){
		this.unregisterUpdateHandler(mPhysicsHandler);
	}

}
