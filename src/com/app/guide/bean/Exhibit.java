package com.app.guide.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 展品信息类,用于随行导游界面数据显示
 */
public class Exhibit {

	private int id;
	private String name;// 展品名
	private String beaconUId;
	private String iconUrl;
	private List<ImageOption> imgList;
	private String audioUrl;
	private String textUrl;
	private HashMap<String, String> labels;
	private int lExhibitBeanId;
	private int rExhibitBeanId;

	public Exhibit() {
		super();
	}

	public Exhibit(int id, String name, String beaconUId, String iconUrl,
			ArrayList<ImageOption> imgList, String audioUrl, String textUrl,
			HashMap<String, String> labels, int lExhibitBeanId,
			int rExhibitBeanId) {
		this.id = id;
		this.name = name;
		this.beaconUId = beaconUId;
		this.iconUrl = iconUrl;
		this.imgList = imgList;
		this.audioUrl = audioUrl;
		this.textUrl = textUrl;
		this.labels = labels;
		this.lExhibitBeanId = lExhibitBeanId;
		this.rExhibitBeanId = rExhibitBeanId;
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

	public String getBeaconUId() {
		return beaconUId;
	}

	public void setBeaconUId(String beaconUId) {
		this.beaconUId = beaconUId;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public List<ImageOption> getImgList() {
		return imgList;
	}

	public void setImgList(List<ImageOption> imgList) {
		this.imgList = imgList;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	public String getTextUrl() {
		return textUrl;
	}

	public void setTextUrl(String textUrl) {
		this.textUrl = textUrl;
	}

	public HashMap<String, String> getLabels() {
		return labels;
	}

	public void setLabels(HashMap<String, String> labels) {
		this.labels = labels;
	}

	public int getlExhibitBeanId() {
		return lExhibitBeanId;
	}

	public void setlExhibitBeanId(int lExhibitBeanId) {
		this.lExhibitBeanId = lExhibitBeanId;
	}

	public int getrExhibitBeanId() {
		return rExhibitBeanId;
	}

	public void setrExhibitBeanId(int rExhibitBeanId) {
		this.rExhibitBeanId = rExhibitBeanId;
	}
}
