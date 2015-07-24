package zce.example.nbdnews.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import zce.app.util.Log;
import zce.example.nbdnews.MainApplication;
import zce.example.nbdnews.R;
import zce.example.nbdnews.entity.ArticleEntity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ArticleAdapter extends ArrayAdapter<ArticleEntity> {

	private SimpleDateFormat sdf;
	private static final int[] marks = { R.drawable.mark_exclusive,
			R.drawable.mark_favor, R.drawable.mark_first, R.drawable.mark_hot,
			R.drawable.mark_recomm };

	public ArticleAdapter(Context context, int resource,
			List<ArticleEntity> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.article_list_item, parent, false);
			holder.mark = (ImageView) convertView.findViewById(R.id.alt_mark);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.description = (TextView) convertView
					.findViewById(R.id.description);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
			int index = (int) (Math.random() * marks.length);
			Log.d("mark index: " + index);
			holder.mark.setBackgroundResource(marks[index]);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ArticleEntity articleEntity = getItem(position);
		holder.title.setText(articleEntity.getTitle());
		holder.description.setText(articleEntity.getDigest());
		holder.time.setText(sdf.format(new Date(Long.parseLong(articleEntity
				.getCreated_at()))));
		ImageLoader.getInstance().displayImage(articleEntity.getImage(),
				holder.image, MainApplication.getOptions());
		return convertView;// super.getView(position, convertView, parent);
	}

	static class ViewHolder {
		public ImageView mark;
		public TextView title;
		public ImageView image;
		public TextView description;
		public TextView time;
	}

}
