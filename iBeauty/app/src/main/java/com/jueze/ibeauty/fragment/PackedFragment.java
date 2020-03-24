package com.jueze.ibeauty.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.adapter.PackedAdapter;
import com.jueze.ibeauty.bean.PackSourceBean;
import java.io.File;
import java.util.List;
import android.support.v7.widget.StaggeredGridLayoutManager;
import com.jueze.ibeauty.util.LogUtil;
import java.util.ArrayList;
import com.jueze.ibeauty.util.ToastUtil;
import java.util.Collections;
import java.util.Comparator;
import java.text.Collator;
import java.util.Locale;

public class PackedFragment extends Fragment {

    private String filePath;
    private View rootView;
    private RecyclerView mRv;
    private List<PackSourceBean> mDataList = new ArrayList<>();
    private PackedAdapter adapter;



    public PackedFragment(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_packed, container, false);
        }
        mRv = rootView.findViewById(R.id.recycler_view);
        load();
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) refresh();
    }
    
	public void refresh(){
		if(adapter!=null){
			handleData();
			adapter.refresh(mDataList);
		}
	}
	
	private void load(){
		if(adapter==null){
			handleData();
			adapter = new PackedAdapter(mDataList);
		}
		mRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
		mRv.setAdapter(adapter);
		
	}
	
    private void handleData() {
        if (adapter != null) adapter.clear();
        File file = new File(filePath);
        try {
            for (File f : file.listFiles()) {
                LogUtil.d(f.getPath());
                String name = f.getName();
                String size = decimal2(f.length());
                mDataList.add(new PackSourceBean(name, size, f.getPath()));
            }

            
            Collections.sort(mDataList, new Comparator<PackSourceBean>(){

                    @Override
                    public int compare(PackSourceBean p1, PackSourceBean p2) {
                        Comparator<Object> com = Collator.getInstance(Locale.CHINA);
                        
                        return com.compare(p1.getTitle(), p2.getTitle());
                    }
            });
        } catch (Exception e) {}
    }


    private String decimal2(long size) {
        String a = null;
        double b = size;
        double kb = size / 1024d;
        double mb = size / 1024 / 1024d;
        double gb = size / 1024 / 1024 / 1024d;
        if (b < 1024) {
            a = String.format("%.2f", b) + " B";
        } else if (kb < 1024) {
            a = String.format("%.2f", kb) + " K";
        } else if (mb < 1024) {
            a = String.format("%.2f", mb) + " M";
        } else if (gb < 1024) {
            a = String.format("%.2f", gb) + " G";
        }
        return a;
    }
}
