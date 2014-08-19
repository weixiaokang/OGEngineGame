package com.orange.flappybird.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.flappybird.res.Res;
import com.orange.flappybird.util.IConstant;
import com.orange.opengl.vbo.VertexBufferObjectManager;

public class BirdSprite extends AnimatedSprite implements IConstant{

	

	private int state = STATE_READY;
	private Body body;
	
	private boolean isCollisionBar = false;
	private boolean isCollisionFloor = false;
	
	private float initY = 0;

	// =====================get&set==================
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	public boolean isCollisionBar() {
		return isCollisionBar;
	}

	public void setCollisionBar(boolean isCollisionBar) {
		this.isCollisionBar = isCollisionBar;
	}

	public boolean isCollisionFloor() {
		return isCollisionFloor;
	}

	public void setCollisionFloor(boolean isCollisionFloor) {
		this.isCollisionFloor = isCollisionFloor;
	}

	// =====================get&set==================

	

	public BirdSprite(float pX, float pY,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, Res.BRID_YELLOW, pVertexBufferObjectManager);
		this.initY = pY;
		refreshState(STATE_READY);
	}

	/**
	 * 刷新状态
	 * @param state
	 */
	public void refreshState(int state) {
		switch (state) {
		case STATE_READY:
			this.setY(initY);
			this.setRotation(0);
			this.animate(180);
			break;
		case STATE_FLY:
			this.animate(100);
			break;
		case STATE_DIE:
			this.stopAnimation(0);
			this.setRotation(90);
			break;
		}

	}

}
