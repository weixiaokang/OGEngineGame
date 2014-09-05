package com.fairy.wuziqi;

import com.donson.momark.AdManager;
import com.donson.momark.view.view.AdView;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class GameBaseActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	static {
		AdManager.init( "3cf8d69e85dc1362","bf84c85307da7704");
	}
	public void initAdview() {
		AdView adView = new AdView(this);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);

		params.gravity = Gravity.BOTTOM | Gravity.END;
		addContentView(adView, params);
	}

	public void activityEnterAnim() {
		int version = Build.VERSION.SDK_INT;
		if (version >= 5) {
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}
	}

	public void activityExitAnim() {
		int version = Build.VERSION.SDK_INT;
		if (version >= 5) {
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
		}

	}
}
