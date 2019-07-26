package build.dream.aerp.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import build.dream.aerp.tuples.Tuple2;

/**
 * Created by liuyandong on 2018/11/23.
 */

public class WebViewUtils {
    public static void initWebView(WebView webView, String url) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.loadUrl(url);
    }

    @SuppressLint("JavascriptInterface")
    public static void addJavascriptInterfaces(WebView webView, Tuple2<Object, String>... tuple2s) {
        for (Tuple2<Object, String> tuple2 : tuple2s) {
            webView.addJavascriptInterface(tuple2._1(), tuple2._2());
        }
    }
}
