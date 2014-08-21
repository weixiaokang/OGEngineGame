package com.orange.game2048.entity;

import com.orange.entity.layer.Layer;
import com.orange.entity.primitive.Rectangle;
import com.orange.entity.scene.Scene;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.game2048.res.Res;

public class HelpLayer extends Layer {

	/** 半透明背景 **/
	private Rectangle mtranslucentBackground;
	private AnimatedSprite help;

	public HelpLayer(Scene pScene) {
		super(pScene);
		mtranslucentBackground = new Rectangle(0, 0, pScene.getCameraWidth(),
				pScene.getCameraHeight(), pScene.getVertexBufferObjectManager());
		mtranslucentBackground.setColor(0, 0, 0, 0.4f);
		this.attachChild(mtranslucentBackground);
		initView();
	}

	private void initView() {
		help = new AnimatedSprite(0, 0, Res.GAME_HELP_BG, getVertexBufferObjectManager());
		this.attachChild(help);
	}

}
