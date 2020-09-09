package com.androidakbar.dz_7_2_2_and;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_CALL_PHONE=55;
    private static final int PERMISSIONS_SMS=56;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appToolbar = findViewById(R.id.app_toolbar);
        appToolbar.setTitle(R.string.title);
        appToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryText));
        appToolbar.setTitleTextAppearance(this, R.style.ToolBarTitleTextAppearance);

        //CALL
        ((Button)findViewById(R.id.btn_call)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callByNumber();
            }
        });

        //SMS
        ((Button)findViewById(R.id.btn_sms)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });

    }


    //позвонить если есть разрешение, если нет, запросить
    private void callByNumber () {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_CALL_PHONE);
        } else {
            EditText edPhone = (EditText)findViewById(R.id.ed_phone);
            if (edPhone.getText().length() > 0) {
                Uri uri = Uri.parse("tel:" + edPhone.getText().toString());
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                startActivity(intent);
            }
        }
    }

    //обработка результатов запроса разрешений
    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permission, int[] grandResults) {
        switch (requestCode) {
            case PERMISSIONS_CALL_PHONE: {
                if (grandResults.length>0 && grandResults[0]==PackageManager.PERMISSION_GRANTED) {
                    callByNumber();
                } else {
                    finish();
                }
                break;
            }
            case PERMISSIONS_SMS: {
                if (grandResults.length>0 && grandResults[0]==PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    finish();
                }
                break;
            }
        }
    }

    //отправить СМС
    private void sendSMS() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSIONS_SMS);
        } else {
            EditText edPhone = (EditText)findViewById(R.id.ed_phone);
            EditText edSMS = (EditText)findViewById(R.id.ed_sms_text);
            if (edPhone.getText().length() > 0 && edSMS.getText().length() > 0) {
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(edPhone.getText().toString(), null, edSMS.getText().toString(), null, null);
            }
        }
    }


}