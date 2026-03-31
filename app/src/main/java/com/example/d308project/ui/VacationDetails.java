package com.example.d308project.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VacationDetails extends AppCompatActivity {

    Repository repository;

    EditText editVacationName, editVacationPrice, editVacationHotel;
    String vacationStartDate, vacationEndDate;
    int vacationID;

    Button buttonVacationDateStart, buttonVacationDateEnd;

    final Calendar vStartCalendar = Calendar.getInstance();
    final Calendar vEndCalendar = Calendar.getInstance();

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);

        View main = findViewById(R.id.main);

        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new Repository(getApplication());

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Excursion> filtered = new ArrayList<>();
        for (Excursion e : repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) {
                filtered.add(e);
            }
        }

        ExcursionAdapter adapter = new ExcursionAdapter(this);
        adapter.setExcursions(filtered);
        recyclerView.setAdapter(adapter);
    }

    // ✅ FIXED DATE PICKER (NO MONTH BUG HERE)
    private void showDatePicker(boolean isStart) {
        Calendar cal = isStart ? vStartCalendar : vEndCalendar;

        new DatePickerDialog(this, (view, year, month, day) -> {

            cal.set(year, month, day);

            String formatted = sdf.format(cal.getTime());

            if (isStart) {
                vacationStartDate = formatted;
                buttonVacationDateStart.setText(formatted);
            } else {
                vacationEndDate = formatted;
                buttonVacationDateEnd.setText(formatted);
            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

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

        if (item.getItemId() == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }

        if (item.getItemId() == R.id.nav_backarrow) {
            finish();
            return true;
        }

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

            repository.delete(new Vacation(
                    vacationID,
                    editVacationName.getText().toString(),
                    Double.parseDouble(editVacationPrice.getText().toString()),
                    editVacationHotel.getText().toString(),
                    vacationStartDate,
                    vacationEndDate
            ));

            finish();
        }

        return true;
    }
}