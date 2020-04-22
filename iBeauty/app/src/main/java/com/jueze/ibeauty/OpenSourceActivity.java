package com.jueze.ibeauty;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.jueze.ibeauty.adapter.OpenSourceAdapter;
import java.util.List;
import com.jueze.ibeauty.bean.OpenSourceBean;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.AdapterView;
import android.widget.Adapter;
import android.view.View;
import android.content.Intent;
import android.net.Uri;

public class OpenSourceActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ListView mListview;
    private List<OpenSourceBean> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source);
        setSupportActionBar(mToolbar);
        setBack("开源");
        OpenSourceAdapter adapter = new OpenSourceAdapter(this, R.layout.item_open_source, list);
        mListview.setAdapter(adapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    OpenSourceBean data = list.get(position);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getUrl())));
                }
        });
    }

    @Override
    public void bindViews() {
       
        mToolbar = findViewById(R.id.toolbar);
        mListview  = findViewById(R.id.list_view);
    }

    @Override
    public void initData() {
        
        list.add(new OpenSourceBean("Gson-2.8.5","A Java serialization/deserialization library to convert Java Objects into JSON and back","https://github.com/google/gson"));
        list.add(new OpenSourceBean("OkHttp-4.2.1","Square’s meticulous HTTP client for Java and Kotlin.","https://github.com/square/okhttp"));
        list.add(new OpenSourceBean("CircleImageView-2.1.0","A circular ImageView for Android","https://github.com/hdodenhof/CircleImageView"));
        list.add(new OpenSourceBean("Glide-3.7.0","An image loading and caching library for Android focused on smooth scrolling.","https://github.com/bumptech/glide"));
        list.add(new OpenSourceBean("StatusBarUtil-1.4.0","A util for setting status bar style on Android App.","https://github.com/laobie/StatusBarUtil"));
        list.add(new OpenSourceBean("Jsoup-1.12.1","Java HTML Parser, with best of DOM, CSS, and jquery","https://github.com/jhy/jsoup"));
		list.add(new OpenSourceBean("FloatingActionButton-1.6.4","Android Floating Action Button based on Material Design specification","https://github.com/Clans/FloatingActionButton"));
		list.add(new OpenSourceBean("ColorPickerPreference","ColorPickerPreference for android to create color picker in preferences. Project created as Library","https://github.com/attenzione/android-ColorPickerPreference"));
    }

	@Override
	public void initEvent() {
	}

    
	
    
}
