package com.jueze.ibeauty.network;

import android.content.Context;
import android.webkit.WebSettings;
import com.jueze.ibeauty.MyApplication;
import com.jueze.ibeauty.bean.PostDataBean;
import java.io.IOException;
import java.util.List;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    private Context mContext;
    private OkHttpClient client;
    private CookieJarImpl cookieJar;
    private Request request;
    private PersistentCookieStore cookieStore;
    private String ua;

    public OkHttpUtil() {
        mContext = MyApplication.getContext();
        cookieStore = new PersistentCookieStore(mContext);
        cookieJar = new CookieJarImpl(cookieStore);
        ua = WebSettings.getDefaultUserAgent(mContext);
    }
	
	//初始化okhttpClient
    private OkHttpClient initClient() {
        client = new OkHttpClient.Builder()
			.connectTimeout(5, TimeUnit.SECONDS)
			.readTimeout(5, TimeUnit.SECONDS)
            .cookieJar(cookieJar)
            .build();
        return client;
    }
	
	//初始化okhttpClient,忽略ssl
	private OkHttpClient initClientIgnoreSSL() {
        client = new OkHttpClient.Builder()
			.connectTimeout(5, TimeUnit.SECONDS)
			.readTimeout(5, TimeUnit.SECONDS)
            .cookieJar(cookieJar)
			.sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
			.hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .build();
        return client;
    }

    //同步get
    public Response getBySync(String url) {
        Response response = null;
        client = initClient();
        request = new Request.Builder()
            .url(url)
            .header("User-Agent", ua)
            .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {}
        return response;
    }

    //同步get忽略ssl
    public Response getBySyncIgnoreSSL(String url) {
        Response response = null;
        client = initClientIgnoreSSL();
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
        client = initClient();

        FormBody.Builder builder = new FormBody.Builder();
        for (PostDataBean data : dataList) {
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
    public void getByAsync(String url, Callback callback) {
        client = initClient();
        request = new Request.Builder()
            .url(url)
            .build();
        client.newCall(request).enqueue(callback);
    }

    //异步post
    public void postWithListByAsync(String url, List<PostDataBean> dataList, Callback callback) {
        client = initClient();

        FormBody.Builder builder = new FormBody.Builder();
        for (PostDataBean data : dataList) {
            builder.add(data.getKey(), data.getValue());
        }

        RequestBody requestBody = builder.build();
        request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();
        client.newCall(request).enqueue(callback);
    }

	//以下代码需要在发起请求后调用
	
    public Request getRequest() {
        return request;
    }
	
    public HttpUrl getHttpUrl() {
        return getRequest().url();
    }
	
	public CookieJarImpl getCookieJar() {
        return cookieJar;
    }

    public PersistentCookieStore getCookieStore() {
        return cookieStore;
    }
	

}
