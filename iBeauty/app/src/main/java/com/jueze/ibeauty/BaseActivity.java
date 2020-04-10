package com.jueze.ibeauty;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.jaeger.library.StatusBarUtil;
import java.lang.reflect.Method;
import android.support.v7.app.ActionBar;

public abstract class BaseActivity extends AppCompatActivity {

    public static int mColor;
    public static int mAlpha;
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mColor = getResources().getColor(R.color.colorPrimary);
        mAlpha = 0;
        setStatusBar();
        bindViews();
        initData();
		initEvent();
    }

    public abstract void bindViews();
	public abstract void initData();
	public abstract void initEvent();
	
    public void setBack() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    public void setBack(String title) {
        if (hasActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }

	public void setToolBarTitle(String title){
		if(hasActionBar()){
			getSupportActionBar().setTitle(title);
		}
	}

	public void setToolBarSubTitle(String title){
		if(hasActionBar()){
			getSupportActionBar().setSubtitle(title);
		}
	}
	public boolean hasActionBar(){
		if(getSupportActionBar()!=null){
			return true;
		}else{
			return false;
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
		StatusBarUtil.setColor(this,mColor,mAlpha);
    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(menu != null){
            if(menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")){
                try{
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu,true);
                }catch(Exception e){}
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
    
    
}
