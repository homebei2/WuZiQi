package com.wing.game.wzq.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wing.game.wzq.Application;
import com.wing.game.wzq.R;
import com.wing.game.wzq.provider.Position;
import com.wing.game.wzq.provider.QiJu;
import com.wing.game.wzq.provider.RecordStack;
import com.wing.game.wzq.provider.onDrawThread;

public class GameView  extends SurfaceView implements SurfaceHolder.Callback{
	private Bitmap wBitmap,wBitmapLast, bBitmap,bBitmapLast,bg;
	private Bitmap[] cursor;
	private onDrawThread drawThread;
	private int cursorTime;
	
	private int chess_dia = 22; // 棋的直径
	private int grid_width; // 棋盘格的宽度
	private int mStartX;// 棋盘定位的左上角X
	private int mStartY;// 棋盘定位的左上角Y	
	private Position lastPos;
	private Paint mPaint; 
	
	private boolean isTouched=false; 
	private boolean runMode=Application.SINGLEPLAYER;
	private boolean isWho = Application.WHITE;
	
	public GameView(Context context) {
		super(context);
		this.getHolder().addCallback(this);
		this.setFocusable(true); // 20090530
		this.setFocusableInTouchMode(true);
		drawThread = new onDrawThread(getHolder(),this);
	}
	public GameView(Context context, AttributeSet attrs) { // 好像调用的是这个构造函数，为什么不是前面的呢
		super(context, attrs);
		this.getHolder().addCallback(this);
		this.setFocusable(true); // 20090530
		this.setFocusableInTouchMode(true);
		drawThread = new onDrawThread(getHolder(),this);
	}
	private void init(int width,int height){
		if (width <= height)
			grid_width = (width-10) / Application.GRID_SIZE;
		else
			grid_width = (height-10) / Application.GRID_SIZE;

		chess_dia = grid_width - 2;
		mStartX = (width - Application.GRID_SIZE * grid_width) >> 1;
		mStartY = 100;
		
		Bitmap bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Resources r = this.getContext().getResources();
		Drawable tile = r.getDrawable(R.drawable.ai);
		tile.setBounds(0, 0, chess_dia, chess_dia);
		tile.draw(canvas);
		bBitmap = bitmap;
		
		bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
		// 标准的Bitmap绘制。
		tile = r.getDrawable(R.drawable.human);
		tile.setBounds(0, 0, chess_dia, chess_dia);
		tile.draw(canvas);
		wBitmap = bitmap;
		
		
		bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
		// 标准的Bitmap绘制。
		tile = r.getDrawable(R.drawable.human_last);
		tile.setBounds(0, 0, chess_dia, chess_dia);
		tile.draw(canvas);
		wBitmapLast = bitmap;
		
		bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
		// 标准的Bitmap绘制。
		tile = r.getDrawable(R.drawable.ai_last);
		tile.setBounds(0, 0, chess_dia, chess_dia);
		tile.draw(canvas);
		bBitmapLast = bitmap;
		
		
		cursor = new Bitmap[3];
		bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
		// 标准的Bitmap绘制。
		tile = r.getDrawable(R.drawable.cursor0);
		tile.setBounds(0, 0, chess_dia, chess_dia);
		tile.draw(canvas);
		cursor[0] = bitmap;		
		bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
		// 标准的Bitmap绘制。
		tile = r.getDrawable(R.drawable.cursor1);
		tile.setBounds(0, 0, chess_dia, chess_dia);
		tile.draw(canvas);
		cursor[1] = bitmap;
		bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
		// 标准的Bitmap绘制。
		tile = r.getDrawable(R.drawable.cursor2);
		tile.setBounds(0, 0, chess_dia, chess_dia);
		tile.draw(canvas);
		cursor[2] = bitmap;
		
		bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
		// 标准的Bitmap绘制。
		tile = r.getDrawable(R.drawable.background);
		tile.setBounds(0, 0, width, height);
		tile.draw(canvas);
		bg = bitmap;

	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		drawThread.setFlag(true);
		drawThread.start();
	}
	private Position getPosition(float x,float y){
		int posX;
		int posY;
		float x0 = grid_width - (x - mStartX) % grid_width;
		float y0 = grid_width - (y - mStartY) % grid_width;
		if (x0 < (grid_width >> 1)) {
			posX = (int) ((x - mStartX) / grid_width)+1;
		} else {
			posX = (int) ((x - mStartX) / grid_width);
		}
		if (y0 < (grid_width >> 1)) {
			posY = (int) ((y - mStartY) / grid_width)+1;
		} else {
			posY = (int) ((y - mStartY) / grid_width);
		}
		Log.v("x,y", "" + posX + "," + posY);
		return new Position(Application.W,posX,posY); 
	}
	private void initPosition(Position pos){
		if(runMode==Application.SINGLEPLAYER){
			//
		}else if(isWho){
			isWho=Application.BLACK;
			//			
		}else{
			isWho=Application.WHITE;
			pos.setID(Application.B); 			
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(!QiJu.getInstance().isWin()){
			Position temp = getPosition(event.getX(),event.getY());
			if(isTouched&&lastPos.equals(temp)){
				//记录棋子落点
				isTouched=false;
				initPosition(temp);
				
				RecordStack.getInstance().put(temp);
				QiJu.getInstance().setPosition(temp);
				
				if(runMode==Application.SINGLEPLAYER&&!QiJu.getInstance().isWin()){
					temp = QiJu.getInstance().getAiBestPosition();
					RecordStack.getInstance().put(temp);
				}
			}else if(QiJu.getInstance().isValid(temp)){//选中位置在棋盘内且没有下子
				lastPos=temp;
				isTouched=true;
			}
		}
		return super.onTouchEvent(event);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		init(w,h);
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		drawThread.setFlag(false);
	}
	public void onDraw(Canvas canvas){
		//canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(bg, 0, 0, mPaint);
		// 画棋盘
			Paint paintRect = new Paint();
			paintRect.setColor(Color.GRAY);
			paintRect.setStrokeWidth(2);
			paintRect.setStyle(Style.STROKE);

			for (int i = 0; i < Application.GRID_SIZE; i++) {
				for (int j = 0; j < Application.GRID_SIZE; j++) {
					int mLeft = i * grid_width + mStartX;
					int mTop = j * grid_width + mStartY;
					int mRright = mLeft + grid_width;
					int mBottom = mTop + grid_width;
					canvas.drawRect(mLeft, mTop, mRright, mBottom, paintRect);
				}
			}
			// 画棋盘的外边框
			paintRect.setStrokeWidth(4);
			canvas.drawRect(mStartX, mStartY, mStartX + grid_width * Application.GRID_SIZE,
					mStartY + grid_width * Application.GRID_SIZE, paintRect);

		// 画棋子
			Position temp;
			for(int i=0;i<RecordStack.getInstance().getCount();i++){
				temp = RecordStack.getInstance().getPosition(i);
				if(RecordStack.getInstance().isLast(i)){
					if(temp.isWho()){
						canvas.drawBitmap(wBitmapLast, mStartX + (temp.getPosX())
								* grid_width - (chess_dia >> 1), mStartY
								+ (temp.getPosY()) * grid_width - (chess_dia >> 1),
								mPaint);
					}else{
						canvas.drawBitmap(bBitmapLast, mStartX + (temp.getPosX())
								* grid_width - (chess_dia >> 1), mStartY
								+ (temp.getPosY()) * grid_width - (chess_dia >> 1),
								mPaint);
					}
						
				}else{
					if(temp.isWho()){
						canvas.drawBitmap(wBitmap, mStartX + (temp.getPosX())
								* grid_width - (chess_dia >> 1), mStartY
								+ (temp.getPosY()) * grid_width - (chess_dia >> 1),
								mPaint);
					}else{
						canvas.drawBitmap(bBitmap, mStartX + (temp.getPosX())
								* grid_width - (chess_dia >> 1), mStartY
								+ (temp.getPosY()) * grid_width - (chess_dia >> 1),
								mPaint);
					}
				}
			}
			if(isTouched){
				canvas.drawBitmap(cursor[cursorTime%3], mStartX + (lastPos.getPosX())
						* grid_width - (chess_dia >> 1), mStartY
						+ (lastPos.getPosY()) * grid_width - (chess_dia >> 1),
						mPaint);
				cursorTime++;
			}
			
	}
}
