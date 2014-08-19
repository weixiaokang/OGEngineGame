package com.orange.flappybird.entity;

import com.orange.entity.layer.Layer;
import com.orange.entity.scene.Scene;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.entity.sprite.ButtonSprite;
import com.orange.entity.sprite.ButtonSprite.OnClickListener;
import com.orange.flappybird.res.Res;
import com.orange.flappybird.scene.GameScene;
import com.orange.flappybird.util.LogUtil;
import com.orange.opengl.vbo.VertexBufferObjectManager;

public class OverLayer extends Layer implements OnClickListener{
	private VertexBufferObjectManager  pVertexBufferObjectManager;
	private AnimatedSprite gameOverTip;
	private ResultGroup mResultGroup;
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
		
		mResultGroup = new ResultGroup(0,gameOverTip.getBottomY()+30,getScene());
		mResultGroup.setCentrePositionX(this.getCentreX());
		this.attachChild(mResultGroup);
		
		startBtn = new ButtonSprite(0, mResultGroup.getBottomY()+30, Res.GAME_START, pVertexBufferObjectManager);
		startBtn.setCentrePositionX(this.getCentreX());
		startBtn.setIgnoreTouch(false);
		startBtn.setOnClickListener(this);
		this.attachChild(startBtn);
		
	}
	
	public void updateScore(int currScore){
		mResultGroup.updateCurrScore(currScore);
		mResultGroup.updateBestScore();
	}
	
	
	@Override
	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if(pButtonSprite == startBtn){
			((GameScene)getScene()).restartGame();
		}
		
	}

}
