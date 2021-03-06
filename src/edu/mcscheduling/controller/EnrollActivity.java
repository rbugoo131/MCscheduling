package edu.mcscheduling.controller;

import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.Account;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EnrollActivity extends ControllerActivity {

	/**
	 * 以下為imageButton變數
	 */
	private ImageButton button_enroll;
	private ImageButton button_back;

	/**
	 * 目前這個Activity
	 */
	public static Activity thisActivity;

	/**
	 * onCreate(Bundle savedInstanceState)
	 * 
	 * Activity的起始function(ex main function)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setLayout();

		thisActivity = this;

		// Listen for button clicks
		setListeners();
	}

	/**
	 * onCreateOptionsMenu(Menu menu)
	 * 
	 * 設定menu button要執行的操作
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	/**
	 * setLayout()
	 * 
	 * 設定layout
	 */
	private void setLayout() {
		// set layout without titleBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// set layout
		setContentView(R.layout.activity_enroll);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_enroll).requestFocus();
	}

	/**
	 * setListeners()
	 * 
	 * 設置每個button被click的時候，要執行的function
	 */
	public void setListeners() {
		button_back = (ImageButton) findViewById(R.id.ImageButton_EnrollPage_back);
		button_enroll = (ImageButton) findViewById(R.id.ImageButton_EnrollPage_enroll);

		button_back.setOnClickListener(back);
		button_enroll.setOnClickListener(enroll);
	}

	/**
	 * back
	 * 
	 * 當你按下back按鈕，返回首頁(Home)
	 */
	private ImageButton.OnClickListener back = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(EnrollActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}
	};

	
	/**
	 * Controller : Enroll
	 * 
	 * 
	 */
	
	private void handleEnroll(View v) {
		String userid = ((EditText)
				findViewById(R.id.userAccount)).getText().toString();
		String username = ((EditText)
				findViewById(R.id.userName)).getText().toString();
		String userpasswd = ((EditText)
				findViewById(R.id.password)).getText().toString();
		String userpasswdConfirm = ((EditText)
				findViewById(R.id.passwordConfirm)).getText().toString();
		
		if ( userid == null || userid.equals("")) {
			Toast.makeText(getApplicationContext(), "使用者帳號不能為空", Toast.LENGTH_LONG)
			.show();
		} else if ( username == null || userpasswd.equals("")) {
			Toast.makeText(getApplicationContext(), "使用者姓名不能為空", Toast.LENGTH_LONG)
			.show();
		} else if ( userpasswd == null || userpasswd.equals("")) {
			Toast.makeText(getApplicationContext(), "密碼不能為空", Toast.LENGTH_LONG)
			.show();
		} else if ( !userpasswd.equals(userpasswdConfirm)) {
			Toast.makeText(getApplicationContext(), "密碼確認不一致", Toast.LENGTH_LONG)
			.show();
		} else {
			
			Account account = new Account(db);
	
			if ( account.register(userid, username, userpasswd) == StatusCode.success ) {
				Toast.makeText(getApplicationContext(), "註冊成功", Toast.LENGTH_LONG)
				.show();
			
				Intent intent = new Intent();
				intent.setClass(this, LoginActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "註冊失敗:使用者已存在", Toast.LENGTH_LONG)
				.show();
			}
		}
	}
	
	/**
	 * enroll
	 * 
	 * 當你按下註冊按鈕，執行對應的操作
	 */
	private ImageButton.OnClickListener enroll = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			handleEnroll(v);			

		}
	};

	/**
	 * openOptionsDialog_leaveAPP()
	 * 
	 * 想要離開app時，開啟optionDialog，確認使用者是否真的要離開
	 */
	public void openOptionsDialog_leaveAPP() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				EnrollActivity.this);
		builder.setTitle("APP訊息");
		builder.setMessage("真的要離開此APP");
		builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int i) {
				thisActivity.finish();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int i) {
			}
		});
		builder.show();
	}

	/**
	 * onKeyDown(int keyCode, KeyEvent event)
	 * 
	 * 設定按下硬體的返回鍵時，要執行的操作。目前這裡讓使用者按下返回鍵時，不執行任何操作
	 * 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do nothing...
		}
		return false;
	}

	// 以下為目前尚未使用，但未來會用到的function----------------------------------------------

	/**
	 * isNetworkAvailable()
	 * 
	 * 檢查目前的網路狀況
	 * 
	 * @return true indicates the network is available. false indicates the
	 *         network is not available.
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * responseToNetworkStatus()
	 * 
	 * 顯示目前的網路狀況，讓使用者知道
	 */
	public void responseToNetworkStatus() {
		if (isNetworkAvailable() == false) {
			Toast.makeText(getApplicationContext(),
					"Network connection error!!", Toast.LENGTH_LONG).show();
		} else {
			// do nothing...
		}
	}
}
