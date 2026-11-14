package com.example.alertbarangay;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_DEMO_REPORT_ID = "demo_report_id";

    private String demoReportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            demoReportId = savedInstanceState.getString(STATE_DEMO_REPORT_ID);
        }

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (view, insets) -> {
                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                                    | WindowInsetsCompat.Type.displayCutout()
                    );

                    view.setPadding(
                            bars.left,
                            bars.top,
                            bars.right,
                            bars.bottom
                    );

                    return WindowInsetsCompat.CONSUMED;
                }
        );

        ViewCompat.requestApplyInsets(findViewById(R.id.main));

        findViewById(R.id.button_sos).setOnClickListener(
                view -> createDemoReport()
        );

        findViewById(R.id.button_detailed_report).setOnClickListener(
                view -> startActivity(
                        new Intent(MainActivity.this, DetailedReportActivity.class)
                )
        );

        BottomNavigationView navigation =
                findViewById(R.id.resident_navigation);

        navigation.setItemActiveIndicatorEnabled(false);

        navigation.setSelectedItemId(R.id.nav_report);

        navigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_track) {
                String message;

                if (demoReportId == null) {
                    message = getString(R.string.demo_empty_track);
                } else {
                    message = getString(
                            R.string.demo_track_body,
                            demoReportId
                    );
                }

                showPreview(R.string.demo_track_title, message);

                return false;
            }

            findViewById(R.id.report_scroll).scrollTo(0, 0);

            return true;
        });

        navigation.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.nav_report) {
                findViewById(R.id.report_scroll).scrollTo(0, 0);
            }
        });
    }

    private void createDemoReport() {
        boolean alreadyCreated = demoReportId != null;

        if (!alreadyCreated) {
            demoReportId = "DEMO-" + System.currentTimeMillis();
        }

        int title = alreadyCreated
                ? R.string.demo_existing_title
                : R.string.demo_created_title;

        String message = getString(
                R.string.demo_report_body,
                demoReportId,
                getString(R.string.sample_location)
        );

        showPreview(title, message);
    }

    private void showPreview(int title, String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.close, null)
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_DEMO_REPORT_ID, demoReportId);
        super.onSaveInstanceState(outState);
    }
}