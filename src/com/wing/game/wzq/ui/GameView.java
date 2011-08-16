package com.wing.game.wzq.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wing.game.wzq.Application;
import com.wing.game.wzq.R;
import com.wing.game.wzq.provider.ChessFormUtil;
import com.wing.game.wzq.provider.Position;
import com.wing.game.wzq.provider.QiJu;
import com.wing.game.wzq.provider.RecordStack;
import com.wing.game.wzq.provider.onDrawThread;

/**
 * @author lichu
 *
 */
/**
 * @author lichu
 *
 */
public class GameView  extends SurfaceView implements SurfaceHolder.Callback{
	private Bitmap wBitmap,last, bBitmap,bg,win,time,vs;
	private Bitmap[] cursor;
	private onDrawThread drawThread;
	private int cursorTime;
	
	private int chess_dia = 22; // ���ֱ��
	private int grid_width; // ���̸�Ŀ��
	private int mStartX;// ���̶�λ�����Ͻ�X
	private int mStartY;// ���̶�λ�����Ͻ�Y	
	private Position lastPos;
	private Paint mPaint; 
	
	/**
	 * �Ƿ��Ѿ�ѡ����������
	 */
	private boolean isTouched=false; 
	/**
	 * ����ģʽ�����ˡ�˫��
	 */
	private static int runMode=Application.SINGLEPLAYER;
	/**
	 * ��˭������
	 */
	private int isWhoThinking = Application.WHITE;
	/**
	 * ����ִ���ӻ��Ǻ���
	 */
	private int whoIsAngler =  Application.BLACK;
	
	private static final int ANGLERTHINK=0;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
				case ANGLERTHINK:
				if(!QiJu.getInstance().isWin()){
//						Log.i("GameView","ANGLERTHINK1:"+ isWhoThinking);
						QiJu.getInstance().getAiBestPosition(isWhoThinking);		
						refreshIsWhoThink();
//						Log.i("GameView","ANGLERTHINK2:"+ isWhoThinking);
				}
				break;
				default:
					super.handleMessage(msg);
			}		
		}

	
	};
	public GameView(Context context) {
		super(context);
		init();
	}
	public GameView(Context context, AttributeSet attrs) { // ������õ���������캯����Ϊʲô����ǰ�����
		super(context, attrs);
		init();
	}
	private void init(){
		this.getHolder().addCallback(this);
		this.setFocusable(true); // 20090530
		this.setFocusableInTouchMode(true);
	}
	private void initQiPan(int width,int height){
		if (width <= height)
			grid_width = (width-10) / Application.GRID_SIZE;
		else
			grid_width = (height-10) / Application.GRID_SIZE;

		chess_dia = grid_width - 2;
		mStartX = (width - Application.GRID_SIZE * grid_width) >> 1;
		mStartY = 80;
		
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
		// ��׼��Bitmap���ơ�
		tile = r.getDrawable(R.drawable.human);
		tile.setBounds(0, 0, chess_dia, chess_dia);
		tile.draw(canvas);
		wBitmap = bitmap;
					
		bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
		// ��׼��Bitmap���ơ�
		tile = r.getDrawable(R.drawable.last);
		tile.setBounds(0, 0, chess_dia, chess_dia);
		tile.draw(canvas);
		last = bitmap;
		
		
		cursor = new Bitmap[3];
		bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
		// ��׼��Bitmap���ơ�
		tile = r.getDrawable(R.drawable.cursor0);
		tile.setBounds(0, 0, chess_dia, chess_dia);
		tile.draw(canvas);
		cursor[0] = bitmap;		
		bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
		// ��׼��Bitmap���ơ�
		tile = r.getDrawable(R.drawable.cursor1);
		tile.setBounds(0, 0, chess_dia, chess_dia);
		tile.draw(canvas);
		cursor[1] = bitmap;
		bitmap = Bitmap.createBitmap(chess_dia, chess_dia,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
		// ��׼��Bitmap���ơ�
		tile = r.getDrawable(R.drawable.cursor2);
		tile.setBounds(0, 0, chess_dia, chess_dia);
		tile.draw(canvas);
		cursor[2] = bitmap;
		
		bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap); // Bitmap + Canvas + Drawable ==
		// ��׼��Bitmap���ơ�
		tile = r.getDrawable(R.drawable.background);
		tile.setBounds(0, 0, width, height);
		tile.draw(canvas);
		bg = bitmap;		
		
		win = reSizePicture(BitmapFactory.decodeResource(getResources(),
				R.drawable.win),2*chess_dia, 2*chess_dia);
			 
		vs = reSizePicture(BitmapFactory.decodeResource(getResources(),
				R.drawable.vs),2*chess_dia, 2*chess_dia);
			 
		time = reSizePicture(BitmapFactory.decodeResource(getResources(),
				R.drawable.time),2*chess_dia, 2*chess_dia);
			 	
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(drawThread==null){
			drawThread = new onDrawThread(getHolder(),this);
		}
		drawThread.setFlag(true);
		drawThread.start();
		Log.e("surfaceCreated", "surfaceCreated");
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
//		Log.v("x,y", "" + posX + "," + posY);
		return new Position(Application.WHITE,posX,posY); 
	}
	public boolean isAnglerThinking(){
		if(!QiJu.getInstance().isWin()&&runMode==Application.SINGLEPLAYER&&isWhoThinking==this.whoIsAngler){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * �������Ѿ�����Ӯ���ߵ��������㣬������������Ӧ
	 * �����Ѿ�ѡ����������λ�����ٴ�ȷ��ͬ��λ�����ж����ӣ����������ж����򣬷���������µĴ���λ��Ϊ��������λ��
	 * ����λ�ý�������ջ��������һ��������ģʽ
	 *               
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(isAnglerThinking())
			return true;
		// TODO Auto-generated method stub
		if(!QiJu.getInstance().isWin()){
			Position temp = getPosition(event.getX(),event.getY());
			if(isTouched&&lastPos.equals(temp)){
				//��¼�������
				isTouched=false;
//				Log.i("GameView","isTouched1:"+ isWhoThinking);
				temp.setID(isWhoThinking); 
				refreshIsWhoThink();	
//				Log.i("GameView","isTouched2:"+ isWhoThinking);
				RecordStack.getInstance().put(temp);
				QiJu.getInstance().setPosition(temp);
			}else if(QiJu.getInstance().isValid(temp)){//ѡ��λ������������û������
				lastPos=temp;
				isTouched=true;
			}
		}
		return super.onTouchEvent(event);
	}
	public void refreshIsWhoThink() {
		// TODO Auto-generated method stub
		if(isWhoThinking==Application.WHITE){
			isWhoThinking=Application.BLACK;			
		}else{
			isWhoThinking=Application.WHITE;			
		}
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		initQiPan(w,h);
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		drawThread.setFlag(false);
		drawThread = null;
		Log.e("surfaceDestroyed", "surfaceDestroyed");
	}
	public void onDraw(Canvas canvas){
		//canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(bg, 0, 0, mPaint);
		// ������
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
			// �����̵���߿�
			paintRect.setStrokeWidth(4);
			canvas.drawRect(mStartX, mStartY, mStartX + grid_width * Application.GRID_SIZE,
					mStartY + grid_width * Application.GRID_SIZE, paintRect);

		// ������
			Position temp;
			for(int i=0;i<RecordStack.getInstance().getCount();i++){
				temp = RecordStack.getInstance().getPosition(i);					
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
					if(RecordStack.getInstance().isLast(i)){
						canvas.drawBitmap(last, mStartX + (temp.getPosX())
								* grid_width - (chess_dia >> 1), mStartY
								+ (temp.getPosY()) * grid_width - (chess_dia >> 1),
								mPaint);
					}
					if(RecordStack.getInstance().isLast(i+1)){
						canvas.drawBitmap(last, mStartX + (temp.getPosX())
								* grid_width - (chess_dia >> 1), mStartY
								+ (temp.getPosY()) * grid_width - (chess_dia >> 1),
								mPaint);
					}
			}
			//��ѡ������λ��
			if(isTouched){
				canvas.drawBitmap(cursor[cursorTime%3], mStartX + (lastPos.getPosX())
						* grid_width - (chess_dia >> 1), mStartY
						+ (lastPos.getPosY()) * grid_width - (chess_dia >> 1),
						mPaint);
				cursorTime++;
			}
			//����������
			canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
					R.drawable.human),20,20,
					mPaint);
			canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
					R.drawable.ai),260,20,
					mPaint);	
			canvas.drawBitmap(vs,138,20,
					mPaint);
			
			Log.i("GameView","onDraw:"+ isWhoThinking);
			
			if(QiJu.getInstance().isWin()){//ʤ��ͼ��
				canvas.drawBitmap(win,isWhoThinking==Application.WHITE?200:80,20,
						mPaint);
			}else{
				canvas.drawBitmap(time,isWhoThinking==Application.WHITE?80:200,20,
						mPaint);
			}
			if(runMode==Application.SINGLEPLAYER&&whoIsAngler==isWhoThinking){
				mHandler.sendEmptyMessage(ANGLERTHINK);		
			}
	}
	public void setRunMode(int runMode){
		this.runMode = runMode;
	}
	public void setWhoIsAngler(int whoIsAngler){
		this.whoIsAngler = whoIsAngler;
	}
	public void reInit(){	
		isWhoThinking=Application.WHITE;	
	}
	public static Bitmap reSizePicture(Bitmap orgImg, int newWidth,
			int newHeight) {
		Bitmap resizedBitmap = null;
		if (orgImg != null) {
			int width = orgImg.getWidth();
			int height = orgImg.getHeight();
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);

			resizedBitmap = Bitmap.createBitmap(orgImg, 0, 0, width, height,
					matrix, true);
		}

		return resizedBitmap;
	}
}
