package com.jueze.ibeauty.fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.adapter.ManualAdapter;
import com.jueze.ibeauty.bean.ManualBean;
import com.jueze.ibeauty.util.FileUtil;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.StaggeredGridLayoutManager;

public class ManualFragment extends BaseFragment {

	private View rootView;
	private RecyclerView mRecyclerView;
	private ManualAdapter adapter;

	private int type;
    private List<ManualBean> mDocList = new ArrayList<>();
	private List<String> fileList;
	
	public ManualFragment(int type, List<String> fileList){
		this.type = type;
		this.fileList = fileList;
	}
	@Override
	public View initView(LayoutInflater inflater, ViewGroup container) {
		if(rootView==null){
			rootView = inflater.inflate(R.layout.fragment_manual, container, false);
		}
		mRecyclerView = rootView.findViewById(R.id.recycler_view);
		return rootView;
	}

	@Override
	public void initEvent() {
	}

	@Override
	public void lazyLoad() {
		handleDoc(type);
	}
	

    private void handleDoc(int z){
        if(adapter != null){
            adapter.removeAll();
        }
        mDocList = new ArrayList<>();
        String nr = FileUtil.readTxtFromSD(fileList.get(z));
		if(TextUtils.isEmpty(nr)) return;
        nr = nr.substring(nr.indexOf("【")+1);
        String[] docList = nr.split("【");
        for(String doc : docList){
            String title = doc.substring(0,doc.indexOf("】")).trim();
            String content = doc.substring(doc.indexOf("】")+1).trim();
            mDocList.add(new ManualBean(title, content));
        }
		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
		adapter = new ManualAdapter(mDocList);
		mRecyclerView.setAdapter(adapter);
    }

	public void filter(String key){
		searchDoc(type, key);
	}

    private void searchDoc(int z, String key){
        if(adapter != null){
            adapter.removeAll();
        }
		mDocList = new ArrayList<>();
        String nr = FileUtil.readTxtFromSD(fileList.get(z));
		if(TextUtils.isEmpty(nr)) return;
        nr = nr.substring(nr.indexOf("【")+1);
        String[] docList = nr.split("【");
        for(String doc : docList){
            String title = doc.substring(0,doc.indexOf("】")).trim();
            String content = doc.substring(doc.indexOf("】")+1).trim();
            if(title.toLowerCase().contains(key.toLowerCase()) || content.toLowerCase().contains(key.toLowerCase())){
                mDocList.add(new ManualBean(title, content));
            }
        }
		adapter.refresh(mDocList);

    }
	
    
}
