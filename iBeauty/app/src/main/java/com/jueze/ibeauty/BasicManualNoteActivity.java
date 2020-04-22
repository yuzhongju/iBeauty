package com.jueze.ibeauty;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.gson.Gson;
import com.jueze.ibeauty.bean.ManualBean;
import com.jueze.utils.ClipBoardUtil;
import com.jueze.utils.ShareUtil;
import com.jueze.utils.ToastUtil;

public class BasicManualNoteActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_note);
        String data = getIntent().getStringExtra("data");
        ManualBean bean = new Gson().fromJson(data, ManualBean.class);
        String title = bean.getDocTitle();
        String note = bean.getDocContent();
        setSupportActionBar(mToolbar);
        setBack(title);
        mTextView.setText(note);
    }

    @Override
    public void bindViews() {
        mToolbar = findViewById(R.id.toolbar);
        mTextView = findViewById(R.id.content);
    }

	@Override
	public void initData() {
	}

	@Override
	public void initEvent() {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manual_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.manual_copy:
				ClipBoardUtil.write(this,mTextView.getText());
				ToastUtil.show(this,"已写入剪切板");
				break;
			case R.id.manual_share:
				ShareUtil.shareText(this, mTextView.getText().toString());
				break;
			case android.R.id.home:
				finish();
				break;
			default:
		}
		return true;
	}
}
