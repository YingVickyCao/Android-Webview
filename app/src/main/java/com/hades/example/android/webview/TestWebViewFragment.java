package com.hades.example.android.webview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.hades.example.android.lib.base.BaseFragment;

public class TestWebViewFragment extends BaseFragment {
    private WebView mWebView;
    private final String ONLINE_URL_1 = "https://developer.android.google.cn/guide/webapps";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.widget_webview, container, false);
        mWebView = view.findViewById(R.id.webView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView.getSettings().setJavaScriptEnabled(true);

//        loadOnlineUrl();
//        loadAssertFolderHtml();
        loadSDCardHtml();
    }

    private void loadOnlineUrl() {
        mWebView.loadUrl(ONLINE_URL_1);
    }

    private void loadAssertFolderHtml() {
        //        mWebView.loadUrl("file:///android_asset/web/maven.html");

        // asserts folder
//        mWebView.loadUrl("file:///android_asset/web/full/index.html"); // not ok. Can Swipe pages up/down,but content is white
//        mWebView.loadUrl("file:///android_asset/web/full/index.html?page=1");// not ok. Can Swipe pages up/down,but content is white
//        mWebView.loadUrl("file:///android_asset/web/full/1.html");// not ok, page 1 is showed,but sth is lost, e.g.,bg or grid
    }

    private void loadSDCardHtml() {
        // /sdcard/maven.html
//        mWebView.loadUrl("file:///" + Environment.getExternalStorageDirectory().getPath() + "/maven.html");// ok
//        mWebView.loadUrl("file:///sdcard/maven.html"); // ok
        mWebView.loadUrl("file:///sdcard/full/index.html"); // not ok. Can Swipe pages up/down,but content is white
//        mWebView.loadUrl("file:///sdcard/full/index.html?page=1"); // not ok. Can Swipe pages up/down,but content is white
//        mWebView.loadUrl("file:///sdcard/full/1.html"); // not ok, page 1 is showed,but sth is lost, e.g.,bg or grid
    }
}
