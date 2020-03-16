package com.jueze.ibeauty;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.jueze.ibeauty.bean.MdIconBean;
import com.jueze.ibeauty.dialog.MyProgressDialog;
import com.jueze.ibeauty.fragment.MdIconFragment;
import com.jueze.ibeauty.network.MyOkHttp;
import com.jueze.ibeauty.util.FileUtil;
import com.jueze.ibeauty.util.MyString;
import com.jueze.ibeauty.util.ToastUtil;
import com.jueze.ibeauty.util.ZipUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;


public class MdIconActivity extends BaseActivity {

    private String dlurl;
    private String basePath;
    private String filePath;
    private String whiteIcons;
    private String blackIcons;
    private String zipFilePath;
    private String destDirPath;

    private List<String> iconCate = new ArrayList<>();
    private List<String> mBgColor = new ArrayList<>();
    private List<String> mNameColor = new ArrayList<>();
	private List<Fragment> mFragments = new ArrayList<>();
	private List<String> mTabTitles = new ArrayList<>();

    private static final int MSG_DOWNLOAD = 1;
    private static final int MSG_UNZIP = 2;
    private AlertDialog mAlertDialog;
    private MyProgressDialog mPd;
    private Context mContext;
    private MyOkHttp mHttp;
    
    private Toolbar mToolbar;
    private LinearLayout mParent;
    private SearchView mSearchView;
	private ViewPager mViewPager;
	private TabLayout mTab;
    private MdIconFragment fg, fg2;
	
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_icon);
        setSupportActionBar(mToolbar);
        setBack("MD图标");
        mContext = this;
        checkFile();  //检查图标资源是否存在 存在则加载 否则下载解压后加载
    }

    @Override
    public void bindViews() {
        mToolbar = findViewById(R.id.toolbar);
        mParent = findViewById(R.id.parent);
		mTab = findViewById(R.id.tab_layout);
		mViewPager = findViewById(R.id.view_pager);
    }
    
    
    public void initData(){
        dlurl = "https://www.lanzous.com/tp/i75r2zg";  //图标资源外链
        basePath = getFilesDir().getPath();  //files目录
        filePath = basePath + "/material-design-icons";  //mdicons存放路径
        whiteIcons = filePath+"/white";
        blackIcons = filePath+"/black";
        
        zipFilePath = basePath + "/material-design-icons.zip";  //zip文件路径
        destDirPath = basePath;  //解压路径

        iconCate.add(whiteIcons);
        iconCate.add(blackIcons);
		
        mBgColor.add("#000000");
        mBgColor.add("#f0f0ff");
		
        mNameColor.add("#ffffff");
        mNameColor.add("#000000");
		
		mFragments.add(fg = new MdIconFragment(0, iconCate, mBgColor, mNameColor));
		mFragments.add(fg2 = new MdIconFragment(1, iconCate, mBgColor, mNameColor));
		
		mTabTitles.add("白色");
		mTabTitles.add("黑色");
        
    }

	@Override
	public void initEvent() {
	}


	
	private void handleViewPager(){
		mViewPager.setOffscreenPageLimit(mFragments.size());
		mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()){

				@Override
				public int getCount() {
					return mFragments.size();
				}

				@Override
				public Fragment getItem(int position) {
					return mFragments.get(position);
				}
				
				public CharSequence getPageTitle(int position){
					return mTabTitles.get(position);
				}
				
			
		});
		mTab.setupWithViewPager(mViewPager);
	}
	
    private void checkFile(){
        
        if (new File(filePath).exists() && new File(filePath).isDirectory()) {
            handleViewPager();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("提示");
            builder.setMessage("没有资源文件，是否下载\nmaterial-design-icons.zip(1.2M)");
            builder.setPositiveButton("下载", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        downloadFile();
                    }
                });
            builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface p1, int p2) {
						finish();
                    }
                });
            mAlertDialog = builder.create();
            mAlertDialog.show();
        }
    }
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView)MenuItemCompat.getActionView(searchItem);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSubmitButtonEnabled(true);
        //焦点
        mSearchView.setFocusable(true);
        mSearchView.requestFocusFromTouch();

        mSearchView.setQueryHint("输入关键字");
        EditText editText = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextSize(14);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

                @Override
                public boolean onQueryTextSubmit(String p1) {
					int position = mViewPager.getCurrentItem();
					if(position==0){
						fg.refresh(position, p1);	
					}else if(position==1){
						fg2.refresh(position, p1);
					}
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String p1) {
					int position = mViewPager.getCurrentItem();
					if(position==0){
						fg.refresh(position, p1);	
					}else if(position==1){
						fg2.refresh(position, p1);
					}
                    return false;
                }
            });
        return super.onCreateOptionsMenu(menu);
    }
    
    private void downloadFile(){
        getRealUrl();
    }
    
    private void getRealUrl() {
        mPd = new MyProgressDialog(mContext);
        mPd.setMessage("正在下载...");
        mPd.show();
        new Thread(new Runnable(){

                @Override
                public void run() {
                    mHttp = new MyOkHttp();
                    Response response = null;
                    String body = null;
                    try {
                        response = mHttp.getBySync(dlurl);
                        body = response.body().string();
                        dlurl = "https://vip.d2.baidupan.com/file/" + MyString.qc(body, "sgo = '(.*?)';");
                        response.close();
                        mHandler.sendEmptyMessage(0);
                    } catch (IOException e) {}
                }
            }).start();
    }

    private void downloadStart(String url){
        try {
            mHttp.getByAsync(url, new okhttp3.Callback(){

                    @Override
                    public void onFailure(Call p1, IOException p2) {
                    }

                    @Override
                    public void onResponse(Call p1, Response response) throws IOException {
                        File file = new File(zipFilePath);
                        InputStream is = response.body().byteStream();
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        fos.flush();
                        is.close();
                        fos.close();
                        Message msg = mHandler.obtainMessage();
                        msg.what = MSG_DOWNLOAD;
                        msg.obj = file.exists();
                        mHandler.sendMessage(msg);
                    }
                });
        } catch (Exception e) {}
    }
    
    private void uncompress(){
        new Thread(new Runnable(){

                @Override
                public void run() {
                    Message msg = mHandler.obtainMessage();
                    msg.what = MSG_UNZIP;
                    msg.obj = ZipUtil.unZipFiles(zipFilePath,destDirPath);
                    mHandler.sendMessage(msg);
                }
        }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        clearFocus();
    }

    private void clearFocus(){
        if (mSearchView != null) {
            mSearchView.onActionViewCollapsed();
            mSearchView.clearFocus();
        }
        mParent.setFocusable(true);
        mParent.setFocusableInTouchMode(true);
        mParent.requestFocus();
    }
    
    
    
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 0:
                    //利用webview获取真实下载地址并下载文件
                    WebView wv = new WebView(mContext);
                    wv.loadUrl(dlurl);
                    wv.setWebViewClient(new WebViewClient(){
                            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
                                webview.loadUrl(url);
                                return true;
                            }
                        });
                    wv.setDownloadListener(new DownloadListener(){

                            @Override
                            public void onDownloadStart(String url, String ua, String contentDisposition, String mimeType, long contentLength) {
                                downloadStart(url);
                            }
                        });
                    break;
                    
                case MSG_DOWNLOAD:
                    boolean z = msg.obj;
                    if(z){
                        mPd.setMessage("开始解压...");
                        uncompress();
                    }else{
                        mPd.dismiss();
                        ToastUtil.show("下载失败");
                    }
                    
                    break;
                case MSG_UNZIP:
                    if((boolean)msg.obj){
                        mPd.setMessage("解压完成");
						handleViewPager();
                        if(FileUtil.deleteFile(zipFilePath)) mPd.dismiss();
                    }else{
                        mPd.dismiss();
                        ToastUtil.show("解压失败");
                    }
                    break;
                default:
            }
        }
        
    };
    

}
