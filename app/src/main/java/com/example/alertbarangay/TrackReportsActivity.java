package com.example.alertbarangay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class TrackReportsActivity extends AppCompatActivity {

    private LinearLayout reportsContainer;
    private View emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_track_reports);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.track_reports_main),
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

        ViewCompat.requestApplyInsets(
                findViewById(R.id.track_reports_main)
        );

        reportsContainer = findViewById(R.id.reports_container);
        emptyState = findViewById(R.id.track_empty_state);

        findViewById(R.id.button_go_to_report).setOnClickListener(
                view -> openReportScreen()
        );

        BottomNavigationView navigation =
                findViewById(R.id.resident_navigation);

        navigation.setItemActiveIndicatorEnabled(false);
        navigation.setSelectedItemId(R.id.nav_track);

        navigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_report) {
                openReportScreen();
                return false;
            }

            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayReports();
    }

    private void displayReports() {
        List<Report> reports = ReportStore.getReports(this);

        reportsContainer.removeAllViews();

        if (reports.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            reportsContainer.setVisibility(View.GONE);
            return;
        }

        emptyState.setVisibility(View.GONE);
        reportsContainer.setVisibility(View.VISIBLE);

        for (Report report : reports) {
            View card = getLayoutInflater().inflate(
                    R.layout.item_report,
                    reportsContainer,
                    false
            );

            TextView idView = card.findViewById(R.id.report_id);
            TextView typeView = card.findViewById(R.id.report_type);
            TextView locationView = card.findViewById(
                    R.id.report_location
            );
            TextView statusView = card.findViewById(
                    R.id.report_status
            );

            idView.setText(report.getId());
            typeView.setText(report.getEmergencyType());
            locationView.setText(report.getLocation());
            statusView.setText("In progress");

            card.findViewById(R.id.button_view_details)
                    .setOnClickListener(
                            view -> showReportDetails(report)
                    );

            reportsContainer.addView(card);
        }
    }

    private void showReportDetails(Report report) {
        Intent intent = new Intent(
                TrackReportsActivity.this,
                ReportDetailsActivity.class
        );

        intent.putExtra(
                ReportDetailsActivity.EXTRA_REPORT_ID,
                report.getId()
        );

        startActivity(intent);
    }

    private void openReportScreen() {
        Intent intent = new Intent(
                TrackReportsActivity.this,
                MainActivity.class
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);
        finish();
    }
}