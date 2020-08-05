package com.hades.example.android.webview;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.hades.example.android.lib.base.PermissionActivity;

public class MainActivity extends PermissionActivity {
    View scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = findViewById(R.id.scrollView);
        findViewById(R.id.openInExtraBrowser).setOnClickListener(v -> openInExtraBrowser());
        findViewById(R.id.pageWebView).setOnClickListener(v -> pageWebView());
        findViewById(R.id.pageWebView_JavaInvokeJS).setOnClickListener(v -> pageWebView_JavaInvokeJS());
        findViewById(R.id.pageWebView_JSInvokeJava).setOnClickListener(v -> pageWebView_JSInvokeJava());
        showCurrentTest();
    }

    @Override
    protected void requestPermission() {
        checkPermission("Request permission for operate storage", Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void showCurrentTest() {
        scrollView.setVisibility(View.GONE);
//        openInExtraBrowser();
//        pageWebView();
        pageWebView_JavaInvokeJS();
//        pageWebView_JSInvokeJava();
    }

    private void openInExtraBrowser() {
        Uri uri = Uri.parse("http://baidu.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void pageWebView() {
        showFragment(new TestWebViewFragment());
    }

    private void pageWebView_JavaInvokeJS() {
        showFragment(new TestWebView4JavaInvokeJsFragment());
    }

    private void pageWebView_JSInvokeJava() {
        showFragment(new TestWebView4JSInvokeJavaFragment());
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentRoot);
        if (fragment instanceof IBackPressed) {
            ((IBackPressed) fragment).onBackPressed();
            return;
        }
        super.onBackPressed();
    }
}