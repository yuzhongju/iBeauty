package com.jueze.ibeauty;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.jaeger.library.StatusBarUtil;
import com.jueze.ibeauty.fragment.ManualFragment;
import java.util.ArrayList;
import java.util.List;

public class BasicManualActivity extends BaseActivity {

    //widget
    private Toolbar mToolbar;
    private SearchView mSearchView;
	private SearchView.SearchAutoComplete mSearchAutoComplete;
    private ViewPager mViewPager;
	private TabLayout mTab;
	private ManualFragment fg, fg2, fg3, fg4, fg5;
    //data
	private List<Fragment> mFragments = new ArrayList<>();
	private List<String> mTabTitles = new ArrayList<>();
    private List<String> fileList = new ArrayList<>();
    private String rootDir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_manual);
        setSupportActionBar(mToolbar);
        setBack("iApp手册");
		handleViewPager();
    }


    @Override
    public void setStatusBar() {
        super.setStatusBar();
        StatusBarUtil.setColorForSwipeBack(this, getResources().getColor(R.color.colorPrimary), 0);
    }

    @Override
    public void bindViews() {
        mToolbar = findViewById(R.id.toolbar);
		mViewPager = findViewById(R.id.view_pager);
		mTab = findViewById(R.id.tab_layout);
    }

	@Override
    public void initData() {
        rootDir = Environment.getExternalStorageDirectory().toString();
		mFragments.add(fg = new ManualFragment(0, fileList));
		mFragments.add(fg2 = new ManualFragment(1, fileList));
		mFragments.add(fg3 = new ManualFragment(2, fileList));
		mFragments.add(fg4 = new ManualFragment(3, fileList));
		mFragments.add(fg5 = new ManualFragment(4, fileList));
		mTabTitles.add("iyu5");
		mTabTitles.add("iyu3");
		mTabTitles.add("ijs3");
		mTabTitles.add("ilua3");
		mTabTitles.add("ijava3");
        fileList.add(rootDir + "/iApp/iyu-helpV5.0.txt");
        fileList.add(rootDir + "/iApp/iyu-helpV3.0.txt");
        fileList.add(rootDir + "/iApp/ijs-helpV3.0.txt");
        fileList.add(rootDir + "/iApp/ilua-helpV3.0.txt");
        fileList.add(rootDir + "/iApp/ijava-helpV3.0.txt");
    }

	@Override
	public void initEvent() {
	}

	private void handleViewPager() {
		mViewPager.setOffscreenPageLimit(mFragments.size());
		mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()){

				@Override
				public int getCount() {
					return mFragments.size();
				}

				@Override
				public Fragment getItem(int position) {
					return mFragments.get(position);
				}

				public CharSequence getPageTitle(int position) {
					return mTabTitles.get(position);
				}
			});
		mTab.setupWithViewPager(mViewPager);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        mSearchView = (SearchView)MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint("输入关键字");

		mSearchAutoComplete = mSearchView.findViewById(R.id.search_src_text);
		mSearchAutoComplete.setTextSize(13);
		
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


	private void query(String p1) {
		int z = mViewPager.getCurrentItem();
		switch (z) {
			case 0:
				fg.filter(p1);
				break;
			case 1:
				fg2.filter(p1);
				break;
			case 2:
				fg3.filter(p1);
				break;
			case 3:
				fg4.filter(p1);
				break;
			case 4:
				fg5.filter(p1);
				break;
			default:
		}
	}

}
