package zce.example.nbdnews.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zce.app.util.Log;
import zce.app.util.Tools;
import zce.example.nbdnews.R;
import zce.example.nbdnews.entity.MsgEntity;
import zce.example.nbdnews.util.HttpUtil;
import zce.example.nbdnews.widget.MsgAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@SuppressWarnings("deprecation")
public class RightDrawerFragment extends DrawerFragment implements
		OnClickListener, OnScrollListener, OnRefreshListener {

	private ListView listView;
	private List<MsgEntity> mList;
	private MsgAdapter mAdapter;
	private View rootView;
	private Button backBtn, sendBtn;
	private EditText msg_edit;
	private JSONObject jObject;
	private JSONObject bObject;
	private UpdateHandler handler;
	private SwipeRefreshLayout refreshLayout;
	private SimpleDateFormat sdf;
	private int page = 1;
	private String Msg;
	private String MyName;

	private static final String HOST = "http://ztools.coding.io/";
	private static final String URL = "http://ztools.coding.io/chat";

	private static final int WHAT_FAILURE = 0;
	private static final int WHAT_GET = 1;
	private static final int WHAT_POST = 2;

	@SuppressLint("HandlerLeak")
	class UpdateHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case WHAT_FAILURE:// 失败
				setRefrshing(false);
				if (msg.obj == null) {
					return;
				}
				Crouton.cancelAllCroutons();
				Crouton.makeText(getActivity(), (String) msg.obj, Style.ALERT)
						.show();
				break;
			case WHAT_GET:
				setRefrshing(false);
				if (msg.obj == null) {
					return;
				}
				Log.d((String) msg.obj);
				try {
					JSONObject jsonObject = new JSONObject((String) msg.obj);
					JSONObject object = jsonObject.getJSONObject("body");
					if (object.getInt("code") == 1) {
						JSONArray jsonArray = object.getJSONArray("list");
						if (jsonArray.length() > 0) {
							page++;
						}
						for (int i = 0; i < jsonArray.length(); i++) {
							MsgEntity entity = new Gson().fromJson(jsonArray
									.getJSONObject(i).toString(),
									MsgEntity.class);
							if (!entity.getUser().equals(MyName)) {
								mAdapter.add(0, entity);// 非自己发送的消息则添加
							}
						}
						mAdapter.notifyDataSetChanged();
						listView.setSelection(0);
						return;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Crouton.cancelAllCroutons();
				Crouton.makeText(getActivity(), "刷新失败", Style.ALERT).show();
				break;
			case WHAT_POST:
				MsgEntity entity = new MsgEntity(1, sdf.format(new Date(System
						.currentTimeMillis())), MyName, Msg, null);
				entity.setWho(1);
				mAdapter.add(entity);
				mAdapter.notifyDataSetChanged();
				listView.setSelection(mAdapter.getCount() - 1);
				break;
			}
		}

	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// return super.onCreateView(inflater, container, savedInstanceState);
		if (rootView == null) {
			Log.d("view is null");
			rootView = inflater.inflate(R.layout.fragment_drawer_right,
					container, false);
		}
		if (sdf == null) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.getDefault());
		}
		if (MyName == null) {
			MyName = getRandomString(4);
		}
		if (mList == null) {
			mList = new ArrayList<MsgEntity>();
		}
		if (mAdapter == null) {
			mAdapter = new MsgAdapter(getActivity(), mList);
		}
		if (refreshLayout == null) {
			refreshLayout = (SwipeRefreshLayout) rootView
					.findViewById(R.id.refresh);
			refreshLayout.setColorSchemeResources(R.color.holo_blue_light,
					R.color.holo_red_light, R.color.holo_orange_light,
					R.color.holo_green_light);
			refreshLayout.setOnRefreshListener(this);
		}
		if (backBtn == null) {
			backBtn = (Button) rootView.findViewById(R.id.back);
			backBtn.setOnClickListener(this);
		}
		if (sendBtn == null) {
			sendBtn = (Button) rootView.findViewById(R.id.send);
			sendBtn.setOnClickListener(this);
		}
		if (msg_edit == null) {
			msg_edit = (EditText) rootView.findViewById(R.id.msg_edit);
		}
		if (jObject == null) {
			jObject = new JSONObject();
			try {
				jObject.put("url", URL);
				jObject.put("method", "POST");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (bObject == null) {
			bObject = new JSONObject();
		}
		if (handler == null) {
			handler = new UpdateHandler();
		}
		if (listView == null) {
			listView = (ListView) rootView.findViewById(R.id.listview);
			listView.setOnScrollListener(this);
			listView.setAdapter(mAdapter);
			getData();
		}
		return rootView;
	}

	public void post(String enData, final int what) {
		HttpUtil.post(HOST, new RequestParams("data", enData),
				new AsyncHttpResponseHandler(true) {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// TODO Auto-generated method stub
						String html;
						html = new String(responseBody);// 服务器返回的加密数据
						html = Tools.getTools().deData(html);// 解密数据
						Message msg = Message.obtain();
						msg.what = what;
						msg.obj = html;
						handler.sendMessage(msg);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						if (what == WHAT_GET) {
							page--;
							if (page == 0) {
								page = 1;
							}
						}
						Message msg = Message.obtain();
						msg.what = WHAT_FAILURE;
						msg.obj = error != null ? error.getMessage() : "发送失败";
						handler.sendMessage(msg);
					}
				});
	}

	/**
	 * 获取消息
	 */
	public void getData() {
		Log.d("getData");
		try {
			setRefrshing(true);
			bObject.put("page", page);
			jObject.put("body", bObject);
			String enData = Tools.getTools().enData(jObject.toString());
			post(enData, WHAT_GET);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			setRefrshing(false);
			e.printStackTrace();
		}
	}

	/**
	 * 发送消息
	 */
	public void sendData(String msg) {
		Log.d("sendData");
		try {
			bObject.remove("page");
			bObject.put("user", getRandomString(4));
			bObject.put("msg", msg);
			jObject.put("body", bObject);
			String enData = Tools.getTools().enData(jObject.toString());
			post(enData, WHAT_POST);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			close();
			break;
		case R.id.send:
			Msg = msg_edit.getText().toString().trim();
			if (Msg != null && Msg.length() > 0) {
				sendData(Msg);
			}
			msg_edit.setText("");
			break;
		}
	}

	/**
	 * length表示生成字符串的长度
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	@Override
	public void onRefresh() {
		Log.d("onRefresh");
		// TODO Auto-generated method stub
		// 正在刷新
		setRefrshing(true);
		getData();
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
			Log.d("refresh enable");
			setEnabled(true);
		} else {
			Log.d("refresh unable");
			setEnabled(false);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (listView.getFirstVisiblePosition() == 0) {
			// 到顶部
			Log.d("refresh enable");
			setEnabled(true);
		} else {
			Log.d("refresh unable");
			setEnabled(false);
		}
	}

}
