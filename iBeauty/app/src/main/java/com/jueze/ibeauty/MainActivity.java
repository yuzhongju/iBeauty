package com.jueze.ibeauty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.jueze.ibeauty.dialog.MyProgressDialog;
import com.jueze.ibeauty.network.MyHttp;
import com.jueze.utils.FileUtil;
import com.jueze.utils.NetworkUtil;
import com.jueze.utils.PermissionUtil;
import com.jueze.utils.ScreenUtil;
import com.jueze.utils.ToastUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.support.v7.widget.GridLayoutManager;

public class MainActivity extends BaseActivity {

    
    //widget
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    
	private RelativeLayout dwParent;
    private ImageView mDwBg;
    private TextView mDwEN, mDwCN;
    private CircleImageView mCircleImageView;
	private TextView headerName, headerEmail;

    private RecyclerView mRecyclerView;
    List<FunBean> funList = new ArrayList<>();
    IndexAdapter adapter;
    //data
    private Context mContext;
	private SharedHelper sh;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PermissionUtil.requestReadPermission(this);
		StatusBarUtil.setTransparentForImageView(this,mToolbar);
		setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
		
    }

    @Override
    public void bindViews() {   
		mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
		dwParent=findViewById(R.id.dw_parent);
        mDwBg = findViewById(R.id.dw_bg);
        mDwCN = findViewById(R.id.dw_cn);
        mDwEN = findViewById(R.id.dw_en);
        
        mRecyclerView = findViewById(R.id.recycler_view);

        View headerView = mNavigationView.getHeaderView(0);
        mCircleImageView = headerView.findViewById(R.id.header);
		headerName = headerView.findViewById(R.id.nickname);
		headerEmail = headerView.findViewById(R.id.email);
    }

	@Override
	public void initData() {
		PermissionUtil.requestReadPermission(this);
		mContext = this;
		sh = new SharedHelper();

		if(NetworkUtil.state(mContext)==0){
			dwParent.setVisibility(View.GONE);
		}else{
			dwParent.setVisibility(View.VISIBLE);
		}
		//显示dayword图片宽高
        handleImageScale();
        handleDayword();
		checkQQInfo();
		handleFun();
		
	}

	@Override
	public void initEvent() {
		mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

                @Override
                public boolean onNavigationItemSelected(MenuItem p1) {
                    switch(p1.getItemId()){
                        case R.id.nav_open_source:
							mContext.startActivity(new Intent(mContext,OpenSourceActivity.class));
                            break;
                    }
					mDrawerLayout.closeDrawer(Gravity.START);
                    return true;
                }
			});
		//NavigationView头像点击设置qq
		mCircleImageView.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					initQQEvent(false);
				}
		});
	}

	//如果qq信息为空，则弹出设置qq对话框，否则设置
	private void checkQQInfo(){
		SharedPreferences sp = getSharedPreferences("qqinfo", Context.MODE_PRIVATE);
		String qq = sp.getString(sh.qqNum, "");
		String name = sp.getString(sh.qqName, "");
		String url = sp.getString(sh.imgUrl, "");
		boolean isNotTip = sp.getBoolean(sh.qqTip, false);
		if(!isNotTip){
			if(TextUtils.isEmpty(qq) || TextUtils.isEmpty(name) || TextUtils.isEmpty(url)){
				initQQEvent(true);
			}else{
				Glide.with(this).load(url).into(mCircleImageView);
				headerName.setText(name);
				headerEmail.setText(qq+"@qq.com");
			}
		}
	}
	
	//设置header
    private void handleHeader() {
		SharedPreferences sp = getSharedPreferences("qqinfo", Context.MODE_PRIVATE);
		String qq = sp.getString(sh.qqNum, "");
		String name = sp.getString(sh.qqName, "");
		String url = sp.getString(sh.imgUrl, "");
		if(!TextUtils.isEmpty(qq) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(url)){
			Glide.with(this).load(url).into(mCircleImageView);
			headerName.setText(name);
			headerEmail.setText(qq+"@qq.com");
		}
    }
	
	//初始化添加qq的AlertDialog
    private void initQQEvent(boolean isStart){
		
		View parent = View.inflate(this,R.layout.dialog_main_set_qq,null);
		final EditText qqInput = parent.findViewById(R.id.qq_input);
		Button saveBtn = parent.findViewById(R.id.qq_confirm);
		Button notTipBtn = parent.findViewById(R.id.qq_not_tip);
		
		if(!isStart){
			notTipBtn.setVisibility(View.GONE);
		}else{
			notTipBtn.setVisibility(View.VISIBLE);
		}
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		alertDialog.setView(parent);
		final AlertDialog dialog = alertDialog.create();
		dialog.show();
		//设置弹窗宽度
		dialog.getWindow().setLayout((ScreenUtil.getWidth(mContext)*2)/3, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		notTipBtn.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					sh.setTip(true);
					dialog.dismiss();
				}
			});
		saveBtn.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					final String qqNum = qqInput.getText().toString();
					if(NetworkUtil.state(mContext)==0){
						ToastUtil.show(mContext,"无网络连接");
					}else if(TextUtils.isEmpty(qqNum)){
						ToastUtil.show(mContext,"输入不能为空");
					}else if(TextUtils.isDigitsOnly(qqNum)){
						final MyProgressDialog pd = new MyProgressDialog(mContext);
						pd.show();
						new Thread(new Runnable(){
								@Override
								public void run() {
									String api = "http://api.5ifxw.cn/qqxt/api.php?qq="+qqNum;
									final String result = MyHttp.getHtml(api);
									runOnUiThread(new Runnable(){
											@Override
											public void run() {
												pd.dismiss();
												if(!TextUtils.isEmpty(result)){
													try{
														JSONObject jsonObj = new JSONObject(result);
														String code = jsonObj.getString("code");
														if(code.equals("1")){
															String qq = qqNum;
															String name = jsonObj.getString("name");
															String imgurl = jsonObj.getString("imgurl");
															sh.save(qq,name,imgurl);
															handleHeader();
															dialog.dismiss();
															ToastUtil.show(mContext,"设置头像成功");
															mDrawerLayout.openDrawer(Gravity.START);
														}else{
															ToastUtil.show(mContext,"设置失败，请重新尝试");
														}
													}catch(Exception e){}
												}else{
													ToastUtil.show(mContext,"网络超时或未知错误");
												}
											}
										});
								}
							}).start();
					}else{
						ToastUtil.show(mContext,"未知错误");
					}
				}
			});
	}
	
	//处理首页功能列表
    private void handleFun(){
		new Thread(new Runnable(){

				@Override
				public void run() {
					String jsonData = FileUtil.readFromAssets(mContext,"function.json");
					Gson gson = new Gson();
					JsonObject jsonObject = new JsonParser().parse(jsonData).getAsJsonObject();
					JsonArray jsonArray = jsonObject.getAsJsonArray("data");
					for (JsonElement data : jsonArray) {
						FunBean funBean = gson.fromJson(data, new TypeToken<FunBean>(){}.getType());
						funList.add(funBean);
					}
					mHandler.sendEmptyMessage(0);
				}
		}).start();
    }

    private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==0){
				mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
				adapter = new IndexAdapter(funList);
				mRecyclerView.setAdapter(adapter);
			}
		}
		
	};
    private void handleImageScale() {
        //设置图像宽高
        float scale = 938 / 580f;
        int width = ScreenUtil.getWidth(mContext);
        int height = (int)(width / scale);
            
        ViewGroup.LayoutParams lp = mDwBg.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mDwBg.setLayoutParams(lp);

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
	protected void onResume() {
		super.onResume();

		if(NetworkUtil.state(mContext)==0){
			dwParent.setVisibility(View.GONE);
		}else{
			dwParent.setVisibility(View.VISIBLE);
			handleDayword();
			handleHeader();
		}
	}
	
	

	//保存qq信息
	class SharedHelper{
		public String spName = "qqinfo";
		public String qqNum = "qqNum";
		public String qqName = "qqName";
		public String imgUrl = "imgUrl";
		public String qqTip = "qqTip";
		private SharedPreferences.Editor editor;
		private SharedPreferences sp;
		public SharedHelper(){
			editor = getSharedPreferences(spName, Context.MODE_PRIVATE).edit();
			sp = getSharedPreferences(spName, Context.MODE_PRIVATE);
		}
		
		public void save(String qqNum, String qqName, String imgUrl){
			editor.putString(this.qqNum, qqNum);
			editor.putString(this.qqName, qqName);
			editor.putString(this.imgUrl, imgUrl);
			editor.apply();
		}
		
		public void setTip(boolean isTip){
			editor.putBoolean(this.qqTip,isTip);
			editor.apply();
		}
		public String getString(String name){
			return sp.getString(name,"");
		}
		
		public boolean getBoolean(String tip){
			return sp.getBoolean(tip,false);
		}
		public void clear(){
			editor.clear();
			editor.apply();
		}
	}
	
}
