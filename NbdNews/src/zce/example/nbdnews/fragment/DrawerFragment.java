package zce.example.nbdnews.fragment;

import zce.example.nbdnews.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

public class DrawerFragment extends Fragment {

	private DrawerLayout mDrawerLayout;
	private View mFragmentContainerView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
	}

	public void open() {
		if (mDrawerLayout != null && mFragmentContainerView != null) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}
	}

	public void close() {
		if (mDrawerLayout != null && mFragmentContainerView != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
	}

}
