package com.jueze.ibeauty;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.jueze.ibeauty.RainyunAppActivity;
import com.jueze.ibeauty.bean.PostDataBean;
import com.jueze.ibeauty.dialog.MyProgressDialog;
import com.jueze.ibeauty.network.CookieStore;
import com.jueze.ibeauty.network.OkHttpUtil;
import com.jueze.ibeauty.util.MyString;
import com.jueze.ibeauty.util.NetworkUtil;
import com.jueze.ibeauty.util.ShapeUtil;
import com.jueze.ibeauty.util.ToastUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Cookie;
import okhttp3.Response;
import com.jueze.ibeauty.util.LogUtil;
import android.os.Handler;

public class RainyunActivity extends BaseActivity {


    private Context mContext;
    //widget
    private Toolbar mToolbar;
    private EditText mUser, mPass;
    private Button mLogin;

    private MyProgressDialog mPd;
    private SharedHelper sh;
    private OkHttpUtil http;
    //data
    private String username;
    private String password;
    private final String BASE_URL = "https://www.rainyun.com/";
    private final String LOGIN = BASE_URL + "login";

	private List<Cookie> cookies=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainyun);
        mContext = this;
        setSupportActionBar(mToolbar);
        setBack("雨云主机");
    }

	@Override
    public void bindViews() {
        mToolbar = findViewById(R.id.toolbar);
        mUser = findViewById(R.id.username);
        mPass = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);
    }

	@Override
	public void initData() {
        ShapeUtil.set(mLogin.getBackground(), getString(R.color.colorPrimary));
        sh = new SharedHelper();
	}

	@Override
	public void initEvent() {
        mLogin.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    username = mUser.getText().toString().trim();
                    password = mPass.getText().toString().trim();
                    if (NetworkUtil.state() == 0) {
                        ToastUtil.show("无网络连接");
                    } else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
						login();
					} else {
						ToastUtil.show("请填写完整信息");
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
        switch (item.getItemId()) {
            case R.id.visit:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL));
                startActivity(intent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void login() {
        mPd = new MyProgressDialog(mContext);
        mPd.setMessage("正在登录...");
        mPd.show();
        new Thread(new Runnable(){

                @Override
                public void run() {

                    http = new OkHttpUtil();
                    Response response = http.getBySync(LOGIN);
					//清除缓存
                    CookieStore cookieStore = http.getCookieJar().getCookieStore();
                    cookies = cookieStore.get(http.getHttpUrl());
					
                    for (Cookie cookie : cookies) {
                        cookieStore.remove(http.getHttpUrl(), cookie);
                    }
					
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {}

                    response = http.getBySync(LOGIN);
                    String token = getToken(response);
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

    private void handleLogin(final Response response) {
        runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    mPd.dismiss();
                    if (!response.isSuccessful()) {
                        ToastUtil.show("登录失败");
                    } else {
                        try {
                            String body = response.body().string();
                            if (body.equals("0")) {
                                ToastUtil.show("登录失败");
                            } else if (body.equals("1")) {
                                ToastUtil.show("登录成功");
                                //登录成功保存用户信息
                                sh.save(username, password,true);
								mContext.startActivity(new Intent(mContext, RainyunAppActivity.class));
                                finish();
                            }
                        } catch (IOException e) {}
                    }

                }
			});
    }
    private String getToken(Response response) {
        String token = "";
        try {
            String body = response.body().string();
            String regex = "token:\"(.*?)\"";
            token = MyString.qc(body, regex);
        } catch (IOException e) {}

        return token;

    }

    @Override
    protected void onStart() {
        super.onStart();
        Map<String, Object> data = sh.read();
		username = data.get("username").toString();
		password = data.get("password").toString();
		if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
			mUser.setText(username);
			mPass.setText(password);
		}
    }





    class SharedHelper {

        public SharedHelper() {
        }

        public void save(String name, String pass,boolean hassignin) {
            SharedPreferences sp = mContext.getSharedPreferences("rainyun", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username", name);
            editor.putString("password", pass);
			editor.putBoolean("hassignin",hassignin);
            editor.apply();
        }

        public Map<String, Object> read() {
            Map<String, Object> data = new HashMap<>();
            SharedPreferences sp = mContext.getSharedPreferences("rainyun", Context.MODE_PRIVATE);
            data.put("username", sp.getString("username", ""));
            data.put("password", sp.getString("password", ""));
			data.put("hassignin",sp.getBoolean("hassignin",false));
            return data;
        }

        public void clear() {
            SharedPreferences sp = mContext.getSharedPreferences("rainyun", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();

        }

    }

}
