package com.example.shutdowndemo;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn;
    private PolicyManager policyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);

        askPermissison();

        btn.setOnClickListener(v -> {
           /* DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            if (devicePolicyManager.isAdminActive(new ComponentName(MainActivity.this, MyDeviceAdminReceiver.class))) {
                devicePolicyManager.lockNow();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                powerManager.reboot(null);
            } else {
                Toast.makeText(MainActivity.this, "Please enable device administrator first.", Toast.LENGTH_SHORT).show();
            }*/
        });
    }

    private void askPermissison() {
        policyManager = new PolicyManager(this);
        if (!policyManager.isAdminActive()) {
            Permission_Admin_Rights();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PolicyManager.DPM_ACTIVATION_REQUEST_CODE) {
            policyManager = new PolicyManager(this);
            if (!policyManager.isAdminActive()) {
                Permission_Admin_Rights();
            }
        }

    }

    @SuppressWarnings("deprecation")
    public void Permission_Admin_Rights(){
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(MainActivity.this);
        popDialog.setTitle("Admin Rights Permission");
        popDialog.setMessage("This App requires Admin Rights permission. In the next screen, grant permission and come back");
        popDialog.setCancelable(false);
        popDialog.setPositiveButton("OK",
                (dialog, which) -> {
                    dialog.dismiss();
                    Intent activateDeviceAdmin = new Intent(
                            DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    activateDeviceAdmin.putExtra(
                            DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                            policyManager.getAdminComponent());
                    activateDeviceAdmin
                            .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                    "After activating admin, you will be able to block application uninstallation.");
                    startActivityForResult(activateDeviceAdmin,
                            PolicyManager.DPM_ACTIVATION_REQUEST_CODE);
                });
        popDialog.create();
        popDialog.show();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}