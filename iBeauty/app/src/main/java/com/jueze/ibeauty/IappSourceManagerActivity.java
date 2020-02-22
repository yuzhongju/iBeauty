package com.jueze.ibeauty;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jueze.ibeauty.adapter.PackSrcFragmentAdapter;
import com.jueze.ibeauty.fragment.PackedFragment;
import com.jueze.ibeauty.fragment.ProjectFragment;
import java.util.ArrayList;
import java.util.List;
import com.jueze.ibeauty.util.Mylog;

public class IappSourceManagerActivity extends BaseActivity {

    //widget
    private Toolbar mToolbar;
    private ViewPager mVp;
    private BottomNavigationBar mBnb;

    //data
    private List<Fragment> mFragmentList = mFragmentList = new ArrayList<>();

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
        super.bindViews();
        mToolbar = findViewById(R.id.toolbar);
        mVp = findViewById(R.id.view_pager);
        mBnb = findViewById(R.id.bnb);
    }

    private void handleViewPager() {
        mBnb.setMode(BottomNavigationBar.MODE_SHIFTING);
        mBnb.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        mBnb.addItem(new BottomNavigationItem(R.drawable.ic_manage, "Project"))
            .addItem(new BottomNavigationItem(R.drawable.ic_pack, "iApp"))
            .addItem(new BottomNavigationItem(R.drawable.ic_pack, "Zip"))
            .initialise();
        mBnb.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){

                @Override
                public void onTabSelected(int position) {
                    mVp.setCurrentItem(position);
                }

                @Override
                public void onTabUnselected(int p1) {
                }

                @Override
                public void onTabReselected(int p1) {
                }
            });

        List<String> backupsPath = new ArrayList<>();
        backupsPath.add(Environment.getExternalStorageDirectory()+"/iBeauty/backups/iapp");
        backupsPath.add(Environment.getExternalStorageDirectory()+"/iBeauty/backups/zip");
        //加载fragment
        mFragmentList.add(new ProjectFragment());
        mFragmentList.add(new PackedFragment(backupsPath.get(0)));
        mFragmentList.add(new PackedFragment(backupsPath.get(1)));
        
        mVp.setOffscreenPageLimit(mFragmentList.size());
        mVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

                @Override
                public void onPageScrolled(int p1, float p2, int p3) {
                }

                @Override
                public void onPageSelected(int position) {
                    mBnb.selectTab(position);
                }

                @Override
                public void onPageScrollStateChanged(int p1) {
                }
            });
        mVp.setAdapter(new PackSrcFragmentAdapter(getSupportFragmentManager(), mFragmentList));


    }

}
