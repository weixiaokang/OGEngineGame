package com.orange.flappybird.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;

/**
 * 保存到 SharedPreferences 的数据
 * 
 * @author luochanghong
 * 
 */
public class SharedUtil {

	private static final String Shared_System = "Shared_og";

	private static final String BEST_SCORE = "best_score";

	public static void setBestScore(Context pContext, int pBestScore) {
		Editor edit = pContext.getSharedPreferences(Shared_System,
				Context.MODE_PRIVATE).edit();
		edit.putInt(BEST_SCORE, pBestScore);
		edit.commit();
	}

	public static int getBestScore(Context context) {
		return context
				.getSharedPreferences(Shared_System, Context.MODE_PRIVATE)
				.getInt(BEST_SCORE, 0);
	}

}
