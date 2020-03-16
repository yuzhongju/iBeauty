package com.jueze.ibeauty.dialog;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.util.ShapeUtil; //此处导入具体看自己的shape类位置

public class MyBottomSheetDialog {

	private OnBsdClickListener listener;
    private Context mContext;
    private BottomSheetDialog bsd;
    private View mView;
    private TextView mTextview;
    private EditText mEditText;
    private Button mButton;
    private String mTitle;
    private String mBtnText;
    private String mHint;

    public MyBottomSheetDialog(Context context) {
        this.mContext = context;
        init();
    }
    public MyBottomSheetDialog(Context context, String title, String btnText) {
        this.mContext = context;
        this.mTitle = title;
        this.mBtnText = btnText;
        init();
    }

    public MyBottomSheetDialog(Context context, String title, String btnText, String hint) {
        this.mContext = context;
        this.mTitle = title;
        this.mBtnText = btnText;
        this.mHint = hint;
        init();
    }

    protected void init() {
        mView = View.inflate(mContext, R.layout.dialog_bsd, null);
        mTextview = mView.findViewById(R.id.bsd_title);
        mEditText = mView.findViewById(R.id.bsd_bjk);
        mButton = mView.findViewById(R.id.bsd_btn);

        ShapeUtil.set(mView, 15, 15, 0, 0, "#ffffff");
        ShapeUtil.set(mButton.getBackground(), mContext.getString(R.color.colorPrimary));

        if (mTitle != null) {
            mTextview.setText(mTitle);
        }
        if (mBtnText != null) {
            mButton.setText(mBtnText);
        }
        if (mHint != null) {
            mEditText.setHint(mHint);
        }
        bsd = new BottomSheetDialog(mContext, R.style.BottomSheetStyle);
        bsd.setContentView(mView);
		
		mButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					if(listener!=null){
						listener.onBsdClick(mEditText.getText().toString());
					}
				}
				
		});
    }

    public void show() {
        bsd.show();
    }

    public void dismiss() {
        bsd.dismiss();
    }

    public Button getBtn() {
        return this.mButton;
    }

    public EditText getEditText(){
        return this.mEditText;
    }
    public String getText() {
        return this.mEditText.getText().toString();
    }

    public void setTitle(String title) {
        this.mTextview.setText(title);
    }

    public void setBtnText(String btnText) {
        this.mButton.setText(btnText);
    }

    public void setHint(String hint) {
        this.mEditText.setHint(hint);
    }
	
	public interface OnBsdClickListener{
		public void onBsdClick(String key);
	}
	
	public void setOnBsdClickListener(OnBsdClickListener listener){
		this.listener = listener;
	}
}

