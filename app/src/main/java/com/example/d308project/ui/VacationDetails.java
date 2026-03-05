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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308project.R;
import com.example.d308project.database.Repository;
import com.example.d308project.entities.Excursion;
import com.example.d308project.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetailsActivity extends AppCompatActivity {

    EditText title;
    EditText hotel;
    EditText start;
    EditText end;

    Repository repository;
    int vacationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        title = findViewById(R.id.title);
        hotel = findViewById(R.id.hotel);
        start = findViewById(R.id.startDate);
        end = findViewById(R.id.endDate);

        repository = new Repository(getApplication());

        Button save = findViewById(R.id.saveButton);

        save.setOnClickListener(v -> saveVacation());
    }

    void saveVacation() {

        Vacation vacation = new Vacation(
                title.getText().toString(),
                hotel.getText().toString(),
                start.getText().toString(),
                end.getText().toString()
        );

        repository.insert(vacation);
        finish();
    }
}