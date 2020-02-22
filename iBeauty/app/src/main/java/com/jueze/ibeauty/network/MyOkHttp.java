package com.jueze.ibeauty.network;

import android.content.Context;
import com.jueze.ibeauty.MyApplication;
import com.jueze.ibeauty.bean.PostDataBean;
import java.io.IOException;
import java.util.List;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.webkit.WebSettings;
import okhttp3.HttpUrl;

public class MyOkHttp {

    private Context mContext;
    private OkHttpClient client;
    private CookieJarImpl cookieJar;
    private Request request;
    private PersistentCookieStore cookieStore;
    private String ua;
    
    public MyOkHttp(){
        mContext = MyApplication.getContext();
        cookieStore = new PersistentCookieStore(mContext);
        cookieJar = new CookieJarImpl(cookieStore);
        ua = WebSettings.getDefaultUserAgent(mContext);
    }
    
    
    //同步get
    public Response getBySync(String url){
        Response response = null;
        client = setClient();
        request = new Request.Builder()
            .url(url)
            .header("User-Agent", ua)
            .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {}
        return response;
    }
    
    //同步post
    public Response postWithListBySync(String url, List<PostDataBean> dataList) {
        Response response = null;
        client = setClient();
        
        FormBody.Builder builder = new FormBody.Builder();
        for(PostDataBean data : dataList){
            builder.add(data.getKey(), data.getValue());
        }
        
        RequestBody requestBody = builder.build();
        request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {}
        
        return response;
    }
    
    //异步get
    public void getByAsync(String url, Callback callback)throws Exception{
        client = setClient();
        request = new Request.Builder()
            .url(url)
            .build();
        client.newCall(request).enqueue(callback);
    }
    
    //异步post
    public void postWithListByAsync(String url, List<PostDataBean> dataList, Callback callback){
        client = setClient();

        FormBody.Builder builder = new FormBody.Builder();
        for(PostDataBean data : dataList){
            builder.add(data.getKey(), data.getValue());
        }

        RequestBody requestBody = builder.build();
        request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();
        client.newCall(request).enqueue(callback);
    }
    
    public OkHttpClient getClient()
    {
        return client;
    }
    
    public CookieJarImpl getCookieJar(){
        return cookieJar;
    }
    
    public Request getRequest(){
        return request;
    }
    public HttpUrl getHttpUrl(){
        return getRequest().url();
    }
    public PersistentCookieStore getCookieStore(){
        return cookieStore;
    }
    private OkHttpClient setClient(){
        client = new OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .build();
        return client;
    }
}
