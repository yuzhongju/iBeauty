package com.jueze.ibeauty;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.jueze.ibeauty.adapter.IndexAdapter;
import com.jueze.ibeauty.bean.DaywordBean;
import com.jueze.ibeauty.bean.FunBean;
import com.jueze.ibeauty.network.MyHttp;
import com.jueze.ibeauty.util.CacheClean;
import com.jueze.ibeauty.util.CheckStoragePermission;
import com.jueze.ibeauty.util.FileHelper;
import com.jueze.ibeauty.util.Screen;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import android.view.ViewGroup;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;
import com.jueze.ibeauty.util.MyIntent;

public class MainActivity extends BaseActivity {

    
    //widget
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    
    private ImageView mDwBg;
    private TextView mDwEN, mDwCN;
    private CircleImageView mCircleImageView;

    private RecyclerView mRecyclerView;
    List<FunBean> funList = new ArrayList<>();
    IndexAdapter adapter;
    //data
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

		setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        handleFun();
        handleImageScale();
        handleNvagationView();
    }


    private void handleNvagationView(){
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

                @Override
                public boolean onNavigationItemSelected(MenuItem p1) {
                    switch(p1.getItemId()){
                        case R.id.nav_open_source:
                            MyIntent.go(OpenSourceActivity.class);
                            break;
                    }
                    return true;
                }
        });
    }
    
    
    @Override
    public void bindViews() {
        super.bindViews();
		mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        mDwBg = findViewById(R.id.dw_bg);
        mDwCN = findViewById(R.id.dw_cn);
        mDwEN = findViewById(R.id.dw_en);
        
        mRecyclerView = findViewById(R.id.recycler_view);
    }
    
    
    private void handleFun(){

        String jsonData = FileHelper.readTxtFromAssets("function.json");
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonParser().parse(jsonData).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("data");
        for (JsonElement data : jsonArray) {
            FunBean funBean = gson.fromJson(data, new TypeToken<FunBean>(){}.getType());
            funList.add(funBean);
        }

        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);
        adapter = new IndexAdapter(funList);
        mRecyclerView.setAdapter(adapter);
    }

    
    private void handleImageScale() {
        //设置图像宽高
        float scale = 938 / 580f;
        int width = Screen.getWidth();
        int height = (int)(width / scale);
        
        
        ViewGroup.LayoutParams lp = mDwBg.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mDwBg.setLayoutParams(lp);

    }

    private void handleHeader() {
        View headerView = mNavigationView.getHeaderView(0);
        mCircleImageView = headerView.findViewById(R.id.header);

        String qqtx = "http://q1.qlogo.cn/g?b=qq&nk=2550962720&s=640";
        Glide.with(this).load(qqtx).into(mCircleImageView);
    }

    private void handleDayword() {
        final String dwurl = "http://open.iciba.com/dsapi";
        new Thread(new Runnable(){

                @Override
                public void run() {
                    String data = MyHttp.getHtml(dwurl);
                    handleJson(data);
                }
            }).start();
    }

    private void handleJson(final String jsonData) {
        runOnUiThread(new Runnable(){

                @Override
                public void run() {

                    Gson gson = new Gson();
                    try {
                        DaywordBean data = gson.fromJson(jsonData, DaywordBean.class);
                        String content = data.getContent();
                        String note = data.getNote();
                        String picture = data.getPicture();
                        mDwEN.setText(content);
                        mDwCN.setText(note);
                        Glide.with(mContext).load(picture).into(mDwBg);
                    } catch (Exception e) {}
                }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckStoragePermission.verifyStoragePermissions(this);

        handleDayword();
        handleHeader();
    }


    
    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,0,mToolbar);
    }
    
}
