package com.longshihan.upload.uploadfile;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.longshihan.upload.upload.APIResponce;
import com.longshihan.upload.upload.UploadAct;

public class MainActivity extends AppCompatActivity implements UploadAct.OnUploadResultListener{
    private TextView uploadText;
    private String path;
    private UploadAct uploadAct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadText=findViewById(R.id.uploadFile);
        uploadAct=UploadAct.getInstance(this);
        uploadAct.setListener(this);
        uploadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击上传
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/123.png";
                uploadAct.upload(path);

            }
        });
    }


    @Override
    public void onUploadResult(APIResponce responce) {
        Log.d("上传",responce.toString());
    }
}
