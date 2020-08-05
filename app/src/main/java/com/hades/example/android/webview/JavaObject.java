package com.hades.example.android.webview;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JavaObject {
    private final Context mContext;

    public JavaObject(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
