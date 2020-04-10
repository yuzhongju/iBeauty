package com.jueze.ibeauty;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.jueze.ibeauty.adapter.BlendentAdapter;
import com.jueze.ibeauty.bean.BlendentBean;
import com.jueze.ibeauty.util.FileUtil;
import java.util.ArrayList;
import android.widget.SeekBar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import com.jueze.ibeauty.util.ShapeUtil;
import com.jueze.ibeauty.util.ClipBoardUtil;
import com.jueze.ibeauty.util.ToastUtil;
import android.view.MotionEvent;

public class MdBlendentActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    //widget
    private Toolbar mToolbar;
    private RecyclerView mRv;
    private BlendentAdapter adapter;

    //data
    private ArrayList<BlendentBean> blendentList;

	//调色板
	private AlertDialog dialog;
	private SeekBar alphaSeekBar, redSeekBar, greenSeekBar, blueSeekBar;
	private Button alphaText, alphaDown, redText, redDown, greenText, greenDown, blueText, blueDown, closePallet;
	private String hexString;
	private String hexColor;
	private TextView hexTextView;
	private RelativeLayout hexColorBg;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_blendent, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.pallet:
				View view = View.inflate(this, R.layout.dialog_pallet, null);

				alphaSeekBar = view.findViewById(R.id.alpha_seek_bar);
				alphaText = view.findViewById(R.id.alpha_text);
				alphaDown = view.findViewById(R.id.alpha_down);

				redSeekBar = view.findViewById(R.id.red_seek_bar);
				redText = view.findViewById(R.id.red_text);
				redDown = view.findViewById(R.id.red_down);


				greenSeekBar = view.findViewById(R.id.green_seek_bar);
				greenText = view.findViewById(R.id.green_text);
				greenDown = view.findViewById(R.id.green_down);


				blueSeekBar = view.findViewById(R.id.blue_seek_bar);
				blueText = view.findViewById(R.id.blue_text);
				blueDown = view.findViewById(R.id.blue_down);


				hexTextView = view.findViewById(R.id.hex_string);
				hexColorBg = view.findViewById(R.id.hex_color_bg);

				closePallet = view.findViewById(R.id.close_pallet);

				dialog = new AlertDialog.Builder(this)
					.setView(view)
					.setCancelable(false)
					.create();
				dialog.show();
				//dialog背景
				ShapeUtil.set(dialog.getWindow().getDecorView(), 15, 15, 15, 15, "#ffffff");
				//seekbar
				alphaSeekBar.setOnSeekBarChangeListener(this);
				redSeekBar.setOnSeekBarChangeListener(this);
				greenSeekBar.setOnSeekBarChangeListener(this);
				blueSeekBar.setOnSeekBarChangeListener(this);

				//加
				alphaText.setOnClickListener(this);
				redText.setOnClickListener(this);
				greenText.setOnClickListener(this);
				blueText.setOnClickListener(this);
				//减
				alphaDown.setOnClickListener(this);
				redDown.setOnClickListener(this);
				greenDown.setOnClickListener(this);
				blueDown.setOnClickListener(this);

				//复制color
				hexTextView.setOnClickListener(this);
				//关闭
				closePallet.setOnClickListener(this);
				break;
			default:
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		hexString = Integer.toHexString(progress);
		hexString = hexString.length() == 1 ?"0" + hexString: hexString;
		switch (seekBar.getId()) {
			case R.id.alpha_seek_bar:
				alphaText.setText(hexString);
				hexTextView.setText(hexColor = "#" + hexString + redText.getText() + greenText.getText() + blueText.getText());
				break;
			case R.id.red_seek_bar:
				redText.setText(hexString);
				hexTextView.setText(hexColor = "#" + alphaText.getText() + hexString + greenText.getText() + blueText.getText());
				break;
			case R.id.green_seek_bar:
				greenText.setText(hexString);
				hexTextView.setText(hexColor = "#" + alphaText.getText() + redText.getText() + hexString + blueText.getText());
				break;
			case R.id.blue_seek_bar:
				blueText.setText(hexString);
				hexTextView.setText(hexColor = "#" + alphaText.getText() + redText.getText() + greenText.getText() + hexString);
				break;
			default:
		}

		hexColorBg.setBackgroundColor(Color.parseColor(hexColor));
	}

	@Override
	public void onStartTrackingTouch(SeekBar p1) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar p1) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.alpha_text:
				alphaSeekBar.setProgress(alphaSeekBar.getProgress() + 1, true);
				break;
			case R.id.alpha_down:
				alphaSeekBar.setProgress(alphaSeekBar.getProgress() - 1, true);
				break;
			case R.id.red_text:
				redSeekBar.setProgress(redSeekBar.getProgress() + 1, true);
				break;
			case R.id.red_down:
				redSeekBar.setProgress(redSeekBar.getProgress() - 1, true);
				break;
			case R.id.green_text:
				greenSeekBar.setProgress(greenSeekBar.getProgress() + 1, true);
				break;
			case R.id.green_down:
				greenSeekBar.setProgress(greenSeekBar.getProgress() - 1, true);
				break;
			case R.id.blue_text:
				blueSeekBar.setProgress(blueSeekBar.getProgress() + 1, true);
				break;
			case R.id.blue_down:
				blueSeekBar.setProgress(blueSeekBar.getProgress() - 1, true);
				break;
			case R.id.close_pallet:
				dialog.dismiss();
				break;
			case R.id.hex_string:
				ClipBoardUtil.write(hexTextView.getText());
				ToastUtil.show("已写入剪切板");
				break;
			default:
		}
	}
	
}
