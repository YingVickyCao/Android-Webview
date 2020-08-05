package com.hades.example.android.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hades.example.android.lib.base.BaseFragment;

import java.util.Objects;

public class TestWebViewFragment extends BaseFragment implements IBackPressed {
    private static final String TAG = TestWebViewFragment.class.getSimpleName();

    private final String ONLINE_URL_1 = "https://developer.android.google.cn/guide/webapps";
    private final String ONLINE_URL_2 = "https://www.cnblogs.com/chhom/p/4758103.html";
    private final String UNENCODED_HTML = "<html><body>'%28' is the code for '('</body></html>";

    private final String APP_CACAHE_DIRNAME = "/sdcard/yc/app_cache";

    private WebView mWebView;
    private TextView mLoadingText;
    private View mLoadingView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.widget_webview, container, false);
        mWebView = view.findViewById(R.id.webView);
        mLoadingView = view.findViewById(R.id.progress);
        mLoadingText = view.findViewById(R.id.loadingText);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setWebView();

        loadOnlineUrl();
//        loadAssertFolderHtml();
//        loadSDCardHtml();
//        loadURLFromAnHTMLString();
    }

    private void loadOnlineUrl() {
//        mWebView.loadUrl(ONLINE_URL_1);
        mWebView.loadUrl(ONLINE_URL_2);
    }

    private void loadAssertFolderHtml() {
        mWebView.loadUrl("file:///android_asset/web/maven.html");
    }

    private void loadSDCardHtml() {
        // /sdcard/maven.html
//        mWebView.loadUrl("file:///" + Environment.getExternalStorageDirectory().getPath() + "/maven.html");// ok
//        mWebView.loadUrl("file:///sdcard/maven.html"); // ok
        mWebView.loadUrl("file:///sdcard/yc/full/index.html"); // not ok. Can Swipe pages up/down,but content is white
    }

    private void loadURLFromAnHTMLString() {
//        mWebView.loadData(UNENCODED_HTML, "text/html", "UFT-8");

        String encodedHtml = Base64.encodeToString(UNENCODED_HTML.getBytes(), Base64.NO_PADDING);
        mWebView.loadData(encodedHtml, "text/html", "base64");
//        TODO: loadData() and loadDataWithBaseURL
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
            //  Support JS
            webSettings.setJavaScriptEnabled(true);

            //支持通过JS打开新窗口
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

            // TODO:支持插件

            // 缩放
            webSettings.setBuiltInZoomControls(true);   // 设置内置的缩放控件。若为false，则该WebView不可缩放
            webSettings.setSupportZoom(true);           // 当setBuiltInZoomControls(true)前提下，支持缩放
            webSettings.setDisplayZoomControls(false);  // 隐藏原生的缩放控件. 默认：不隐藏
            webSettings.setTextZoom(100);               // 设置文本的缩放倍数，默认为 100

            // 字体
            webSettings.setStandardFontFamily("sans-serif");//设置 WebView 的字体，默认字体为 "sans-serif"
            webSettings.setDefaultFontSize(30);//设置 WebView 字体的大小，默认大小为 16
            webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8

            //文件权限
            webSettings.setAllowFileAccess(true);       // 是否允许访问WebView内部文件，默认true
            webSettings.setAllowFileAccessFromFileURLs(true);   // 是否允许获取WebView的内容URL ，可以让WebView访问ContentPrivider存储的内容。 默认true
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setAllowContentAccess(true);


            // 使用缓存
            webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
            webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能

            webSettings.setAppCacheEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);  //关闭webview中缓存
            String cacheDirPath = getActivity().getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
            webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录

            //设置用户代理
//            webSettings.setUserAgentString("User-Agent:MicroMessenger");

            // 自适应手机屏幕
            webSettings.setUseWideViewPort(true);           // 将图片调整到适合webView的大小
            webSettings.setLoadWithOverviewMode(true);          // 是否启动概述模式浏览界面，当页面宽度超过WebView显示宽度时，缩小页面适应WebView。默认false
            webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
            webSettings.setLoadsImagesAutomatically(true);      //支持自动加载图片

            // 多窗口
            webSettings.supportMultipleWindows();

            webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
            mWebView.requestFocusFromTouch();   // 获取焦点

            // Android5.0 WebView中Http和Https混合问题
            // 在Android 5.0上 Webview 默认不允许加载 Http 与 Https 混合内容
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            mWebView.setHorizontalScrollBarEnabled(true);
            mWebView.setHorizontalFadingEdgeEnabled(true);
        }

        // 比例缩放：设置了此项，当页面加载完成后，就按比例显示页面。如果不手动缩放，缩放比例不会变
//        mWebView.setInitialScale(100);  //

        mWebView.setWebChromeClient(new WebChromeClient() {

            // 获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d(TAG, "onReceivedTitle: title=" + title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            @Override
            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
                super.onReceivedTouchIconUrl(view, url, precomposed);
            }

            @Override
            public void onRequestFocus(WebView view) {
                super.onRequestFocus(view);
            }

            // 获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
                mLoadingText.setText(newProgress + "%");
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
            }

            // 实现 Native 与 JS 通信
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            // 开始加载 URL
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // view.getTitle() : Title in Tab
                Log.d(TAG, "onPageStarted: url=" + url + ",title=" + view.getTitle());
                mLoadingView.setVisibility(View.VISIBLE);
            }

            // 结束加载 URL
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "onPageFinished: ");
                mLoadingView.setVisibility(View.GONE);
            }

            // 对 UrlLoading 进行拦截
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // 打开link时，默认使用外部浏览器
                boolean result = super.shouldOverrideUrlLoading(view, request);
                Log.d(TAG, "shouldOverrideUrlLoading:url=" + request.getUrl() + ",result=" + result);
                return result;
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
//                handler.proceed();
            }

            @Override
            public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
                return super.onRenderProcessGone(view, detail);
            }

            @Override
            public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
                super.onReceivedLoginRequest(view, realm, account, args);
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
            }

            @Override
            public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
                super.onSafeBrowsingHit(view, request, threatType, callback);
            }

            @Override
            public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
                super.onUnhandledKeyEvent(view, event);
            }

        });

//        mWebView.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                Log.d(TAG, "onDownloadStart: url=" + url);
//            }
//        });
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