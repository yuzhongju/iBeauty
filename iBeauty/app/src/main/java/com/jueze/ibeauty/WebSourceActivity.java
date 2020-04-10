package com.jueze.ibeauty;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jueze.ibeauty.bean.PostDataBean;
import com.jueze.ibeauty.dialog.MyProgressDialog;
import com.jueze.ibeauty.network.CookieJarImpl;
import com.jueze.ibeauty.network.PersistentCookieStore;
import com.jueze.ibeauty.network.SSLSocketClient;
import com.jueze.ibeauty.util.ClipBoardUtil;
import com.jueze.ibeauty.util.NetworkUtil;
import com.jueze.ibeauty.util.ShapeUtil;
import com.jueze.ibeauty.util.SystemUtil;
import com.jueze.ibeauty.util.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.jaeger.library.StatusBarUtil;
import android.support.design.widget.FloatingActionButton;
import android.widget.Scroller;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.support.v4.widget.NestedScrollView;
import android.animation.ObjectAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.jueze.ibeauty.util.FileUtil;
import android.text.TextUtils;
import java.util.Arrays;
import com.jueze.ibeauty.util.LogUtil;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class WebSourceActivity extends BaseActivity implements View.OnClickListener {


    private static Context mContext;
    private Toolbar mToolbar;

    private ScrollView mParent;
    private EditText mUrl, mPostData, mCookie, mUserAgent;
    private Button mGet, mPost, mCopy, mClear;
    private TextView mHeaders,mResult;

    private MyProgressDialog mPd;
	ViewGroup.LayoutParams lp;
	NestedScrollView scroll;
	FloatingActionButton fab;
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
		mClear.setOnClickListener(this);
		
		scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){

				@Override
				public void onScrollChange(NestedScrollView p1, int p2, int p3, int p4, int p5) {
					if(p3-p5>0){
						fabHide();
						//fab.setVisibility(View.GONE);
					}else{
						fabShow();
						//fab.setVisibility(View.VISIBLE);
					}
				}
			});
			
		fab.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					scroll.scrollTo(0,0);
				}
			});
    }

    @Override
    public void bindViews() {

		scroll=findViewById(R.id.nested_scroll);
		fab=findViewById(R.id.fab);
        mToolbar = findViewById(R.id.toolbar);

        mParent = findViewById(R.id.web_src_response);
		
        mUrl = findViewById(R.id.web_src_url);
        mPostData = findViewById(R.id.web_src_post_data);
        mCookie = findViewById(R.id.web_src_cookie);
        mUserAgent = findViewById(R.id.web_src_ua);
		
        mGet = findViewById(R.id.web_src_get);
        mPost = findViewById(R.id.web_src_post);
        mCopy = findViewById(R.id.web_src_copy);
		mClear=findViewById(R.id.web_src_del);
		
        mHeaders = findViewById(R.id.web_src_headers);
        mResult = findViewById(R.id.web_src_result);
		
        String color = getString(R.color.colorPrimary);
        ShapeUtil.set(mGet.getBackground(), getString(R.color.colorPrimary));
        ShapeUtil.set(mPost.getBackground(), getString(R.color.colorPrimary));
        ShapeUtil.set(mCopy.getBackground(), color);
		ShapeUtil.set(mClear.getBackground(),color);
    }

	@Override
	public void initData() {
		lp=fab.getLayoutParams();
	}

	@Override
	public void initEvent() {
	}

	@Override
	public void setStatusBar() {
		//super.setStatusBar();
		StatusBarUtil.setTranslucentForCoordinatorLayout(this,0);
		
	}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.web_src_get:
                String url = mUrl.getText().toString();
                String postData = null;
                String cookie = mCookie.getText().toString();
                String ua = mUserAgent.getText().toString();

                if (NetworkUtil.state() == 0) {
                    ToastUtil.show("无网络连接");
                } else {
                    if (url == null || url.equals("")) {
                        ToastUtil.show("请输入链接");
                    } else {
                        try {
                            sendOkHttp(url, postData, cookie, ua);
                            SystemUtil.hideSystemSoftKeyboard(this);
                        } catch (Exception e) {}
                    }
                }
                break;

            case R.id.web_src_post:

                url = mUrl.getText().toString();
                postData = mPostData.getText().toString();
                cookie = mCookie.getText().toString();
                ua = mUserAgent.getText().toString();

                if (NetworkUtil.state() == 0) {
                    ToastUtil.show("无网络连接");
                } else {
                    if (url == null || url.equals("")) {
                        ToastUtil.show("请输入链接");
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
                if (res == null || res.equals("")) {
                    ToastUtil.show("没有数据");
                } else {
                    ClipBoardUtil.write(res);
                    ToastUtil.show("已写入剪切板");
                }
                break;
			case R.id.web_src_del:
				mUrl.setText("");
				mPostData.setText("");
				mCookie.setText("");
				mUserAgent.setText("");
				mHeaders.setText("");
				mResult.setText("");
				mParent.setVisibility(View.GONE);
				fabHide();
				break;
            default:
        }
    }


    private void sendOkHttp(final String url, final String strPostData, final String cookie, final String ua) {
        mPd = new MyProgressDialog(mContext);
		//mPd.setCancelable(true);
        mPd.show();
        mParent.setVisibility(View.GONE);
        mHeaders.setText("");
        mResult.setText("");
        new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        Response resp = wl.hq(url, strPostData, cookie, ua, false);
						if (resp == null) {
							resp = wl.hq(url, strPostData, cookie, ua, true);
						}
						if (resp != null && resp.isSuccessful()) {
                            String headers = resp.headers().toString();
                            String body = resp.body().string();
                            setWebData(headers, body);
                        } else {
                            mPd.dismiss();
                            ToastUtil.show("获取失败");
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
					Matcher m = Pattern.compile("html>").matcher(response);
					if(m.find()){
						Document doc=Jsoup.parse(response);
						mResult.setText(doc.toString().replaceFirst("doctype", "DOCTYPE"));
					}else{
						mResult.setText(response);
					}
					fab.setVisibility(View.VISIBLE);
					fabShow();
                }
            });
    }
	public void fabShow(){
		//fab.setVisibility(View.VISIBLE);
		fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3));
	}

	public void fabHide(){

		fab.animate().translationY(fab.getHeight()+fab.getBottom()).setInterpolator(new AccelerateInterpolator(3));
		//fab.setVisibility(View.GONE);
	}
	
    static class wl {
        public static Response hq(String url, String strPostData, String cookie, String ua, boolean ignoreSLL) {
            Response response = null;
            try {
				PersistentCookieStore cookieStore = new PersistentCookieStore(mContext);
                CookieJarImpl cookieJar = new CookieJarImpl(cookieStore);
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.cookieJar(cookieJar);
				builder.connectTimeout(5, TimeUnit.SECONDS);
				if (ignoreSLL) {
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
