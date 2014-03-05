package com.zxc.taskkiller;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int maxNum = 64;
	
	private ActivityManager manager;
	private TextView textView;
	private Timer timer;
	private WeakReference<MainActivity> ref = new WeakReference<MainActivity>(this);
	private Handler handler = new MainHandler(ref);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Retrieve textView1
		textView = (TextView) findViewById(R.id.textView1);
		// Get activity manager
		manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		// Instantiate timer
		timer = new Timer();
		// Schedule the task
		timer.schedule(new Task(), 0, 1000);
	}
	
	@Override
	protected void onDestroy() {
		timer.cancel();
		super.onDestroy();
	}
	
	public void button_onClick(View v) {
		// Get running tasks
		List<RunningTaskInfo> tasks = manager.getRunningTasks(maxNum);
		// Get running services
		List<RunningServiceInfo> services = manager.getRunningServices(maxNum);		
		// Iterate tasks
		for (RunningTaskInfo task : tasks)
			manager.killBackgroundProcesses(task.baseActivity.getPackageName());
		// Iterate services
		for (RunningServiceInfo service : services)
			manager.killBackgroundProcesses(service.service.getPackageName());
	}
	
	private class Task extends TimerTask {
		private MemoryInfo mem;
		
		public Task() {
			mem = new MemoryInfo();
		}
		
		public void run() {
			// Get MemoryInfo
			manager.getMemoryInfo(mem);	
			// Get available memory
			long avail = mem.availMem;
			// Instantiate a message
			Message message = Message.obtain();
			message.obj = "Available : " + String.valueOf(avail >> 20) + "MB";
			// Send message
			handler.sendMessage(message);
		}
	}
	
	private static class MainHandler extends Handler {
		private WeakReference<MainActivity> ref;
		
		public MainHandler(WeakReference<MainActivity> r) {
			super();
			ref = r;
		}
		
		@Override
		public void handleMessage(Message msg) {
			ref.get().textView.setText(msg.obj.toString());
		}
	}
}