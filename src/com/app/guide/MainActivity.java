package com.app.guide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.app.guide.sql.DatabaseContext;
import com.app.guide.sql.SdCardDBHelper;
import com.app.guide.ui.BaseActivity;
import com.app.guide.ui.CityActivity;

public class MainActivity extends BaseActivity {

	private static final String PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/sql";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent1 = new Intent(MainActivity.this, CityActivity.class);
		startActivity(intent1);

		DatabaseContext context = new DatabaseContext(this, PATH);
		SdCardDBHelper helper = new SdCardDBHelper(context, "my.db");
		helper.getWritableDatabase();
	}
}
