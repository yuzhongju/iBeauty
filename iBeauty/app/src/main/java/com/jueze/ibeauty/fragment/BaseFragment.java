package com.jueze.ibeauty.fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;

public abstract class BaseFragment extends Fragment{
    public boolean isFirstLoad = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = initView(inflater, container);
		initEvent();
		isFirstLoad = true;
		if(getUserVisibleHint()){
			lazyLoad();
			isFirstLoad = false;
		}
		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isFirstLoad && isVisibleToUser){
			lazyLoad();
			isFirstLoad = false;
		}
	}

	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		isFirstLoad = false;
	}
	
	
	
	public abstract View initView(LayoutInflater inflater, ViewGroup container);
	public abstract void initEvent();
	public abstract void lazyLoad();
}
