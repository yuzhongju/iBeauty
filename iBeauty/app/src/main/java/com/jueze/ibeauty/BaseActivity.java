package com.jueze.ibeauty;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.jaeger.library.StatusBarUtil;

public class BaseActivity extends AppCompatActivity {

    public static int mColor;
    public static int mAlpha;
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mColor = getResources().getColor(R.color.colorPrimary);
        mAlpha = 0;
        bindViews();
        setStatusBar();
        initData();
        
    }

    public void bindViews() {

    }

    public void setBack() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    public void setBack(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void setStatusBar(){
        StatusBarUtil.setColorForSwipeBack(this,mColor,mAlpha);
    }
    
    public void initData(){
        
    }
    
}
