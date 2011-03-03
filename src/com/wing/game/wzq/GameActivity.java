package com.wing.game.wzq;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.guohead.sdk.GuoheAdLayout;
import com.guohead.sdk.GuoheAdManager;
import com.wing.game.wzq.provider.QiJu;
import com.wing.game.wzq.provider.RecordStack;
import com.wing.game.wzq.ui.GameView;

public class GameActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private TextView timer;
	private static int time=0;
	private TimeThread timeThread;
	private Button restart,back;
	private GameView gameView;
	private static boolean runMode;
	private Handler timeHandler;
	private boolean flag = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.main);
        gameView = (GameView) this.findViewById(R.id.gameview);
        if(runMode!=getIntent().getBooleanExtra("runMode", true)){
        	runMode = !runMode;
        	reInit();
        }
        gameView.setRunMode(runMode);
        
        restart = (Button)this.findViewById(R.id.restart);
        restart.setOnClickListener(this);
        back =(Button) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        
        RelativeLayout.LayoutParams GuoheAdLayoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        LinearLayout footer = (LinearLayout)findViewById(R.id.adLayout);
        GuoheAdLayout adLayout = new GuoheAdLayout(this);
        footer.addView(adLayout, GuoheAdLayoutParams);
        footer.invalidate();
        //timer = (TextView) this.findViewById(R.id.time);
        //timer.setTextColor(Color.BLACK);
        //timeHandler = new TimeHandler(Looper.getMainLooper());  
        //timeThread = new TimeThread();
        //timeThread.start();
    }
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("onPause", "onPause");
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		flag = false;
		GuoheAdManager.finish(this);
		Log.e("onDestroy", "onDestroy");
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("onResume", "onResume");
		this.findViewById(R.id.main).invalidate();
	}
	private void fullScreen(){
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v== restart){
			reInit();
		}else if(v== back){
			if(!QiJu.getInstance().isWin()){
				RecordStack.getInstance().pop();
			}		
		}
		
	}
	public void reInit(){
		RecordStack.getInstance().clear();
		QiJu.getInstance().clear();
		gameView.reInit();
		time=0;
	}
	private String formateTime(){
//		return String.format(getResources().getString(R.string.time),
//					new SimpleDateFormat("hh:mm:ss").format(new Date()),time/60,time%60);
		return String.format(getResources().getString(R.string.time),time/60,time%60);
	}
	class TimeHandler extends Handler{
		TimeHandler(Looper looper){
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);			
			timer.setText(formateTime());
		}		
	}
	class TimeThread extends Thread{
		public void run() {
			while(flag){
				if(!QiJu.getInstance().isWin()){
					time++;
				}
				timeHandler.sendEmptyMessage(0);
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}
}