package com.orange.block.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;

/**
 * 保存到 SharedPreferences 的数据
 * 
 * @author lch
 * 
 */
public class SharedUtil {

	private static final String SHARED_OG = "Shared_og";

	private static final String RESULT_RECORD = "result_record";

	/**
	 * 保持最新的记录
	 * @param pContext
	 * @param pMillisPass
	 */
	public static void setRecord(Context pContext, long pMillisPass) {
		Editor edit = pContext.getSharedPreferences(SHARED_OG,
				Context.MODE_PRIVATE).edit();
		edit.putLong(RESULT_RECORD, pMillisPass);
		edit.commit();
	}

	/**
	 * 获取记录
	 * @param context
	 * @return
	 */
	public static long getRecord(Context context) {
		return context
				.getSharedPreferences(SHARED_OG, Context.MODE_PRIVATE)
				.getLong(RESULT_RECORD, 0);
	}

}
