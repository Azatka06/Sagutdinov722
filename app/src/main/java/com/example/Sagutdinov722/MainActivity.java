package com.example.Sagutdinov722;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request();
        init();
    }

    private void init(){
        Button btnCall=findViewById(R.id.btnCall);
        Button btnSend=findViewById(R.id.btnSend);

        final TextView phoneNumber=findViewById(R.id.phoneNumber);
        final TextView smsText=findViewById(R.id.smsText);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        String tempNumber=phoneNumber.getText().toString();
                        if(checkNumber(tempNumber)) {
                            intent.setData(Uri.parse("tel:"+tempNumber));
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(MainActivity.this,getString(R.string.permissionDeniedCall),Toast.LENGTH_LONG).show();
                        requestCall();
                    }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED){
                    SmsManager smsmsnrg=SmsManager.getDefault();
                    String tempNumber=phoneNumber.getText().toString();
                    if(checkNumber(tempNumber)) {
                        smsmsnrg.sendTextMessage(tempNumber, null, smsText.getText().toString(), null, null);
                    }
                }else{
                        Toast.makeText(MainActivity.this,getString(R.string.permissionDeniedSend),Toast.LENGTH_LONG).show();
                        requestSend();
                }
            }
        });
    }

    private void request(){
        requestCall();
        requestSend();
    }

    private void requestSend(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }

    private void requestCall(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }

    private boolean checkNumber(String phoneNumber){
        char[] tempString=phoneNumber.toCharArray();
        if(tempString.length<11 || tempString.length>13){
            Toast.makeText(getApplicationContext(),getString(R.string.numberLengthError),Toast.LENGTH_LONG).show();
            return false;
        }else{
            boolean flag=true;
            for(int i=0;i<tempString.length;i++){
                if(!(Character.isDigit(tempString[i])) || !(tempString[i]=='+')){
                    flag=false;
                }
                if(flag){
                    Toast.makeText(getApplicationContext(),getString(R.string.numberFormatError),Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            return true;
        }
    }
}
