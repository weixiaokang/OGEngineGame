package com.example.ogenginedemo_plane.entity;

import com.example.ogenginedemo_plane.util.ConstantUtil;
import com.orange.engine.handler.physics.PhysicsHandler;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.SoundRes;

public class Enemy extends AnimatedSprite {
	
	private PhysicsHandler mPhysicsHandler;
	//type敌机代表种类
	int type;
	//IsPass判断敌机是否飞过
	boolean IsPass;
	//blood敌机的血量
	int  blood;
	//Score敌机的分值
	int Score;
	//飞机速度
	int speed;
	
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
	
	public int getBlood () {
		return this.blood;
	}
	
	public void setBlood(int Blood) {
		this.blood=Blood;
	}
	
	public int getScore() {
		return this.Score;
	}
	
	public Enemy(float pX, float pY, String pTextureRegionName,int type,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegionName, pVertexBufferObjectManager);
		this.IsPass=false;
		this.type=type;
		switch (type) {
		case 0 :
			this.blood = 25;
			this.Score=100;
			this.speed=300;
			break;
		case 1:
			this.blood = 100;
			this.Score=500;
			this.speed=200;
			break;
		case 2:
			this.blood = 300;
			this.Score=2000;
			this.speed=150;
			break;
		}
		
		mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(mPhysicsHandler);
		//设置移动方向以及速度
		mPhysicsHandler.setVelocityY(this.speed);		
	}
	
	public void PlaySound() {
		switch (this.getType()) {
			case 0:
				SoundRes.playSound(ConstantUtil.SOUND_ENEMY1_DOWN);
				break;
			case 1:
				SoundRes.playSound(ConstantUtil.SOUND_ENEMY1_DOWN);
				break;
			case 2:
				SoundRes.playSound(ConstantUtil.SOUND_ENEMY1_DOWN);
				break;
		}
	}
	
	/**
	 * 停止移动
	 */
	public void stopMove(){
		this.unregisterUpdateHandler(mPhysicsHandler);
	}
	
}