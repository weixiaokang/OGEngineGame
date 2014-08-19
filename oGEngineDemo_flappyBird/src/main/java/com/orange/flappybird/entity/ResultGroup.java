package com.orange.flappybird.entity;

import com.orange.entity.group.EntityGroup;
import com.orange.entity.scene.Scene;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.entity.text.Text;
import com.orange.flappybird.res.Res;
import com.orange.flappybird.util.IConstant;
import com.orange.flappybird.util.SharedUtil;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.FontRes;

public class ResultGroup extends EntityGroup implements IConstant{
	private VertexBufferObjectManager  pVertexBufferObjectManager;
	private AnimatedSprite medalSprite;// 奖牌
	private Text currScoreText;
	private Text bestScoreText;
	public ResultGroup(float pX,float pY,Scene pScene) {
		super(pX,pY,332, 168, pScene);
		pVertexBufferObjectManager = getVertexBufferObjectManager();
		initView();
		updateCurrScore(0);
	}

	private void initView() {
		AnimatedSprite bg = new AnimatedSprite(0, 0, Res.GAME_RESULT_BG, pVertexBufferObjectManager);
		this.attachChild(bg);
		currScoreText = new Text(0, 45, FontRes.getFont(FONT_SCORE_SMALL), "", 9, pVertexBufferObjectManager);
		this.attachChild(currScoreText);
		
		bestScoreText = new Text(0, 103, FontRes.getFont(FONT_SCORE_SMALL), "", 9, pVertexBufferObjectManager);
		updateBestScore();
		this.attachChild(bestScoreText);
		
		medalSprite = new AnimatedSprite(44, 63, Res.GAME_MEDAL, pVertexBufferObjectManager);
		this.attachChild(medalSprite);
		
	}
	

	public void updateCurrScore(int currScore){
		currScoreText.setText(currScore+"");
		currScoreText.setRightPositionX(290);
		
		if(currScore>SharedUtil.getBestScore(getActivity())){
			SharedUtil.setBestScore(getActivity(), currScore);
		}
		
		// 奖牌
		medalSprite.setVisible(true);
		if (currScore >= 40) {
			medalSprite.setCurrentTileIndex(3);
		} else if (currScore >= 30) {
			medalSprite.setCurrentTileIndex(2);
		} else if (currScore >= 20) {
			medalSprite.setCurrentTileIndex(1);
		} else if (currScore >= 10) {
			medalSprite.setCurrentTileIndex(0);
		} else {
			medalSprite.setVisible(false);
		}
	}
	
	public void updateBestScore(){
		bestScoreText.setText(SharedUtil.getBestScore(getActivity())+"");
		bestScoreText.setRightPositionX(290);
	}
}
