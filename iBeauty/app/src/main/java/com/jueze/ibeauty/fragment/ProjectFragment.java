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
import com.jueze.ibeauty.util.FileHelper;
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
        handleData();
        return mView;
    }
    
    private void handleData() {
        if (adapter != null) adapter.removeAll();
        String dir = "/storage/emulated/0/iApp/ProjectApp";
        File file = new File(dir);

        try {
            File[] fileList = file.listFiles();

            for (File f : fileList) {
                String manifestPath = f + "/AndroidManifest.xml";
                String manifestTxt = FileHelper.readTxtFromSD(manifestPath);

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
            LinearLayoutManager lm = new LinearLayoutManager(mContext);
            mRv.setLayoutManager(lm);

                adapter = new IappProjectAdapter(mDataList);
            
            mRv.setAdapter(adapter);

        } catch (Exception e) {}
    }
}
