package com.wing.game.wzq;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.adview.AdViewInterface;
import com.adview.AdViewLayout;
import com.adview.AdViewTargeting;
import com.adview.AdViewTargeting.RunMode;
import com.adview.AdViewTargeting.UpdateMode;
import com.wing.game.wzq.provider.QiJu;
import com.wing.game.wzq.provider.RecordStack;
import com.wing.game.wzq.ui.GameView;

public class GameActivity extends BaseActivity implements OnClickListener, AdViewInterface {
    /** Called when the activity is first created. */
	private TextView timer;
	private static int time=0;
	private TimeThread timeThread;
	private Button restart,back;
	private GameView gameView;
	private static int runMode;
	private Handler timeHandler;
	private boolean flag = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        
        setContentView(R.layout.main);
        gameView = (GameView) this.findViewById(R.id.gameview);
        int tempMode = getIntent().getIntExtra("runMode", 1);
        if(runMode!=tempMode){
        	runMode = tempMode;
        	reInit();
        	gameView.setRunMode(runMode);
            if(runMode==Application.SINGLEPLAYER)
            	this.showDialog(0);
        }              
        restart = (Button)this.findViewById(R.id.restart);
        restart.setOnClickListener(this);
        back =(Button) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        
        
		  /*下面两行只用于测试,完成后一定要去掉,参考文挡说明*/
//      AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME);  //每次都从服务器取配置
//      AdViewTargeting.setRunMode(RunMode.TEST);        //保证所有选中的广告公司都为测试状态
       
        AdViewLayout adViewLayout = (AdViewLayout)findViewById(R.id.adview_ayout);
        adViewLayout.setAdViewInterface(this);
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
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
//		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (!gameView.isAnglerThinking()) {
			if (v == restart) {
				reInit();
			} else if (v == back) {
				if (runMode == Application.SINGLEPLAYER) {
					RecordStack.getInstance().pop();
				} else {
					gameView.refreshIsWhoThink();
				}
				RecordStack.getInstance().pop();
				QiJu.getInstance().onBackButton();
			}
		}

	}
	public void reInit(){
		RecordStack.getInstance().clear();
		QiJu.getInstance().clear();
		gameView.reInit();
		time=0;
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch(id){
		case 0:
			return	new AlertDialog.Builder(this)
				 .setMessage(R.string.dialog_message)
				 .setNegativeButton(R.string.dialog_cancel, new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							gameView.setWhoIsAngler(Application.WHITE);
							dialog.dismiss();
						}
						 
					 })
				 .setPositiveButton(R.string.dialog_ok, new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
					 
				 }).create();
		default:
		return super.onCreateDialog(id);
		}
	}
		

	private String formateTime(){
//		return String.format(getResources().getString(R.string.time),
//					new SimpleDateFormat("hh:mm:ss").format(new Date()),time/60,time%60);
		return String.format(getResources().getString(R.string.time),time/60,time%60);
	}		
	class TimeHandler extends Handler{
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
	@Override
	public void onClickAd() {
		// TODO Auto-generated method stub
		Log.i("onClickAd", "onClickAd");
	}
	@Override
	public void onDisplayAd() {
		// TODO Auto-generated method stub
		Log.i("onDisplayAd", "onDisplayAd");
	}
}