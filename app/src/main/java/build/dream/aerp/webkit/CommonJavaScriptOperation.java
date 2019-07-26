package build.dream.aerp.webkit;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by liuyandong on 2018/12/30.
 */

public class CommonJavaScriptOperation {
    private Activity activity;

    public CommonJavaScriptOperation(Activity activity) {
        this.activity = activity;
    }

    //    测试方法
    @JavascriptInterface
    public void makeText(String text, int duration) {
        Toast.makeText(activity, text, duration).show();
    }
}
