package com.jueze.ibeauty;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.jaeger.library.StatusBarUtil;
import com.jueze.ibeauty.util.MyClipBoard;
import com.jueze.ibeauty.util.MyShape;
import com.jueze.ibeauty.util.MyToast;

public class Base64Activity extends BaseActivity implements View.OnClickListener {



    private Toolbar mToolbar;
    private EditText mOrginal, mResult;
    private Button mEncrypt, mDecode, mCopy, mClear;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base64);
        setSupportActionBar(mToolbar);
        setBack("BASE64加密");
        handleViews();
        mEncrypt.setOnClickListener(this);
        mDecode.setOnClickListener(this);
        mCopy.setOnClickListener(this);
        mClear.setOnClickListener(this);
    }

    @Override
    public void bindViews() {
        super.bindViews();
        mToolbar = findViewById(R.id.toolbar);
        mOrginal = findViewById(R.id.original);
        mResult = findViewById(R.id.result);
        mEncrypt = findViewById(R.id.encrypt);
        mDecode = findViewById(R.id.decode);
        mCopy = findViewById(R.id.copy);
        mClear = findViewById(R.id.clear);
    }

    @Override
    public void setStatusBar() {
        super.setStatusBar();
        StatusBarUtil.setColorForSwipeBack(this,mColor,mAlpha);
    }
    
    
    private void handleViews(){
        MyShape.set(mOrginal,10,10,10,10,"#f0f0ff");
        MyShape.set(mResult,10,10,10,10,"#f0f0ff");
        MyShape.set(mEncrypt.getBackground(),getString(R.color.colorPrimary));
        MyShape.set(mDecode.getBackground(),getString(R.color.colorPrimary));
        MyShape.set(mCopy.getBackground(),getString(R.color.colorPrimary));
        MyShape.set(mClear.getBackground(),getString(R.color.colorPrimary));
    }
    
    @Override
    public void onClick(View view) {
        String original = null;
        String result = null;
        switch(view.getId()){
            case R.id.encrypt:
                original = mOrginal.getText().toString();
                if(original.equals("")){
                    MyToast.ts("输入不能为空");
                }else{
                    result = Base64.encodeToString(original.getBytes(), Base64.DEFAULT);
                    mResult.setText(result.trim());
                }
                break;
            case R.id.decode:
                original = mOrginal.getText().toString();
                if(original.equals("")){
                    MyToast.ts("输入不能为空");
                }else{
                    result = new String(Base64.decode(original.getBytes(), Base64.DEFAULT));
                    mResult.setText(result.trim());
                }
                break;
            case R.id.copy:
                if(mResult.getText().toString().equals("")){
                    MyToast.ts("没有内容可复制");
                }else{
                    MyClipBoard.write(mResult.getText());
                    MyToast.ts("已写入剪切板");
                }
                break;                                     
            case R.id.clear:
                mOrginal.setText("");
                mResult.setText("");
                break;
            default:
        }
    }
    
    
}
