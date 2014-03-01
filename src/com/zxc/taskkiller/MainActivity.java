package com.zxc.taskkiller;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int maxNum = 64;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get activity manager
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		// Get running tasks
		List<RunningTaskInfo> tasks = manager.getRunningTasks(maxNum);
		// Get running services
		List<RunningServiceInfo> services = manager.getRunningServices(maxNum);
		// Get MemoryInfo
		MemoryInfo mem = new MemoryInfo();
		manager.getMemoryInfo(mem);
		// Get total memory
		// long total = mem.totalMem;
		// Get available memory
		long avail1 = mem.availMem;
		// Iterate tasks
		for (RunningTaskInfo task : tasks)
			manager.killBackgroundProcesses(task.baseActivity.getPackageName());
		// Iterate services
		for (RunningServiceInfo service : services)
			manager.killBackgroundProcesses(service.service.getPackageName());
		// Get MemoryInfo
		manager.getMemoryInfo(mem);
		// Get available memory
		long avail2 = mem.availMem;
		// Build message
		StringBuilder msg = new StringBuilder().append("¿ÉÓÃÄÚ´æ  ")
				.append(avail1 >> 20)
				.append("MiB -> ")
				.append(avail2 >> 20)
				.append("MiB");
		// Show the toast
		Toast.makeText(this, msg,  Toast.LENGTH_LONG).show();
		// Finish the activity
		finish();
	}
}
