package com.yetwish.tourismguide.view;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yetwish.tourismguide.R;
import com.yetwish.tourismguide.adapter.GridAdapter;
import com.yetwish.tourismguide.adapter.GridAdapter.GridItemClickListener;

/**
 * 选择器 view，包含一个textView(组标签) 和一个gridView(子标签集)
 * @author yetwish
 * @date 2015-4-26
 */
public class SelectorView extends LinearLayout{

	private TextView tvTitle;
	private GridView gvItems;
	private List<String> items;
	private GridAdapter adapter;
	private Context mContext;
	private GridItemClickListener itemListener;
	private DisplayMetrics dm;
	public SelectorView(Context context,List<String> items,GridItemClickListener listener) {
		super(context);
		this.mContext = context;
		this.items = items;
		this.itemListener = listener;
		LayoutInflater.from(context).inflate(R.layout.fragment_subject_selector_list_item, this);
		initViews();
	}

	private void initViews() {
		dm = new DisplayMetrics();
		WindowManager manager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);
		tvTitle = (TextView)findViewById(R.id.fragment_subject_list_item_tv);
		gvItems = (GridView)findViewById(R.id.fragment_subject_list_item_gridview);
		tvTitle.setText(items.get(0));
		setGridView();
	}

	/**
	 * 设置gridView 可水平滚动 
	 */
	private void setGridView(){
		int size =  items.size()-1;
		int length = 80;
		float density = dm.density;
		int gridviewWidth =(int) (size *(length+4)*density);
		int itemWidth = (int)(length*density);
		LayoutParams params = new LayoutParams(gridviewWidth,LayoutParams.MATCH_PARENT);
		gvItems.setLayoutParams(params);
		gvItems.setColumnWidth(itemWidth); // 根据itemWidth 设置gridView 的列宽
        gvItems.setHorizontalSpacing(10); // 设置水平间隔
		gvItems.setNumColumns(size);
		gvItems.setStretchMode(GridView.NO_STRETCH);
		adapter = new GridAdapter(mContext, getGridItems(),itemListener);
		gvItems.setAdapter(adapter);
	}
	
	//更新gridView items的数据
	public void updateView(List<String> items){
		this.items = items;
		setGridView();
		adapter.notifyDataSetChanged();
	}
	
	//获取gridView items的数据
	public List<String> getGridItems(){
		return Collections.unmodifiableList(items.subList(1, items.size()));
	}
	
	
	
}
