package com.wing.game.wzq.provider;

import android.util.Log;
import com.wing.game.wzq.Application;

public class QiJu {
	private static QiJu instance = new QiJu();
	private boolean isWin;

	private QiJu() {
		instance = this;
	}

	public static QiJu getInstance() {
		return instance;
	}

	private static int[][] qiPan = new int[Application.GRID_SIZE + 1][Application.GRID_SIZE + 1];
	public void clear() {
		for (int i = 0; i < (Application.GRID_SIZE + 1); i++)
			for (int j = 0; j < (Application.GRID_SIZE + 1); j++) {
				qiPan[i][j] = Application.NO;
			}
		isWin = false;
	}

	public void setPosition(Position pos) {
		int x = pos.getPosX();
		int y = pos.getPosY();
		qiPan[x][y] = pos.getID();
	}

	public boolean isValid(Position pos) {
		return pos.isValid()
				&& (qiPan[pos.getPosX()][pos.getPosY()] == Application.NO);
	}

	public boolean isWin() {
		if (isWin||RecordStack.getInstance().getCount()<=0)
			return isWin;
		else {
			// 横
			Position temp = RecordStack.getInstance().getPosition(
					RecordStack.getInstance().getCount() - 1);
			int x = temp.getPosX();
			int y = temp.getPosY();
			int q = 0;
			for (int i = 0; i < 5; i++) {
				// Log.i("isWin","(x,y,i)"+x+","+y+","+i);
				if (x + i <= Application.GRID_SIZE && (x + i - 4) >= 0) {
					q = (qiPan[x + i][y] + qiPan[x + i - 1][y]
							+ qiPan[x + i - 2][y] + qiPan[x + i - 3][y] + qiPan[x
							+ i - 4][y]);
					if (q == 5 || q == -5) {
						isWin = true;
						break;
					}
				}
				// 竖
				if (y + i <= Application.GRID_SIZE && (y + i - 4) >= 0) {
					q = (qiPan[x][y + i] + qiPan[x][y + i - 1]
							+ qiPan[x][y + i - 2] + qiPan[x][y + i - 3] + qiPan[x][y
							+ i - 4]);
					if (q == 5 || q == -5) {
						isWin = true;
						break;
					}
				}
				// 左斜
				if (y + i <= Application.GRID_SIZE && (y + i - 4) >= 0
						&& x + i <= Application.GRID_SIZE && (x + i - 4) >= 0) {
//					Log.i("isWin", (y + i - 4) + "," + (x + i - 4) + ","
//							+ (y + i) + "," + (x + i));
					q = (qiPan[x + i][y + i] + qiPan[x + i - 1][y + i - 1]
							+ qiPan[x + i - 2][y + i - 2]
							+ qiPan[x + i - 3][y + i - 3] + qiPan[x + i - 4][y
							+ i - 4]);
					if (q == 5 || q == -5) {
						isWin = true;
						break;
					}
				}
				// 右斜
				if (y + i <= Application.GRID_SIZE && (y + i - 4) >= 0
						&& (x - i + 4) <= Application.GRID_SIZE && (x - i) >= 0) {
					q = (qiPan[x - i][y + i] + qiPan[x - i + 1][y + i - 1]
							+ qiPan[x - i + 2][y + i - 2]
							+ qiPan[x - i + 3][y + i - 3] + qiPan[x - i + 4][y
							+ i - 4]);
					if (q == 5 || q == -5) {
						isWin = true;
						break;
					}
				}
			}
		}
		return isWin;
	}

	public void getAiBestPosition() {
		Position bestPosition;
		clearPoints();
		analyzeChessMater(0,0,Application.GRID_SIZE,Application.GRID_SIZE);
		if (fiveBetterPoints[0].getScore() > 30) {
			bestPosition = fiveBetterPoints[0];
		} else {
			Position[] tempPoints = new Position[5];
			Position temp = null;
			int goodIdx = 0;
			int i = 0;
			int startx, starty, endx, endy;
			for (i = 0; i < 5; i++) {
				tempPoints[i] = fiveBetterPoints[i];
			}
			for (i = 0; i < 5; i++) {
				clearPoints();
				if (tempPoints[i] == null)
					break;	
				startx = tempPoints[i].getPosX() - 5;
				starty = tempPoints[i].getPosY() - 5;
				if (startx < 0) {
					startx = 0;
				}
				if (starty < 0) {
					starty = 0;
				}
				endx = startx + 10;
				endy = starty + 10;
				if (endx > Application.GRID_SIZE) {
					endx = Application.GRID_SIZE;
				}
				if (endy > Application.GRID_SIZE) {
					endy = Application.GRID_SIZE;
				}				
				setPosition(new Position(Application.B,tempPoints[i].getPosX(),tempPoints[i].getPosY()));
				analyzeChessMater(startx, starty, endx, endy);
				if (temp != null) {
					if (temp.getScore() < fiveBetterPoints[0].getScore()) {
						temp = fiveBetterPoints[0];
						goodIdx = i;
					}
				} else {
					temp = fiveBetterPoints[0];
					goodIdx = i;
				}
				setPosition(new Position(Application.NO,tempPoints[i].getPosX(),tempPoints[i].getPosY()));
			}
			bestPosition = tempPoints[goodIdx];
		}
		setPosition(bestPosition);
		RecordStack.getInstance().put(bestPosition);
//		for (int i = 0; i < 5&&fiveBetterPoints[i]!=null; i++){
//			Log.e("getAiBestPosition", fiveBetterPoints[i].getScore() + ":"+ fiveBetterPoints[i].getPosX() + "," + fiveBetterPoints[i].getPosY());
//		}
	}

	private void analyzeChessMater(int sx, int sy, int ex, int ey ) {
		// 具体代码...
		int i, j, k;
		int tempScore = 0;
		// 分析电脑的棋型/////////////////////////////////////////////////////
		for (i = sx; i <= ex; i++) {
			for (j = sy; j <= ey; j++) {
				if (qiPan[i][j] == Application.NO) {
					// 找出横向的棋子的棋型
					clearArray(tmpChessHeng);
					for (k = 1; k <= Application.HALF_LEN; k++) {
						if ((j + k) <= Application.GRID_SIZE) {
							tmpChessHeng[Application.HALF_LEN + (k - 1)] = qiPan[i][j
									+ k];
						}
						if ((j - k) >= 0) {
							tmpChessHeng[Application.HALF_LEN - k] = qiPan[i][j
									- k];
						}
					}
					// materChess[i][j][0] = analyzeDir(tmpChess, isWho);
					// 找出竖向的棋子的棋型
					clearArray(tmpChessShu);

					for (k = 1; k <= Application.HALF_LEN; k++) {
						if ((i + k) <= Application.GRID_SIZE) {
							tmpChessShu[Application.HALF_LEN + (k - 1)] = qiPan[i
									+ k][j];
						}
						if ((i - k) >= 0) {
							tmpChessShu[Application.HALF_LEN - k] = qiPan[i - k][j];
						}
					}
					// materChess[i][j][1] = analyzeDir(tmpChess, isWho);
					// 找出左斜的棋子的棋型
					clearArray(tmpChessZX);
					for (k = 1; k <= Application.HALF_LEN; k++) {
						if ((i + k) <= Application.GRID_SIZE
								&& (j + k) <= Application.GRID_SIZE) {
							tmpChessZX[Application.HALF_LEN + (k - 1)] = qiPan[i
									+ k][j + k];
						}
						if ((i - k) >= 0 && (j - k) >= 0) {
							tmpChessZX[Application.HALF_LEN - k] = qiPan[i - k][j
									- k];
						}
					}
					// materChess[i][j][2] = analyzeDir(tmpChess, isWho);
					// 找出右斜的棋子的棋型
					clearArray(tmpChessYX);
					for (k = 1; k <= Application.HALF_LEN; k++) {
						if ((i - k) >= 0 && (j + k) <= Application.GRID_SIZE) {
							tmpChessYX[Application.HALF_LEN + (k - 1)] = qiPan[i
									- k][j + k];
						}
						if ((i + k) <= Application.GRID_SIZE && (j - k) >= 0) {
							tmpChessYX[Application.HALF_LEN - k] = qiPan[i + k][j
									- k];
						}
					}
					// 保存较好的点
					int wScore, bScore;
					wScore = analyzeXlian(tmpChessHeng, Application.W)
							+ analyzeXlian(tmpChessShu, Application.W)
							+ analyzeXlian(tmpChessZX, Application.W)
							+ analyzeXlian(tmpChessYX, Application.W);
					bScore = analyzeXlian(tmpChessHeng, Application.B)
							+ analyzeXlian(tmpChessShu, Application.B)
							+ analyzeXlian(tmpChessZX, Application.B)
							+ analyzeXlian(tmpChessYX, Application.B);
					if(wScore>=tempScore||bScore>=tempScore){
						tempScore = wScore > bScore ? wScore : bScore;
						insertBetterChessPoint(new Position(Application.B, i, j,tempScore));
					}
				}
			}
		}
	}

	private void clearArray(int[] array) {
		for (int i = 0; i < array.length; i++)
			array[i] = 0;
	}

	private void clearPoints() {
		for (int i = 0; i < 5; i++)
			fiveBetterPoints[i] = null;
	}

	/**
	 * 分析具体的方向
	 * 
	 * @param tmpChess
	 *            该方向的棋型
	 */
	private int analyzeXlian(int[] tmpChess, int isWho) {
		// 如果满足活四，就不活三、满足活三就不冲四，如此...
		int score = 0;
		if (ChessFormUtil.getInstance().analyzeWulian(tmpChess, isWho)) {
			score = ChessFormUtil.WU_LIAN;
		} else if (ChessFormUtil.getInstance().analyzeHuosi(tmpChess, isWho)) {
			score = ChessFormUtil.HUO_SI;
		} else if (ChessFormUtil.getInstance().analyzeHuosan(tmpChess, isWho)) {
			score = ChessFormUtil.HUO_SAN;
		} else if (ChessFormUtil.getInstance().analyzeChongsi(tmpChess, isWho)) {
			score = ChessFormUtil.CHONG_SI;
		} else if (ChessFormUtil.getInstance().analyzeHuoEr(tmpChess, isWho)) {
			score = ChessFormUtil.HUO_ER;
		} else if (ChessFormUtil.getInstance().analyzeMianSan(tmpChess, isWho)) {
			score = ChessFormUtil.MIAN_SAN;
		} else if (ChessFormUtil.getInstance().analyzeMianEr(tmpChess, isWho)) {
			score = ChessFormUtil.MIAN_ER;
		} else {
			score = 0;
		}

		return score;
	}

	// 保存前5个较好的落子点
	private Position[] fiveBetterPoints = new Position[5];
	private int[] tmpChessHeng = new int[Application.ANALYZE_LEN];
	private int[] tmpChessShu = new int[Application.ANALYZE_LEN];
	private int[] tmpChessZX = new int[Application.ANALYZE_LEN];
	private int[] tmpChessYX = new int[Application.ANALYZE_LEN];

	private void insertBetterChessPoint(Position cp) {
//		 Log.i("insertBetterChessPoint",cp.getScore()+":"+cp.getPosX()+","+cp.getPosY());
		int i, j = 0;
		Position tmpcp = null;
		for (i = 0; i < 5; i++) {
			if (null != fiveBetterPoints[i]) {
				if (cp.getScore() > fiveBetterPoints[i].getScore()) {
					tmpcp = fiveBetterPoints[i];
					fiveBetterPoints[i] = cp;
					for (j = i; j < 5; j++) {
						if (null != fiveBetterPoints[j]) {
							if (tmpcp.getScore() > fiveBetterPoints[j]
									.getScore()) {
								cp = fiveBetterPoints[j];
								fiveBetterPoints[j] = tmpcp;
								tmpcp =cp;
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
}
