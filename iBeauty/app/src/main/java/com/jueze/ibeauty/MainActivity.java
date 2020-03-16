package com.jueze.ibeauty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.jueze.ibeauty.util.CheckStoragePermission;
import com.jueze.ibeauty.util.DisplayUtil;
import com.jueze.ibeauty.util.FileUtil;
import com.jueze.ibeauty.util.NetworkUtil;
import com.jueze.ibeauty.util.ScreenUtil;
import com.jueze.ibeauty.util.ShapeUtil;
import com.jueze.ibeauty.util.ToastUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class MainActivity extends BaseActivity {

    
    //widget
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    
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
        CheckStoragePermission.verifyStoragePermissions(this);
        setContentView(R.layout.activity_main);
        mContext = this;
		StatusBarUtil.setTransparentForImageView(this,mToolbar);
		setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        handleFun();
        handleImageScale();
        handleNvagationView();
        handleDayword();
        handleHeader();
		setHeader();
    }


    private void handleNvagationView(){
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

                @Override
                public boolean onNavigationItemSelected(MenuItem p1) {
                    switch(p1.getItemId()){
                        case R.id.nav_open_source:
							mContext.startActivity(new Intent(mContext,OpenSourceActivity.class));
                            break;
                    }
                    return true;
                }
        });
    }
    
    
    @Override
    public void bindViews() {
        
		mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        mDwBg = findViewById(R.id.dw_bg);
        mDwCN = findViewById(R.id.dw_cn);
        mDwEN = findViewById(R.id.dw_en);
        
        mRecyclerView = findViewById(R.id.recycler_view);
    }

	@Override
	public void initData() {
		sh = new SharedHelper();

        View headerView = mNavigationView.getHeaderView(0);
        mCircleImageView = headerView.findViewById(R.id.header);
		headerName = headerView.findViewById(R.id.nickname);
		headerEmail = headerView.findViewById(R.id.email);
	}

	@Override
	public void initEvent() {
		mCircleImageView.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					initQQEvent();
				}
		});
	}


	
	private void setHeader(){
		SharedPreferences sp = getSharedPreferences("qqinfo", Context.MODE_PRIVATE);
		String qq = sp.getString(sh.qqNum, "");
		String name = sp.getString(sh.qqName, "");
		String url = sp.getString(sh.imgUrl, "");
		if(TextUtils.isEmpty(qq) || TextUtils.isEmpty(name) || TextUtils.isEmpty(url)){
			initQQEvent();
		}
	}
	
    private void initQQEvent(){
		LinearLayout parent = new LinearLayout(this);
		final EditText qqInput = new EditText(this);
		Button saveBtn = new Button(this);

		int width = -1;
		int height = -2;

		parent.addView(qqInput, width, height);
		parent.addView(saveBtn, width, height);

		int padding = DisplayUtil.dip2px(8);
		parent.setOrientation(LinearLayout.VERTICAL);
		parent.setPadding(padding,padding,padding,padding);
		parent.setFocusable(true);
		parent.setFocusableInTouchMode(true);
		
		qqInput.setText(sh.get(sh.qqNum));
		qqInput.setSelectAllOnFocus(true);
		qqInput.setSingleLine(true);
		qqInput.setTextSize(13);

		ShapeUtil.set(saveBtn.getBackground(),getString(R.color.colorPrimary));
		saveBtn.setText("保存");
		saveBtn.setTextSize(13);
		saveBtn.setTextColor(Color.parseColor("#ffffff"));

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		alertDialog.setView(parent);
		final AlertDialog dialog = alertDialog.create();
		dialog.show();
		dialog.getWindow().setLayout((ScreenUtil.getWidth()*2)/3, LinearLayout.LayoutParams.WRAP_CONTENT);

		saveBtn.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					final String qqNum = qqInput.getText().toString();
					if(NetworkUtil.state()==0){
						ToastUtil.show("请检查网络连接");
					}else if(TextUtils.isEmpty(qqNum)){
						ToastUtil.show("输入不能为空");
					}else if(!TextUtils.isDigitsOnly(qqNum)){
						ToastUtil.show("格式错误，只允许数字");
					}else if(TextUtils.isDigitsOnly(qqNum)){
						final MyProgressDialog pd = new MyProgressDialog(mContext);
						pd.show();
						new Thread(new Runnable(){
								@Override
								public void run() {
									String api = "https://api.toubiec.cn/qq?qq="+qqNum+"&size=100";
									final String result = MyHttp.getHtml(api);
									runOnUiThread(new Runnable(){
											@Override
											public void run() {
												pd.dismiss();
												if(!TextUtils.isEmpty(result)){
													try{
														JSONObject jsonObj = new JSONObject(result);
														String code = jsonObj.getString("code");
														if(code.equals("200")){
															String qq = jsonObj.getString("qq");
															String imgurl = "http://q1.qlogo.cn/g?b=qq&nk="+qq+"&s=640";
															String name = jsonObj.getString("name");
															sh.save(qq,name,imgurl);
															handleHeader();
															dialog.dismiss();
															ToastUtil.show("设置头像成功");
														}else{
															ToastUtil.show("QQ号错误");
														}
													}catch(Exception e){}
												}else{
													ToastUtil.show("QQ号错误");
												}
											}
										});
								}
							}).start();
					}else{
						ToastUtil.show("未知错误");
					}
				}
			});
	}
	
    private void handleFun(){

        String jsonData = FileUtil.readTxtFromAssets("function.json");
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
        int width = ScreenUtil.getWidth();
        int height = (int)(width / scale);
        
        
        ViewGroup.LayoutParams lp = mDwBg.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mDwBg.setLayoutParams(lp);

    }

    private void handleHeader() {
		SharedPreferences sp = getSharedPreferences("qqinfo", Context.MODE_PRIVATE);
		String qq = sp.getString(sh.qqNum, "");
		String name = sp.getString(sh.qqName, "");
		String url = sp.getString(sh.imgUrl, "");
		if(!TextUtils.isEmpty(qq) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(url)){
			//String qqtx = "http://q1.qlogo.cn/g?b=qq&nk=2550962720&s=640";
			Glide.with(this).load(url).into(mCircleImageView);
			headerName.setText(name);
			headerEmail.setText(qq+"@qq.com");
		}
		
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

	class SharedHelper{
		public String spName = "qqinfo";
		public String qqNum = "qqNum";
		public String qqName = "qqName";
		public String imgUrl = "imgUrl";
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
		
		public String get(String name){
			return sp.getString(name,"");
		}
		
		public void clear(){
			editor.clear();
			editor.apply();
		}
	}
	
}
