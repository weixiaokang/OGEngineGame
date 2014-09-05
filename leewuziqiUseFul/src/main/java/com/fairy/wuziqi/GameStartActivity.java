
package com.fairy.wuziqi;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.donson.momark.view.view.AdView;
import com.fairy.wuziqi.view.GameView;


public class GameStartActivity extends GameBaseActivity {
	GameView gbv;
	private GridView bottomMenuGrid;
	private String[] bottom_menu_itemName;
	private AdView adView;
	int[] bottom_menu_itemSource = { R.drawable.menu_restart, R.drawable.menu_about,
			 R.drawable.menu_mute 
			 };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game_main_view);
		gbv = (GameView) this.findViewById(R.id.gobangview);
		gbv.setTextView((TextView) this.findViewById(R.id.text));
		adView = (AdView)findViewById(R.id.adView);
		adView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			loadBottomMenu();
			if (bottomMenuGrid.getVisibility() == View.VISIBLE) {
				bottomMenuGrid.setVisibility(View.GONE);
				adView.setVisibility(View.VISIBLE);
			} else {
				bottomMenuGrid.setVisibility(View.VISIBLE);
				adView.setVisibility(View.GONE);
			}
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			activityExitAnim();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void loadBottomMenu() {
		bottom_menu_itemName = this.getResources().getStringArray(
				R.array.grid_view_group);
		if (bottomMenuGrid == null) {
			bottomMenuGrid = (GridView) findViewById(R.id.gv_buttom_menu);
			bottomMenuGrid.setBackgroundResource(R.drawable.channelgallery_bg);
			bottomMenuGrid.setNumColumns(5);
			bottomMenuGrid.setGravity(Gravity.CENTER); 
			bottomMenuGrid.setVerticalSpacing(8);
			bottomMenuGrid.setHorizontalSpacing(8);
			bottomMenuGrid.setAdapter(getMenuAdapter(bottom_menu_itemName,
					bottom_menu_itemSource));
			bottomMenuGrid.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					AlertDialog.Builder builder = new AlertDialog.Builder(GameStartActivity.this);
					switch (arg2) {
					case 0:
						gbv = (GameView) GameStartActivity.this
						.findViewById(R.id.gobangview);
						gbv.setTextView((TextView) GameStartActivity.this
								.findViewById(R.id.text));
						gbv.mGameState = GameView.GAMESTATE_RUN;
						gbv.setVisibility(View.VISIBLE);
						gbv.mStatusTextView.setVisibility(View.INVISIBLE);
						gbv.init();
						gbv.invalidate();
				        break;
					case 2:
						GameStartActivity.this.finish();
						activityExitAnim();
						break;
					}
				}
			});
		}

	}

	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.game_item_menu,
				new String[] { "itemImage", "itemText" }, new int[] {
						R.id.item_image, R.id.item_text });
		return simperAdapter;
	}

}