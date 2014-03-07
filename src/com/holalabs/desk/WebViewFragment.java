package com.holalabs.desk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewFragment extends Fragment {

	private WebView myWebView, childView;
	private ViewGroup mContent;
    final FragmentActivity activity = getActivity();

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_webview, container, false);
        
        setRetainInstance(true);

        mContent = container;
        myWebView = (WebView) v.findViewById(R.id.webView);
        
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAppCacheMaxSize(1024*1024*32);
        String appCachePath = getActivity().getApplicationContext().getCacheDir().getAbsolutePath();
        String appCacheManualPath = "/data/data/" + getActivity().getPackageName() + "/cache";
        webSettings.setAppCachePath(appCacheManualPath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDatabaseEnabled(true);
        String databasePath = "/data/data/" + getActivity().getPackageName() + "/databases/";
        webSettings.setDatabasePath(databasePath);
        webSettings.setGeolocationEnabled(true);
        webSettings.setSaveFormData(true);
        
        final FragmentActivity activity = getActivity();
        myWebView.addJavascriptInterface(new NativeInterface(getActivity()), "Native");
        myWebView.setHorizontalScrollBarEnabled(true);
        myWebView.setVerticalScrollBarEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient() {
        	// Add new webview in same window
            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
                childView = new WebView(getActivity());
                childView.getSettings().setJavaScriptEnabled(true);
                childView.getSettings().setBuiltInZoomControls(true);
                childView.getSettings().setSupportZoom(true);
                childView.setWebChromeClient(this);
                childView.setWebViewClient(new MyWebViewClient());
                childView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                mContent.addView(childView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(childView);
                resultMsg.sendToTarget();
                return true;
            }

            // remove new added webview whenever onCloseWindow gets called for new webview.
            @Override
            public void onCloseWindow(WebView window) {
                mContent.removeView(childView);
                childView = null;
            }
            
            @Override
            public void onProgressChanged(WebView view, int progress) {
            	if (childView != null && mContent.getChildCount()==2) {
            		activity.setProgress(progress*100);
            	}
            }
            
            @Override
            public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
                  quotaUpdater.updateQuota(spaceNeeded * 2);
            }
        });
        myWebView.loadUrl("https://desk.holalabs.com/dev/desk");

        return v;
    }
	
	public Boolean onBackPressed() {
		if (childView != null && mContent.getChildCount()==2) {
    		if (childView.canGoBack()) {
    			childView.goBack();
    		} else {
	    		/*mContent.removeView(childView);
	    		childView = null;*/
    			childView.loadUrl("javascript:window.close()");
    		}
    		return false;
    	} else {
    		return true;
    	}
	}
    
    final class MyWebViewClient extends WebViewClient {

    	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

    		String error;
    		if (failingUrl.equals("https://desk.holalabs.com/dev/desk")) {
    			myWebView.loadUrl("https://desk/holalabs.com/dev/desk/index.html");
    			return;
    		}else if (failingUrl.equals("https://desk.holalabs.com/dev/desk/index.html")){
    			error = getString(R.string.desknotloading);
    		} else {
    			error = getString(R.string.sitenotloading);
    		}
    		/*myWebView.setVisibility(View.GONE);
    		TextView errorTextView = (TextView)findViewById(R.id.error);
    		errorTextView.setText(error);
    		errorTextView.setVisibility(View.VISIBLE);*/
            System.out.println(" failingUrl " + failingUrl);
            System.out.println(" description " + description);
            System.out.println(" errorCode " + errorCode);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
