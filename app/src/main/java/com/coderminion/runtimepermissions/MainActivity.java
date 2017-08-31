package com.coderminion.runtimepermissions;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    public  static final  int REQUEST_CODE = 1;
    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);
        final Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndRequestPermissions())
                {
                    button.setText("All Permissions Granted :)");
                    button.setEnabled(false);
                }
            }
        });
    }



    private  boolean checkAndRequestPermissions() {
        int readexternalpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int callpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (readexternalpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (callpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_CODE);
            return false;
        }
        return true;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "Permission granted");

                    }
                    else
                    {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("Grant permissions to proceed.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    toast.setText("Go to settings and enable permissions");
                                                    toast.show();

                                                    break;
                                            }
                                        }
                                    });
                        }
                        else {
                            toast.setText("Go to settings and enable permissions");
                            toast.show();


                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}
