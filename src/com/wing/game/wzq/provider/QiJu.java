package com.wing.game.wzq.provider;

import java.util.Random;

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
	}
	public void setPosition(Position pos){
		int x = pos.getPosX();
		int y = pos.getPosY();
		qiPan[x][y]=pos.getID();
		//ºá 
		int q = 0;
		for(int i=0;i<5;i++){
			Log.i("setPositon","(x,y,i)"+x+","+y+","+i);
			if(x+i<=Application.GRID_SIZE&&(x+i-4)>=0){
				q=(qiPan[x+i][y]+qiPan[x+i-1][y]+qiPan[x+i-2][y]+qiPan[x+i-3][y]+qiPan[x+i-4][y]);
				if(q==5||q==-5){
					isWin=true;
					break;
				}
			}
			if(y+i<=Application.GRID_SIZE&&(y+i-4)>=0){
				q=(qiPan[x][y+i]+qiPan[x][y+i-1]+qiPan[x][y+i-2]+qiPan[x][y+i-3]+qiPan[x][y+i-4]);
				if(q==5||q==-5){
					isWin=true;
					break;
				}
			}
			if(y+i<=Application.GRID_SIZE&&(y+i-4)>=0&&x+i<=Application.GRID_SIZE&&(x+i-4)>=0){
				q=(qiPan[x+i][y+i]+qiPan[x+i+1][y+i-1]+qiPan[x+i-2][y+i-2]+qiPan[x+i-3][y+i-3]+qiPan[x+i-4][y+i-4]);
				if(q==5||q==-5){
					isWin=true;
					break;
				}
			}
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
		for(int i=0;i<=Application.GRID_SIZE;i++){
			for(int j=0;j<=Application.GRID_SIZE;j++){
				if(qiPan[i][j]==Application.NO){		
					Position temp = new Position(Application.B,i,j);
					this.setPosition(temp);
					return temp;
				}
			}
		}
		return null;
	}
}
