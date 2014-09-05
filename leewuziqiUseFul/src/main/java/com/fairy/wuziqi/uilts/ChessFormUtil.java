package com.fairy.wuziqi.uilts;

/**
 * 棋形分析工具
 * 
 * @author vagasnail
 * 
 */
public class ChessFormUtil {

	// 定义各种棋形的分数
	/** 待分析的棋型列表的长度 */
	public static final int ANALYZE_LEN = 8;

	public static final int HALF_LEN = ANALYZE_LEN >> 1;
	/**
	 * 五连:你只需下一步就可以胜利了
	 */
	public static final int WU_LIAN = 85;
	/**
	 * 活四：两边都可成五的点
	 */
	public static final int HUO_SI = 40;
	/**
	 * 活三：在走一步可以成活四的点
	 */
	public static final int HUO_SAN = 15;
	/**
	 * 冲四：只有一端可成五的点
	 */
	public static final int CHONG_SI = 6;
	/**
	 * 活二：在走一步可成活三的点
	 */
	public static final int HUO_ER = 4;

	/**
	 * 眠三：在走一步可成冲四的点
	 */
	public static final int MIAN_SAN = 2;
	
	/**
	 * 眠二：在走一步可成眠三的点
	 */
	public static final int MIAN_ER = 1;

	// -------------------------------------------------------------
	/**
	 * 分析存在五连
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeWulian(int[] tmpChess, int isWho) {
		int count = 0;
		for (int i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		for (int i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (count == 4) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 分析活四 return 是否存在活四
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeHuosi(int[] tmpChess, int isWho) {
		int count = 0;
		int i = 0;
		boolean isSpace = false;
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN - (i + 1)] == 0) {
			isSpace = true;
		}
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN + i] == 0) {
			isSpace = true;
		} else {
			isSpace = false;
		}

		if (count == 3 && isSpace) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 分析活三 return 是否存在活三
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeHuosan(int[] tmpChess, int isWho) {
		int count = 0;
		int i = 0;
		boolean isSpace = false;
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN - (i + 1)] == 0) {
			isSpace = true;
		}
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN + i] == 0) {
			isSpace = true;
		} else {
			isSpace = false;
		}

		if (count == 2 && isSpace) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 分析冲四 return 是否存在冲四
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeChongsi(int[] tmpChess, int isWho) {
		int count = 0;
		int i = 0;
		boolean isSpace = false;
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN - (i + 1)] == 0) {
			isSpace = true;
		}
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN + i] == 0) {
			isSpace = true;
		}

		if (count == 3 && isSpace) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 分析活二 return 是否存在活二
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeHuoEr(int[] tmpChess, int isWho) {
		
		int count = 0;
		int i = 0;
		boolean isSpace = false;
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN - (i + 1)] == 0) {
			isSpace = true;
		}
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN + i] == 0) {
			isSpace = true;
		} else {
			isSpace = false;
		}

		if (count == 1 && isSpace) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 分析眠三 return 是否存在眠三
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeMianSan(int[] tmpChess, int isWho) {
		int count = 0;
		int i = 0;
		boolean isSpace = false;
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN - (i + 1)] == 0) {
			isSpace = true;
		}
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN + i] == 0) {
			isSpace = true;
		}

		if (count == 2 && isSpace) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * 分析眠二 return 是否存在眠二
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeMianEr(int[] tmpChess, int isWho) {
		int count = 0;
		int i = 0;
		boolean isSpace = false;
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN - (i + 1)] == 0) {
			isSpace = true;
		}
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN + i] == 0) {
			isSpace = true;
		}
		if (count == 1 && isSpace) {
			return true;
		}
		return false;
	}
	
}
