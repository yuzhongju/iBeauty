package com.jueze.ibeauty;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import com.jaeger.library.StatusBarUtil;
import com.jueze.ibeauty.adapter.BlendentAdapter;
import com.jueze.ibeauty.bean.BlendentBean;
import com.jueze.ibeauty.util.FileUtil;
import java.util.ArrayList;

public class MdBlendentActivity extends BaseActivity {

    //widget
    private Toolbar mToolbar;
    private RecyclerView mRv;
    private BlendentAdapter adapter;

    //data
    private ArrayList<BlendentBean> blendentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_blendent);
        setSupportActionBar(mToolbar);
        setBack("MD配色");
        handleDoc();
    }

    @Override
    public void bindViews() {
        mToolbar = findViewById(R.id.toolbar);
        mRv = findViewById(R.id.recycler_view);
    }

	@Override
	public void initData() {
	}

	@Override
	public void initEvent() {
	}


	
    @Override
    public void setStatusBar() {
        super.setStatusBar();
        StatusBarUtil.setColorForSwipeBack(this, mColor, mAlpha);
    }


    private void handleDoc() {
        blendentList = new ArrayList<>();
        String nr = FileUtil.readTxtFromAssets("md");
        String[] docList = nr.split("\n");
        for (String doc : docList) {
            String name = doc.substring(0, doc.indexOf("["));
            int level = 500;
            String color = doc.substring(doc.indexOf("[") + 1, doc.indexOf("]"));
            String colors = doc.substring(doc.indexOf("(") + 1, doc.indexOf(")"));
            blendentList.add(new BlendentBean(name, level, color, colors));
        }
        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRv.setLayoutManager(lm);
        adapter = new BlendentAdapter(blendentList);
        mRv.setAdapter(adapter);
    }

}
