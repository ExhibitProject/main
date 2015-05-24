package com.app.guide.adapter;

import java.util.List;

import android.content.Context;

import com.android.volley.toolbox.ImageLoader;
import com.app.guide.R;
import com.app.guide.bean.Exhibit;
import com.app.guide.bean.ExhibitBean;
import com.app.guide.utils.BitmapUtils;

/**
 * 展品listView adapter
 * 
 * @author yetwish
 */
public class ExhibitAdapter extends CommonAdapter<ExhibitBean> {

	public ExhibitAdapter(Context context, List<ExhibitBean> data, int layoutId) {
		super(context, data, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, int position) {
		holder.setImageBitmap(R.id.item_exhibit_iv_icon, mContext,
				mData.get(position).getImgUrl())
				.setText(R.id.item_exhibit_tv_name,
						mData.get(position).getName())
				.setText(R.id.item_exhibit_tv_address,
						mData.get(position).getAddress())
				.setText(R.id.item_exhibit_tv_introduction,
						mData.get(position).getIntroduction());

	}

}
