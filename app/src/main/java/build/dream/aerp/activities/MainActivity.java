package build.dream.aerp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import build.dream.aerp.EventBusEvent;
import build.dream.aerp.R;
import build.dream.aerp.utils.ApplicationHandler;
import build.dream.aerp.utils.EventBusUtils;
import build.dream.aerp.utils.JacksonUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText loginNameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBusUtils.register(this);

        loginNameEditText = findViewById(R.id.activity_main_edit_text_login_name);
        passwordEditText = findViewById(R.id.activity_main_edit_text_password);
        loginButton = findViewById(R.id.activity_main_button_login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginName = loginNameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                ApplicationHandler.authorize(loginName, password);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEvent eventBusEvent) {
        Toast.makeText(this, JacksonUtils.writeValueAsString(eventBusEvent.getSource()), Toast.LENGTH_LONG).show();
    }
}
