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
                Intent intent = new Intent(
                        MainActivity.this,
                        TrackReportsActivity.class
                );

                startActivity(intent);
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
        if (demoReportId == null) {
            demoReportId = ReportIdGenerator.nextId(this);
        }

        Intent intent = new Intent(
                MainActivity.this,
                ReportConfirmationActivity.class
        );

        intent.putExtra(
                ReportConfirmationActivity.EXTRA_REPORT_ID,
                demoReportId
        );

        intent.putExtra(
                ReportConfirmationActivity.EXTRA_EMERGENCY_TYPE,
                "SOS emergency — not specified"
        );

        intent.putExtra(
                ReportConfirmationActivity.EXTRA_LOCATION,
                "La Salle, Bacolod City"
        );

        intent.putExtra(
                ReportConfirmationActivity.EXTRA_DETAILS,
                "No description provided."
        );

        intent.putExtra(
                ReportConfirmationActivity.EXTRA_HAS_PHOTO,
                false
        );

        ReportStore.saveReport(
                this,
                new Report(
                        demoReportId,
                        "SOS emergency — not specified",
                        "La Salle, Bacolod City",
                        "No description provided.",
                        false
                )
        );

        startActivity(intent);
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