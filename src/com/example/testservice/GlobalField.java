package com.example.testservice;

import android.app.Service;

public class GlobalField
{
	public enum ServiceType
	{
		ServiceType_CustomDefine,
		ServiceType_IntentService
	}
	
	public enum BindType
	{
		BindType_CustomDefinedBinder,
		BindType_Messager,
		BindType_AIDL,
	}
	
	public static ServiceType serviceType = ServiceType.ServiceType_CustomDefine;
	public static int serviceStartupType = Service.START_NOT_STICKY;
	public static boolean bBindService = false;
	public static BindType bindType = BindType.BindType_CustomDefinedBinder;
}
