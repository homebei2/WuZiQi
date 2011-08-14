package com.wing.game.wzq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

public class ViewActivity  extends BaseActivity implements OnClickListener {
	private Button single,multi;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.view);
        single = (Button)this.findViewById(R.id.Single);
        single.setOnClickListener(this);
        multi =(Button) this.findViewById(R.id.Multi);
        multi.setOnClickListener(this);
        
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.fade);
        this.findViewById(R.id.main).startAnimation(shake);
    }
	private void fullScreen(){
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
//		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,GameActivity.class);
		if(v == single ){
			intent.putExtra("runMode", Application.SINGLEPLAYER);
		}else{
			intent.putExtra("runMode", Application.MULTIPLAYER);
		}
		this.startActivity(intent);
		
	}
}
