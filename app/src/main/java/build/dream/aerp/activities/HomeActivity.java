package build.dream.aerp.activities;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import build.dream.aerp.R;
import build.dream.aerp.domains.Branch;
import build.dream.aerp.domains.OAuthToken;
import build.dream.aerp.domains.Pos;
import build.dream.aerp.domains.SystemUser;
import build.dream.aerp.domains.Tenant;
import build.dream.aerp.utils.ApplicationHandler;
import build.dream.aerp.utils.DatabaseUtils;
import build.dream.aerp.utils.StatusBarUtils;

public class HomeActivity extends AppCompatActivity {
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(this, R.color.color41d09b);
        setContentView(R.layout.activity_home);

        logoutButton = findViewById(R.id.activity_home_button_logout_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseUtils.delete(OAuthToken.TABLE_NAME);
                DatabaseUtils.delete(Tenant.TABLE_NAME);
                DatabaseUtils.delete(SystemUser.TABLE_NAME);
                DatabaseUtils.delete(Branch.TABLE_NAME);
                DatabaseUtils.delete(Pos.TABLE_NAME);
                HomeActivity.this.finish();
            }
        });

        Branch branch = DatabaseUtils.find(Branch.class);
        TextView branchNameTextView = findViewById(R.id.activity_home_text_view_branch_name);
        branchNameTextView.setText(branch.getName());

        LocationManager locationManager = ApplicationHandler.obtainLocationManager(this);
        String locationProvider = ApplicationHandler.obtainLocationProvider(locationManager);

        if (StringUtils.isNoneBlank(locationProvider)) {
            requestLocationUpdates(locationManager, locationProvider);
        }
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates(LocationManager locationManager, String locationProvider) {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(HomeActivity.this, "精度：" + location.getLongitude() + ",纬度：" + location.getLatitude(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }
}
