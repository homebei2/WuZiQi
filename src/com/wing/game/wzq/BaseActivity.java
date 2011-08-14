package com.wing.game.wzq;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class BaseActivity extends Activity {
	private final static String QUIT = "com.wing.app.quit";
	private QuitIntentReceiver quitReceiver;
	public static int density;
	protected int num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		quitReceiver = new QuitIntentReceiver();
		IntentFilter filter = new IntentFilter(QUIT);
		registerReceiver(quitReceiver, filter);
	}

	private Handler quithandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			BaseActivity.this.finish();
		}
	};

	private class QuitIntentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			quithandler.sendEmptyMessage(0);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(quitReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem it) {
		// TODO Auto-generated method stub
		int id = it.getItemId();
		if (id == R.id.about) {
			this.showDialog(1);
			return true;
		} else if (id == R.id.quit) {
			this.sendBroadcast(new Intent().setAction(QUIT));
			return true;
		} else
			return super.onOptionsItemSelected(it);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case 1:
			return new AlertDialog.Builder(this).setTitle(R.string.about)
					.setMessage(R.string.app_about)
					.setPositiveButton(R.string.app_about_user, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (isMarketInstalled())
								startActivity(new Intent(Intent.ACTION_VIEW,
										Uri.parse("market://search?q=pub:yale")));
							else
								dialog.dismiss();
						}
					}).setNegativeButton(R.string.app_about_ok, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).create();
		default:
			return super.onCreateDialog(id);
		}
	}

	public boolean isMarketInstalled() {
		PackageManager pm = this.getPackageManager();
		return !pm.queryIntentActivities(
				new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://search?q=pub:yale")), 0).isEmpty();
	}
}
