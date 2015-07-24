package zce.example.nbdnews.fragment;

import zce.app.util.Log;
import zce.example.nbdnews.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LeftDrawerFragment extends DrawerFragment {

	private View rootView;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			Log.d("view is null");
			rootView = inflater.inflate(R.layout.fragment_drawer_left,
					container, false);
		}
		// return super.onCreateView(inflater, container, savedInstanceState);
		return rootView;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		Log.d("onDestroyView");
		super.onDestroyView();
		if (rootView != null) {
			((ViewGroup) rootView.getParent()).removeView(rootView);
		}
	}

}
