package com.app.guide.widget;


public class MarkObject {

	private float mapX;
	private float mapY;
	private MarkClickListener listener;

	public MarkObject() {

	}

	public MarkObject(float mapX, float mapY) {
		super();
		this.mapX = mapX;
		this.mapY = mapY;
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
	
	public MarkClickListener getMarkListener() {
		return listener;
	}

	public void setMarkListener(MarkClickListener listener) {
		this.listener = listener;
	}

	public interface MarkClickListener {
		public void onMarkClick(MarkObject object, int x, int y);
	}

}
