package zce.example.nbdnews.widget;

import java.util.List;

import zce.app.util.Log;
import zce.example.nbdnews.R;
import zce.example.nbdnews.entity.MsgEntity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MsgAdapter extends BaseAdapter {

	private List<MsgEntity> mList;
	private Context mContext;
	private LayoutInflater mInflater;

	public MsgAdapter(Context context, List<MsgEntity> list) {
		setmContext(context);
		mList = list;
		mInflater = LayoutInflater.from(context);// (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void add(MsgEntity entity) {
		mList.add(entity);
	}

	public void add(int index, MsgEntity entity) {
		mList.add(index, entity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mList == null) {
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (mList == null) {
			return null;
		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		MsgEntity msgEntity = mList.get(position);
		Log.d(msgEntity.toString());
		Log.d("" + getWho(position));
		if (convertView == null) {
			if (msgEntity.getWho() == 1) {
				// 右(自己发送的消息)
				convertView = mInflater.inflate(R.layout.msg_right, null);
				Log.d("right convertView");
			} else {
				// 左(别人的消息)
				convertView = mInflater.inflate(R.layout.msg_left, null);
				Log.d("left convertView");
			}
			holder = new ViewHolder();
			holder.tv_sendtime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			holder.tv_username = (TextView) convertView
					.findViewById(R.id.tv_username);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.tv_chatimg = (CircleImageView) convertView
					.findViewById(R.id.tv_chatimg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_sendtime.setText(msgEntity.getTime());
		holder.tv_username.setText(msgEntity.getUser());
		holder.tv_content.setText(msgEntity.getMsg());
		holder.tv_chatimg.setVisibility(View.GONE);
		// holder.tv_chatimg.setText(msgEntity.getUsername());
		return convertView;
	}

	/**
	 * 消息类型，是别人发的消息，还是自己发送出去的
	 * 
	 * @param position
	 * @return
	 */
	public int getWho(int position) {
		return mList.get(position).getWho();
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public final class ViewHolder {
		public TextView tv_sendtime;
		public TextView tv_username;
		public TextView tv_content;
		public CircleImageView tv_chatimg;
	}

}
