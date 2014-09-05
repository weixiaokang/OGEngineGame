
package com.fairy.wuziqi.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.fairy.wuziqi.R;
import com.fairy.wuziqi.uilts.ChessFormUtil;
import com.fairy.wuziqi.uilts.KeyCode;

class ChessPoint {
	int x;
	int y;
	int score;
}

public class GameView extends View {
	private final int CHESS_GRID = KeyCode.GRID_SIZE-1 ;

	private final static int CHECK_DIR = 4;

	private int chess_dia =22;

	private int grid_width;
	private int mStartX;
	private int mStartY;

	private Bitmap[] mChessBW;

	private int[][] mChessTable = new int[CHESS_GRID][CHESS_GRID];  

	private int[][][] computerTable = new int[CHESS_GRID][CHESS_GRID][CHECK_DIR];
	private int[][][] playerTable = new int[CHESS_GRID][CHESS_GRID][CHECK_DIR];

	public static final int BLACK = 2;
	public static final int WHITE = 1;

	private int whoTurn = WHITE;
	private int mWinFlag = 0;

	private static final int GAMESTATE_PRE = 0;
	public static final int GAMESTATE_RUN = 1;
	public static final int GAMESTATE_PAUSE = 2;
	private static final int GAMESTATE_END = 3;
	public int mGameState = GAMESTATE_RUN;
	public TextView mStatusTextView;
	private CharSequence mText;
	private CharSequence STRING_WIN = "you win";
	private CharSequence STRING_LOSE = "you lose";
	private CharSequence STRING_EQUAL = "deuce";
	private Paint mPaint;
	private boolean bitmapLoaded = false;
	private ChessFormUtil cfUtil;
	private ChessPoint[] fiveBetterPoints = new ChessPoint[5];

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		cfUtil = new ChessFormUtil();
		mPaint =new Paint();
		mPaint.setAntiAlias(true);
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		cfUtil = new ChessFormUtil();
	}

	public void init() {
		mGameState = 1;
		whoTurn = WHITE;
		mWinFlag = 0;

		for (int i = 0; i < CHESS_GRID; i++) {
			for (int j = 0; j < CHESS_GRID; j++) {
				mChessTable[i][j] = 0;
				for (int k = 0; k < CHECK_DIR; k++) {
					computerTable[i][j][k] = 0;
					playerTable[i][j][k] = 0;
				}
			}
		}
	}
	public void setTextView(TextView tv) {
		mStatusTextView = tv;
		mStatusTextView.setVisibility(View.INVISIBLE);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (w <= h)
			grid_width = w / KeyCode.GRID_SIZE;
		else
			grid_width = h / KeyCode.GRID_SIZE;
		chess_dia = grid_width - 4;
		mStartX = (w - KeyCode.GRID_SIZE * grid_width) >> 1;
		mStartY = (h - KeyCode.GRID_SIZE * grid_width) >> 1;
		if (!bitmapLoaded) {
			mChessBW = new Bitmap[2];
			Bitmap bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			Resources r = this.getContext().getResources();
			Drawable tile = r.getDrawable(R.drawable.black);
			tile.setBounds(0, 0, chess_dia, chess_dia);
			tile.draw(canvas);
			mChessBW[0] = bitmap;
			// bitmap.recycle(); //
			// ����bitmap���ڴ棬��֤��Bitmap�����գ�����һ�㲻��Ҫ�ֶ����ã����GC������Bitmap
			bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
					Bitmap.Config.ARGB_8888);
			canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
			// ��׼��Bitmap���ơ�
			tile = r.getDrawable(R.drawable.whrite);
			tile.setBounds(0, 0, chess_dia, chess_dia);
			tile.draw(canvas);
			mChessBW[1] = bitmap;
			bitmapLoaded = true;
			init();
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (mGameState) {
			case GAMESTATE_PRE:
				break;
			case GAMESTATE_RUN: {
				int x;
				int y;
				float x0 = grid_width - (event.getX() - mStartX) % grid_width;
				float y0 = grid_width - (event.getY() - mStartY) % grid_width;
				if (x0 < (grid_width >> 1)) {
					x = (int) ((event.getX() - mStartX) / grid_width);
				} else {
					x = (int) ((event.getX() - mStartX) / grid_width) - 1;
				}
				if (y0 < (grid_width >> 1)) {
					y = (int) ((event.getY() - mStartY) / grid_width);
				} else {
					y = (int) ((event.getY() - mStartY) / grid_width) - 1;
				}
				Log.v("x,y", "" + y + "," + x);
				if ((x >= 0 && x < CHESS_GRID) && (y >= 0 && y < CHESS_GRID)) {
					if (mChessTable[x][y] == 0) {
						if (whoTurn == WHITE) {
							putChess(x, y, WHITE);
						//	SoundPlayer.playSound(R.raw.effect);
							if (checkWin(WHITE)) {
								mText = STRING_WIN;
								mGameState = GAMESTATE_END;
								showTextView(mText);
							} else if (checkFull()) {// �����������
								mText = STRING_EQUAL;
								mGameState = GAMESTATE_END;
								showTextView(mText);
							}
							whoTurn = BLACK;
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							// ���Կ�ʼ����
							analyzeChess();
							// ��������֮��ʼ���������ʱ�Ƿ�ʤ��
							if (checkWin(BLACK)) { // ����Ǻ���Ӯ��
								mText = STRING_LOSE;
								mGameState = GAMESTATE_END;
								showTextView(mText);
							} else if (checkFull()) {// �����������
								mText = STRING_EQUAL;
								mGameState = GAMESTATE_END;
								showTextView(mText);
							}
							whoTurn = WHITE;
						}
					}
				}
			}
				break;
			case GAMESTATE_PAUSE:
				break;
			case GAMESTATE_END:
				mGameState = GAMESTATE_RUN;
				this.setVisibility(View.VISIBLE);
				this.mStatusTextView.setVisibility(View.INVISIBLE);
				this.init();
				this.invalidate();
				break;
			}
			this.invalidate();
		}
		return true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		Log.e("lee", " " + keyCode);
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			switch (mGameState) {
			case GAMESTATE_PRE:
				break;
			case GAMESTATE_RUN:
				break;
			case GAMESTATE_PAUSE:
				break;
			case GAMESTATE_END: {// ��Ϸ����󣬰�CENTER�����
				Log.e("lee", "FIRE");
				mGameState = GAMESTATE_RUN;
				this.setVisibility(View.VISIBLE);
				this.mStatusTextView.setVisibility(View.INVISIBLE);
				this.init();
				this.invalidate();
			}
				break;
			}
		}
		return super.onKeyDown(keyCode, msg);
	}
	private void clearArray(int[] array) {
		for (int i = 0; i < array.length; i++)
			array[i] = 0;
	}
	private void clearChessArray() {
		int i, j;
		for (i = 0; i < CHESS_GRID; i++) {
			for (j = 0; j < CHESS_GRID; j++) {
				computerTable[i][j][0] = 0;
				computerTable[i][j][1] = 0;
				computerTable[i][j][2] = 0;
				computerTable[i][j][3] = 0;

				playerTable[i][j][0] = 0;
				playerTable[i][j][1] = 0;
				playerTable[i][j][2] = 0;
				playerTable[i][j][3] = 0;

			}
		}

		for (i = 0; i < 5; i++) {
			fiveBetterPoints[i] = null;
		}

	}

	/**
	 * ��������ķ���
	 * 
	 * @param tmpChess
	 *            �÷��������
	 */
	private int analyzeDir(int[] tmpChess, int isWho) {
		// ���������ģ��Ͳ������������Ͳ����ģ����...
		int score = 0;
		if (cfUtil.analyzeWulian(tmpChess, isWho)) {
			score = ChessFormUtil.WU_LIAN;
		} else if (cfUtil.analyzeHuosi(tmpChess, isWho)) {
			score = ChessFormUtil.HUO_SI;
		} else if (cfUtil.analyzeHuosan(tmpChess, isWho)) {
			score = ChessFormUtil.HUO_SAN;
		} else if (cfUtil.analyzeChongsi(tmpChess, isWho)) {
			score = ChessFormUtil.CHONG_SI;
		} else if (cfUtil.analyzeHuoEr(tmpChess, isWho)) {
			score = ChessFormUtil.HUO_ER;
		} else if (cfUtil.analyzeMianSan(tmpChess, isWho)) {
			score = ChessFormUtil.MIAN_SAN;
		} else if (cfUtil.analyzeMianEr(tmpChess, isWho)) {
			score = ChessFormUtil.MIAN_ER;
		} else {
			score = 0;
		}

		return score;
	}
	/**
	 * �������ܷ�����õ�����ص�
	 */
	private void analyzeChess() {
		
		if (whoTurn == BLACK) {
			// �뵱ǰ���������Ҫ�������͵���������
			clearChessArray();
			analyzeChessMater(computerTable, BLACK, 0, 0, CHESS_GRID,
					CHESS_GRID);
			// ������ҵ�����/////////////////////////////////////////////////////
			analyzeChessMater(playerTable, WHITE, 0, 0, CHESS_GRID, CHESS_GRID);

			ChessPoint bestPoint = findBestPoint();
			// Log.v("Best Chess", "x = " + bestPoint.x + ", y = " +
			// bestPoint.y);
			if(bestPoint!=null){
				
			mChessTable[bestPoint.y][bestPoint.x] = BLACK;
		//	SoundPlayer.playSound(R.raw.effect);
			}

		}
	}

	/**
	 * ����ָ��������
	 * 
	 * @param materChess
	 *            ��������
	 */
	private void analyzeChessMater(int[][][] materChess, int isWho, int sx,
			int sy, int ex, int ey) {
		int[] tmpChess = new int[ChessFormUtil.ANALYZE_LEN];

		// �������...
		int i, j, k;
		// �������Ե�����/////////////////////////////////////////////////////
		for (i = sy; i < ey; i++) {
			for (j = sx; j < ex; j++) {
				if (mChessTable[i][j] == 0) {
					// �ҳ���������ӵ�����
					clearArray(tmpChess);
					for (k = 1; k <= ChessFormUtil.HALF_LEN; k++) {
						if ((j + k) < ex) {
							tmpChess[ChessFormUtil.HALF_LEN + (k - 1)] = mChessTable[i][j
									+ k];
						}
						if ((j - k) >= 0) {
							tmpChess[ChessFormUtil.HALF_LEN - k] = mChessTable[i][j
									- k];
						}
					}
					materChess[i][j][0] = analyzeDir(tmpChess, isWho);
					// �ҳ���������ӵ�����
					clearArray(tmpChess);

					for (k = 1; k <= ChessFormUtil.HALF_LEN; k++) {
						if ((i + k) < ey) {
							tmpChess[ChessFormUtil.HALF_LEN + (k - 1)] = mChessTable[i
									+ k][j];
						}
						if ((i - k) >= 0) {
							tmpChess[ChessFormUtil.HALF_LEN - k] = mChessTable[i
									- k][j];
						}
					}
					materChess[i][j][1] = analyzeDir(tmpChess, isWho);
					// �ҳ���б�����ӵ�����
					clearArray(tmpChess);
					for (k = 1; k <= ChessFormUtil.HALF_LEN; k++) {
						if ((i + k) < ey && (j + k) < ex) {
							tmpChess[ChessFormUtil.HALF_LEN + (k - 1)] = mChessTable[i
									+ k][j + k];
						}
						if ((i - k) >= 0 && (j - k) >= 0) {
							tmpChess[ChessFormUtil.HALF_LEN - k] = mChessTable[i
									- k][j - k];
						}
					}
					materChess[i][j][2] = analyzeDir(tmpChess, isWho);
					// �ҳ���б�����ӵ�����
					clearArray(tmpChess);
					for (k = 1; k <= ChessFormUtil.HALF_LEN; k++) {
						if ((i - k) >= 0 && (j + k) < ex) {
							tmpChess[ChessFormUtil.HALF_LEN + (k - 1)] = mChessTable[i
									- k][j + k];
						}
						if ((i + k) < ey && (j - k) >= 0) {
							tmpChess[ChessFormUtil.HALF_LEN - k] = mChessTable[i
									+ k][j - k];
						}
					}
					materChess[i][j][3] = analyzeDir(tmpChess, isWho);
					// mChessTable[i][]
					// �����������õ����...
				}
			}
		}
	}

	private void insertBetterChessPoint(ChessPoint cp) {
		int i, j = 0;
		ChessPoint tmpcp = null;
		for (i = 0; i < 5; i++) {
			if (null != fiveBetterPoints[i]) {
				if (cp.score > fiveBetterPoints[i].score) {
					tmpcp = fiveBetterPoints[i];
					fiveBetterPoints[i] = cp;
					for (j = i; j < 5; j++) {
						if (null != fiveBetterPoints[j]) {
							if (tmpcp.score > fiveBetterPoints[j].score) {
								tmpcp = fiveBetterPoints[j];
								fiveBetterPoints[j] = tmpcp;
							}
						} else {
							fiveBetterPoints[j] = tmpcp;
							break;
						}
					}
					break;
				}
			} else {
				fiveBetterPoints[i] = cp;
				break;
			}
		}

		tmpcp = null;
	}

	/**
	 * �ҵ���ѵ�
	 * 
	 * @return ��ѵ�
	 */
	private ChessPoint findBestPoint() {
		int i, j;
		ChessPoint point;
		int maxScore = 0;
		int tmpScore = 0;
		for (i = 0; i < CHESS_GRID; i++) {
			for (j = 0; j < CHESS_GRID; j++) {
				// ���ԱȽ�
				tmpScore = computerTable[i][j][0];
				tmpScore += computerTable[i][j][1];
				tmpScore += computerTable[i][j][2];
				tmpScore += computerTable[i][j][3];
				if (maxScore <= tmpScore) {
					maxScore = tmpScore;
					point = new ChessPoint();
					point.x = j;
					point.y = i;
					point.score = maxScore;
					// ��������0��Ϊ��С������룬��Ϊ
					// ���¸õ����壬��ķ���Ҳ��Ϊ0
					// �������ﲻ���жϾͻὫ����ĵط��滻��
					if (maxScore > 0) {
						insertBetterChessPoint(point);
					}

				}
				// ��ұȽ�
				tmpScore = playerTable[i][j][0];
				tmpScore += playerTable[i][j][1];
				tmpScore += playerTable[i][j][2];
				tmpScore += playerTable[i][j][3];
				if (maxScore <= tmpScore) {
					maxScore = tmpScore;
					point = new ChessPoint();
					point.x = j;
					point.y = i;
					point.score = maxScore;
					if (maxScore > 0) {
						insertBetterChessPoint(point);
					}
				}

			}
		}

		// Log.v("cmaxpoint = ", "" + cMaxScore);
		// Log.v("pmaxpoint = ", "" + pMaxScore);

		return analyzeBetterChess();
	}

	/**
	 * �ҵ������бȽϺõĵ�
	 * 
	 * @return ��ѵ�
	 */
	private ChessPoint findBetterPoint(int sx, int sy, int ex, int ey) {
		int i, j;
		ChessPoint point;
		int maxScore = 0;
		int tmpScore = 0;
		for (i = sy; i < ey; i++) {
			for (j = sx; j < ex; j++) {
				// ���ԱȽ�
				tmpScore = computerTable[i][j][0];
				tmpScore += computerTable[i][j][1];
				tmpScore += computerTable[i][j][2];
				tmpScore += computerTable[i][j][3];
				if (maxScore <= tmpScore) {
					maxScore = tmpScore;
					point = new ChessPoint();
					point.x = j;
					point.y = i;
					point.score = maxScore;
					insertBetterChessPoint(point);
				}
				// ��ұȽ�
				tmpScore = playerTable[i][j][0];
				tmpScore += playerTable[i][j][1];
				tmpScore += playerTable[i][j][2];
				tmpScore += playerTable[i][j][3];
				if (maxScore <= tmpScore) {
					maxScore = tmpScore;
					point = new ChessPoint();
					point.x = j;
					point.y = i;
					point.score = maxScore;
					insertBetterChessPoint(point);
				}

			}
		}

		return fiveBetterPoints[0];
	}

	private ChessPoint analyzeBetterChess() {
		if (fiveBetterPoints[0]!=null&&fiveBetterPoints[0].score > 30) {
			return fiveBetterPoints[0];
		} else {
			ChessPoint betterPoint = null;
			ChessPoint tmpPoint = null;

			int goodIdx = 0;
			int i = 0;
			int startx, starty, endx, endy;
			ChessPoint[] fbpTmp = new ChessPoint[5];
			for (i = 0; i < 5; i++) {
				fbpTmp[i] = fiveBetterPoints[i];
			}

			for (i = 0; i < 5; i++) {
				if (fbpTmp[i] == null)
					break;
				mChessTable[fbpTmp[i].y][fbpTmp[i].x] = BLACK;
				clearChessArray();

				startx = fbpTmp[i].x - 5;
				starty = fbpTmp[i].y - 5;

				if (startx < 0) {
					startx = 0;
				}

				if (starty < 0) {
					starty = 0;
				}

				endx = startx + 10;
				endy = starty + 10;

				if (endx > CHESS_GRID) {
					endx = CHESS_GRID;
				}

				if (endy > CHESS_GRID) {
					endy = CHESS_GRID;
				}
				analyzeChessMater(computerTable, BLACK, startx, starty, endx,
						endy);
				// ������ҵ�����/////////////////////////////////////////////////////
				analyzeChessMater(playerTable, WHITE, startx, starty, endx,
						endy);
				tmpPoint = findBetterPoint(startx, starty, endx, endy);
				if (betterPoint != null) {
					if (betterPoint.score <= tmpPoint.score) {
						betterPoint = tmpPoint;
						goodIdx = i;
					}
				} else {
					betterPoint = tmpPoint;
					goodIdx = i;
				}

				mChessTable[fbpTmp[i].y][fbpTmp[i].x] = 0;
			}
			tmpPoint = null;
			betterPoint = null;
			return fbpTmp[goodIdx];
		}

	}

	@Override
	public void onDraw(Canvas canvas) {
		{
			Paint paintRect = new Paint();
			paintRect.setColor(Color.rgb(98,98, 98));
			paintRect.setStrokeWidth(2);
			paintRect.setStyle(Style.STROKE);
			paintRect.setAntiAlias(true);
			for (int i = 1; i < KeyCode.GRID_SIZE; i++) {
				for (int j = 1; j < KeyCode.GRID_SIZE-1; j++) {
					int mLeft = i * grid_width ;
					int mTop = j * grid_width +mStartY;
					int mRright =  grid_width+grid_width;
					int mBottom =  grid_width+mTop;
					canvas.drawRect(mLeft, mTop, mRright, mBottom, paintRect);
				}
			}

			// �����̵���߿�
//			paintRect.setStrokeWidth(4);
//			canvas.drawRect(mStartX, mStartY, mStartX + grid_width * KeyCode.GRID_SIZE,
//					mStartY + grid_width * KeyCode.GRID_SIZE, paintRect);
		}

		// ������

		if (bitmapLoaded) {
			for (int i = 0; i < CHESS_GRID; i++) {
				for (int j = 0; j < CHESS_GRID; j++) {
					if (mChessTable[i][j] == BLACK) {
						// ͨ��ͼƬ����
						canvas.drawBitmap(mChessBW[0], mStartX + (i + 1)
								* grid_width - (chess_dia >> 1), mStartY
								+ (j + 1) * grid_width - (chess_dia >> 1),
								mPaint);
						///SoundPlayer.playSound(R.raw.effect);
					} else if (mChessTable[i][j] == WHITE) {
						// ͨ��ͼƬ����
						canvas.drawBitmap(mChessBW[1], mStartX + (i + 1)
								* grid_width - (chess_dia >> 1), mStartY
								+ (j + 1) * grid_width - (chess_dia >> 1),
								mPaint);
						//SoundPlayer.playSound(R.raw.effect);

					}

				}
			}
		}
	}

	private void putChess(int x, int y, int blackwhite) {
		mChessTable[x][y] = blackwhite;
	}

	private boolean checkWin(int wbflag) {
		for (int i = 0; i < KeyCode.GRID_SIZE - 1; i++)
			for (int j = 0; j < KeyCode.GRID_SIZE - 1; j++) {
				// ���Ǽ�������ߣ��μ�Ӣ�����
				// �������������
				if (((i + 4) < (KeyCode.GRID_SIZE - 1))
						&& (mChessTable[i][j] == wbflag)
						&& (mChessTable[i + 1][j] == wbflag)
						&& (mChessTable[i + 2][j] == wbflag)
						&& (mChessTable[i + 3][j] == wbflag)
						&& (mChessTable[i + 4][j] == wbflag)) {
					// Log.e("check win or loss:", wbflag + "win");

					mWinFlag = wbflag;
				}

				// ����5������
				if (((j + 4) < (KeyCode.GRID_SIZE - 1))
						&& (mChessTable[i][j] == wbflag)
						&& (mChessTable[i][j + 1] == wbflag)
						&& (mChessTable[i][j + 2] == wbflag)
						&& (mChessTable[i][j + 3] == wbflag)
						&& (mChessTable[i][j + 4] == wbflag)) {
					// Log.e("check win or loss:", wbflag + "win");

					mWinFlag = wbflag;
				}

				// ���ϵ�����5������
				if (((j + 4) < (KeyCode.GRID_SIZE - 1)) && ((i + 4) < (KeyCode.GRID_SIZE - 1))
						&& (mChessTable[i][j] == wbflag)
						&& (mChessTable[i + 1][j + 1] == wbflag)
						&& (mChessTable[i + 2][j + 2] == wbflag)
						&& (mChessTable[i + 3][j + 3] == wbflag)
						&& (mChessTable[i + 4][j + 4] == wbflag)) {
					// Log.e("check win or loss:", wbflag + "win");

					mWinFlag = wbflag;
				}

				// ���ϵ�����5������
				if (((i - 4) >= 0) && ((j + 4) < (KeyCode.GRID_SIZE - 1))
						&& (mChessTable[i][j] == wbflag)
						&& (mChessTable[i - 1][j + 1] == wbflag)
						&& (mChessTable[i - 2][j + 2] == wbflag)
						&& (mChessTable[i - 3][j + 3] == wbflag)
						&& (mChessTable[i - 4][j + 4] == wbflag)) {
					// Log.e("check win or loss:", wbflag + "win");

					mWinFlag = wbflag;
				}
			}

		if (mWinFlag == wbflag) {
			return true;
		} else
			return false;

	}

	private boolean checkFull() {
		int mNotEmpty = 0;
		for (int i = 0; i < KeyCode.GRID_SIZE - 1; i++)
			for (int j = 0; j < KeyCode.GRID_SIZE - 1; j++) {
				if (mChessTable[i][j] != 0)
					mNotEmpty += 1;
			}

		if (mNotEmpty == (KeyCode.GRID_SIZE - 1) * (KeyCode.GRID_SIZE - 1))
			return true;
		else
			return false;
	}

	private void showTextView(CharSequence mT) {
		this.mStatusTextView.setText(mT);
		mStatusTextView.setVisibility(View.VISIBLE);

	}

}