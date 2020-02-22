package com.jueze.ibeauty.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jueze.ibeauty.AppManagerActivity;
import com.jueze.ibeauty.Base64Activity;
import com.jueze.ibeauty.BasicManualActivity;
import com.jueze.ibeauty.BookMarkActivity;
import com.jueze.ibeauty.IappSourceManagerActivity;
import com.jueze.ibeauty.MdBlendentActivity;
import com.jueze.ibeauty.MdIconsActivity;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.RainyunActivity;
import com.jueze.ibeauty.WebSourceActivity;
import com.jueze.ibeauty.bean.FunBean;
import com.jueze.ibeauty.bean.NetdiskBean;
import com.jueze.ibeauty.bean.ShortUrlBean;
import com.jueze.ibeauty.bean.TrushBean;
import com.jueze.ibeauty.dialog.MyBottomSheetDialog;
import com.jueze.ibeauty.dialog.MyProgressDialog;
import com.jueze.ibeauty.network.MyHttp;
import com.jueze.ibeauty.util.MyClipBoard;
import com.jueze.ibeauty.util.MyIntent;
import com.jueze.ibeauty.util.MyNetWorkInfo;
import com.jueze.ibeauty.util.MyShape;
import com.jueze.ibeauty.util.MyString;
import com.jueze.ibeauty.util.MyToast;
import com.jueze.ibeauty.util.dip2px;
import java.util.List;

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.ViewHolder> {

    private List<FunBean> funList;
    private MyBottomSheetDialog mBsd;
    private MyProgressDialog mPd;
    private AlertDialog.Builder alertDialog;

    private Context mContext;
    private Activity mActivity;

    public IndexAdapter(List<FunBean> list) {
        this.funList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, intro;
        LinearLayout parent;
        public ViewHolder(View view) {
            super(view);
            parent = view.findViewById(R.id.item_parent);
            name = view.findViewById(R.id.name);
            intro = view.findViewById(R.id.intro);
        }
    }

    @Override
    public IndexAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int p2) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(IndexAdapter.ViewHolder holder, int position) {

        FunBean data = funList.get(position);
        String name =  data.getName();
        String intro = data.getIntro();
        final String spell = data.getPy();

        if (intro.equals("")) {
            intro = "什么也木有";
        }
        holder.name.setText(name);
        holder.intro.setText(intro);

        holder.parent.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mContext = view.getContext();
                    mActivity = (Activity)mContext;
                    handleData(spell);
                }
            });
    }

    @Override
    public int getItemCount() {
        return funList.size();
    }

    private void handleData(final String spell) {
        switch (spell) {
            case "yygl":
                MyIntent.go(AppManagerActivity.class);
                break;
            case "ljfl":
                handleTrush();
                break;
            case "smxx":
                handleSmxx();
                break;
            case "yyzj":
                MyIntent.go(RainyunActivity.class);
                break;
            case "base64":
                MyIntent.go(Base64Activity.class);
                break;
            case "shorturl":
                handleShortUrl();
                break;
            case "bdwptqm":
                handleNetdisk();
                break;
            case "hqwyym":
                MyIntent.go(WebSourceActivity.class);
                break;
            case "wdsq":
                MyIntent.go(BookMarkActivity.class);
                break;
            case "mdtb":
                MyIntent.go(MdIconsActivity.class);
                break;
            case "iappymgl":
                MyIntent.go(IappSourceManagerActivity.class);
                break;
            case "mdps":
                MyIntent.go(MdBlendentActivity.class);
                break;
            case "iappsc":
                MyIntent.go(BasicManualActivity.class);
                break;
            default:
        }
    }

    private void handleNetdisk() {
        mBsd = new MyBottomSheetDialog(mContext, "提取码查询", "查询", "格式：https://pan.baidu.com/s/1xxxx");
        mBsd.show();
        mBsd.getBtn().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if (MyNetWorkInfo.get() == 0) {
                        MyToast.ts("请检查您的网络连接");
                    } else {
                        final String key = mBsd.getText();
                        if (key.equals("")) {
                            MyToast.ts("输入不能为空");
                        } else {
                            mPd = new MyProgressDialog(mContext);
                            mPd.setMessage("查询中...");
                            mPd.show();
                            final String api = "https://nuexini.gq/bdp.php?url=" + key;
                            new Thread(new Runnable(){

                                    @Override
                                    public void run() {
                                        String r = MyHttp.getHtml(api);
                                        handleNetdisk2(mPd, r);
                                    }
                                }).start();
                        }
                    }

                }
            });
    }
    private void handleNetdisk2(final MyProgressDialog pd, final String data) {
        mActivity.runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    pd.dismiss();
                    Gson gson = new Gson();
                    try {
                        NetdiskBean netdiskBean = gson.fromJson(data, NetdiskBean.class);
                        String state = netdiskBean.getState();
                        String code = netdiskBean.getCode();
                        View view = View.inflate(mContext, R.layout.dialog_netdisk, null);
                        final TextView codetv = view.findViewById(R.id.netdisk_code);
                        final TextView statetv = view.findViewById(R.id.netdisk_state);

                        alertDialog = new AlertDialog.Builder(mContext);
                        alertDialog.setView(view);
                        final AlertDialog dialog = alertDialog.create();
                        dialog.show();
                        dialog.getWindow().setLayout(dip2px.to(120), LinearLayout.LayoutParams.WRAP_CONTENT);
                        codetv.setText(code);
                        statetv.setText(state.toLowerCase());
                        view.setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    MyClipBoard.write(codetv.getText());
                                    MyToast.ts("已写入剪切板");
                                    dialog.dismiss();
                                }
                            });
                    } catch (Exception e) {
                        MyToast.ts("查询失败");
                    }
                }
            });
    }

    private void handleTrush() {
        mBsd = new MyBottomSheetDialog(mContext, "垃圾分类", "查询", "输入查询关键字");
        mBsd.show();

        mBsd.getBtn().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if (MyNetWorkInfo.get() == 0) {
                        MyToast.ts("请检查您的网络连接");
                    } else {
                        final String key = mBsd.getText();
                        if (key.equals("")) {
                            MyToast.ts("输入不能为空");
                        } else {
                            mPd = new MyProgressDialog(mContext);
                            mPd.setMessage("查询中...");
                            mPd.show();
                            final String api = "https://service.xiaoyuan.net.cn/garbage/index/search?kw=" + key;
                            new Thread(new Runnable(){

                                    @Override
                                    public void run() {
                                        String r = MyHttp.getHtml(api);
                                        handleTrush2(mPd, r, key);
                                    }
                                }).start();
                        }
                    }
                }
            });
    }

    private void handleTrush2(final MyProgressDialog pd, final String data, final String key) {
        mActivity.runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    pd.dismiss();
                    Gson gson = new Gson();
                    try {
                        TrushBean trushBean = gson.fromJson(data, new TypeToken<TrushBean>(){}.getType());

                        String msg = trushBean.getMsg();
                        if (MyString.qc(msg,"到","条").equals("0")) {
                            MyToast.ts("没有查询到相关数据");
                        } else {
                            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_trush_cate, null);

                            MyShape.set(view, 15, 15, 0, 0, "#ffffff");
                            TextView trushTitle = view.findViewById(R.id.trush_title);
                            ImageView trushClose = view.findViewById(R.id.trush_cancel);
                            ListView trushListView = view.findViewById(R.id.trush_list_view);

                            final BottomSheetDialog bsd = new BottomSheetDialog(mContext, R.style.BottomSheetStyle);
                            bsd.setContentView(view);
                            bsd.show();

                            trushTitle.setText("关键字：" + key);
                            trushClose.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View view) {
                                        bsd.dismiss();
                                    }
                                });
                            TrushAdapter adapter = new TrushAdapter(mContext, R.layout.item_trush_query, trushBean.getData());
                            trushListView.setAdapter(adapter);
                        }
                    } catch (Exception e) {}


                }
            });
    }

    private void handleShortUrl() {
        mBsd = new MyBottomSheetDialog(mContext, "短链接", "确认", "输入需要转换的链接");
        mBsd.show();

        mBsd.getBtn().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (MyNetWorkInfo.get() == 0) {
                        MyToast.ts("请检查您的网络连接");
                    } else {
                        String key = mBsd.getText();
                        if (key.equals("")) {
                            MyToast.ts("输入不能为空");
                        } else {
                            mPd = new MyProgressDialog(mContext);
                            mPd.show();
                            final String apia = "https://api.uomg.com/api/long2dwz?dwzapi=tcn&url=" + key;
                            final String apib = "https://api.uomg.com/api/long2dwz?dwzapi=urlcn&url=" + key;

                            new Thread(new Runnable(){
                                    @Override
                                    public void run() {
                                        String r = MyHttp.getHtml(apia);
                                        String r2 = MyHttp.getHtml(apib);
                                        handleShortUrl2(mPd, r, r2);
                                    }
                                }).start();
                        }
                    }

                }
            });
    }


    private void handleShortUrl2(final MyProgressDialog pd, final String data, final String data2) {
        mActivity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    pd.dismiss();
                    Gson gson = new Gson();
                    try {
                        ShortUrlBean a = gson.fromJson(data, ShortUrlBean.class);
                        ShortUrlBean b = gson.fromJson(data2, ShortUrlBean.class);
                        final String url = a.getUrl();
                        final String url2 = b.getUrl();

                        alertDialog = new AlertDialog.Builder(mContext);
                        alertDialog.setTitle("");
                        alertDialog.setMessage("① 新浪短链\n" + url + "\n\n② 腾讯短链\n" + url2);
                        alertDialog.setCancelable(true);
                        alertDialog.setPositiveButton("复制②", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which) {
                                    MyClipBoard.write(url2);
                                    MyToast.ts("已写入剪切板");
                                }
                            });
                        alertDialog.setNegativeButton("复制①", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which) {
                                    MyClipBoard.write(url);
                                    MyToast.ts("已写入剪切板");
                                }
                            });
                        alertDialog.show();
                    } catch (Exception e) {}
                }
            });
    }

    private void handleSmxx() {
        String base = "aHR0cHM6Ly9qaWF6aGFuZy5xcS5jb20vd2FwL2hlYWx0aC9kaXN0L2hvbWUvaW5kZXguaHRtbA==";
        String url = "mqqapi://forward/url?version=1&src_type=web&url_prefix=" + base;
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

}
