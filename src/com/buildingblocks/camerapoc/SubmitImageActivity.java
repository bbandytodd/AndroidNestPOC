package com.buildingblocks.camerapoc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SubmitImageActivity extends Activity {

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.submitimage);
		
		String imagePath = getIntent().getStringExtra("imagePath");
		
		if(imagePath!=null) {
			ImageView i = (ImageView) findViewById(R.id.previewImage);
			if(i!=null) {
				Drawable image = android.graphics.drawable.Drawable.createFromPath(imagePath);
				i.setImageDrawable(image);
			}
		}
		
	}
	
}
