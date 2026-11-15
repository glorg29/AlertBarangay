package com.example.alertbarangay;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class ReportStore {

    private static final String PREFERENCES_NAME = "report_storage";
    private static final String REPORTS_KEY = "reports";

    private ReportStore() {
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getApplicationContext().getSharedPreferences(
                PREFERENCES_NAME,
                Context.MODE_PRIVATE
        );
    }

    public static synchronized List<Report> getReports(Context context) {
        List<Report> reports = new ArrayList<>();

        String savedReports = getPreferences(context).getString(
                REPORTS_KEY,
                "[]"
        );

        try {
            JSONArray array = new JSONArray(savedReports);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);

                if (object == null) {
                    continue;
                }

                String id = object.optString("id", "");

                if (id.isEmpty()) {
                    continue;
                }

                Report report = new Report(
                        id,
                        object.optString(
                                "emergencyType",
                                "SOS emergency — not specified"
                        ),
                        object.optString(
                                "location",
                                "La Salle, Bacolod City"
                        ),
                        object.optString(
                                "details",
                                "No description provided."
                        ),
                        object.optBoolean("hasPhoto", false)
                );

                reports.add(report);
            }

        } catch (JSONException exception) {
            Log.e(
                    "ReportStore",
                    "Unable to read saved reports.",
                    exception
            );
        }

        return reports;
    }

    public static synchronized void saveReport(
            Context context,
            Report report
    ) {
        List<Report> reports = getReports(context);

        for (int i = reports.size() - 1; i >= 0; i--) {
            if (reports.get(i).getId().equals(report.getId())) {
                reports.remove(i);
            }
        }

        reports.add(0, report);

        JSONArray array = new JSONArray();

        try {
            for (Report savedReport : reports) {
                JSONObject object = new JSONObject();

                object.put("id", savedReport.getId());
                object.put(
                        "emergencyType",
                        savedReport.getEmergencyType()
                );
                object.put("location", savedReport.getLocation());
                object.put("details", savedReport.getDetails());
                object.put("hasPhoto", savedReport.hasPhoto());

                array.put(object);
            }

            getPreferences(context)
                    .edit()
                    .putString(REPORTS_KEY, array.toString())
                    .apply();

        } catch (JSONException exception) {
            Log.e(
                    "ReportStore",
                    "Unable to save report.",
                    exception
            );
        }
    }
}