package com.hades.example.android.webview;

import android.Manifest;
import android.os.Bundle;
import com.hades.example.android.lib.base.PermissionActivity;

public class MainActivity extends PermissionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.pageWebView).setOnClickListener(v -> pageWebView());
        showCurrentTest();
    }

    @Override
    protected void requestPermission() {
        checkPermission("Request permission for operate storage", Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void showCurrentTest() {
        pageWebView();
    }

    private void pageWebView() {
        showFragment(new TestWebViewFragment());
    }
}