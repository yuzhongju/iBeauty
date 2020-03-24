package com.jueze.ibeauty;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.util.ImageUtil;


public class BrowserActivity extends BaseActivity implements View.OnClickListener {

	private WebView mWebView;
	private LinearLayout backView, forwardView, homeView, refreshView, linkView;
	private ImageView exitImg, rightImg;
	private MultiAutoCompleteTextView titleView;
	private String url;
	private String loadingUrl;

	private Bitmap rightBmp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);

		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		initWebview();
	}

	public static void actionStart(Context context, String url) {
		Intent intent = new Intent(context, BrowserActivity.class);
		intent.putExtra("url", url);
		context.startActivity(intent);
	}
	@Override
	public void bindViews() {
		mWebView = findViewById(R.id.webview);

		exitImg = findViewById(R.id.exit);
		rightImg = findViewById(R.id.rightEvent);

		titleView = findViewById(R.id.browser_title);

		backView = findViewById(R.id.back);
		forwardView = findViewById(R.id.forward);
		homeView = findViewById(R.id.home);
		refreshView = findViewById(R.id.refresh);
		linkView = findViewById(R.id.link);
	}

	@Override
	public void initData() {
		rightBmp = ImageUtil.rotate(180, ImageUtil.getBitmapById(this, R.drawable.browser_left));
		rightImg.setImageBitmap(rightBmp);
		backView.setOnClickListener(this);
		forwardView.setOnClickListener(this);
		homeView.setOnClickListener(this);
		refreshView.setOnClickListener(this);
		linkView.setOnClickListener(this);
		rightImg.setOnClickListener(this);
		exitImg.setOnClickListener(this);
	}

	@Override
	public void initEvent() {
	

	}


	
    private void initWebview() {
		initWebViewSettings();
		mWebView.loadUrl(url);
		mWebView.setWebViewClient(client);
	}


	private void initWebViewSettings() {
		WebSettings webSetting = mWebView.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setDisplayZoomControls(false);
		webSetting.setDomStorageEnabled(true);
		webSetting.setLoadWithOverviewMode(true);

		//webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		//webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		//webSetting.setAllowFileAccess(true);
		//webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		//webSetting.setUseWideViewPort(true);
		//webSetting.setSupportMultipleWindows(true);
		//webSetting.setAppCacheEnabled(true);
		//webSetting.setDatabaseEnabled(true);
		//webSetting.setGeolocationEnabled(true);
		//webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		//webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		//webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		//webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		// this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
		// settings 的设计

		
	}
	private WebViewClient client = new WebViewClient(){
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			try{
				if(!url.startsWith("http://") && !url.startsWith("https://")){
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
					return true;
				}
			}catch(Exception e){
				return false;
			}
			loadingUrl = url;
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			titleView.setText(url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			loadingUrl = url;
			titleView.setText(view.getTitle());
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
				loadingUrl = TextUtils.isEmpty(loadingUrl) ?url: loadingUrl;
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(loadingUrl)));
				break;
			case R.id.rightEvent:
				try {
					mWebView.loadUrl(titleView.getText().toString());
				} catch (Exception e) {}
				break;
			case R.id.exit:
				finish();
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
