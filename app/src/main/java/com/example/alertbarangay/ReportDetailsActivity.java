package com.example.alertbarangay;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class ReportDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_REPORT_ID = "report_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_details);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.report_details_main),
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
                findViewById(R.id.report_details_main)
        );

        String reportId = getIntent().getStringExtra(
                EXTRA_REPORT_ID
        );

        Report selectedReport = findReport(reportId);

        if (selectedReport == null) {
            finish();
            return;
        }

        TextView idView = findViewById(R.id.details_report_id);
        TextView typeView = findViewById(
                R.id.details_emergency_type
        );
        TextView locationView = findViewById(
                R.id.details_location
        );
        TextView statusView = findViewById(R.id.details_status);
        TextView descriptionView = findViewById(
                R.id.details_description
        );
        TextView photoView = findViewById(
                R.id.details_photo_status
        );

        idView.setText(selectedReport.getId());
        typeView.setText(selectedReport.getEmergencyType());
        locationView.setText(selectedReport.getLocation());
        statusView.setText("In progress");

        String details = selectedReport.getDetails();

        if (details == null || details.trim().isEmpty()) {
            details = "No description provided.";
        }

        descriptionView.setText(details);

        photoView.setText(
                selectedReport.hasPhoto()
                        ? "Selected locally · Not uploaded"
                        : "No photo selected"
        );

        findViewById(R.id.button_back_to_track)
                .setOnClickListener(view -> finish());
    }

    private Report findReport(String reportId) {
        if (reportId == null) {
            return null;
        }

        List<Report> reports = ReportStore.getReports(this);

        for (Report report : reports) {
            if (reportId.equals(report.getId())) {
                return report;
            }
        }

        return null;
    }
}