package com.example.ogenginedemo_plane;

import com.orange.audio.music.MusicFactory;
import com.orange.audio.sound.SoundFactory;
import com.orange.engine.camera.ZoomCamera;
import com.orange.engine.options.PixelPerfectEngineOptions;
import com.orange.engine.options.PixelPerfectMode;
import com.orange.engine.options.ScreenOrientation;
import com.example.ogenginedemo_plane.Res.*;
import com.example.ogenginedemo_plane.util.*;
import com.example.ogenginedemo_plane.scene.*;
import com.orange.res.FontRes;
import com.orange.res.MusicRes;
import com.orange.res.RegionRes;
import com.orange.res.SoundRes;
import com.orange.ui.activity.GameActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;


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
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 25, true,
				Color.WHITE, ConstantUtil.FONT_SCORE_NUM);

		MusicFactory.setAssetBasePath("mfx/");
		MusicRes.loadMusicFromAssets(ConstantUtil.SOUND_GAME_MUSIC, "game_music.mp3");
		// 加载音效资源
		SoundFactory.setAssetBasePath("mfx/");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_ACHIEVEMENT, "achievement.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_BIG_SPACESHIP_FLYING, "big_spaceship_flying.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_BULLET, "bullet.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_BUTTON, "button.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_COUNTDOWN, "countdown.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_ENEMY1_DOWN, "enemy1_down.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_ENEMY2_DOWN, "enemy2_down.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_ENEMY3_DOWN, "enemy3_down.mp3");
		
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_GAME_OVER, "game_over.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_GET_BOMB, "get_bomb.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_GET_DOUBLE_LASER, "get_double_laser.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_OUT_PORP, "out_porp.mp3");
		SoundRes.loadSoundFromAssets(ConstantUtil.SOUND_USE_BOMB, "use_bomb.mp3");
		
		
	}

	@Override
	protected void onLoadComplete() {
		// 加载资源完成后
		this.startScene(GameScene.class);
	}

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		//LogUtil.i("MainActivity---onCreate");
		super.onCreate(pSavedInstanceState);
	}
	
	@Override
	protected synchronized void onResume() {
		super.onResume();
//		this.doResumeGame();
		//LogUtil.i("MainActivity---onResume:"+mEngine.isRunning());

	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		this.doPauseGame();
		//LogUtil.i("MainActivity---onPause:"+mEngine.isRunning());

	}
}
