package com.example.alertbarangay;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public final class ReportIdGenerator {

    private ReportIdGenerator() {
    }

    public static synchronized String nextId(Context context) {
        SharedPreferences preferences =
                context.getApplicationContext().getSharedPreferences(
                        "report_id_counter",
                        Context.MODE_PRIVATE
                );

        long nextNumber =
                preferences.getLong("last_report_number", 0L) + 1L;

        preferences.edit()
                .putLong("last_report_number", nextNumber)
                .apply();

        return String.format(Locale.US, "R-%03d", nextNumber);
    }
}