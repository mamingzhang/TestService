package com.example.testservice;

import com.example.testservice.GlobalField.ServiceType;

import android.os.Bundle;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;

public class MainActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Spinner serviceType = (Spinner) findViewById(R.id.spinnerServiceType);
	    ArrayAdapter<String> serviceTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
	    		new String[]{"CustomService", "IntentService"});
	    serviceType.setAdapter(serviceTypeAdapter);
	    serviceType.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				if(position == 0)
				{
					GlobalField.serviceType = ServiceType.ServiceType_CustomDefine;
					findViewById(R.id.serviceStartUpType).setVisibility(View.VISIBLE);
					findViewById(R.id.bindService).setVisibility(View.VISIBLE);
				}
				else
				{
					GlobalField.serviceType = ServiceType.ServiceType_IntentService;
					findViewById(R.id.serviceStartUpType).setVisibility(View.GONE);
					findViewById(R.id.bindService).setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub
				
			}
	    	
	    });
	    
	    Spinner serviceStartUpType = (Spinner) findViewById(R.id.spinnerServiceStartUpType);
	    ArrayAdapter<String> serviceStartUpTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
	    		new String[]{"START_NOT_STICKY", "START_STICKY", "START_REDELIVER_INTENT"});
	    serviceStartUpType.setAdapter(serviceStartUpTypeAdapter);
	    serviceStartUpType.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				if(position == 0)
				{
					GlobalField.serviceStartupType = Service.START_NOT_STICKY;
				}
				else if(position == 1)
				{
					GlobalField.serviceStartupType = Service.START_STICKY;
				}
				else
				{
					GlobalField.serviceStartupType = Service.START_REDELIVER_INTENT;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub
				
			}
	    	
	    });
	    
	    CheckBox bindService = (CheckBox) findViewById(R.id.bindService);
	    bindService.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				// TODO Auto-generated method stub
				GlobalField.bBindService = isChecked;
			}
		});
	    
	    findViewById(R.id.startService).setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, ServiceControlActivity.class);
				startActivity(intent);
				
				finish();
			}
		});
	    
	    findViewById(R.id.serviceStartUpType).setVisibility(View.GONE);
		findViewById(R.id.bindService).setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}
