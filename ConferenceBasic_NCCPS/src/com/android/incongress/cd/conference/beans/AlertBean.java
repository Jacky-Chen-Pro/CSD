package com.android.incongress.cd.conference.beans;

import java.io.Serializable;

//大会提醒
public class AlertBean implements Serializable {
	private static final long serialVersionUID = -1572348995870574183L;
	public final static int TYPE_SESSTION = 0;
	public final static int TYPE_MEETING = 1;
	public final static int TYPE_DISABLE = 0;
	public final static int TYPE_ENABLE = 1;

    private String id;//提醒id
    private String date;//提醒时间
    private String repeatdistance;//提醒间隔
    private String repeattimes;//提醒次数
    private int enable;//提醒可用 0不可用 1为可用
    private String title;//提醒内容
    private int type;//提醒类型 0会议 1为发言
    private String relativeid;//提醒关联ID 关联sessionid或meetingid；
    private String start;
    private String end;
    private String room;
    private long time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRepeatdistance() {
		return repeatdistance;
	}
	public void setRepeatdistance(String repeatdistance) {
		this.repeatdistance = repeatdistance;
	}
	public String getRepeattimes() {
		return repeattimes;
	}
	public void setRepeattimes(String repeattimes) {
		this.repeattimes = repeattimes;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRelativeid() {
		return relativeid;
	}
	public void setRelativeid(String relativeid) {
		this.relativeid = relativeid;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "AlertBean [id=" + id + ", date=" + date + ", repeatdistance="
				+ repeatdistance + ", repeattimes=" + repeattimes + ", enable="
				+ enable + ", title=" + title + ", type=" + type
				+ ", relativeid=" + relativeid + ", start=" + start + ", end="
				+ end + ", room=" + room + ", time=" + time + "]";
	}
}
