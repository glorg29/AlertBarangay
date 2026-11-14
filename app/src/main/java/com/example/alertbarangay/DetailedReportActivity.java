package com.example.alertbarangay;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class DetailedReportActivity extends AppCompatActivity {

    private static final String STATE_PHOTO_URI = "photo_uri";

    private ChipGroup emergencyTypeGroup;
    private TextView emergencyTypeError;
    private TextView photoStatus;
    private ImageView photoPreview;
    private View removePhotoButton;
    private TextInputEditText detailsInput;
    private Uri selectedPhotoUri;

    private final ActivityResultLauncher<String[]> photoPicker =
            registerForActivityResult(
                    new ActivityResultContracts.OpenDocument(),
                    uri -> {
                        if (uri != null) {
                            displayPhoto(uri);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed_report);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.detailed_report_main),
                (view, insets) -> {
                    Insets safeInsets = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                                    | WindowInsetsCompat.Type.displayCutout()
                                    | WindowInsetsCompat.Type.ime()
                    );

                    view.setPadding(
                            safeInsets.left,
                            safeInsets.top,
                            safeInsets.right,
                            safeInsets.bottom
                    );

                    return WindowInsetsCompat.CONSUMED;
                }
        );

        ViewCompat.requestApplyInsets(
                findViewById(R.id.detailed_report_main)
        );

        emergencyTypeGroup = findViewById(R.id.emergency_type_group);
        emergencyTypeError = findViewById(R.id.emergency_type_error);
        photoStatus = findViewById(R.id.photo_status);
        photoPreview = findViewById(R.id.photo_preview);
        removePhotoButton = findViewById(R.id.button_remove_photo);
        detailsInput = findViewById(R.id.edit_report_details);

        findViewById(R.id.button_back).setOnClickListener(
                view -> finish()
        );

        findViewById(R.id.button_add_photo).setOnClickListener(
                view -> photoPicker.launch(new String[]{"image/*"})
        );

        removePhotoButton.setOnClickListener(
                view -> clearPhoto()
        );

        emergencyTypeGroup.setOnCheckedStateChangeListener(
                (group, checkedIds) -> {
                    if (!checkedIds.isEmpty()) {
                        emergencyTypeError.setVisibility(View.GONE);
                    }
                }
        );

        findViewById(R.id.button_submit_report).setOnClickListener(
                view -> submitDemoReport()
        );

        if (savedInstanceState != null) {
            String savedPhoto = savedInstanceState.getString(
                    STATE_PHOTO_URI
            );

            if (savedPhoto != null) {
                displayPhoto(Uri.parse(savedPhoto));
            }
        }
    }

    private void displayPhoto(Uri uri) {
        try {
            photoPreview.setImageURI(null);
            photoPreview.setImageURI(uri);

            if (photoPreview.getDrawable() == null) {
                clearPhoto();

                Toast.makeText(
                        this,
                        "Unable to preview this image. Choose another photo.",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            selectedPhotoUri = uri;
            photoPreview.setVisibility(View.VISIBLE);
            removePhotoButton.setVisibility(View.VISIBLE);
            photoStatus.setText("Photo selected · Not uploaded");

        } catch (SecurityException | IllegalArgumentException exception) {
            clearPhoto();

            Toast.makeText(
                    this,
                    "Unable to open this photo. Please select it again.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void clearPhoto() {
        selectedPhotoUri = null;
        photoPreview.setImageDrawable(null);
        photoPreview.setVisibility(View.GONE);
        removePhotoButton.setVisibility(View.GONE);
        photoStatus.setText("No photo selected");
    }

    private void submitDemoReport() {
        int selectedId = emergencyTypeGroup.getCheckedChipId();

        if (selectedId == View.NO_ID) {
            emergencyTypeError.setVisibility(View.VISIBLE);

            NestedScrollView scrollView =
                    findViewById(R.id.detailed_report_main);

            scrollView.smoothScrollTo(0, 0);

            return;
        }

        Chip selectedChip = findViewById(selectedId);
        String emergencyType = selectedChip.getText().toString();

        String details = detailsInput.getText() == null
                ? ""
                : detailsInput.getText().toString().trim();

        String reportId = "DEMO-" + System.currentTimeMillis();

        String description = details.isEmpty()
                ? "No description provided."
                : details;

        String photo = selectedPhotoUri == null
                ? "No photo selected"
                : "Selected locally · Not uploaded";

        String message =
                "Report ID: " + reportId
                        + "\n\nEmergency type: " + emergencyType
                        + "\nLocation: La Salle, Bacolod City (sample)"
                        + "\nPhoto: " + photo
                        + "\n\nDetails:\n" + description
                        + "\n\nDemo only. No report has been sent "
                        + "to barangay staff.";

        new MaterialAlertDialogBuilder(this)
                .setTitle("Demo report created")
                .setMessage(message)
                .setPositiveButton("Close", null)
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (selectedPhotoUri != null) {
            outState.putString(
                    STATE_PHOTO_URI,
                    selectedPhotoUri.toString()
            );
        }

        super.onSaveInstanceState(outState);
    }
}