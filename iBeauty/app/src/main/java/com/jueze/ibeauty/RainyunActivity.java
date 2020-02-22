package com.jueze.ibeauty;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.jueze.ibeauty.bean.PostDataBean;
import com.jueze.ibeauty.dialog.MyProgressDialog;
import com.jueze.ibeauty.network.CookieStore;
import com.jueze.ibeauty.network.MyOkHttp;
import com.jueze.ibeauty.util.MyIntent;
import com.jueze.ibeauty.util.MyNetWorkInfo;
import com.jueze.ibeauty.util.MyShape;
import com.jueze.ibeauty.util.MyString;
import com.jueze.ibeauty.util.MyToast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Cookie;
import okhttp3.Response;

public class RainyunActivity extends BaseActivity {

    private Context mContext;
    //widget
    private Toolbar mToolbar;
    private EditText mUser, mPass;
    private Button mLogin;
   
    private MyProgressDialog mPd;
    private SharedHelper sh;
    private MyOkHttp http;
    //data
    private String username;
    private String password;
    private final String BASE_URL = "https://www.rainyun.com/";
    private final String LOGIN = BASE_URL+"login";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainyun);
        mContext = this;
        setSupportActionBar(mToolbar);
        setBack("雨云主机");
        
        sh = new SharedHelper();
        MyShape.set(mLogin.getBackground(), getString(R.color.colorPrimary));
        mLogin.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    username = mUser.getText().toString().trim();
                    password = mPass.getText().toString().trim();
                    if(MyNetWorkInfo.get() == 0){
                        MyToast.ts("无网络连接");
                    }else{
                        if(username == null || username.length() == 0 || password == null || password.length() == 0){
                            MyToast.ts("请填写完整信息");
                        }else{
                            login();
                        }
                    }
                }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rainyun_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.visit:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL));
                startActivity(intent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    
    

    private void login(){
        mPd = new MyProgressDialog(mContext);
        mPd.setMessage("正在登录...");
        mPd.show();
        new Thread(new Runnable(){

                @Override
                public void run() {
                    
                    http = new MyOkHttp();
                    Response response = http.getBySync(LOGIN);
                    
                    CookieStore cookieStore = http.getCookieJar().getCookieStore();
                    List<Cookie> cookies = cookieStore.get(http.getHttpUrl());
                    for(Cookie cookie : cookies){
                        cookieStore.remove(http.getHttpUrl(), cookie);
                    }
                    
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {}
                   
                    response = http.getBySync(LOGIN);
                    String token = getToken(response);
                    username = mUser.getText().toString().trim();
                    password = mPass.getText().toString().trim();
                    List<PostDataBean> postDataList = new ArrayList<>();
                    postDataList.add(new PostDataBean("_token", token));
                    postDataList.add(new PostDataBean("log_name", username));
                    postDataList.add(new PostDataBean("log_pass", password));
                    
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {}
                    response = http.postWithListBySync(LOGIN, postDataList);
                    handleLogin(response);
                    
                }
        }).start();
    }
    
    private void handleLogin(final Response response){
        runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    mPd.dismiss();
                    if(!response.isSuccessful()){
                        MyToast.ts("登录失败");
                    }else{
                        try {
                            String body = response.body().string();
                            if(body.equals("0")){
                                MyToast.ts("登录失败");
                            }else if(body.equals("1")){
                                MyToast.ts("登录成功");
                                //登录成功保存用户信息
                                sh.save(username, password);
                                MyIntent.go(RainyunAppActivity.class);
                                finish();
                            }
                        } catch (IOException e) {}
                    }

                }
        });
    }
    private String getToken(Response response){
        String token = "";
        try {
            String body = response.body().string();
            String regex = "token:\"(.*?)\"";
            token = MyString.qc(body,regex);
        } catch (IOException e) {}
        
        return token;

    }
    
    @Override
    protected void onStart() {
        super.onStart();
        Map<String, String> data = sh.read();
        mUser.setText(data.get("username"));
        mPass.setText(data.get("password"));
    }

    
    @Override
    public void bindViews() {
        super.bindViews();
        mToolbar = findViewById(R.id.toolbar);
        mUser = findViewById(R.id.username);
        mPass = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);
    }
    
    class SharedHelper{
        
        private String filename;
        
        public SharedHelper(){
        }
        
        public void save(String name, String pass){
            SharedPreferences sp = mContext.getSharedPreferences("rainyun", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username", name);
            editor.putString("password", pass);
            editor.commit();
        }
        
        public Map<String, String> read(){
            Map<String, String> data = new HashMap<String, String>();
            SharedPreferences sp = mContext.getSharedPreferences("rainyun", Context.MODE_PRIVATE);
            data.put("username", sp.getString("username", ""));
            data.put("password", sp.getString("password", ""));
            return data;
        }
        
        public void clear(){
            SharedPreferences sp = mContext.getSharedPreferences("rainyun", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
            
        }
        
    }

}
