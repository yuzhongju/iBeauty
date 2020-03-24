package com.jueze.ibeauty.fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.adapter.IappProjectAdapter;
import com.jueze.ibeauty.bean.IappProjectBean;
import com.jueze.ibeauty.util.FileUtil;
import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.jueze.ibeauty.util.ToastUtil;

public class ProjectFragment extends Fragment {

    private Context mContext;
    private View mView;
    private RecyclerView mRv;
    private List<IappProjectBean> mDataList = new ArrayList<>();
    private IappProjectAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_project, container, false);
        }
        mContext = mView.getContext();
        mRv = mView.findViewById(R.id.recycler_view);
		loadRv();	//第一次加载
        return mView;
    }


	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			refresh();
		}
	}

	public void refresh(){
		if(adapter!=null){
			handleData();
			adapter.refresh(mDataList);
		}
	}
	
	private void loadRv(){
		if(adapter==null){
			handleData();
			adapter = new IappProjectAdapter(mDataList);
		}
		mRv.setLayoutManager(new LinearLayoutManager(mContext));
		mRv.setAdapter(adapter);
	}
	
	
    private void handleData() {
		if(adapter!=null) adapter.clear();
        String dir = "/storage/emulated/0/iApp/ProjectApp";
        File file = new File(dir);
        try {
            File[] fileList = file.listFiles();

            for (File f : fileList) {
                String manifestPath = f + "/AndroidManifest.xml";
                String manifestTxt = FileUtil.readTxtFromSD(manifestPath);

                Document doc = Jsoup.parse(manifestTxt);
                Elements etitle = doc.getElementsByTag("title");
                Elements eyuv = doc.getElementsByTag("yuv");
                Elements ebm = doc.getElementsByTag("packageName");
                Elements eremark = doc.getElementsByTag("remark");
                String title = etitle.get(0).text();
                String yuv = "v" + eyuv.get(0).text();
                String bm = ebm.get(0).text();
                String remark = eremark.get(0).text();
                String icon = f + "/icon.png";
                String path = f.getPath();
                mDataList.add(new IappProjectBean(title, yuv, bm, remark, icon, path));
            }

            Collections.sort(mDataList, new Comparator<IappProjectBean>(){
                    @Override
                    public int compare(IappProjectBean p1, IappProjectBean p2) {
                        Comparator<Object> com = Collator.getInstance(Locale.CHINA);
                        return com.compare(p1.getTitle(), p2.getTitle());
                    }
				});

            Collections.sort(mDataList, new Comparator<IappProjectBean>(){
                    @Override
                    public int compare(IappProjectBean p1, IappProjectBean p2) {
                        Comparator<Object> com = Collator.getInstance(Locale.CHINA);
                        return com.compare(p2.getYuv(), p1.getYuv());
                    }
				});
        } catch (Exception e) {}
    }
}
