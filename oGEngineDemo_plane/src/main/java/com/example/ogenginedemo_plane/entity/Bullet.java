package com.example.ogenginedemo_plane.entity;

import com.orange.engine.handler.physics.PhysicsHandler;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.opengl.vbo.VertexBufferObjectManager;

public class Bullet extends AnimatedSprite{
	private PhysicsHandler mPhysicsHandler;
	//子弹种类
	int type;
	//子弹是否飞过屏幕
	boolean IsPass=false;
	//子弹是否打中敌机
	boolean IsHit=false;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean IsPass() {
		return IsPass;
	}

	public void setPass(boolean IsPass) {
		this.IsPass = IsPass;
	}
	
	public boolean IsHit() {
		return IsHit;
	}
	
	public void setHit(boolean IsHit) {
		this.IsHit=IsHit;
	}
	
	public Bullet(float pX, float pY, String pTextureRegionName,int type,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegionName, pVertexBufferObjectManager);
		this.type=type;
		mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(mPhysicsHandler);
		//设置移动方向以及速度
		mPhysicsHandler.setVelocityY(-450);		
	}
	
	/**
	 * 停止移动
	 */
	public void stopMove(){
		this.unregisterUpdateHandler(mPhysicsHandler);
	}
}