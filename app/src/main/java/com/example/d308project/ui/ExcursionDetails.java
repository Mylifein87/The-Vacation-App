package com.example.d308project.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308project.R;
import com.example.d308project.database.Repository;
import com.example.d308project.entities.Excursion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    EditText excursionName;
    Button excursionDate;

    Repository repository;

    int excursionID;
    int vacationID;

    String vacationStartDate;
    String vacationEndDate;

    String name;
    String date;

    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        // Repository
        repository = new Repository(getApplication());

        // UI
        excursionName = findViewById(R.id.editExcursionName);
        excursionDate = findViewById(R.id.buttonExcursionDate);

        Button saveButton = findViewById(R.id.saveExcursionButton);
        Button deleteButton = findViewById(R.id.deleteExcursionButton);

        // Intent data
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        vacationStartDate = getIntent().getStringExtra("start");
        vacationEndDate = getIntent().getStringExtra("end");

        // DatePicker
        excursionDate.setOnClickListener(v -> {
            new DatePickerDialog(
                    ExcursionDetails.this,
                    (view, year, month, dayOfMonth) -> {

                        month++;
                        String selectedDate = month + "/" + dayOfMonth + "/" + year;
                        excursionDate.setText(selectedDate);

                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // SAVE
        saveButton.setOnClickListener(v -> {

            name = excursionName.getText().toString();
            date = excursionDate.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "Enter excursion title", Toast.LENGTH_SHORT).show();
                return;
            }

            if (date.equals("mm/dd/yyyy")) {
                Toast.makeText(this, "Select a date", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔥 Requirement 5e
            if (!isDateInRange(date, vacationStartDate, vacationEndDate)) {
                Toast.makeText(this, "Date must be within vacation dates", Toast.LENGTH_LONG).show();
                return;
            }

            Excursion excursion;

            if (excursionID == -1) {
                excursion = new Excursion(name, date, vacationID);
                repository.insert(excursion);
            } else {
                excursion = new Excursion(excursionID, name, date, vacationID);
                repository.update(excursion);
            }

            Toast.makeText(this, "Excursion saved", Toast.LENGTH_SHORT).show();
            finish();
        });

        // DELETE
        deleteButton.setOnClickListener(v -> {

            if (excursionID == -1) {
                finish();
                return;
            }

            Excursion excursion = new Excursion(excursionID, name, date, vacationID);
            repository.delete(excursion);

            Toast.makeText(this, "Excursion deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // 🔥 Date validation (Requirement 5e)
    private boolean isDateInRange(String date, String start, String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

            Date d = sdf.parse(date);
            Date s = sdf.parse(start);
            Date e = sdf.parse(end);

            return !d.before(s) && !d.after(e);

        } catch (Exception e) {
            return false;
        }
    }

    // 🔔 Alert
    private void scheduleAlert(String date, String message) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date alertDate = sdf.parse(date);

            Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
            intent.setAction("exc action");
            intent.putExtra("key", message);

            PendingIntent sender = PendingIntent.getBroadcast(
                    ExcursionDetails.this,
                    MainActivity.numAlert++,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null && alertDate != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, alertDate.getTime(), sender);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setExcursionAlert() {

        name = excursionName.getText().toString();
        date = excursionDate.getText().toString();

        if (name.isEmpty() || date.equals("mm/dd/yyyy")) {
            Toast.makeText(this, "Enter valid excursion data", Toast.LENGTH_SHORT).show();
            return;
        }

        scheduleAlert(date, name + " excursion is today!");

        Toast.makeText(this, "Excursion alert set", Toast.LENGTH_LONG).show();
    }
}