package com.wing.game.wzq.provider;

import com.wing.game.wzq.Application;

public class QiJu {
	private static QiJu instance=new QiJu();
	private QiJu(){
		instance = this;
	}
	public static QiJu getInstance(){
		return instance;
	}
	private static int[][] qiPan = new int[13][13];

	public void clear(){
		for(int i=0;i<(Application.GRID_SIZE+1);i++)
			for(int j=0;j<(Application.GRID_SIZE+1);j++)
			{
				qiPan[i][j]=Application.NO;
			}
	}
	public void setPosition(Position pos){
		qiPan[pos.posX][pos.posY]=pos.ID;
	}
	public boolean isValid(Position pos){
		return pos.isValid()&&(qiPan[pos.posX][pos.posY]==Application.NO);
	}
}
