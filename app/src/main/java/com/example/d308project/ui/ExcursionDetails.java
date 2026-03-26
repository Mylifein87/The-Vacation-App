package com.example.d308project.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        repository = new Repository(getApplication());

        excursionName = findViewById(R.id.editExcursionName);
        excursionDate = findViewById(R.id.buttonExcursionDate);

        Button saveButton = findViewById(R.id.saveExcursionButton);
        Button deleteButton = findViewById(R.id.deleteExcursionButton);

        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        vacationStartDate = getIntent().getStringExtra("start");
        vacationEndDate = getIntent().getStringExtra("end");

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

        saveButton.setOnClickListener(v -> saveExcursion());
        deleteButton.setOnClickListener(v -> deleteExcursion());
    }

    private void saveExcursion() {
        String name = excursionName.getText().toString();
        String date = excursionDate.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Enter excursion title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (date.equals("mm/dd/yyyy")) {
            Toast.makeText(this, "Select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isDateInRange(date, vacationStartDate, vacationEndDate)) {
            Toast.makeText(this, "Date must be within vacation dates", Toast.LENGTH_LONG).show();
            return;
        }

        Excursion excursion = new Excursion(name, date, vacationID);

        if (excursionID != -1) {
            excursion.setExcursionId(excursionID);
            repository.update(excursion);
        } else {
            repository.insert(excursion);
        }

        Toast.makeText(this, "Excursion saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteExcursion() {
        if (excursionID == -1) {
            finish();
            return;
        }

        String name = excursionName.getText().toString();
        String date = excursionDate.getText().toString();

        Excursion excursion = new Excursion(name, date, vacationID);
        excursion.setExcursionId(excursionID);

        repository.delete(excursion);

        Toast.makeText(this, "Excursion deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

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
        String name = excursionName.getText().toString();
        String date = excursionDate.getText().toString();

        if (name.isEmpty() || date.equals("mm/dd/yyyy")) {
            Toast.makeText(this, "Enter valid excursion data", Toast.LENGTH_SHORT).show();
            return;
        }

        scheduleAlert(date, name + " excursion is today!");

        Toast.makeText(this, "Excursion alert set", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // ---- NAVIGATION BUTTONS ----
        if (item.getItemId() == R.id.nav_home) {
            Intent intent = new Intent(ExcursionDetails.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == R.id.nav_backarrow) {
            finish(); // back to previous screen
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}