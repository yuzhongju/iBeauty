package com.jueze.ibeauty;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.PostDataBean;
import com.jueze.ibeauty.dialog.MyProgressDialog;
import com.jueze.ibeauty.network.CookieJarImpl;
import com.jueze.ibeauty.network.PersistentCookieStore;
import com.jueze.ibeauty.network.SSLSocketClient;
import com.jueze.utils.ClipBoardUtil;
import com.jueze.utils.NetworkUtil;
import com.jueze.utils.ShapeUtil;
import com.jueze.utils.SystemUtil;
import com.jueze.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebSourceActivity extends BaseActivity implements View.OnClickListener {



    private static Context mContext;
    private Toolbar mToolbar;

    private ScrollView mParent;
    private EditText mUrl, mPostData, mCookie, mUserAgent;
    private Button mGet, mPost, mCopy;
    private TextView mHeaders,mResult;

    private MyProgressDialog mPd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_source);
        mContext = this;
        setSupportActionBar(mToolbar);
        setBack("获取网页源码");

        mGet.setOnClickListener(this);
        mPost.setOnClickListener(this);
        mCopy.setOnClickListener(this);

    }



    @Override
    public void bindViews() {
        
        mToolbar = findViewById(R.id.toolbar);

        mParent = findViewById(R.id.web_src_response);
        mUrl = findViewById(R.id.web_src_url);
        mPostData = findViewById(R.id.web_src_post_data);
        mCookie = findViewById(R.id.web_src_cookie);
        mUserAgent = findViewById(R.id.web_src_ua);
        mGet = findViewById(R.id.web_src_get);
        mPost = findViewById(R.id.web_src_post);
        mCopy = findViewById(R.id.web_src_copy);
        mHeaders = findViewById(R.id.web_src_headers);
        mResult = findViewById(R.id.web_src_result);
        
        String color = getString(R.color.colorPrimary);
        ShapeUtil.set(mGet.getBackground(), getString(R.color.colorPrimary));
        ShapeUtil.set(mPost.getBackground(), getString(R.color.colorPrimary));
        ShapeUtil.set(mCopy.getBackground(), color);
    }

	@Override
	public void initData() {
	}

	@Override
	public void initEvent() {
	}



	
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.web_src_get:
                String url = mUrl.getText().toString();
                String postData = null;
                String cookie = mCookie.getText().toString();
                String ua = mUserAgent.getText().toString();

                if (NetworkUtil.state(mContext) == 0) {
                    ToastUtil.show(mContext,"无网络连接");
                } else {
                    if (url == null || url.equals("")) {
                        ToastUtil.show(mContext,"请输入链接");
                    } else {
                        try {
                            sendOkHttp(url, postData, cookie, ua);
                            SystemUtil.hideSoftKeyboard(this);
                        } catch (Exception e) {}
                    }
                }
                break;

            case R.id.web_src_post:

                url = mUrl.getText().toString();
                postData = mPostData.getText().toString();
                cookie = mCookie.getText().toString();
                ua = mUserAgent.getText().toString();

                if (NetworkUtil.state(mContext) == 0) {
                    ToastUtil.show(mContext,"无网络连接");
                } else {
                    if (url == null || url.equals("")) {
                        ToastUtil.show(mContext,"请输入链接");
                    } else {
                        try {
                            sendOkHttp(url, postData, cookie, ua);
                            SystemUtil.hideSoftKeyboard(this);
                        } catch (Exception e) {}
                    }
                }

                break;
            case R.id.web_src_copy:
                String res = mResult.getText().toString();
                if(res==null || res.equals("")){
                    ToastUtil.show(mContext,"没有数据");
                }else{
                    ClipBoardUtil.write(mContext,res);
                    ToastUtil.show(mContext,"已写入剪切板");
                }
                break;
            default:
        }
    }


    private void sendOkHttp(final String url, final String strPostData, final String cookie, final String ua) {
        mPd = new MyProgressDialog(mContext);
        mPd.show();
        mParent.setVisibility(View.GONE);
        mHeaders.setText("");
        mResult.setText("");
        new Thread(new Runnable(){
                @Override
                public void run() {

                    try {
                        Response resp = wl.hq(url, strPostData, cookie, ua, false);
						if(resp==null){
							resp = wl.hq(url, strPostData, cookie, ua, true);
						}
						if (resp!=null && resp.isSuccessful()) {
                            String headers = resp.headers().toString();
                            String body = resp.body().string();
                            setWebData(headers, body);
                        } else {
                            mPd.dismiss();
                            ToastUtil.show(mContext,"获取失败");
                        }
                    } catch (Exception e) {}
                    
                    mPd.dismiss();
                }
            }).start();
    }


    private void setWebData(final String headers, final String response) {
        runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    mPd.dismiss();
                    mParent.setVisibility(View.VISIBLE);
                    mHeaders.setText("");
                    mResult.setText("");
                    mHeaders.setText(headers);
                    mResult.setText(response);
                }
            });
    }


    static class wl {
        public static Response hq(String url, String strPostData, String cookie, String ua, boolean ignoreSLL) {
            Response response = null;
            try {
				PersistentCookieStore cookieStore = new PersistentCookieStore(mContext);
                CookieJarImpl cookieJar = new CookieJarImpl(cookieStore);
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.cookieJar(cookieJar);
				builder.connectTimeout(5,TimeUnit.SECONDS);
				builder.readTimeout(5,TimeUnit.SECONDS);
				if(ignoreSLL){
					builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
					builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
				}
				
                OkHttpClient client = builder.build();

                Request.Builder requestBuilder = new Request.Builder();
                requestBuilder.url(url);

                //处理postdata;
                if (strPostData != null && !strPostData.equals("")) {
                    List<PostDataBean> dataList = new ArrayList<>();
                    for (String str : strPostData.split("&")) {
                        String key = str.substring(0, str.indexOf("="));
                        String value = str.substring(str.indexOf("=") + 1);
                        dataList.add(new PostDataBean(key, value));
                    }

                    FormBody.Builder formBuilder = new FormBody.Builder();
                    for (PostDataBean data : dataList) {
                        formBuilder.add(data.getKey(), data.getValue());
                    }
                    FormBody requestBody = formBuilder.build();
                    requestBuilder.post(requestBody);
                }

                //处理cookie
                if (cookie != null && !cookie.equals("")) requestBuilder.header("Cookie", cookie);
                //处理ua
                if (ua != null && !ua.equals("")) requestBuilder.header("User-Agent", ua);


                Request request = requestBuilder.build();

                response = client.newCall(request).execute();
            } catch (Exception e) {}

            return response;
        }

    }

}
