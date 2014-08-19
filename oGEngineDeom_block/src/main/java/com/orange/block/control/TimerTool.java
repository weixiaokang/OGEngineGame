package com.orange.block.control;

import com.orange.engine.handler.timer.ITimerCallback;
import com.orange.engine.handler.timer.TimerHandler;
import com.orange.block.scene.GameScene;
import com.orange.block.util.LogUtil;

public class TimerTool {

	private GameScene mGameScene;
	private TimerHandler mTimerHandler;
	/**当前经过的总毫秒数**/
	private long millisPass = 0;
	/**是否计时中**/
	private boolean isCountDowning = false;
	/**多少毫秒累加一次计时**/
	private static long stepMillis = 83;

	public TimerTool(GameScene pGameScene) {
		this.mGameScene = pGameScene;
		initTimerHandler();
	}

	// 初始化TimerHandler,设置为每隔stepMillis * 0.001f秒循环回调onTimePassed方法
	private void initTimerHandler() {

		mTimerHandler = new TimerHandler(stepMillis * 0.001f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						// 累加所经过的时间，并在界面上更新显示当前时间
						addMillisPass();
					}
				});

	}

	// 重置时间
	public void resetTimer() {
		millisPass = 0;
		isCountDowning = false;
	}
	// 累加所经过的时间，并在界面上更新显示当前时间
	private void addMillisPass() {
		millisPass += stepMillis;
		mGameScene.getTimerText().setText(millisToTimer(millisPass));
	}
	// 获取当前经过的总毫秒数
	public long getMillisPass() {
		return millisPass;
	}
	// 把毫秒转化为一种分、秒、毫秒的文本显示模式字符串
	public String millisToTimer(long pMillisPass) {
		String timer = "";
		long min = pMillisPass / 60000;
		long sec = (pMillisPass - min * 60000) / 1000;
		String secStr = sec < 10 ? "0" + sec : String.valueOf(sec);
		long millisec = pMillisPass % 1000;
		String millisecStr = millisec < 100 ? "0" + millisec : String
				.valueOf(millisec);
//		LogUtil.d("pMillisPass--->"+pMillisPass+"   millisecStr--->"+millisecStr);

		if (min > 0) {
			timer += min + ":";
		}

		timer += secStr + "." + millisecStr + "\"";

		return timer;
	}
	// 注册mTimerHandler开始计时
	public void start() {
		if (!isCountDowning) {
			isCountDowning = true;
			mGameScene.registerUpdateHandler(mTimerHandler);
		}
	}
	// 反注册mTimerHandler停止计时
	public void stop() {
		mGameScene.unregisterUpdateHandler(mTimerHandler);
	}
}
