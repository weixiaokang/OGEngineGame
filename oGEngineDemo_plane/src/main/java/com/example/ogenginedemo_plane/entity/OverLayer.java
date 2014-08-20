package com.example.ogenginedemo_plane.entity;

import com.orange.entity.layer.Layer;
import com.orange.entity.scene.Scene;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.entity.sprite.ButtonSprite;
import com.orange.entity.sprite.ButtonSprite.OnClickListener;
import com.example.ogenginedemo_plane.Res.*;
import com.example.ogenginedemo_plane.scene.*;
import com.orange.opengl.vbo.VertexBufferObjectManager;

public class OverLayer extends Layer implements OnClickListener{
	private VertexBufferObjectManager  pVertexBufferObjectManager;
	private AnimatedSprite gameOverTip;

	private ButtonSprite startBtn;
	public OverLayer(float pWidth, float pHeight, Scene pScene) {
		super(pWidth, pHeight, pScene);
		pVertexBufferObjectManager = getVertexBufferObjectManager();
		initView();
	}
	private void initView() {
		
		gameOverTip = new AnimatedSprite(0, 150, Res.GAME_OVER, pVertexBufferObjectManager);
		gameOverTip.setCentrePositionX(this.getCentreX());
		this.attachChild(gameOverTip);		
		
		startBtn = new ButtonSprite(0, 250, Res.GAME_START, pVertexBufferObjectManager);
		startBtn.setCentrePositionX(this.getCentreX());
		startBtn.setIgnoreTouch(false);
		startBtn.setOnClickListener(this);
		this.attachChild(startBtn);
		
	}
			
	@Override
	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if(pButtonSprite == startBtn){
			((GameScene)getScene()).restartGame();
		}
		
	}

}