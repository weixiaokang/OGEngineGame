package com.orange.flappybird.util;

public interface IConstant {

	public static final float DESIRED_SIZE = 480;
	
	public static final int STATE_READY = 0;
	public static final int STATE_FLY = 1;
	public static final int STATE_DIE = 2;
	
	// 用于Z索引排序
	public static final int pZIndex_middle = 2;
	public static final int pZIndex_top = 3;
	
	public static final String FONT_SCORE = "score";
	public static final String FONT_SCORE_SMALL = "score_small";
	
	
	public static final String SOUND_DIE = "die";
	public static final String SOUND_HIT = "hit";
	public static final String SOUND_POINT = "point";
	public static final String SOUND_SWOOSING = "swoosing";
	public static final String SOUND_WING = "wing";
	
}
