package com.hades.example.android.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hades.example.android.lib.base.BaseFragment;

import java.util.Objects;

public class TestWebView4JavaInvokeJsFragment extends BaseFragment implements IBackPressed {
    private static final String TAG = TestWebView4JavaInvokeJsFragment.class.getSimpleName();

    private final String WEB_PAGE_1 = "file:///android_asset/web/web_java_invoke_js.html";

    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.widget_webview_java_invoke_js, container, false);
        mWebView = view.findViewById(R.id.webView);
        view.findViewById(R.id.javaInvokeJSFunction_Alert).setOnClickListener(v -> javaInvokeJSFunction_Alert());
        view.findViewById(R.id.javaInvokeJSFunction_Confirm).setOnClickListener(v -> javaInvokeJSFunction_Confirm());
        view.findViewById(R.id.javaInvokeJSFunction_Prompt).setOnClickListener(v -> javaInvokeJSFunction_Prompt());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setWebView();
        loadAssertFolderHtml();
    }

    private void javaInvokeJSFunction_Alert() {
        mWebView.loadUrl("javascript:sum_alert(2,5)");
    }

    private void javaInvokeJSFunction_Confirm() {
        mWebView.loadUrl("javascript:sum_confirm(2,5)");
    }

    private void javaInvokeJSFunction_Prompt() {
        mWebView.loadUrl("javascript:sum_prompt(2,5)");
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

        mWebView.setWebChromeClient(new WebChromeClient() {

            // 实现 Native 与 JS 通信
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
//                return onJsConfirm_custom_by_android(view, url, message, result);
            }

            private boolean onJsConfirm_custom_by_android(WebView view, String url, String message, JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("Confirm");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                b.create().show();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
//                return onJsPrompt_custom_by_android(view, url, message, defaultValue, result);
            }

            private boolean onJsPrompt_custom_by_android(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                final View v = View.inflate(getActivity(), R.layout.prompt_dialog, null);
                ((TextView) v.findViewById(R.id.prompt_message_text)).setText(message);
                ((EditText) v.findViewById(R.id.prompt_input_field)).setText(defaultValue);

                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("Prompt");
                b.setView(v);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = ((EditText) v.findViewById(R.id.prompt_input_field)).getText().toString();
                        result.confirm(value);
                    }
                });
                b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                b.create().show();
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
//                return onJsAlert_custom_by_android(view, url, message, result);
            }

            private boolean onJsAlert_custom_by_android(WebView view, String url, String message, JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
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