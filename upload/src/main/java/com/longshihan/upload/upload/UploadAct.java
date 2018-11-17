package com.longshihan.upload.upload;

import okhttp3.*;

import java.io.File;
import java.util.UUID;

public class UploadAct {

   public static APIResponce upload(String filePath) {
       String url = "http://118.24.74.167:10006/file/upload";
       try {
           File file=new File(filePath);
           if (file==null){
               return  APIResponce.fail(-1,"文件不存在");
           }
           OkHttpClient client = new OkHttpClient();
           RequestBody requestBody = new MultipartBody.Builder()
                   .setType(MultipartBody.FORM)
                   .addFormDataPart("file", file.getName(),
                           RequestBody.create(MediaType.parse("multipart/form-data"),file))
                   .build();
           Request request = new Request.Builder()
                   .header("Authorization", "Client-ID " + UUID.randomUUID())
                   .url(url)
                   .post(requestBody)
                   .build();
           Response response = client.newCall(request).execute();
           if (!response.isSuccessful()){
               return  APIResponce.fail("网络连接失败");
           }
           return (APIResponce) GsonUtils.JSONToObject(response.body().string(),APIResponce.class);
       }catch (Exception e){
           return  APIResponce.fail("文件上传失败");
       }
    }

   public static void main(String[] args){
        try {
            String filePath = "D:\\apk\\123.png";
            String url = "http://118.24.74.167:10006/file/upload";
            System.out.println(upload(filePath).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
