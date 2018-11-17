package com.longshihan.upload.uploadfile;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.longshihan.upload.upload.APIResponce;
import com.longshihan.upload.upload.UploadAct;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private TextView uploadText;
    private String path;
    private String txtPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadText=findViewById(R.id.uploadFile);




        uploadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击上传
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/123.png";
                APIResponce responce=UploadAct.upload(path);
                Log.d("上传",responce.toString());
            }
        });
    }



    //判断文件是否存在
    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void showToast(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
    }



}
