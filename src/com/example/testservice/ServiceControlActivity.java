package com.example.testservice;

import com.example.testservice.CustomService.LocalBinder;
import com.example.testservice.GlobalField.BindType;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ServiceControlActivity extends Activity implements OnClickListener
{
	private TextView serviceInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_control);
		
		serviceInfo = (TextView) findViewById(R.id.serviceInfo);
		findViewById(R.id.startService).setOnClickListener(this);
		findViewById(R.id.cancelService).setOnClickListener(this);
		findViewById(R.id.stopService).setOnClickListener(this);
		findViewById(R.id.bindService).setOnClickListener(this);
		findViewById(R.id.unbindService).setOnClickListener(this);
		findViewById(R.id.unbindService).setEnabled(false);
		if(!GlobalField.bBindService)
		{
			findViewById(R.id.bindUnbindGroup).setVisibility(View.GONE);
			findViewById(R.id.clientToServerGroup).setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if(bBinded)
			CustomService.actionUnBind(this, mServiceConnection);
	}
	
	private static int initSingature = 0;
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.startService:
			CustomService.actionStart(this, String.valueOf(++initSingature));
			break;
		case R.id.cancelService:
			CustomService.actionCancel(this);
			break;
		case R.id.stopService:
			CustomService.actionStop(this);
			break;
		case R.id.bindService:
			CustomService.actionBind(this, mServiceConnection, String.valueOf(++initSingature));
			break;
		case R.id.unbindService:
			findViewById(R.id.unbindService).setEnabled(false);
			CustomService.actionUnBind(this, mServiceConnection);
			break;
		case R.id.sendmsgToService:
			if(GlobalField.bBindService && bBinded)
			{
				if(GlobalField.bindType == BindType.BindType_CustomDefinedBinder)
				{
					customService.sendMsgToService("From Client");
				}
				else if (GlobalField.bindType == BindType.BindType_Messager)
				{
					Message message = Message.obtain(null, 0, "From Client");
					try
					{
						messenger.send(message);
					}
					catch (RemoteException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			break;
			
		}
	}
	
	private boolean bBinded = false;
	private CustomService customService = null;
	private Messenger messenger = null;
	
	private ServiceConnection mServiceConnection = new ServiceConnection()
	{
		
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			// TODO Auto-generated method stub
			bBinded = false;
			customService = null;
			messenger = null;
			findViewById(R.id.unbindService).setEnabled(false);
			logServiceInfo("onServiceDisconnected");
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			// TODO Auto-generated method stub
			logServiceInfo("onServiceConnected");
			if(GlobalField.bindType == BindType.BindType_CustomDefinedBinder)
			{
				customService = ((LocalBinder)service).getService();
				bBinded = true;
			}
			else if (GlobalField.bindType == BindType.BindType_Messager)
			{
				messenger = new Messenger(service);
			}
			findViewById(R.id.unbindService).setEnabled(true);
		}
	};
	
	private void logServiceInfo(String info)
	{
		StringBuilder builder = new StringBuilder(info+"\n");
		builder.append(serviceInfo.getText());
		serviceInfo.setText(builder.toString());
	}
}
