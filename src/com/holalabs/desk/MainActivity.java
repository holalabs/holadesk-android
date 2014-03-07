package com.holalabs.desk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class MainActivity extends FragmentActivity {

	WebViewFragment webviewFragment = new WebViewFragment();
    @SuppressLint("SetJavaScriptEnabled")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,  Window.PROGRESS_VISIBILITY_ON);
        
        // First time init, create the UI.
        if (savedInstanceState == null) {
        	android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        	android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        	fragmentTransaction.add(android.R.id.content,
                    webviewFragment).commit();
        }
                
    }
    
    @Override
    public void onBackPressed() {
    	if (webviewFragment.onBackPressed()) {
    		super.onBackPressed();
    	}
    }

}
