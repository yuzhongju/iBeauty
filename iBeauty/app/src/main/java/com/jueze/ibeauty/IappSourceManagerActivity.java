package com.jueze.ibeauty;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import com.jueze.ibeauty.fragment.PackedFragment;
import com.jueze.ibeauty.fragment.ProjectFragment;
import java.util.ArrayList;
import java.util.List;

public class IappSourceManagerActivity extends BaseActivity {

    //widget
    private Toolbar mToolbar;
    private ViewPager mVp;
	private TabLayout mTab;

    //data
    private List<Fragment> mFragmentList = new ArrayList<>();
	private List<String> mTabTitleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iapp_source_manager);
        setSupportActionBar(mToolbar);
        setBack("iApp源码管理");

        handleViewPager();
    }

    @Override
    public void bindViews() {
        mToolbar = findViewById(R.id.toolbar);
        mVp = findViewById(R.id.view_pager);
		mTab = findViewById(R.id.tab_layout);
    }

	@Override
	public void initData() {
        List<String> backupsPath = new ArrayList<>();
        backupsPath.add(Environment.getExternalStorageDirectory()+"/iBeauty/backups/iapp");
        backupsPath.add(Environment.getExternalStorageDirectory()+"/iBeauty/backups/zip");
        //加载fragment
        mFragmentList.add(new ProjectFragment());
        mFragmentList.add(new PackedFragment(backupsPath.get(0)));
        mFragmentList.add(new PackedFragment(backupsPath.get(1)));
        mTabTitleList.add("项目");
		mTabTitleList.add("iApp");
		mTabTitleList.add("zip");
	}

	@Override
	public void initEvent() {
	}



	
    private void handleViewPager() {
		
		
        mVp.setOffscreenPageLimit(mFragmentList.size());
        //mVp.setAdapter(new PackSrcFragmentAdapter(getSupportFragmentManager(), mTabTitleList,mFragmentList));
		mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()){

				@Override
				public int getCount() {
					return mFragmentList.size();
				}

				@Override
				public Fragment getItem(int p1) {
					return mFragmentList.get(p1);
				}
				
				public CharSequence getPageTitle(int position){
					return mTabTitleList.get(position);
				}
			
			
		});
		mTab.setupWithViewPager(mVp);

    }

}
