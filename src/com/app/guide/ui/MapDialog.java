package com.app.guide.ui;

import com.app.guide.R;
import com.app.guide.bean.MapExhibitBean;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MapDialog extends PopupWindow {

	private TextView titleTextView;
	private ImageView mImageView;
	private MapExhibitBean mapExhibitBean;

	public MapDialog(Context context, MapExhibitBean exhibitBean) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mapExhibitBean = exhibitBean;
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_map_exhibit, null);
		titleTextView = (TextView) view.findViewById(R.id.dia_map_title);
		mImageView = (ImageView) view.findViewById(R.id.dia_map_img);
		titleTextView.setText(mapExhibitBean.getName());
		mImageView.setImageBitmap(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.e1));
		this.setWidth(200);
		this.setHeight(200);
		setContentView(view);
		this.setBackgroundDrawable(context.getResources().getDrawable(  
                R.drawable.bg_popupwindow));
		this.setOutsideTouchable(true);
	}

}
