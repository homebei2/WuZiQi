package com.wing.game.wzq;
/**
 * 
 * @author Administrator
 * ��¼���ӵ��߷���λ��
 */
public class Position {
	int ID;//������ʲô����
	int posX;//��ʼ��x����
	int posY;//��ʼ��Y����
	int score;//ֵ,��ֵʱ���õ�
	public Position(int ID, int posX,int posY,int score){//������
		this.ID = ID;//���ӵ�����
		this.posX = posX;
		this.posY = posY;
		this.score = score;
	}
}
