package com.buildingblocks.camerapoc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.text.SimpleDateFormat;


import com.facebook.android.AsyncFacebookRunner.*;
import com.facebook.android.Facebook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;

/*
 * This Activity shows the two buttons to choose where to get the image from
 */
public class CameraPOCActivity extends Activity {
	
	private Uri fileUri;
	private final int PHOTO_INTENT = 100;
	private final int GALLERY_INTENT = 200; 

	Facebook facebook = new Facebook("371825396199782");
	AsyncFacebookRunner mAsyncRunner;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void logout(View view) {
    	mAsyncRunner = new AsyncFacebookRunner(facebook);
    	mAsyncRunner.logout(getBaseContext(), new RequestListener() {
    		 @Override
    		 public void onComplete(String response, Object state) {
    			 Intent i = new Intent(getBaseContext(),LoginActivity.class);
    			 startActivity(i);
    		 }
    		  
    		  @Override
    		  public void onIOException(IOException e, Object state) {}
    		  
    		  @Override
    		  public void onFileNotFoundException(FileNotFoundException e,
    		        Object state) {}
    		  
    		  @Override
    		  public void onMalformedURLException(MalformedURLException e,
    		        Object state) {}
    		  
    		  @Override
    		  public void onFacebookError(FacebookError e, Object state) {}
    	});
    }
    
    
    public void captureFromCamera(View view) {
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	fileUri = getOutputMediaFileUri(1);
    	intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
    	startActivityForResult(intent,PHOTO_INTENT);
    }
    
    
    public void pickFromGallery(View view) {
    	Intent intent = new Intent(Intent.ACTION_PICK);
    	intent.setType("image/*");
    	startActivityForResult(intent,GALLERY_INTENT);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	//--if the intent was to choose from the gallery, get the result here
    	if(requestCode==GALLERY_INTENT) {
    		if(resultCode==RESULT_OK) {
    			Uri galleryUri = data.getData();
    			Log.d("CameraPOC","Got gallery Uri as " + galleryUri);
    			if(galleryUri !=null) {
    				String[] filePathColumn = {MediaStore.Images.Media.DATA};
    				Cursor cursor = getContentResolver().query(galleryUri,filePathColumn,null,null,null);
    				cursor.moveToFirst();
    				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
    				fileUri =  Uri.parse(cursor.getString(columnIndex));
    				cursor.close();
    			}
    		}
    	}
    	
    	//--start the submit to Nest intent, passing the image path
    	Intent intent = new Intent(getBaseContext(), SubmitImageActivity.class);
    	intent.putExtra("imagePath",fileUri.getPath());
    	startActivity(intent);
    }
    
    /*
     * Creates a new file placeholder and returns its Uri
     */
    public Uri getOutputMediaFileUri(int type) {
    	return Uri.fromFile(getOutputMediaFile(type));
    }
    
    /*
     * Creates a new file placeholder when capturing from a camera
     */
    public File getOutputMediaFile(int type) {
    	File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"CameraPOC");
    	
    	if(!mediaStorageDir.exists()) {
    		if(!mediaStorageDir.mkdirs()) {
    			Log.d("CameraPOC","Failed to create directory");
    			return null;
    		}
    	}
    	
    	
    	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    	File mediaFile;
    	
    	if(type == 1) {
    		mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"  + timeStamp + ".jpg");
    	} else if(type == 2) {
    		mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
    	} else {
    		return null;
    	}
    	return mediaFile;
    	
    	
    }
}