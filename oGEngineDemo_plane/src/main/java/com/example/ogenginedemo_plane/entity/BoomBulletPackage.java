package com.example.ogenginedemo_plane.entity;

import com.orange.engine.handler.physics.PhysicsHandler;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.opengl.vbo.VertexBufferObjectManager;

public class BoomBulletPackage extends AnimatedSprite{
	
	private PhysicsHandler mPhysicsHandler;

	//IsPass判断包是否飞过
	boolean IsPass=false;
	//type表示弹药包种类
	int type;	
	
	public boolean isPass() {
		return this.IsPass;
	}
	
	public void setPass(boolean IsPass) {
		this.IsPass=IsPass;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setPass(int type) {
		this.type=type;
	}
	
	public BoomBulletPackage(float pX, float pY, String pTextureRegionName,int type,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegionName, pVertexBufferObjectManager);				
		this.setPass(false);
		this.type=type;
		mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(mPhysicsHandler);
		//设置移动方向以及速度
		mPhysicsHandler.setVelocityY(80);		
	}
	
	/**
	 * 停止移动
	 */
	public void stopMove(){
		this.unregisterUpdateHandler(mPhysicsHandler);
	}
}