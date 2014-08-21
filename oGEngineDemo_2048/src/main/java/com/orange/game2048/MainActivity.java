package com.orange.game2048;

import android.graphics.Color;
import android.graphics.Typeface;

import com.orange.audio.sound.SoundFactory;
import com.orange.engine.camera.ZoomCamera;
import com.orange.engine.options.PixelPerfectEngineOptions;
import com.orange.engine.options.PixelPerfectMode;
import com.orange.engine.options.ScreenOrientation;
import com.orange.game2048.res.Res;
import com.orange.game2048.scene.GameScene;
import com.orange.game2048.util.ConstantUtil;
import com.orange.res.FontRes;
import com.orange.res.RegionRes;
import com.orange.res.SoundRes;
import com.orange.ui.activity.GameActivity;

/**
 * 主 Activity 入口
 * 
 * @author lch<OGEngine@orangegame.cn>
 * 
 */
public class MainActivity extends GameActivity {

	@Override
	protected PixelPerfectEngineOptions onCreatePixelPerfectEngineOptions() {
		PixelPerfectEngineOptions pixelPerfectEngineOptions = new PixelPerfectEngineOptions(
				this, ZoomCamera.class);
		// 设置竖屏
		pixelPerfectEngineOptions
				.setScreenOrientation(ScreenOrientation.PORTRAIT_FIXED);
		// 适配模式,这里设置为“保持宽度不变，改变高”
		pixelPerfectEngineOptions
				.setPixelPerfectMode(PixelPerfectMode.CHANGE_HEIGHT);
		// 参考尺寸
		pixelPerfectEngineOptions.setDesiredSize(ConstantUtil.DESIRED_SIZE);
		return pixelPerfectEngineOptions;
	}

	@Override
	protected void onLoadResources() {
		// 加载图片资源
		RegionRes.loadTexturesFromAssets(Res.ALL_XML);

		// 加载字体资源
		FontRes.loadFont(128, 128,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true,
				Color.BLACK, ConstantUtil.FONT_CARD_NUM);
		FontRes.loadFont(128, 128,
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 25, true,
				Color.WHITE, ConstantUtil.FONT_SCORE_NUM);

		// 加载音效资源
		SoundFactory.setAssetBasePath("mfx/");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_SELECT, "select.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_SETPOS, "setpos.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_MERGE, "merge.mp3");
	}

	@Override
	protected void onLoadComplete() {
		// 加载资源完成后
		this.startScene(GameScene.class);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
