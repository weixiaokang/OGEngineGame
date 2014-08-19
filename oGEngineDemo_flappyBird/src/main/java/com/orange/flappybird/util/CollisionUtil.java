package com.orange.flappybird.util;


public class CollisionUtil {

	/**
	 * 矩形碰撞检测 参数为x,y,width,height
	 * 
	 * @param x1
	 *            第一个矩形的x
	 * @param y1
	 *            第一个矩形的y
	 * @param w1
	 *            第一个矩形的w
	 * @param h1
	 *            第一个矩形的h
	 * @param x2
	 *            第二个矩形的x
	 * @param y2
	 *            第二个矩形的y
	 * @param w2
	 *            第二个矩形的w
	 * @param h2
	 *            第二个矩形的h
	 * @return 是否碰撞
	 */
	public static boolean IsRectCollision(float x1, float y1, float w1,
			float h1, float x2, float y2, float w2, float h2) {
		if (x2 > x1 && x2 > x1 + w1) {
			return false;
		} else if (x2 < x1 && x2 < x1 - w2) {
			return false;
		} else if (y2 > y1 && y2 > y1 + h1) {
			return false;
		} else if (y2 < y1 && y2 < y1 - h2) {
			return false;
		} else {
			return true;
		}
	}

}
