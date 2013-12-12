package com.example.testservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.example.testservice.GlobalField.BindType;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

public class CustomService extends Service
{
	private static final String TAG = "TestService";
	private static final String START_ACTION = "com.test.service.startservice";
	private static final String CANCEL_ACTION = "com.test.service.cancelservice";
	private static final String SIGNATURE = "singatrue";
	
	private ExecutorService executor = Executors.newFixedThreadPool(1);
	private Future<Void> mFuture = null;
	
	public static void actionStart(Context context, String singature)
	{
		Intent intent = new Intent(context, CustomService.class);
		intent.setAction(START_ACTION);
		intent.putExtra(SIGNATURE, singature);
		
		context.startService(intent);
	}
	
	public static void actionBind(Context context, ServiceConnection connection, String singature)
	{
		Intent intent = new Intent(context, CustomService.class);
		intent.putExtra(SIGNATURE, singature);
		intent.setAction(START_ACTION);
		
		context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}
	
	public static void actionUnBind(Context context, ServiceConnection connection)
	{		
		context.unbindService(connection);
	}
	
	public static void actionCancel(Context context)
	{
		Intent intent = new Intent(context, CustomService.class);
		intent.setAction(CANCEL_ACTION);
		
		context.startService(intent);
	}
	
	public static void actionStop(Context context)
	{
		Intent intent = new Intent(context, CustomService.class);
		
		context.stopService(intent);
	}
	
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v(TAG, "onCreate");
	}

	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.v(TAG, "onStartCommand : " + startId);
		processIntent(intent, startId);
		return GlobalField.serviceStartupType;

	};

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(TAG, "onDestroy");
		if(mFuture != null && !mFuture.isCancelled())
		{
			mFuture.cancel(true);
			mFuture = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "onBind");
		
		processIntent(intent, -1);

		if (GlobalField.bindType == BindType.BindType_CustomDefinedBinder)
			return mBinder;
		else if (GlobalField.bindType == BindType.BindType_Messager)
			return innerMessenger.getBinder();	
		
		return null;
	}
	
	@Override
	public boolean onUnbind(Intent intent)
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "onUnbind");
		return super.onUnbind(intent);
	}
	
	@SuppressWarnings("unchecked")
	private void processIntent(Intent intent, int startID)
	{
		if(intent == null)
		{
			Log.v(TAG, "intent is null");
		}
		else
		{
			String action = intent.getAction();
			if(action == null)
			{
				Log.v(TAG, "action is null");
			}
			else if(action.equals(START_ACTION))
			{
				if(mFuture != null && !mFuture.isCancelled())
				{
					Log.v(TAG, "start action already started");
					return;
				}
				
				CustomRunnable customRunnable = new CustomRunnable();
				customRunnable.startID = startID;
				mFuture = (Future<Void>) executor.submit(customRunnable);
				Log.v(TAG, "start action");
			}
			else if(action.equals(CANCEL_ACTION))
			{
				if(mFuture != null && !mFuture.isCancelled() && !mFuture.isDone())
				{
					mFuture.cancel(true);
					mFuture = null;
					Log.v(TAG, "cancel action canceled");
					return;
				}
				
				if(startID != -1)
					stopSelf();
				else
					stopSelf(startID);
				
				Log.v(TAG, "cancel action already canceled");
			}
		}
	}
	
	private LocalBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder
	{
		public CustomService getService()
		{
			return CustomService.this;
		}
	}

	public void sendMsgToService(String msg)
	{
		Log.v(TAG, "handlerMsg : "+msg);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler innerHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			Log.v(TAG, "handlerMsg : "+msg.obj.toString());
		};
	};
	
	private Messenger innerMessenger = new Messenger(innerHandler);
	
	private static final int RUN_COUNT = 10;
	private static final int RUN_TIME_PER = 1000;
	
	private class CustomRunnable implements Runnable
	{
		public int startID = -1;
		
		@Override
		public void run()
		{
			Log.v(TAG, "thread begin");
			
			int sum_run_count = 0;
			while(sum_run_count <= RUN_COUNT)
			{
				++sum_run_count;
				Log.v(TAG, "thread running : "+sum_run_count);
				try
				{
					Thread.sleep(RUN_TIME_PER);
				}
				catch (InterruptedException e)
				{
					// TODO: handle exception
					Log.v(TAG, "service may be interrupted!");
					break;
				}
			}
			
			if(startID != -1)
				stopSelf();
			else
				stopSelf(startID);
			
			Log.v(TAG, "thread end :" + sum_run_count);
		}
	};
}
