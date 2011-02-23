package com.wing.game.wzq.provider;

import com.wing.game.wzq.ui.GameView;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class onDrawThread extends Thread{//ˢ֡�߳�
	private int span = 100;//˯�ߵĺ����� 
	private SurfaceHolder surfaceHolder;//SurfaceHolder������
	private GameView gameView;//gameView������
	private boolean flag = false;//ѭ����־λ
    public onDrawThread(SurfaceHolder surfaceHolder, GameView gameView) {//������
        this.surfaceHolder = surfaceHolder;//�õ�SurfaceHolder����
        this.gameView = gameView;//�õ�GameView������
    }
    public void setFlag(boolean flag) {//����ѭ�����
    	this.flag = flag;
    }
	public void run() {//��д�ķ���
		Canvas c;//����
        while (this.flag) {//ѭ������
            c = null;
            try {
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {
                	gameView.onDraw(c);//���û��Ʒ���
                }
            } finally {//��finally��֤�������һ����ִ��
                if (c != null) {
                	//������Ļ��ʾ����
                    this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            try{
            	Thread.sleep(span);//˯��span����
            }catch(Exception e){//�����쳣��Ϣ
            	e.printStackTrace();//��ӡ�쳣��ջ��Ϣ
            }
        }
	}
}
