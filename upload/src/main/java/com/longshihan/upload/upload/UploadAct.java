package com.longshihan.upload.upload;

import android.Manifest;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.longshihan.permissionlibrary.CheckPermissionListener;
import com.longshihan.permissionlibrary.CheckPermissions;
import com.longshihan.permissionlibrary.Permission;

import java.io.File;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadAct {
    OkHttpClient client;
    public static UploadAct uploadAct = null;
    private static Handler handler = new Handler();
    private CheckPermissions checkPermissions;
    private String filePath;
    private OnUploadResultListener listener;

    public UploadAct(Fragment fragment) {
        client = new OkHttpClient();
        checkPermissions=new CheckPermissions(fragment);
        checkPermissions.setLisener(getCheckPermissionListener()).setLogging(false);
    }

    public UploadAct(FragmentActivity fragmentActivity) {
        client = new OkHttpClient();
        checkPermissions=new CheckPermissions(fragmentActivity);
        checkPermissions.setLisener(getCheckPermissionListener()).setLogging(false);
    }

    public static UploadAct getInstance(FragmentActivity fragmentActivity) {
        if (uploadAct == null) {
            uploadAct = new UploadAct(fragmentActivity);
        }
        return uploadAct;
    }

    public static UploadAct getInstance(Fragment fragment) {
        if (uploadAct == null) {
            uploadAct = new UploadAct(fragment);
        }
        return uploadAct;
    }

    public UploadAct upload(final String filePath) {
        this.filePath=filePath;
        checkPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE);
        return this;
    }

    private APIResponce uploadFile(String filePath) {
        APIResponce apiResponce;
        String url = "http://118.24.74.167:10006/file/upload";
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return APIResponce.fail(-1, "文件不存在");
            }
            if (client == null) {
                client = new OkHttpClient();
            }
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("multipart/form-data"), file))
                    .build();
            Request request = new Request.Builder()
                    .header("Authorization", "Client-ID " + UUID.randomUUID())
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                apiResponce = APIResponce.fail("网络连接失败");
            } else {
                apiResponce = (APIResponce) GsonUtils.JSONToObject(response.body().string(), APIResponce.class);
            }
        } catch (Exception e) {
            apiResponce = APIResponce.fail("文件上传失败:" + e.getMessage());
        }
        return apiResponce;
    }

    public UploadAct setListener(OnUploadResultListener listener) {
        this.listener = listener;
        return this;
    }


    public CheckPermissionListener getCheckPermissionListener() {
        return new CheckPermissionListener() {
            @Override
            public void CheckPermissionResult(Permission permission) {
                if (!permission.granted){
                    if (listener != null) {
                        listener.onUploadResult(APIResponce.fail("暂无存储权限"));
                    }
                }else {
                    //处理动态权限
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final APIResponce responce = uploadFile(filePath);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (listener != null) {
                                        listener.onUploadResult(responce);
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        };
    }

    public interface OnUploadResultListener {
        void onUploadResult(APIResponce responce);
    }
}
