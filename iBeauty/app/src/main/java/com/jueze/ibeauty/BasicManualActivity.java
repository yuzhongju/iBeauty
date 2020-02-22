package com.jueze.ibeauty;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jaeger.library.StatusBarUtil;
import com.jueze.ibeauty.adapter.ManualAdapter;
import com.jueze.ibeauty.bean.ManualBean;
import com.jueze.ibeauty.util.FileHelper;
import java.util.ArrayList;

public class BasicManualActivity extends BaseActivity {

    //widget
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private BottomNavigationBar mBnb;
    private ManualAdapter adapter;
    private LinearLayout mParent;
    private SearchView mSearchView;
    
    //data
    private ArrayList<String> fileList;
    private ArrayList<ManualBean> mDocList;
    private String rootDir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_manual);
        setSupportActionBar(mToolbar);
        setBack("iApp手册");
        
        initData();
        
        handleDoc(0);
        handleBnb();
    }

    
    @Override
    public void setStatusBar() {
        super.setStatusBar();
        StatusBarUtil.setColorForSwipeBack(this,getResources().getColor(R.color.colorPrimary),0);
    }

    @Override
    public void bindViews() {
        super.bindViews();
        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
        mBnb = findViewById(R.id.bnb);
        mParent = findViewById(R.id.parent);
    }
    
    public void initData(){
        
        rootDir = Environment.getExternalStorageDirectory().toString();
        fileList = new ArrayList<>();
        fileList.add(rootDir+"/iApp/iyu-helpV5.0.txt");
        fileList.add(rootDir+"/iApp/iyu-helpV3.0.txt");
        fileList.add(rootDir+"/iApp/ijs-helpV3.0.txt");
        fileList.add(rootDir+"/iApp/ilua-helpV3.0.txt");
        fileList.add(rootDir+"/iApp/ijava-helpV3.0.txt");
    }
    private void handleBnb(){
        mBnb.setMode(BottomNavigationBar.MODE_SHIFTING);
        mBnb.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        mBnb.addItem(new BottomNavigationItem(R.drawable.ic_manual, "iyu5"))
            .addItem(new BottomNavigationItem(R.drawable.ic_manual, "iyu3"))
            .addItem(new BottomNavigationItem(R.drawable.ic_manual, "ijs3"))
            .addItem(new BottomNavigationItem(R.drawable.ic_manual, "ilua3"))
            .addItem(new BottomNavigationItem(R.drawable.ic_manual, "ijava3"))
            .initialise();
            
        mBnb.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){

                @Override
                public void onTabSelected(int position) {
                    handleDoc(position);
                }

                @Override
                public void onTabUnselected(int p1) {
                }

                @Override
                public void onTabReselected(int p1) {
                }
            });
    }
    
    private void handleDoc(int z){
        if(adapter != null){
            adapter.removeAll();
        }
        mDocList = new ArrayList<>();
        String nr = FileHelper.readTxtFromSD(fileList.get(z));
        nr = nr.substring(nr.indexOf("【")+1);
        String[] docList = nr.split("【");
        for(String doc : docList){
            String title = doc.substring(0,doc.indexOf("】")).trim();
            String content = doc.substring(doc.indexOf("】")+1).trim();
            mDocList.add(new ManualBean(title, content));
        }
        
        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);
        adapter = new ManualAdapter(mDocList);
        mRecyclerView.setAdapter(adapter);
    }
    

    private void searchDoc(int z, String key){
        if(adapter != null){
            adapter.removeAll();
        }
        mDocList = new ArrayList<>();
        String nr = FileHelper.readTxtFromSD(fileList.get(z));
        nr = nr.substring(nr.indexOf("【")+1);
        String[] docList = nr.split("【");
        for(String doc : docList){
            String title = doc.substring(0,doc.indexOf("】")).trim();
            String content = doc.substring(doc.indexOf("】")+1).trim();
            if(title.toLowerCase().contains(key.toLowerCase()) || content.toLowerCase().contains(key.toLowerCase())){
                mDocList.add(new ManualBean(title, content));
            }
        }

        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);
        adapter = new ManualAdapter(mDocList);
        mRecyclerView.setAdapter(adapter);
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
                    int z = mBnb.getCurrentSelectedPosition();
                    searchDoc(z, p1);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String p1) {
                    int z = mBnb.getCurrentSelectedPosition();
                    searchDoc(z, p1);
                    return false;
                }
        });
        
        return super.onCreateOptionsMenu(menu);
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

    
    
    
}
