package com.jueze.ibeauty;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.jueze.ibeauty.adapter.AppManagerAdapter;
import com.jueze.ibeauty.bean.AppDataBean;
import com.jueze.ibeauty.dialog.MyProgressDialog;
import com.jueze.ibeauty.util.CacheUtil;
import com.jueze.ibeauty.util.GetAppInfo;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class AppManagerActivity extends BaseActivity {

    private Toolbar mToolbar;
    private RecyclerView mRv;
    private Context mContext;
    private SearchView mSearchView;
	private SearchView.SearchAutoComplete mSearchAutoComplete;

    //data
    private List<AppDataBean> mAppList = new ArrayList<>();
    private AppManagerAdapter adapter;

    private GetAppInfo mGetAppInfo;
    private MyProgressDialog mPd;
    private int currentProgress = 0;
	private StaggeredGridLayoutManager layoutManager;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    mPd.setProgress(currentProgress);
                    break;
                case 2:
                    mPd.dismiss();
                    mRv.setLayoutManager(layoutManager);
                    adapter = new AppManagerAdapter(mContext, mAppList);
                    mRv.setAdapter(adapter);
                    break;
                default:
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        setSupportActionBar(mToolbar);
        setBack("应用管理");
        loadAppList();
    }

    @Override
    public void bindViews() {
        mToolbar = findViewById(R.id.toolbar);
        mRv = findViewById(R.id.recycler_view);
    }
	@Override
	public void initData() {
		mContext = AppManagerActivity.this;
		layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
	}

	@Override
	public void initEvent() {
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView)MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint("应用名称 包名");

		mSearchAutoComplete = mSearchView.findViewById(R.id.search_src_text);
        mSearchAutoComplete.setTextSize(13);

		//键盘搜索
		mSearchAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener(){

				@Override
				public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_SEARCH) {
						query(tv.getText().toString());
						return true;
					}
					return false;
				}
			});
			
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

                @Override
                public boolean onQueryTextSubmit(String p1) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String p1) {
                    query(p1);
                    return true;
                }
            });
			return true;
    }
    
    private void loadAppList() {
        mPd = new MyProgressDialog(mContext);
        mPd.setMessage("正在加载...");
        mPd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mPd.setMax(100);
        mPd.setIndeterminate(false);
        mPd.show();

        new Thread(new Runnable(){

                @Override
                public void run() {
                    List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
                    List<String> temp = new ArrayList<>();

                    for (PackageInfo app : packages) {
                        if ((app.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                            temp.add(app.packageName);
                        }
                    }
                    int i = 0;
                    for (String packageName : temp) {
                        i++;
                        currentProgress = ((i * 100) / temp.size());
                        mHandler.sendEmptyMessage(1);
                        mGetAppInfo = new GetAppInfo(mContext, packageName);
                        mAppList.add(new AppDataBean(
                                         mGetAppInfo.getAppName(),
                                         mGetAppInfo.getPackageName(),
                                         mGetAppInfo.getAppIcon(),
                                         mGetAppInfo.getAppSize(),
                                         mGetAppInfo.getVersionCode(),
                                         mGetAppInfo.getVersionName(),
                                         mGetAppInfo.getFirstInstallDate(),
                                         mGetAppInfo.getLastUpdateDate(),
                                         mGetAppInfo.getTargetSdkVersion(),
                                         mGetAppInfo.getMinSdkVersion(),
                                         mGetAppInfo.getDataDir(),
                                         mGetAppInfo.getSourceDir(),
                                         mGetAppInfo.getUid(),
                                         mGetAppInfo.getFlags(),
                                         mGetAppInfo.getMainActivity()));
                    }

                    Collections.sort(mAppList, new Comparator<AppDataBean>(){

                            @Override
                            public int compare(AppDataBean p1, AppDataBean p2) {
                                Comparator<Object> com = Collator.getInstance(Locale.CHINA);
                                return com.compare(p1.getAppName(), p2.getAppName());
                            }
                        }
                    );
                    mHandler.sendEmptyMessage(2);
                }
        }).start();
    }
    
	private void query(String key){
		adapter.getFilter().filter(key);
	}
	
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheUtil.delete("apks");
    }
    


}
