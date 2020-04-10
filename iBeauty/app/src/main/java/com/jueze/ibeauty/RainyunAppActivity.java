package com.jueze.ibeauty;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jueze.ibeauty.bean.PostDataBean;
import com.jueze.ibeauty.dialog.MyProgressDialog;
import com.jueze.ibeauty.network.OkHttpUtil;
import com.jueze.ibeauty.util.NetworkUtil;
import com.jueze.ibeauty.util.ShapeUtil;
import com.jueze.ibeauty.util.ToastUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Response;

public class RainyunAppActivity extends BaseActivity {


    private final String BASE_URL = "https://www.rainyun.com/";
    private final String REWARD = BASE_URL+"app/usr/reward";
    private OkHttpUtil http;
    private String score = "";
    private Context mContext;
    
    private LinearLayout mParent;
    private TextView mScore;
    private Button mSignin;
    private Toolbar mToolbar;
    private MyProgressDialog mPd;
	private boolean isSignined=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainyun_app);
        mContext = this;
        setSupportActionBar(mToolbar);
        setBack("控制台");
        initUI();
        initData();
        firstLoad();
        mSignin.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1) {
                    if(NetworkUtil.state() == 0){
                        ToastUtil.show("无网络连接");
                    }else{
                        signin();
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
    
    
    @Override
    public void bindViews() {
        
        mParent = findViewById(R.id.yy_parent);
        mToolbar = findViewById(R.id.toolbar);
        mScore = findViewById(R.id.yy_score);
        mSignin = findViewById(R.id.yy_signin);
    }

	@Override
    public void initData(){
        http = new OkHttpUtil();
    }

	@Override
	public void initEvent() {
	}

    

    private void initUI(){
        ShapeUtil.set(mSignin.getBackground(),getString(R.color.colorPrimary));
        ShapeUtil.set(mParent,10,10,10,10,"#f0f0ff");
    }
    
    
    private void firstLoad(){
        new Thread(new Runnable(){

                @Override
                public void run() {
                    Response response = http.getBySync(REWARD);
                    getScore(response);
                }
        }).start();
    }
    
    private void getScore(final Response response){
        runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    String token = null;
                    try {
                        String body = response.body().string();
                        Matcher m = Pattern.compile("primary\">(.*?)<").matcher(body);
                        if(m.find()){
                            score = m.group(1).trim();
                        }
                        m = Pattern.compile("token:\"(.*?)\"").matcher(body);
                        if(m.find()){
                            token = m.group(1);
                        }
                    } catch (IOException e) {}
                    mScore.setText(score);
                    mScore.setTag(R.id.tag_yy_sigin_token, token);
                }
            });
    }
    
    
    private void signin(){
		
        mPd = new MyProgressDialog(mContext);
        mPd.setMessage("签到中...");
        mPd.show();
        final String token = mScore.getTag(R.id.tag_yy_sigin_token).toString();
        new Thread(new Runnable(){

                @Override
                public void run() {
                    List<PostDataBean> data = new ArrayList<>();
                    data.add(new PostDataBean("_token", token));
                    data.add(new PostDataBean("Action","GetReward"));
                    data.add(new PostDataBean("TaskName","每日签到"));
                    Response resp = http.postWithListBySync(REWARD, data);
                    handleSignin(resp);
                }
        }).start();
    }
    
    private void handleSignin(final Response response){
        runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    mPd.dismiss();
                    try {
                        String body = response.body().string();
                        if(body.equals("1")){
                            ToastUtil.show("签到成功");
                            firstLoad();
                        }else{
                            ToastUtil.show("签到失败");
                        }
                    } catch (IOException e) {}
                }
        });
    }

	
}
