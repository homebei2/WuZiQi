package com.wing.game.wzq.provider;

import com.wing.game.wzq.Application;

/**
 * 
 * @author Administrator
 * ��¼���ӵ��߷���λ��
 */
public class Position {
	int ID;//������ʲô����
	int posX;//��ʼ��x����
	int posY;//��ʼ��Y����
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
	int score;//ֵ,��ֵʱ���õ�
	public Position(int ID, int posX,int posY,int score){//������
		this.ID = ID;//���ӵ�����
		this.posX = posX;
		this.posY = posY;
		this.score = score;
	}
	public Position(int ID, int posX,int posY){//������
		this.ID = ID;//���ӵ�����
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
