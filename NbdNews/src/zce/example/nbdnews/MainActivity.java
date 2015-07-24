package zce.example.nbdnews;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zce.app.util.Log;
import zce.example.nbdnews.entity.HeadersEntity;
import zce.example.nbdnews.fragment.LeftDrawerFragment;
import zce.example.nbdnews.fragment.MainFragment;
import zce.example.nbdnews.fragment.RightDrawerFragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {

	private ViewGroup tab;
	private ViewPager pager;
	private SmartTabLayout viewPagerTab;

	// 左边Fragment
	private LeftDrawerFragment leftDrawerFragment;
	// 右边Fragment
	private RightDrawerFragment rightDrawerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 初始化tab
		tab = (ViewGroup) findViewById(R.id.tab);
		tab.addView(LayoutInflater.from(this).inflate(R.layout.custom_tab_text,
				tab, false));
		pager = (ViewPager) findViewById(R.id.pager);
		viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

		FragmentPagerItems pages = new FragmentPagerItems(this);
		try {
			InputStream is = getAssets().open("headers.json");
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String json = new String(buffer, "utf-8");
			JSONArray hlist = new JSONArray(json);
			// 首页
			HeadersEntity entity;
			entity = new Gson().fromJson(hlist.getJSONObject(0).toString(),
					HeadersEntity.class);
			Bundle bundle;
			bundle = new Bundle();
			bundle.putSerializable("index", entity);
			bundle.putSerializable("list", entity.getList());
			pages.add(FragmentPagerItem.of(entity.getName(),
					MainFragment.class, bundle));
			for (int i = 1; i < hlist.length(); i++) {
				JSONObject item = hlist.getJSONObject(i);
				entity = new Gson().fromJson(item.toString(),
						HeadersEntity.class);
				Log.d(entity.getId() + " " + entity.getName() + " "
						+ entity.getList());
				bundle = new Bundle();
				bundle.putSerializable("list", entity.getList());
				pages.add(FragmentPagerItem.of(entity.getName(),
						MainFragment.class, bundle));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
				getSupportFragmentManager(), pages);
		pager.setAdapter(adapter);
		viewPagerTab.setViewPager(pager);

		// 初始化左边和右边fragment
		leftDrawerFragment = (LeftDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.left_drawer);
		leftDrawerFragment.setUp(R.id.left_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		rightDrawerFragment = (RightDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.right_drawer);
		rightDrawerFragment.setUp(R.id.right_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			if (rightDrawerFragment.isDrawerOpen()) {
				rightDrawerFragment.close();
			}
			if (leftDrawerFragment.isDrawerOpen()) {
				leftDrawerFragment.close();
			} else {
				leftDrawerFragment.open();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 返回键事件 onKeyDown
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (leftDrawerFragment.isDrawerOpen()) {
				leftDrawerFragment.close();
				return true;
			}
			if (rightDrawerFragment.isDrawerOpen()) {
				rightDrawerFragment.close();
				return true;
			}
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出按钮
	 * 
	 * @param v
	 */
	public void exit_btn(View v) {
		// TODO Auto-generated method stub
		finish();
	}
}
