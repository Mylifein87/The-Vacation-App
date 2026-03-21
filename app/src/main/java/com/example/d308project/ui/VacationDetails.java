package com.example.d308project.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308project.R;
import com.example.d308project.database.Repository;
import com.example.d308project.entities.Excursion;
import com.example.d308project.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VacationDetails extends AppCompatActivity {

    Repository repository;

    EditText editVacationName, editVacationPrice, editVacationHotel;
    String vacationStartDate, vacationEndDate;
    int vacationID;

    Button buttonVacationDateStart, buttonVacationDateEnd;

    DatePickerDialog.OnDateSetListener vStartDate, vEndDate;
    final Calendar vStartCalendar = Calendar.getInstance();
    final Calendar vEndCalendar = Calendar.getInstance();

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);

        editVacationName = findViewById(R.id.textNameId);
        editVacationPrice = findViewById(R.id.textPriceId);
        editVacationHotel = findViewById(R.id.textHotelId);

        vacationID = getIntent().getIntExtra("id", -1);

        editVacationName.setText(getIntent().getStringExtra("name"));
        editVacationPrice.setText(String.valueOf(getIntent().getDoubleExtra("price", 0.0)));
        editVacationHotel.setText(getIntent().getStringExtra("hotel"));

        vacationStartDate = getIntent().getStringExtra("start");
        vacationEndDate = getIntent().getStringExtra("end");

        buttonVacationDateStart = findViewById(R.id.buttonVacationDateStart);
        buttonVacationDateEnd = findViewById(R.id.buttonVacationDateEnd);

        if (vacationStartDate == null) {
            vacationStartDate = sdf.format(new Date());
        }
        if (vacationEndDate == null) {
            vacationEndDate = sdf.format(new Date());
        }

        buttonVacationDateStart.setText(vacationStartDate);
        buttonVacationDateEnd.setText(vacationEndDate);

        buttonVacationDateStart.setOnClickListener(v -> showDatePicker(true));
        buttonVacationDateEnd.setOnClickListener(v -> showDatePicker(false));

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExcursionDetails.class);
            intent.putExtra("vacationID", vacationID);
            intent.putExtra("start", vacationStartDate);
            intent.putExtra("end", vacationEndDate);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        repository = new Repository(getApplication());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ExcursionAdapter adapter = new ExcursionAdapter(this);

        List<Excursion> filtered = new ArrayList<>();
        for (Excursion e : repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) {
                filtered.add(e);
            }
        }

        adapter.setExcursions(filtered);
        recyclerView.setAdapter(adapter);
    }

    private void showDatePicker(boolean isStart) {
        Calendar cal = isStart ? vStartCalendar : vEndCalendar;

        new DatePickerDialog(this, (view, year, month, day) -> {
            cal.set(year, month, day);

            String formatted = sdf.format(cal.getTime());

            if (isStart) {
                vStartCalendar.setTime(cal.getTime());
                vacationStartDate = formatted;
                buttonVacationDateStart.setText(formatted);
            } else {
                vEndCalendar.setTime(cal.getTime());
                vacationEndDate = formatted;
                buttonVacationDateEnd.setText(formatted);
            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    // ✅ DATE VALIDATION (Requirement 3c + 3d)
    private boolean validateDates() {
        try {
            Date start = sdf.parse(vacationStartDate);
            Date end = sdf.parse(vacationEndDate);

            if (end.before(start)) {
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateFields() {
        if (editVacationName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter vacation name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (editVacationPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter price", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.vacationsave) {

            if (!validateFields() || !validateDates()) return true;

            Vacation vacation = new Vacation(
                    vacationID == -1 ? (int) System.currentTimeMillis() : vacationID,
                    editVacationName.getText().toString(),
                    Double.parseDouble(editVacationPrice.getText().toString()),
                    editVacationHotel.getText().toString(),
                    vacationStartDate,
                    vacationEndDate
            );

            if (vacationID == -1) repository.insert(vacation);
            else repository.update(vacation);

            finish();
        }

        if (item.getItemId() == R.id.vacationdelete) {

            if (repository.getAssociatedExcursions(vacationID).size() > 0) {
                Toast.makeText(this, "Cannot delete vacation with excursions", Toast.LENGTH_SHORT).show();
                return true;
            }

            Vacation vacation = new Vacation(
                    vacationID,
                    editVacationName.getText().toString(),
                    Double.parseDouble(editVacationPrice.getText().toString()),
                    editVacationHotel.getText().toString(),
                    vacationStartDate,
                    vacationEndDate
            );

            repository.delete(vacation);
            finish();
        }

        if (item.getItemId() == R.id.vacationshare) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");

            String text = "Vacation: " + editVacationName.getText()
                    + "\nHotel: " + editVacationHotel.getText()
                    + "\nStart: " + vacationStartDate
                    + "\nEnd: " + vacationEndDate;

            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(intent, "Share Vacation"));
        }

        if (item.getItemId() == R.id.vacationstartalert) {
            scheduleAlert(vacationStartDate, editVacationName.getText().toString() + " starts today!", "start action");
        }

        if (item.getItemId() == R.id.vacationendalert) {
            scheduleAlert(vacationEndDate, editVacationName.getText().toString() + " ends today!", "end action");
        }

        return true;
    }

    private void scheduleAlert(String date, String message, String action) {
        try {
            Date alertDate = sdf.parse(date);

            Intent intent = new Intent(this, MyReceiver.class);
            intent.setAction(action);

            if (action.equals("start action")) {
                intent.putExtra("start key", message);
            } else {
                intent.putExtra("end key", message);
            }

            PendingIntent sender = PendingIntent.getBroadcast(
                    this,
                    MainActivity.numAlert++,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null && alertDate != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, alertDate.getTime(), sender);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}