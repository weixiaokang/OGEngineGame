package com.fairy.wuziqi.uilts;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class GameSharedPreferences {
	private static Context context;
	public static final String GAMEPREFS = "GAME_PREFS";
	private static SharedPreferences sp;
	private static GameSharedPreferences gamePreferences;

	public GameSharedPreferences(Context mContext) {
		super();
		context = mContext;
	}
	public static GameSharedPreferences getGameSharedPreferences(
			Context mContext) {
		if (gamePreferences == null) {
			gamePreferences = new GameSharedPreferences(mContext);
		}
		return gamePreferences;
	}
	public void initPreferences() {
		int mode = Activity.MODE_WORLD_READABLE;
		sp = context.getSharedPreferences(GAMEPREFS, mode);
	}

	public  void saveScorePreferences() {
		initPreferences();
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("game", 0);
		editor.commit();
	}

	public void saveDegreePreferences(int degreeId) {
		initPreferences();
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("game", degreeId);
		editor.commit();
	}

	
	public  int loadDegreeIdPreferences() {
		initPreferences();
		int degreeId = sp.getInt("game", 0);
		return degreeId;
	}

}
