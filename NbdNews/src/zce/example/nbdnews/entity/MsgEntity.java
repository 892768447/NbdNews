package zce.example.nbdnews.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MsgEntity implements Serializable {

	/**
	 * 0 别人的消息 1自己发送的消息
	 */
	private int who = 0;

	/**
	 * 时间
	 */
	private String time;

	/**
	 * 用户名
	 */
	private String user;

	/**
	 * 消息内容
	 */
	private String msg;

	/**
	 * 头像
	 */
	private String head;

	public MsgEntity(String time, String user, String msg, String head) {
		setTime(time);
		setUser(user);
		setMsg(msg);
		setHead(head);
	}

	public MsgEntity(int who, String time, String user, String msg, String head) {
		setWho(who);
		setTime(time);
		setUser(user);
		setMsg(msg);
		setHead(head);
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public int getWho() {
		return who;
	}

	public void setWho(int who) {
		this.who = who;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		// return super.toString();
		return String.format("who:%s user:%s msg:%s time:%s head:%s", getWho(),
				getUser(), getMsg(), getTime(), getHead());
	}

}
