package com.example.shutdowndemo;

import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "disabled dpm", Toast.LENGTH_SHORT).show();
        super.onDisabled(context, intent);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "enabled dpm", Toast.LENGTH_SHORT).show();
        super.onEnabled(context, intent);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // TODO Auto-generated method stub
		/*HeadService head=new HeadService();
		head.lock();*/
		/*Intent intent2 = new Intent(context, MainActivity.class);
		intent2.setAction("show_device_admin_blocker_action");
		intent2.putExtra("clear_settings_activity", true);
		context.startService(intent2);
		return "Are yousure you want to del?";*/
        // TODO Auto-generated method stub
        Log.i("asdfg", "disabled: ");
        Intent setingsintent = new Intent("android.settings.SETTINGS");
        // intent.addFlags();337674240
        try {
            //Log.i("asdfg", "try1: ");
            PendingIntent.getActivity(context, 0, setingsintent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            Log.i("asdfg", "onDisableRequested settings: " + e);
        }

        Intent homeScreenIntent = new Intent(context, MainActivity.class);
        homeScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        homeScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            PendingIntent.getActivity(context, 0, homeScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT).send();
            //Log.i("asdfg", "try2: ");
        } catch (PendingIntent.CanceledException e) {
            Log.i("asdfg", "onDisableRequested home: " + e);
        }
        return "Bye?";
    }

}
