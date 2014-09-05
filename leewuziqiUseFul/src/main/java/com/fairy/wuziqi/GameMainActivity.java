package com.fairy.wuziqi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fairy.wuziqi.uilts.GameSharedPreferences;
import com.fairy.wuziqi.uilts.KeyCode;


public class GameMainActivity extends GameBaseActivity implements
		OnClickListener {
	private Button startGameButon, degreeButton,exitButton;
	private static final int DEGREE_DIALOG = 0;
	private int mSingleChoiceID = -1;
	private String[] degrees = null;
	private GameSharedPreferences gameSharedPreferences;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_main_menu);
		initView();
		initAdview();
	}


	private void initView() {
		startGameButon = (Button) findViewById(R.id.start_game_button);
		startGameButon.setOnClickListener(this);
		gameSharedPreferences = GameSharedPreferences
		.getGameSharedPreferences(this);
		degrees = this.getResources().getStringArray(R.array.game_degree_array);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		gameSharedPreferences = GameSharedPreferences
				.getGameSharedPreferences(this);
		super.onResume();
	}


	public void creatDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DEGREE_DIALOG:
			mSingleChoiceID = -1;
			builder.setTitle(getString(R.string.game_degree_str));
			builder.setSingleChoiceItems(degrees, gameSharedPreferences
					.loadDegreeIdPreferences(),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							mSingleChoiceID = whichButton;
							gameSharedPreferences
									.saveDegreePreferences(mSingleChoiceID);
							dialog.dismiss();
						}
					});
			break;
		}
		builder.create().show();
	}

	public void onClick(View v) {
		Intent gameIntent = null;
		switch (v.getId()) {
		case R.id.start_game_button:
			gameIntent = new Intent(this, GameStartActivity.class);
			setChess();
			this.startActivity(gameIntent);
			activityEnterAnim();
			break;
		default:
			break;
		}
	}
	
	private void setChess(){
		int key =gameSharedPreferences.loadDegreeIdPreferences();
		switch (key) {
		case 0:
			KeyCode.GRID_SIZE=12;
			break;
		case 1:
			KeyCode.GRID_SIZE=10;
			break;
		default:
			break;
		}
	}

}
