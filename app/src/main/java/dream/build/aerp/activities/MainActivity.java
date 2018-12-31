package dream.build.aerp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import dream.build.aerp.R;
import dream.build.aerp.tuples.Tuple2;
import dream.build.aerp.utils.WebViewUtils;
import dream.build.aerp.webkit.CommonJavaScriptOperation;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.main_activity_web_view);
        webView.addJavascriptInterface(new CommonJavaScriptOperation(this), "common");
        WebViewUtils.initWebView(webView, "http://192.168.31.200:3000/");
        WebViewUtils.addJavascriptInterfaces(webView, new Tuple2<Object, String>(new CommonJavaScriptOperation(this), "common"));
    }
}
