package zce.example.nbdnews.entity;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class HeadersEntity implements Serializable {

	/**
	 * 分类id
	 */
	private int id;
	/**
	 * 分类名
	 */
	private String name;
	/**
	 * 子分类
	 */
	private ArrayList<HeadersEntity> list;

	public int page = 1;// 分页

	public int currentItem = 0;

	public int getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(int currentItem) {
		this.currentItem = currentItem;
	}

	public int getPage() {
		return page++;
	}

	public int getCurrentPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<HeadersEntity> getList() {
		return list;
	}

	public void setList(ArrayList<HeadersEntity> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		// return super.toString();
		return getName();
	}

}
