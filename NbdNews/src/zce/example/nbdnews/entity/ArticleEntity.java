package zce.example.nbdnews.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ArticleEntity implements Serializable {

	private int is_rolling_news;

	private int pos;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 简介
	 */
	private String digest;

	/**
	 * 网址
	 */
	private String url;

	/**
	 * 创建时间
	 */
	private String created_at;

	private int column_id;

	private int columnist_id;

	private int id;

	/**
	 * 网页内容
	 */
	private String content;

	/**
	 * 图片地址
	 */
	private String image;

	public int getIs_rolling_news() {
		return is_rolling_news;
	}

	public void setIs_rolling_news(int is_rolling_news) {
		this.is_rolling_news = is_rolling_news;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public int getColumn_id() {
		return column_id;
	}

	public void setColumn_id(int column_id) {
		this.column_id = column_id;
	}

	public int getColumnist_id() {
		return columnist_id;
	}

	public void setColumnist_id(int columnist_id) {
		this.columnist_id = columnist_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
