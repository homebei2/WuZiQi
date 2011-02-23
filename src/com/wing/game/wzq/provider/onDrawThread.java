package com.wing.game.wzq.provider;

import com.wing.game.wzq.ui.GameView;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class onDrawThread extends Thread{//刷帧线程
	private int span = 100;//睡眠的毫秒数 
	private SurfaceHolder surfaceHolder;//SurfaceHolder的引用
	private GameView gameView;//gameView的引用
	private boolean flag = false;//循环标志位
    public onDrawThread(SurfaceHolder surfaceHolder, GameView gameView) {//构造器
        this.surfaceHolder = surfaceHolder;//得到SurfaceHolder引用
        this.gameView = gameView;//得到GameView的引用
    }
    public void setFlag(boolean flag) {//设置循环标记
    	this.flag = flag;
    }
	public void run() {//重写的方法
		Canvas c;//画布
        while (this.flag) {//循环绘制
            c = null;
            try {
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {
                	gameView.onDraw(c);//调用绘制方法
                }
            } finally {//用finally保证下面代码一定被执行
                if (c != null) {
                	//更新屏幕显示内容
                    this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            try{
            	Thread.sleep(span);//睡眠span毫秒
            }catch(Exception e){//不会异常信息
            	e.printStackTrace();//打印异常堆栈信息
            }
        }
	}
}
