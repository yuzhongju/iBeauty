package com.jueze.ibeauty;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.jueze.ibeauty.bean.ManualBean;

public class BasicManualNoteActivity extends BaseActivity {

    private Toolbar mToolbar;
    private EditText mEdittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_note);
        setSupportActionBar(mToolbar);
        handleIntent();
    }

    @Override
    public void bindViews() {
        super.bindViews();
        mToolbar = findViewById(R.id.toolbar);
        mEdittext = findViewById(R.id.content);
    }

    @Override
    public void setStatusBar() {
        super.setStatusBar();
        StatusBarUtil.setColorForSwipeBack(this,mColor,mAlpha);
    }
    
    private void handleIntent(){
        String data = getIntent().getStringExtra("data");
        ManualBean bean = new Gson().fromJson(data, ManualBean.class);
        String title = bean.getDocTitle();
        String note = bean.getDocContent();
        setBack(title);
        mEdittext.setText(note);
    }
    
}
