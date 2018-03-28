package com.ics.callmotionsample;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.motion.Smotion;
import com.samsung.android.sdk.motion.SmotionCall;

public class CallActivity extends AppCompatActivity {
    private Smotion smotion;
    private SmotionCall smotionCall;
    private String TAG = "MOTION_TAG";
    TextView textName;
    TextView textNumber;
    private static final int PERMISSION_REQUEST_CALL = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        textName = (TextView) findViewById(R.id.text_name);
        textNumber = (TextView) findViewById(R.id.text_number);

        String name = getIntent().getExtras().getString("name");
        String number = getIntent().getExtras().getString("number");
        textName.setText(name);
        textNumber.setText(number);
        initMotion();
    }

    private void initMotion() {
        smotion = new Smotion();
        try {
            smotion.initialize(this);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "initMotion" + e.getMessage());
        } catch (SsdkUnsupportedException e) {
            Log.d(TAG, "initMotion" + e.getMessage());
            e.printStackTrace();
        }

        smotionCall = new SmotionCall(Looper.getMainLooper(), smotion);
    }

    @Override
    protected void onStart() {
        super.onStart();
        smotionCall.start(changeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        smotionCall.stop();
    }

    public void askForContactPermission(String phone) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.CALL_PHONE}
                                    , PERMISSION_REQUEST_CALL);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_CALL);
                }
            } else {
                doCall(phone);
            }
        } else {
            doCall(phone);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //doCall();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "No permission for contacs", Toast.LENGTH_SHORT).show();
                    //ToastMaster.showMessage(getActivity(),"No permission for contacts");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    final SmotionCall.ChangeListener changeListener = new
            SmotionCall.ChangeListener() {
                @Override
                public void onChanged(SmotionCall.Info info) {
// TODO Auto-generated method stub
                    switch (info.getCallPosition()) {
                        case SmotionCall.POSITION_LEFT:
                            Log.d("Call", "onChanged: left");
                            askForContactPermission(textNumber.getText().toString());
                            break;
                        case SmotionCall.POSITION_RIGHT:
                            Log.d("Call", "onChanged: right");
                            askForContactPermission(textNumber.getText().toString());
                            break;
                    }
                }
            };

    void doCall(String phone){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setPackage("com.android.server.telecom");
        intent.setData(Uri.fromParts("tel", phone, null));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }
}
