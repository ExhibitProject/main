package com.app.guide.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "exhibit")
public class OfflineExhibitBean {

	@DatabaseField(generatedId = true, columnName = "id")
	private int id;
	@DatabaseField(columnName = "name")
	private String name;
	@DatabaseField(columnName = "museumId")
	private String museumId;
	@DatabaseField(columnName = "beaconId")
	private String beaconId;
	@DatabaseField(columnName = "introduce")
	private String introduce;
	@DatabaseField(columnName = "address")
	private String address;
	@DatabaseField(columnName = "mapX")
	private float mapX;
	@DatabaseField(columnName = "mapY")
	private float mapY;
	@DatabaseField(columnName = "floor")
	private int floor;
	@DatabaseField(columnName = "iconUrl")
	private String iconUrl;
	@DatabaseField(columnName = "imgJson")
	private String imgJson;
	@DatabaseField(columnName = "audioUrl")
	private String audioUrl;
	@DatabaseField(columnName = "textUrl")
	private String textUrl;
	@DatabaseField(columnName = "labelJson")
	private String labelJson;
	@DatabaseField(columnName = "isBoutique")
	private boolean isBoutique;
	@DatabaseField(columnName = "version")
	private int version;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the museumId
	 */
	public String getMuseumId() {
		return museumId;
	}

	/**
	 * @param museumId
	 *            the museumId to set
	 */
	public void setMuseumId(String museumId) {
		this.museumId = museumId;
	}

	/**
	 * @return the beaconId
	 */
	public String getBeaconId() {
		return beaconId;
	}

	/**
	 * @param beaconId
	 *            the beaconId to set
	 */
	public void setBeaconId(String beaconId) {
		this.beaconId = beaconId;
	}

	/**
	 * @return the introduce
	 */
	public String getIntroduce() {
		return introduce;
	}

	/**
	 * @param introduce
	 *            the introduce to set
	 */
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the mapX
	 */
	public float getMapX() {
		return mapX;
	}

	/**
	 * @param mapX
	 *            the mapX to set
	 */
	public void setMapX(float mapX) {
		this.mapX = mapX;
	}

	/**
	 * @return the mapY
	 */
	public float getMapY() {
		return mapY;
	}

	/**
	 * @param mapY
	 *            the mapY to set
	 */
	public void setMapY(float mapY) {
		this.mapY = mapY;
	}

	/**
	 * @return the floor
	 */
	public int getFloor() {
		return floor;
	}

	/**
	 * @param floor
	 *            the floor to set
	 */
	public void setFloor(int floor) {
		this.floor = floor;
	}

	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * @param iconUrl
	 *            the iconUrl to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	/**
	 * @return the imgJson
	 */
	public String getImgJson() {
		return imgJson;
	}

	/**
	 * @param imgJson
	 *            the imgJson to set
	 */
	public void setImgJson(String imgJson) {
		this.imgJson = imgJson;
	}

	/**
	 * @return the audioUrl
	 */
	public String getAudioUrl() {
		return audioUrl;
	}

	/**
	 * @param audioUrl
	 *            the audioUrl to set
	 */
	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	/**
	 * @return the textUrl
	 */
	public String getTextUrl() {
		return textUrl;
	}

	/**
	 * @param textUrl
	 *            the textUrl to set
	 */
	public void setTextUrl(String textUrl) {
		this.textUrl = textUrl;
	}

	/**
	 * @return the labelJson
	 */
	public String getLabelJson() {
		return labelJson;
	}

	/**
	 * @param labelJson
	 *            the labelJson to set
	 */
	public void setLabelJson(String labelJson) {
		this.labelJson = labelJson;
	}

	/**
	 * @return the isBoutique
	 */
	public boolean isBoutique() {
		return isBoutique;
	}

	/**
	 * @param isBoutique
	 *            the isBoutique to set
	 */
	public void setBoutique(boolean isBoutique) {
		this.isBoutique = isBoutique;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

}
