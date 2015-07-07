package com.app.guide.adapter;

import java.util.List;

import android.content.Context;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.guide.R;
import com.app.guide.bean.ImageOption;
import com.app.guide.utils.BitmapUtils;

/**
 * 图展adapter
 * 
 * @author yetwish
 */
public class HorizontalScrollViewAdapter extends CommonAdapter<ImageOption> {

	private ImageLoader mImageLoader;

	public HorizontalScrollViewAdapter(Context context, List<ImageOption> data,
			int layoutId) {
		super(context, data, layoutId);
		mImageLoader = BitmapUtils.getImageLoader(context);
	}

	// 如何回收bitmap
	@Override
	public void convert(ViewHolder holder, int position) {
		NetworkImageView imageView = (NetworkImageView) holder
				.getView(R.id.item_gallery_iv);
		imageView.setDefaultImageResId(R.drawable.picture_no);
		imageView.setErrorImageResId(R.drawable.picture_error);
		imageView.setImageUrl(mData.get(position).getImgUrl(), mImageLoader);

	}

}
