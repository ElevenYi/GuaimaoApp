package com.guaimao.guaimaoapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import listener.ReturnListener;
import utils.Utils;
import views.GeneralDialogFragment;
import views.QuitConfirmDialog;

public class MainActivity extends Activity {

    WebView webView;
    String curUrl;
    String homeUrl = "http://www.gm88.com/new/";
    GeneralDialogFragment dialogFragment;
    QuitConfirmDialog quitConfirmDialog;
    ReturnListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        PushAgent.getInstance(this).onAppStart();
        webView = (WebView) findViewById(R.id.wv_guaimao_wb);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                System.out.println(newProgress + " ");
                if (newProgress != 100)
                    Utils.showLoadingDialog(MainActivity.this, "加载中...", false);
                else
                    Utils.closeLoadingDialog();
//                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setDownloadListener(new MyWebViewDownLoadListener());
        this.curUrl = "http://t.gm88.com/apk.php";
        webView.loadUrl(curUrl);
        quitConfirmDialog = new QuitConfirmDialog();
        dialogFragment = new GeneralDialogFragment();
        listener = new ReturnListener() {
            @Override
            public void clickRefresh() {
                webView.reload();
            }

            @Override
            public void clickGoHome() {
                webView.loadUrl(homeUrl);
            }

            @Override
            public void clickQuitGame() {
                webView.loadUrl(homeUrl);
            }

            @Override
            public void clickClearCache() {
                webView.clearCache(true);
            }

            @Override
            public void clickConfirm() {
                quitConfirmDialog.dismiss();
                finish();
            }

            @Override
            public void clickCancel() {
                if (quitConfirmDialog != null)
                    quitConfirmDialog.dismiss();
            }
        };
        quitConfirmDialog.setListener(listener);
        dialogFragment.setListener(listener);

        String deviceToken = UmengRegistrar.getRegistrationId(this);

        Log.d("MainActivity",deviceToken);

        //开启推送并设置注册的回调处理
        mPushAgent.enable(new IUmengRegisterCallback() {

            @Override
            public void onRegistered(final String registrationId) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //onRegistered方法的参数registrationId即是device_token
                        Log.d("device_token", registrationId);
                    }
                });
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack() && curUrl.contains("h5game")) {
//            webView.goBack();
            dialogFragment.show(getFragmentManager(), "tips");
            return true;
        }
        //如果是首页的话 就需要
        if ((keyCode == KeyEvent.KEYCODE_BACK) && !webView.canGoBack() || curUrl.equals(homeUrl)) {
            quitConfirmDialog.show(getFragmentManager(), "confirm");
            return true;
        }
        //如果是详情页或者详细介绍页就返回上一页
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack() && !curUrl.contains("h5game")) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            curUrl = url;
            view.loadUrl(url);
            Log.i("首页的url是：", homeUrl);
            Log.i("当前的url是：", curUrl);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Utils.showLoadingDialog(MainActivity.this, "加载中...", false);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Utils.closeLoadingDialog();
            curUrl = url;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    }

    class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
