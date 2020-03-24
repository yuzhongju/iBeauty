package com.jueze.ibeauty.fragment;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.adapter.MdIconAdapter;
import com.jueze.ibeauty.bean.MdIconBean;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.jueze.ibeauty.util.MyString;

public class MdIconFragment extends BaseFragment {

	private Context mContext;
	private View rootView;
	private RecyclerView mRecyclerView;
	private MdIconAdapter adapter;


	private int type;
    private List<String> iconCate;
    private List<String> mBgColor;
    private List<String> mNameColor;
    private List<MdIconBean> mIconList = new ArrayList<>();


	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
                    adapter = new MdIconAdapter(mContext, mIconList);
					mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
					mRecyclerView.setAdapter(adapter);
					break;
				default:
			}
		}

	};

	public MdIconFragment(int type, List<String> iconCate, List<String> bgColor, List<String> nameColor) {
		this.type = type;
		this.iconCate = iconCate;
		this.mBgColor = bgColor;
		this.mNameColor = nameColor;
	}
	@Override
	public View initView(LayoutInflater inflater, ViewGroup container) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_mdicon, container, false);
		}
		mContext = rootView.getContext();
		mRecyclerView = rootView.findViewById(R.id.recycler_view);
		return rootView;
	}

	@Override
	public void initEvent() {
	}

	@Override
	public void lazyLoad() {
		loadIcon(type);
	}

	public void filter(String key) {
		adapter.getFilter().filter(key);
	}

    private void loadIcon(final int i) {
        new Thread(new Runnable(){
                @Override
                public void run() {
                    File file = new File(iconCate.get(i));
                    File[] fileList = file.listFiles();
                    for (File icon : fileList) {
                        String name = icon.getName();
						if(i==0){
							name = MyString.qc(name, "ic_", "_white");
						}else if(i==1){
							name = MyString.qc(name, "ic_", "_black");
						}
                        String path = icon.getPath();
                        mIconList.add(new MdIconBean(name, path, mBgColor.get(i), mNameColor.get(i)));
                    }
                    Collections.sort(mIconList, new Comparator<MdIconBean>(){

                            @Override
                            public int compare(MdIconBean p1, MdIconBean p2) {
                                return p1.getIconName().compareTo(p2.getIconName());
                            }
                        });
					mHandler.sendEmptyMessage(0);
                }
			}).start();
    }
}
