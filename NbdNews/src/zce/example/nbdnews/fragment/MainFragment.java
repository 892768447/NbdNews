package zce.example.nbdnews.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zce.app.util.Log;
import zce.example.nbdnews.DetailActivity;
import zce.example.nbdnews.R;
import zce.example.nbdnews.entity.ArticleEntity;
import zce.example.nbdnews.entity.HeadersEntity;
import zce.example.nbdnews.util.HttpUtil;
import zce.example.nbdnews.widget.ArticleAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@SuppressWarnings("deprecation")
public class MainFragment extends Fragment implements OnItemClickListener,
		OnScrollListener, OnRefreshListener, OnClickListener {

	private ListView tabListView, pageListView;
	private SwipeRefreshLayout refreshLayout;
	private ArrayAdapter<HeadersEntity> adapter;
	private ArrayList<HeadersEntity> hList;
	private HeadersEntity cHeadersEntity;// 当前的
	private HeadersEntity iHeadersEntity;// 首页的
	private View rootView;
	private Button upBtn;
	private Map<HeadersEntity, ArticleAdapter> adapters;
	private UpdateHandler handler;
	private static final String URL = "http://api.nbd.com.cn/v1/columns/%d/articles.json?count=10&page=%d";

	/**
	 * 获取数据失败
	 */
	private static final int FAILURE = 0;
	/**
	 * 获取数据成功
	 */
	private static final int SUCCESS = 1;
	/**
	 * 停止顶部进度刷新
	 */
	private static final int STOPREFRESH = 2;

	@SuppressLint("HandlerLeak")
	class UpdateHandler extends Handler {

		public void error(String msg) {
			int page = cHeadersEntity.getCurrentPage();
			Log.d("page: " + page);
			if (page == 0) {
				cHeadersEntity.setPage(1);
			} else {
				cHeadersEntity.setPage(page - 1);
			}
			Crouton.cancelAllCroutons();
			Crouton.makeText(getActivity(), msg, Style.ALERT).show();
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case STOPREFRESH:
				setRefrshing(false);
				break;
			case SUCCESS:// 获取数据成功并且对json解析显示
				if (msg.obj == null) {
					return;
				}
				Log.d((String) msg.obj);
				try {
					// 把网页数据->json
					JSONObject object = new JSONObject((String) msg.obj);
					// 取出里面的articles
					JSONArray articles = object.getJSONArray("articles");
					if (articles == null) {
						return;
					}
					ArticleAdapter adapter = adapters.get(cHeadersEntity);
					for (int i = 0; i < articles.length(); i++) {
						ArticleEntity articleEntity = new Gson().fromJson(
								articles.getJSONObject(i).toString(),
								ArticleEntity.class);
						adapter.add(articleEntity);
					}
					adapter.notifyDataSetChanged();
					if (cHeadersEntity.getCurrentPage() == 1) {
						pageListView.setSelection(0);
					} else {
						pageListView.setSelection(cHeadersEntity
								.getCurrentItem());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					error("网络错误");
					e.printStackTrace();
				}
				break;
			case FAILURE:// 获取失败
				error((String) msg.obj);
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (hList == null) {
			Log.d("hList is null");
			// 获取传递过来的子header
			hList = (ArrayList<HeadersEntity>) getArguments().getSerializable(
					"list");
		}
		if (handler == null) {
			handler = new UpdateHandler();
		}
		if (adapters == null) {
			Log.d("adapters is null");
			adapters = new HashMap<HeadersEntity, ArticleAdapter>();
			if (hList.size() > 0) {
				// 默认创建第一个adapter
				cHeadersEntity = hList.get(0);
				adapters.put(cHeadersEntity, new ArticleAdapter(getActivity(),
						R.layout.article_list_item,
						new ArrayList<ArticleEntity>()));
			}
		}
		if (rootView == null) {
			Log.d("rootView is null");
			rootView = inflater.inflate(R.layout.fragment_page, container,
					false);
		}
		if (upBtn == null) {
			upBtn = (Button) rootView.findViewById(R.id.up);
			upBtn.setOnClickListener(this);
		}
		if (adapter == null) {
			Log.d("adapter is null");
			adapter = new ArrayAdapter<HeadersEntity>(getActivity(),
					R.layout.simple_list_item_1, hList);
		}
		if (refreshLayout == null) {
			refreshLayout = (SwipeRefreshLayout) rootView
					.findViewById(R.id.refresh);
			refreshLayout.setColorSchemeResources(R.color.holo_blue_light,
					R.color.holo_red_light, R.color.holo_orange_light,
					R.color.holo_green_light);
			refreshLayout.setOnRefreshListener(this);
			setEnabled(false);
		}
		if (tabListView == null) {
			Log.d("tabListView is null");
			tabListView = (ListView) rootView.findViewById(R.id.tab);
			tabListView.setAdapter(adapter);
			tabListView.setOnItemClickListener(this);
			if (hList.size() == 0) {
				tabListView.setVisibility(ListView.GONE);
			} else {
				getData(hList.get(0).getId(), 1);
			}
		}
		if (iHeadersEntity == null) {
			iHeadersEntity = (HeadersEntity) getArguments().getSerializable(
					"index");
			if (iHeadersEntity != null) {
				cHeadersEntity = iHeadersEntity;
				adapters.put(cHeadersEntity, new ArticleAdapter(getActivity(),
						R.layout.article_list_item,
						new ArrayList<ArticleEntity>()));
				getData(iHeadersEntity.getId(), 1);
			}
		}
		if (pageListView == null) {
			Log.d("pageListView is null");
			pageListView = (ListView) rootView.findViewById(R.id.page);
			pageListView.setAdapter(adapters.get(cHeadersEntity));
			pageListView.setOnItemClickListener(this);
			pageListView.setOnScrollListener(this);
		}
		return rootView;
	}

	@SuppressLint("DefaultLocale")
	public void getData(int id, final int page) {
		setRefrshing(true);// 刷新显示
		HttpUtil.get(String.format(URL, id, page),
				new AsyncHttpResponseHandler(true) {
					@Override
					public void onSuccess(int code, Header[] headers,
							byte[] data) {
						// TODO Auto-generated method stub
						finishGet(page, new String(data));
					}

					@Override
					public void onFailure(int code, Header[] headers,
							byte[] data, Throwable e) {
						// TODO Auto-generated method stub
						finishGet(page, e != null ? e.getMessage().toString()
								: "获取失败");
					}
				});
	}

	public void finishGet(int page, String data) {
		handler.sendEmptyMessage(STOPREFRESH);
		Message msg = Message.obtain();
		msg.what = SUCCESS;
		msg.obj = data;
		handler.sendMessage(msg);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.d(parent.toString());
		Log.d(view.toString());
		if (parent == tabListView) {
			// 左侧tab
			cHeadersEntity = adapter.getItem(position);
			Log.d(cHeadersEntity.getName());
			ArticleAdapter adapter = adapters.get(cHeadersEntity);
			if (adapter == null) {
				// 该adapter不存在
				adapter = new ArticleAdapter(getActivity(),
						R.layout.article_list_item,
						new ArrayList<ArticleEntity>());
				adapters.put(cHeadersEntity, adapter);
				getData(cHeadersEntity.getId(), 1);
			}
			pageListView.setAdapter(adapter);
		} else {
			// 右侧listview新闻列表
			// 点击查看新闻详情
			Intent intent = new Intent(getActivity(), DetailActivity.class);
			ArticleEntity entity = (ArticleEntity) pageListView.getAdapter()
					.getItem(position);
			intent.putExtra("title", entity.getTitle());
			intent.putExtra("content", entity.getContent());
			getActivity().startActivity(intent);
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		// 正在刷新
		setRefrshing(true);
		if (cHeadersEntity != null) {
			cHeadersEntity.setPage(1);
			adapters.get(cHeadersEntity).clear();
			Log.d("page: 1");
			cHeadersEntity.setPage(1);
			getData(cHeadersEntity.getId(), 1);
		}
	}

	/**
	 * 设置刷新状态
	 * 
	 * @param refreshing
	 */
	public void setRefrshing(boolean refreshing) {
		if (refreshLayout != null) {
			refreshLayout.setRefreshing(refreshing);
		}
	}

	/**
	 * 设置刷新控件是否可用
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		if (refreshLayout != null) {
			refreshLayout.setEnabled(enabled);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (firstVisibleItem == 0) {
			// 到顶部
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (pageListView.getFirstVisiblePosition() == 0) {
			// 到顶部
			setEnabled(true);
		} else {
			setEnabled(false);
		}
		// 当不滚动时
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			if (view.getLastVisiblePosition() > 8) {
				upBtn.setVisibility(Button.VISIBLE);
			} else {
				upBtn.setVisibility(Button.GONE);
			}
			// 判断是否滚动到底部
			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				// 加载更多功能的代码
				cHeadersEntity.setCurrentItem(view.getCount() - 1);
				getData(cHeadersEntity.getId(), cHeadersEntity.getPage());
				Log.d("page: " + cHeadersEntity.getCurrentPage());
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.up) {
			pageListView.setSelection(0);
			upBtn.setVisibility(Button.GONE);
			setEnabled(true);
		}
	}

}
