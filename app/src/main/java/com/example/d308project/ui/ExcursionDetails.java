package com.example.d308project.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308project.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    EditText excursionName;
    Button excursionDate;

    String name;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        excursionName = findViewById(R.id.editExcursionName);
        excursionDate = findViewById(R.id.buttonExcursionDate);
    }

    private void scheduleAlert(String date, String message) {

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date alertDate = sdf.parse(date);

            Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
            intent.putExtra("key", message);

            PendingIntent sender = PendingIntent.getBroadcast(
                    ExcursionDetails.this,
                    MainActivity.numAlert++,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null && alertDate != null) {
                alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        alertDate.getTime(),
                        sender
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setExcursionAlert() {

        name = excursionName.getText().toString();
        date = excursionDate.getText().toString();

        scheduleAlert(
                date,
                name + " excursion is today!!"
        );

        Toast.makeText(this,
                "Excursion alert set",
                Toast.LENGTH_LONG).show();
    }
}