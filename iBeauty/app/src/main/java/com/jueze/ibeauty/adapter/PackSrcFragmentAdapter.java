package com.jueze.ibeauty.adapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import java.util.List;
import android.os.Parcelable;

public class PackSrcFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList;
    public PackSrcFragmentAdapter(FragmentManager fm, List<Fragment> list){
        super(fm);
        this.mFragmentList = list;
    }
    
    
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }
    
    
    
}
