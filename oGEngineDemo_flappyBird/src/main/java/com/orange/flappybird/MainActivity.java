package com.orange.flappybird;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.orange.audio.sound.SoundFactory;
import com.orange.engine.camera.ZoomCamera;
import com.orange.engine.options.PixelPerfectEngineOptions;
import com.orange.engine.options.PixelPerfectMode;
import com.orange.engine.options.ScreenOrientation;
import com.orange.flappybird.res.Res;
import com.orange.flappybird.scene.GameScene;
import com.orange.flappybird.util.IConstant;
import com.orange.flappybird.util.LogUtil;
import com.orange.res.FontRes;
import com.orange.res.RegionRes;
import com.orange.res.SoundRes;
import com.orange.ui.activity.GameActivity;

public class MainActivity extends GameActivity implements IConstant{

	@Override
	protected PixelPerfectEngineOptions onCreatePixelPerfectEngineOptions() {
		
		PixelPerfectEngineOptions mPixelPerfectEngineOptions = new PixelPerfectEngineOptions(this, ZoomCamera.class);
		// 参考尺寸
		mPixelPerfectEngineOptions.setDesiredSize(DESIRED_SIZE);
		// 设置竖屏
		mPixelPerfectEngineOptions.setScreenOrientation(ScreenOrientation.PORTRAIT_FIXED);
		// 适配模式,这里设置为“保持宽度不变，改变高”
		mPixelPerfectEngineOptions.setPixelPerfectMode(PixelPerfectMode.CHANGE_HEIGHT);
		
		LogUtil.i("MainActivity---onCreatePixelPerfectEngineOptions");
		
		return mPixelPerfectEngineOptions;
	}

	@Override
	protected void onLoadResources() {
		// 注册打印 FPS
		LogUtil.i("MainActivity---onLoadResources");
		
		// 加载图片资源
		RegionRes.loadTexturesFromAssets(Res.ALL_XML);
		// 加载字体资源
		FontRes.loadStrokeFont(256, 128, Typeface.create(Typeface.SERIF, Typeface.BOLD), 60, true, Color.WHITE, 2, Color.BLACK, false, FONT_SCORE);
		FontRes.loadStrokeFont(256, 128, Typeface.create(Typeface.SERIF, Typeface.BOLD), 30, true, Color.WHITE, 0.8f, Color.BLACK, false, FONT_SCORE_SMALL);

		
		// 加载音效资源
		SoundFactory.setAssetBasePath("mfx/");
		SoundRes.loadSoundFromAssets(SOUND_DIE, "mfx_die.ogg");
		SoundRes.loadSoundFromAssets(SOUND_HIT, "mfx_hit.ogg");
		SoundRes.loadSoundFromAssets(SOUND_POINT, "mfx_point.ogg");
		SoundRes.loadSoundFromAssets(SOUND_SWOOSING, "mfx_swooshing.ogg");
		SoundRes.loadSoundFromAssets(SOUND_WING, "mfx_wing.ogg");
		
	}

	@Override
	protected void onLoadComplete() {
		LogUtil.i("MainActivity---onLoadComplete");
		startScene(GameScene.class);
	}
	
	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		LogUtil.i("MainActivity---onCreate");
		super.onCreate(pSavedInstanceState);
	}
	
	@Override
	protected synchronized void onResume() {
		super.onResume();
//		this.doResumeGame();
		LogUtil.i("MainActivity---onResume:"+mEngine.isRunning());

	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		this.doPauseGame();
		LogUtil.i("MainActivity---onPause:"+mEngine.isRunning());

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.i("MainActivity---onDestroy");
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
