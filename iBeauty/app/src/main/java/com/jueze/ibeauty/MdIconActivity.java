package com.jueze.ibeauty;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.jueze.utils.FileUtil;
import android.support.v7.widget.RecyclerView;
import com.jueze.utils.ZipUtil;
import com.jueze.utils.ToastUtil;
import java.util.List;
import com.jueze.ibeauty.bean.MdIconBean;
import java.util.ArrayList;
import java.io.File;
import com.jueze.utils.LogUtil;
import java.util.Collections;
import java.util.Comparator;
import com.jueze.ibeauty.adapter.MdIconAdapter;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.GridLayoutManager;

public class MdIconActivity extends BaseActivity {
	
	
    private Toolbar mToolbar;
    private SearchView mSearchView;
	private SearchView.SearchAutoComplete mSearchAutoComplete;

	
	private String destDir;
	private String mark;
	private String destZip;
	private RecyclerView mRecyclerView;
	private MdIconAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_icon);
        setSupportActionBar(mToolbar);
        setBack("MD图标");
		
    }

	@Override
	public void bindViews() {
		mToolbar=findViewById(R.id.toolbar);
		mRecyclerView=findViewById(R.id.recycler_view);
	}

	@Override
	public void initData() {
		destDir=getFilesDir()+"/mdicon/";
		mark=destDir+"mark";
		destZip=destDir+"a.zip";
	}
	
	@Override
	public void initEvent() {
		check();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView)MenuItemCompat.getActionView(searchItem);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setQueryHint("输入关键字");
		mSearchAutoComplete = mSearchView.findViewById(R.id.search_src_text);
        mSearchAutoComplete.setTextSize(13);
		mSearchAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener(){

				@Override
				public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_SEARCH) {
						
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
					mAdapter.getFilter().filter(p1);
                    return true;
                }
            });
        return super.onCreateOptionsMenu(menu);
    }

	private void check(){
		if(!FileUtil.isExists(mark)){
			new Thread(new Runnable(){
					@Override
					public void run() {
						if(FileUtil.copyFromAssets(MdIconActivity.this,"mdicon/a.zip",destZip)){
							if(ZipUtil.unZipFiles(destZip,destDir)){
								FileUtil.deleteFile(destZip);
								FileUtil.writeToApp(mark,"");
								runOnUiThread(new Runnable(){
										@Override
										public void run() {
											handleData();
										}
									});
							}
						}
					}
				}).start();
			
		}else{
			handleData();
		}
	}
	
	private List<MdIconBean> mIconList=new ArrayList<>();
	private void handleData(){
		for(File f:new File(destDir).listFiles()){
			mIconList.add(new MdIconBean(f));
		}
		Collections.sort(mIconList, new Comparator<MdIconBean>(){

				@Override
				public int compare(MdIconBean p1, MdIconBean p2) {
					return p1.getIconName().compareTo(p2.getIconName());
				}
			});
	mAdapter=new MdIconAdapter(mIconList);
	mRecyclerView.setAdapter(mAdapter);
	mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
	}

}
