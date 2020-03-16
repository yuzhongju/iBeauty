package com.jueze.ibeauty;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jueze.ibeauty.adapter.BookMarkAdapter;
import com.jueze.ibeauty.bean.BookMarkBean;
import com.jueze.ibeauty.dialog.MyBottomSheetDialog;
import com.jueze.ibeauty.dialog.MyProgressDialog;
import com.jueze.ibeauty.network.MyOkHttp;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import okhttp3.Call;
import okhttp3.Response;

public class BookMarkActivity extends BaseActivity {

    private String path;;
    private String url;
    private Toolbar mToolbar;
    private RecyclerView mRv;
    private MyOkHttp mokhttp;
    private BookMarkAdapter adapter;
    private MyProgressDialog mPd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        setSupportActionBar(mToolbar);
        setBack("网络书签");
        load();
    }


    @Override
    public void bindViews() {
        mToolbar = findViewById(R.id.toolbar);
        mRv = findViewById(R.id.recycler_view);
    }
	
	@Override
    public void initData() {

        for(File file : getFilesDir().getParentFile().listFiles()){
            if(file.getName().equals("shared_prefs")){
                path = file+"/bookmark.xml";
            }
        }
        url = "https://gitee.com/jueze/iapp/raw/master/bookmark.json";
    }

	@Override
	public void initEvent() {
	}


	

    private void load(){
        if(adapter != null){
            adapter.removeAll();
        }

        mPd = new MyProgressDialog(this);
        mPd.show();
        String a = read();
        if(a != null && !a.equals("")){
            url = a;
        }
        mokhttp = new MyOkHttp();
        
        try {
            mokhttp.getByAsync(url, new okhttp3.Callback(){

                    @Override
                    public void onFailure(Call p1, IOException p2) {
                    }

                    @Override
                    public void onResponse(Call p1, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            load2(response.body().string());
                        }else{
                            mPd.dismiss();
                        }
                    }
                });
        } catch (Exception e) {}
    }
    
    private void load2(final String data){
        runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    mPd.dismiss();
                    Gson gson = new Gson();
                    try{
                        BookMarkBean bookmarkBean = gson.fromJson(data, new TypeToken<BookMarkBean>(){}.getType());
                        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                        mRv.setLayoutManager(lm);
                        adapter = new BookMarkAdapter(bookmarkBean.getBookMark());
                        mRv.setAdapter(adapter);
                    }catch(Exception e){}
                    
                }
                
            
        });
    }
    
    
    private void setBookMark(){
        final MyBottomSheetDialog bsd = new MyBottomSheetDialog(this,"设置书签","确认");
        bsd.getEditText().setText(url);
        bsd.show();
        bsd.getBtn().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    save(bsd.getText());
                    bsd.dismiss();
                    load();
                }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bookmark_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
    
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.mark_note:
                
                WebView wv = new WebView(this);
                wv.setWebViewClient(new WebViewClient());
                wv.loadUrl("file:///android_asset/bookmark_intro.html");
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setView(wv);
                dialog.show();
                break;
            case R.id.mark_refresh:
                load();
                break;
            case R.id.mark_edit:
                setBookMark();
                break;
            case R.id.mark_reset:
                save("https://gitee.com/jueze/iapp/raw/master/bookmark.json");
                load();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    
   
    private void save(String value) {
        SharedPreferences sp = getSharedPreferences("bookmark", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("url",value);
        editor.apply();
    }

    private String read() {
        try {
            SharedPreferences sp = getSharedPreferences("bookmark", Context.MODE_PRIVATE);
            return sp.getString("url","");
        } catch (Exception e) {}

        return null;
    }

    private void clear(){
        SharedPreferences sp = getSharedPreferences("bookmark", Context.MODE_PRIVATE);
        if(sp != null ) sp.edit().clear().apply();
    }
    
    private boolean delete(){
        File file = new File(path);
        if(file.exists()){
            return file.delete();
        }
        return false;
    }

}
