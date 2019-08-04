package build.dream.aerp.activities;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import build.dream.aerp.R;
import build.dream.aerp.utils.ApplicationHandler;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView branchNameTextView = findViewById(R.id.activity_home_text_view_branch_name);
        branchNameTextView.setText("总部");

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
                Toast.makeText(HomeActivity.this, "精度：" + location.getLongitude() + ",维度：" + location.getLatitude(), Toast.LENGTH_LONG).show();
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
