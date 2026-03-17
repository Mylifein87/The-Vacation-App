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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.d308project.R;
import com.example.d308project.database.Repository;
import com.example.d308project.entities.Excursion;
import com.example.d308project.entities.Vacation;
import com.example.d308project.ui.ExcursionAdapter;
import com.example.d308project.ui.ExcursionDetails;
import com.example.d308project.ui.MainActivity;
import com.example.d308project.ui.MyReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {

    Repository repository;

    EditText editVacationName;
    EditText editVacationPrice;
    EditText editVacationHotel;
    String vacationName;
    double vacationPrice;
    String vacationHotel;
    String vacationStartDate;
    String vacationEndDate;
    int vacationID;
    Button buttonVacationDateStart;
    Button buttonVacationDateEnd;
    DatePickerDialog.OnDateSetListener vStartDate;
    DatePickerDialog.OnDateSetListener vEndDate;
    final Calendar vStartCalendar = Calendar.getInstance();
    final Calendar vEndCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);

        //section sets the fields to designated name and price
        editVacationName = findViewById(R.id.textNameId); //from activity_vacation_details
        editVacationPrice = findViewById(R.id.textPriceId);
        editVacationHotel = findViewById(R.id.textHotelId);

        //get values from intent extras
        vacationID = getIntent().getIntExtra("id", -1);
        Log.d("vacationID", "VacationID is: " + vacationID);
        vacationName = getIntent().getStringExtra("name");
        vacationPrice = getIntent().getDoubleExtra("price", 0.0);
        vacationHotel = getIntent().getStringExtra("hotel");
        vacationStartDate = getIntent().getStringExtra("start");
        vacationEndDate = getIntent().getStringExtra("end");
        Log.d("vacationStartDate", "start date is " + vacationStartDate);

        //set text of editText fields to data passed from intent extras
        editVacationName.setText(vacationName);
        editVacationPrice.setText(Double.toString(vacationPrice));
        editVacationHotel.setText(vacationHotel);

        buttonVacationDateStart = findViewById(R.id.buttonVacationDateStart);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        buttonVacationDateStart.setText(vacationStartDate); //set text to value from intent extra

        //default date to current if no value was passed
        if (vacationStartDate == null) {
            vStartCalendar.setTime(new Date()); //set start calendar to current date
            vacationStartDate = sdf.format(vStartCalendar.getTime()); //update value
            buttonVacationDateStart.setText(vacationStartDate);
            //getIntent().putExtra("start", vacationStartDate);
        }

        buttonVacationDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Date date;
                String info = buttonVacationDateStart.getText().toString(); //get button text
                try {
                    vStartCalendar.setTime(sdf.parse(info)); //parse info into a date object
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this,
                        vStartDate,
                        vStartCalendar.get(Calendar.YEAR),
                        vStartCalendar.get(Calendar.MONTH),
                        vStartCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        vStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                vStartCalendar.set(Calendar.YEAR, year);
                vStartCalendar.set(Calendar.MONTH, month);
                vStartCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //check if after end date
                if (vStartCalendar.after(vEndCalendar)) {
                    Toast.makeText(VacationDetails.this, "Start date cannot be after end date", Toast.LENGTH_SHORT).show();
                    vStartCalendar.setTime(vEndCalendar.getTime());
                } else {
                    vacationStartDate = new SimpleDateFormat("MM/dd/yy", Locale.US).format(vStartCalendar.getTime());
                    buttonVacationDateStart.setText(vacationStartDate);
                    updateStartLabel();
                }
            }
        };

        buttonVacationDateEnd = findViewById(R.id.buttonVacationDateEnd);
        String endFormat = "MM/dd/yy";
        SimpleDateFormat sdfEnd = new SimpleDateFormat(endFormat, Locale.US);
        buttonVacationDateEnd.setText(vacationEndDate);

        if (vacationEndDate == null) {
            vEndCalendar.setTime(new Date());
            vacationEndDate = sdfEnd.format(vEndCalendar.getTime());
            buttonVacationDateEnd.setText(vacationEndDate);
            //getIntent().putExtra("end", vacationEndDate);
        }

        buttonVacationDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Date date;
                String info = buttonVacationDateEnd.getText().toString(); //get button text
                try {
                    vEndCalendar.setTime(sdfEnd.parse(info)); //parse info into a date object
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this,
                        vEndDate,
                        vEndCalendar.get(Calendar.YEAR),
                        vEndCalendar.get(Calendar.MONTH),
                        vEndCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        vEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                vEndCalendar.set(Calendar.YEAR, year);
                vEndCalendar.set(Calendar.MONTH, month);
                vEndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //check if end date is before start date
                if (vEndCalendar.before(vStartCalendar)) {
                    Toast.makeText(VacationDetails.this, "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                    vEndCalendar.setTime(vStartCalendar.getTime());
                } else {
                    vacationEndDate = new SimpleDateFormat("MM/dd/yy", Locale.US).format(vEndCalendar.getTime());
                    buttonVacationDateEnd.setText(vacationEndDate);
                    updateEndLabel();
                }
            }
        };

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("vacationID", vacationID);
                intent.putExtra("start", vacationStartDate);
                intent.putExtra("end", vacationEndDate);

                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Excursion> filteredExcursions = new ArrayList<>(); //make filtered list by vacation ID
        for (Excursion e : repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) {
                filteredExcursions.add(e);
            }
        }

        excursionAdapter.setExcursions(filteredExcursions); //send only filtered excursions
        //excursionAdapter.setExcursions(repository.getmAllExcursions()); //get all excursions from repo
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.vacationsave) { //save vacation
            Vacation vacation;
            if (vacationID == -1) { //no id returned means vacation doesn't exist
                if (repository.getmALLVacations().size() == 0) {
                    vacationID = 1;
                } else {
                    vacationID = repository.getmALLVacations().get(repository.getmALLVacations().size() - 1).getVacationID() + 1;
                }
                vacation = new Vacation(
                        vacationID,
                        editVacationName.getText().toString(),
                        Double.parseDouble(editVacationPrice.getText().toString()),
                        editVacationHotel.getText().toString(),
                        vacationStartDate,
                        vacationEndDate
                );
                repository.insert(vacation); //create new vacation
                this.finish(); //close and go back after saving
            } else { //id returned means vacation exists
                vacation = new Vacation(
                        vacationID,
                        editVacationName.getText().toString(),
                        Double.parseDouble(editVacationPrice.getText().toString()),
                        editVacationHotel.getText().toString(),
                        vacationStartDate,
                        vacationEndDate
                );
                repository.update(vacation); //not insert bc changing an existing vacation
                this.finish(); //close and go back after saving
            }
        }
        if (item.getItemId() == R.id.vacationdelete) {
            Vacation vacation;
            List<Excursion> excursions = repository.getAssociatedExcursions(vacationID); //check for associated excursions
            if (!excursions.isEmpty()) { //if there are associated excursions
                //alert the user and do nothing
                Toast.makeText(VacationDetails.this, "Cannot delete a vacation with excursions", Toast.LENGTH_SHORT).show();
            } else if (vacationID == -1) { //no id returned means vacation doesn't exist yet
                this.finish(); //do nothing and go back
            } else { //id returned means vacation exists
                vacation = new Vacation(
                        vacationID,
                        editVacationName.getText().toString(),
                        Double.parseDouble(editVacationPrice.getText().toString()),
                        editVacationHotel.getText().toString(),
                        vacationStartDate,
                        vacationEndDate
                );
                repository.delete(vacation); //not insert bc changing an existing vacation
                this.finish();
            }
        }
        if (item.getItemId() == R.id.vacationshare) {
            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TEXT, editVacationName.getText().toString() + " at " + editVacationHotel.getText().toString() + " starting on " + vacationStartDate + " and ends on" + vacationEndDate + ".");
            sentIntent.putExtra(Intent.EXTRA_TITLE, editVacationName.getText().toString());
            sentIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);
            return true;
        }
        if (item.getItemId()==R.id.vacationstartalert) {
            String dateFromScreen = vacationStartDate;
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;

            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Long trigger = myDate.getTime();

            //broadcast receiver sends the notification
            Intent intent = new Intent(VacationDetails.this, MyReceiver.class); //intent to send
            intent.setAction("start action");
            intent.putExtra("start key", vacationName + " starts today!"); //notification message
            PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE); //increments from 0 for the whole app every alert
            //alarm service wakes up the app to send the notification
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

            return true;
        }
        if (item.getItemId()==R.id.vacationendalert) {
            String dateFromScreen = vacationEndDate;
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;

            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Long trigger = myDate.getTime();

            //broadcast receiver sends the notification
            Intent intent = new Intent(VacationDetails.this, MyReceiver.class); //intent to send
            intent.setAction("end action");
            intent.putExtra("end key", vacationName + " ends today!"); //notification message
            PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE); //increments from 0 for the whole app every alert
            //alarm service wakes up the app to send the notification
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish(); //go back to parent activity
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        // Add any code you want to execute when the activity resumes
        // For example, you can refresh data or update UI components
        super.onResume();

        List<Excursion> filteredExcursions = repository.getAssociatedExcursions(vacationID);
        Log.d("filteredExcursions", "List of excursions: " + filteredExcursions);

        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        excursionAdapter.setExcursions(filteredExcursions);
        excursionAdapter.notifyDataSetChanged();
    }

    private void updateStartLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        vacationStartDate = sdf.format(vStartCalendar.getTime());
        buttonVacationDateStart.setText(sdf.format(vStartCalendar.getTime()));
    }

    private void updateEndLabel() {
        String endFormat = "MM/dd/yy";
        SimpleDateFormat sdfEnd = new SimpleDateFormat(endFormat, Locale.US);

        vacationEndDate = sdfEnd.format(vEndCalendar.getTime());
        buttonVacationDateEnd.setText(sdfEnd.format(vEndCalendar.getTime()));
    }
}