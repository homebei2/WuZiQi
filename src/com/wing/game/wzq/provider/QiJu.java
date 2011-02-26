package com.wing.game.wzq.provider;
import android.util.Log;

import com.wing.game.wzq.Application;

public class QiJu {
	private static QiJu instance=new QiJu();
	private boolean isWin;
	private QiJu(){
		instance = this;
	}
	public static QiJu getInstance(){
		return instance;
	}
	private static int[][] qiPan = new int[Application.GRID_SIZE+1][Application.GRID_SIZE+1];

	public void clear(){
		for(int i=0;i<(Application.GRID_SIZE+1);i++)
			for(int j=0;j<(Application.GRID_SIZE+1);j++)
			{
				qiPan[i][j]=Application.NO;
			}
			isWin=false;
	}
	public void setPosition(Position pos){
		int x = pos.getPosX();
		int y = pos.getPosY();
		qiPan[x][y]=pos.getID();
		//横 
		int q = 0;
		for(int i=0;i<5;i++){
//			Log.i("setPositon","(x,y,i)"+x+","+y+","+i);
			if(x+i<=Application.GRID_SIZE&&(x+i-4)>=0){
				q=(qiPan[x+i][y]+qiPan[x+i-1][y]+qiPan[x+i-2][y]+qiPan[x+i-3][y]+qiPan[x+i-4][y]);
				if(q==5||q==-5){
					isWin=true;
					break;
				}
			}
			//竖
			if(y+i<=Application.GRID_SIZE&&(y+i-4)>=0){
				q=(qiPan[x][y+i]+qiPan[x][y+i-1]+qiPan[x][y+i-2]+qiPan[x][y+i-3]+qiPan[x][y+i-4]);
				if(q==5||q==-5){
					isWin=true;
					break;
				}
			}
			//左撇
			if(y+i<=Application.GRID_SIZE&&(y+i-4)>=0&&x+i<=Application.GRID_SIZE&&(x+i-4)>=0){
				Log.i("setPosition", (y+i-4)+","+(x+i-4)+","+(y+i)+","+(x+i));
				q=(qiPan[x+i][y+i]+qiPan[x+i-1][y+i-1]+qiPan[x+i-2][y+i-2]+qiPan[x+i-3][y+i-3]+qiPan[x+i-4][y+i-4]);
				if(q==5||q==-5){
					isWin=true;
					break;
				}
			}
			//右捺
			if(y+i<=Application.GRID_SIZE&&(y+i-4)>=0&&(x-i+4)<=Application.GRID_SIZE&&(x-i)>=0){
				q=(qiPan[x-i][y+i]+qiPan[x-i+1][y+i-1]+qiPan[x-i+2][y+i-2]+qiPan[x-i+3][y+i-3]+qiPan[x-i+4][y+i-4]);
				if(q==5||q==-5){
					isWin=true;
					break;
				}
			}
		}
	}
	public boolean isValid(Position pos){
		return pos.isValid()&&(qiPan[pos.getPosX()][pos.getPosY()]==Application.NO);
	}
	public boolean isWin(){
		return isWin;
	}
	public Position getAiBestPosition(){
		Position temp;
		clearPoints();
		analyzeChessMater();
		if (fiveBetterPoints[0].score >0) {
			temp = fiveBetterPoints[0];
		}else{
			analyzeChessMater();
			clearPoints();
			temp = fiveBetterPoints[0];
		}
		setPosition(temp);
		Log.i("getAiBestPosition", fiveBetterPoints[0].score+":"+temp.getPosX()+","+temp.getPosY());
		return temp;
//		for(int i=0;i<=Application.GRID_SIZE;i++){
//			for(int j=0;j<=Application.GRID_SIZE;j++){
//				if(qiPan[i][j]==Application.NO){		
//					Position temp = new Position(Application.B,i,j);
//					this.setPosition(temp);
//					return temp;
//				}
//			}
//		}
	}
	private void analyzeChessMater() {
		// 具体代码...
		int i, j, k;
		// 分析电脑的棋型/////////////////////////////////////////////////////
		for (i = 0; i <= Application.GRID_SIZE; i++) {
			for (j = 0; j <= Application.GRID_SIZE; j++) {
				if (qiPan[i][j] == 0) {
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
				//	materChess[i][j][0] = analyzeDir(tmpChess, isWho);
					// 找出竖向的棋子的棋型
					clearArray(tmpChessShu);

					for (k = 1; k <= Application.HALF_LEN; k++) {
						if ((i + k) <= Application.GRID_SIZE) {
							tmpChessShu[Application.HALF_LEN + (k - 1)] = qiPan[i
									+ k][j];
						}
						if ((i - k) >= 0) {
							tmpChessShu[Application.HALF_LEN - k] = qiPan[i
									- k][j];
						}
					}
				//	materChess[i][j][1] = analyzeDir(tmpChess, isWho);
					// 找出左斜的棋子的棋型
					clearArray(tmpChessZX);
					for (k = 1; k <= Application.HALF_LEN; k++) {
						if ((i + k) <= Application.GRID_SIZE && (j + k) <= Application.GRID_SIZE) {
							tmpChessZX[Application.HALF_LEN + (k - 1)] = qiPan[i
									+ k][j + k];
						}
						if ((i - k) >= 0 && (j - k) >= 0) {
							tmpChessZX[Application.HALF_LEN - k] = qiPan[i
									- k][j - k];
						}
					}
				//	materChess[i][j][2] = analyzeDir(tmpChess, isWho);
					// 找出右斜的棋子的棋型
					clearArray(tmpChessYX);
					for (k = 1; k <= Application.HALF_LEN; k++) {
						if ((i - k) >= 0 && (j + k)<= Application.GRID_SIZE) {
							tmpChessYX[Application.HALF_LEN + (k - 1)] = qiPan[i
									- k][j + k];
						}
						if ((i + k) <= Application.GRID_SIZE && (j - k) >= 0) {
							tmpChessYX[Application.HALF_LEN - k] = qiPan[i
									+ k][j - k];
						}
					}
					//保存较好的点
					int wScore,bScore;
					wScore = analyzeXlian(tmpChessShu,Application.W)+analyzeXlian(tmpChessShu,Application.W)
						+analyzeXlian(tmpChessZX,Application.W)+analyzeXlian(tmpChessYX,Application.W);
					bScore = analyzeXlian(tmpChessShu,Application.B)+analyzeXlian(tmpChessShu,Application.B)
					+analyzeXlian(tmpChessZX,Application.B)+analyzeXlian(tmpChessYX,Application.B);

					insertBetterChessPoint(new Position(Application.B,i,j,(wScore>bScore?wScore:bScore)));
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
	 * 分析存在几连
	 * 
	 * @param tmpChess
	 */
	public int analyzeXlian(int[] tmpChess, int isWho) {
		int count = 0;
		for (int i = 0; i < Application.HALF_LEN; i++) {
			if (tmpChess[Application.HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		for (int i = 0; i < Application.HALF_LEN; i++) {
			if (tmpChess[Application.HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}		
		return count;
	}
	// 保存前5个较好的落子点
	private Position[] fiveBetterPoints = new Position[5];
	private int[] tmpChessHeng = new int[Application.ANALYZE_LEN];
	private int[] tmpChessShu = new int[Application.ANALYZE_LEN];
	private int[] tmpChessZX = new int[Application.ANALYZE_LEN];
	private int[] tmpChessYX = new int[Application.ANALYZE_LEN];
	private void insertBetterChessPoint(Position cp) {
//		Log.i("insertBetterChessPoint", cp.score+":"+cp.getPosX()+","+cp.getPosY());
		int i, j = 0;
		Position tmpcp = null;
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
}
