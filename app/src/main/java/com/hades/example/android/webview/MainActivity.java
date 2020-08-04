package com.hades.example.android.webview;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.hades.example.android.lib.base.PermissionActivity;

public class MainActivity extends PermissionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.openInExtraBrowser).setOnClickListener(v -> openInExtraBrowser());
        findViewById(R.id.pageWebView).setOnClickListener(v -> pageWebView());
        showCurrentTest();
    }

    @Override
    protected void requestPermission() {
        checkPermission("Request permission for operate storage", Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void showCurrentTest() {
//        openInExtraBrowser();
        pageWebView();
    }

    private void openInExtraBrowser() {
        Uri uri = Uri.parse("http://baidu.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void pageWebView() {
        showFragment(new TestWebViewFragment());
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentRoot);
        if (null != fragment && fragment instanceof IBackPressed) {
            ((IBackPressed) fragment).onBackPressed();
            return;
        }
        super.onBackPressed();
    }
}