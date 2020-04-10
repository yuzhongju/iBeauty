package com.jueze.ibeauty;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.jueze.ibeauty.R;


public class BrowserActivity extends BaseActivity implements View.OnClickListener {

	private WebView mWebView;
	private LinearLayout backView, forwardView, homeView, refreshView, linkView;
	private String title, url;

	private Toolbar toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);

		Intent intent = getIntent();
		title=intent.getStringExtra("title");
		url = intent.getStringExtra("url");

		setSupportActionBar(toolbar);
		setBack(title);
		initWebview();
	}

	public static void actionStart(Context context, String title, String url) {
		Intent intent = new Intent(context, BrowserActivity.class);
		intent.putExtra("title",title);
		intent.putExtra("url", url);
		context.startActivity(intent);
	}
	
	@Override
	public void bindViews() {
		toolbar = findViewById(R.id.toolbar);
		mWebView = findViewById(R.id.webview);

		backView = findViewById(R.id.back);
		forwardView = findViewById(R.id.forward);
		homeView = findViewById(R.id.home);
		refreshView = findViewById(R.id.refresh);
		linkView = findViewById(R.id.link);
	}

	@Override
	public void initData() {
	}

	@Override
	public void initEvent() {
		backView.setOnClickListener(this);
		forwardView.setOnClickListener(this);
		homeView.setOnClickListener(this);
		refreshView.setOnClickListener(this);
		linkView.setOnClickListener(this);
	}

    private void initWebview() {
		initWebViewSettings();
		mWebView.loadUrl(url);
		mWebView.setWebViewClient(client);
		
	}

	private void initWebViewSettings() {
		WebSettings webSetting = mWebView.getSettings();
		//支持js
		webSetting.setJavaScriptEnabled(true);
		//缩放
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setDisplayZoomControls(false);
		//文档存储
		webSetting.setDomStorageEnabled(true);
		//概览
		webSetting.setLoadWithOverviewMode(true);
	}

	private WebViewClient client = new WebViewClient(){
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

	};


	@Override
	public void onClick(View p1) {
		switch (p1.getId()) {
			case R.id.back:
				if (mWebView.canGoBack()) mWebView.goBack();
				break;
			case R.id.forward:
				if (mWebView.canGoForward()) mWebView.goForward();
				break;
			case R.id.home:
				mWebView.loadUrl(url);
				break;
			case R.id.refresh:
				mWebView.reload();
				break;
			case R.id.link:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mWebView.getUrl())));
				break;
			default:
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (mWebView.canGoBack()) {
					mWebView.goBack();
				} else {
					finish();
				}
				break;
			default:
		}
		return true;
	}


}
