package com.hades.example.android.webview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.hades.example.android.lib.base.BaseFragment;

import java.util.Objects;

public class TestWebView4JSInvokeJavaFragment extends BaseFragment implements IBackPressed {
    private static final String TAG = TestWebView4JSInvokeJavaFragment.class.getSimpleName();

    private final String WEB_PAGE_1 = "file:///android_asset/web/web_js_invoke_java.html";

    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.widget_webview_js_invoke_java, container, false);
        mWebView = view.findViewById(R.id.webView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setWebView();
        loadAssertFolderHtml();
    }

    private void loadAssertFolderHtml() {
        mWebView.loadUrl(WEB_PAGE_1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onDestroy() {
        // 销毁 WebView
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    private void setWebView() {
        WebSettings webSettings = mWebView.getSettings();
        if (null != webSettings) {
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        }
        mWebView.addJavascriptInterface(new JavaObject(getActivity()), "android");
    }

    @Override
    public void onBackPressed() {
        // When user press Back, not close Fragment, and go to previous page,
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        Objects.requireNonNull(getActivity()).finish();
    }
}