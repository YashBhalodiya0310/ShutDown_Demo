package com.example.shutdowndemo;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PolicyManager extends AppCompatActivity {

    public static final int DPM_ACTIVATION_REQUEST_CODE = 100;

    private Context mContext;
    private DevicePolicyManager mDPM;
    private ComponentName adminComponent;
    private static final int REQUEST_CODE_ENABLE_ADMIN = 1;

    public PolicyManager(Context context) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        mDPM = (DevicePolicyManager) mContext
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(mContext.getPackageName(),
                mContext.getPackageName() + ".SampleDeviceAdminReceiver");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String phone = bundle.getString("phone");
            String time = bundle.getString("time");
            if (bundle.getString("key").equals("WD$")) {
                wipeData();
            }
        }
    }
    /**
     * This function is used to lock the device and prevent uninstallations
     */
    public void lock()
    {
        mDPM.lockNow();

    }


    public void wipeData()
    {
        if ( Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            // 2.2+
            if (!mDPM.isAdminActive(adminComponent)) {

                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Process will remove user installed applications, settings, wallpaper and sound settings. Are you sure you want to wipe device?");
                startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
            } else {
                // device administrator, can do security operations
                mDPM.wipeData(0);
            }

        } else {
            // 2.1
            try {
                Context foreignContext = createPackageContext("com.android.settings", Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
                Class<?> yourClass = foreignContext.getClassLoader().loadClass("com.android.settings.MasterClear");
                Intent i = new Intent(foreignContext, yourClass);
                startActivityForResult(i, REQUEST_CODE_ENABLE_ADMIN);
            } catch (ClassNotFoundException e) {

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }


    }

    /**
     * Checks if the device has admin rights
     * @return true if device has admin rights otherwise false
     */
    public boolean isAdminActive() {
        return mDPM.isAdminActive(adminComponent);
    }

    public ComponentName getAdminComponent() {
        return adminComponent;
    }

    /**
     * This function is uded to remove admin rights
     */
    public void disableAdmin() {
        mDPM.removeActiveAdmin(adminComponent);
    }

}
