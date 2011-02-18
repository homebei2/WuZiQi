package com.wing.game.wzq;
/**
 * 
 * @author Administrator
 * 记录棋子的走法、位置
 */
public class Position {
	int ID;//表明是什么棋子
	int posX;//起始的x坐标
	int posY;//起始的Y坐标
	int score;//值,估值时会用到
	public Position(int ID, int posX,int posY,int score){//构造器
		this.ID = ID;//棋子的类型
		this.posX = posX;
		this.posY = posY;
		this.score = score;
	}
}
