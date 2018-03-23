package kr.owens.drivetube.wrapper;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewClientWrapper extends WebViewClient {

    public WebViewClientWrapper() {

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        view.loadUrl(url);
        Log.e("DEBUG", "URL : " + url);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//        view.loadUrl(request.getUrl().toString());
        Log.e("DEBUG", "URL : " + request.getUrl().toString());
        return true;
    }

}
