package com.example.alertbarangay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ReportConfirmationActivity extends AppCompatActivity {

    public static final String EXTRA_REPORT_ID = "report_id";
    public static final String EXTRA_EMERGENCY_TYPE = "emergency_type";
    public static final String EXTRA_LOCATION = "location";
    public static final String EXTRA_DETAILS = "details";
    public static final String EXTRA_HAS_PHOTO = "has_photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_confirmation);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.report_confirmation_main),
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
                findViewById(R.id.report_confirmation_main)
        );

        Intent reportIntent = getIntent();

        String reportId = reportIntent.getStringExtra(EXTRA_REPORT_ID);

        if (reportId == null || reportId.trim().isEmpty()) {
            Toast.makeText(
                    this,
                    "No report was provided.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        String emergencyType = getReportText(
                reportIntent,
                EXTRA_EMERGENCY_TYPE,
                "SOS emergency — not specified"
        );

        String location = getReportText(
                reportIntent,
                EXTRA_LOCATION,
                "La Salle, Bacolod City"
        );

        String details = getReportText(
                reportIntent,
                EXTRA_DETAILS,
                "No description provided."
        );

        boolean hasPhoto = reportIntent.getBooleanExtra(
                EXTRA_HAS_PHOTO,
                false
        );

        TextView reportIdView = findViewById(
                R.id.confirmation_report_id
        );

        TextView emergencyTypeView = findViewById(
                R.id.confirmation_emergency_type
        );

        TextView locationView = findViewById(
                R.id.confirmation_location
        );

        TextView detailsView = findViewById(
                R.id.confirmation_details
        );

        TextView photoStatusView = findViewById(
                R.id.confirmation_photo_status
        );

        reportIdView.setText(reportId);
        emergencyTypeView.setText(emergencyType);
        locationView.setText(location);
        detailsView.setText(details);

        photoStatusView.setText(
                hasPhoto
                        ? "Selected locally · Not uploaded"
                        : "No photo selected"
        );

        findViewById(R.id.button_track_report).setOnClickListener(
                view -> {
                    Intent intent = new Intent(
                            ReportConfirmationActivity.this,
                            TrackReportsActivity.class
                    );

                    intent.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    );

                    startActivity(intent);
                    finish();
                }
        );

        findViewById(R.id.button_back_to_report).setOnClickListener(
                view -> {
                    Intent intent = new Intent(
                            this,
                            MainActivity.class
                    );

                    intent.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    );

                    startActivity(intent);
                    finish();
                }
        );
    }

    private String getReportText(
            Intent intent,
            String key,
            String fallback
    ) {
        String value = intent.getStringExtra(key);

        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }

        return value.trim();
    }
}