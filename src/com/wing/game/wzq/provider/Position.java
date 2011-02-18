package com.wing.game.wzq.provider;

import com.wing.game.wzq.Application;

/**
 * 
 * @author Administrator
 * 记录棋子的走法、位置
 */
public class Position {
	int ID;//表明是什么棋子
	int posX;//起始的x坐标
	int posY;//起始的Y坐标
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	int score;//值,估值时会用到
	public Position(int ID, int posX,int posY,int score){//构造器
		this.ID = ID;//棋子的类型
		this.posX = posX;
		this.posY = posY;
		this.score = score;
	}
	public Position(int ID, int posX,int posY){//构造器
		this.ID = ID;//棋子的类型
		this.posX = posX;
		this.posY = posY;
		this.score = 0;
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		Position pos = (Position)o;
		if(posX==pos.posX&&posY==pos.posY)
			return true;
		else
			return false;
	}
	public boolean isValid(){
		return (0<=posX&&posX<=Application.GRID_SIZE)&&(0<=posY&&posY<=Application.GRID_SIZE);
	}
	
}
