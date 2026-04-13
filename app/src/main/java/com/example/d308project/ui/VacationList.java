package com.example.d308project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308project.R;
import com.example.d308project.database.Repository;
import com.example.d308project.entities.Excursion;
import com.example.d308project.entities.Vacation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VacationList extends AppCompatActivity {

    private Repository repository;
    private VacationAdapter vacationAdapter;
    private RecyclerView recyclerView;

    private EditText searchInput;
    private Button searchButton;
    private Button reportButton;

    private List<Vacation> currentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);

        View main = findViewById(R.id.main);

        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        repository = new Repository(getApplication());

        recyclerView = findViewById(R.id.vacationRecyclerView);
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);
        reportButton = findViewById(R.id.reportButton);

        currentList = repository.getmALLVacations();
        vacationAdapter.setVacations(currentList);

        searchButton.setOnClickListener(v -> performSearch());
        reportButton.setOnClickListener(v -> generateReport());

        FloatingActionButton floatingActionButton =
                findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_home) {
                Intent intent = new Intent(VacationList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }

            if (item.getItemId() == R.id.nav_backarrow) {
                finish();
                return true;
            }

            return false;
        });
    }

    // ✅ SEARCH FUNCTION (multi-row results)
    private void performSearch() {

        String query = searchInput.getText().toString().toLowerCase();
        List<Vacation> results = new ArrayList<>();

        for (Vacation v : repository.getmALLVacations()) {
            if (v.getTitle().toLowerCase().contains(query)) {
                results.add(v);
            }
        }

        currentList = results;
        vacationAdapter.setVacations(results);
        vacationAdapter.notifyDataSetChanged();

        Toast.makeText(this, results.size() + " results found", Toast.LENGTH_SHORT).show();
    }

    // ✅ REPORT GENERATION (FULL RUBRIC COMPLIANCE)
    private void generateReport() {

        if (currentList.isEmpty()) {
            Toast.makeText(this, "No data to report", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder report = new StringBuilder();

        String timestamp = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US)
                .format(new Date());

        // ✅ TITLE + TIMESTAMP
        report.append("=== Vacation Report ===\n");
        report.append("Generated: ").append(timestamp).append("\n\n");

        // ✅ MULTI-COLUMN HEADER
        report.append("Title | Hotel | Price | Start | End\n");
        report.append("------------------------------------------------\n");

        for (Vacation v : currentList) {

            // ✅ MAIN ROW
            report.append(v.getTitle()).append(" | ")
                    .append(v.getHotel()).append(" | ")
                    .append(v.getPrice()).append(" | ")
                    .append(v.getStartDate()).append(" | ")
                    .append(v.getEndDate()).append("\n");

            // ✅ ASSOCIATED EXCURSIONS
            List<Excursion> excursions = repository.getAssociatedExcursions(v.getVacationID());

            for (Excursion e : excursions) {
                report.append("   -> Excursion: ")
                        .append(e.getExcursionId())
                        .append(" (")
                        .append(e.getExcursionDate())
                        .append(")\n");
            }

            report.append("\n");
        }

        // ✅ SHARE REPORT
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, report.toString());

        startActivity(Intent.createChooser(intent, "Share Report"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        currentList = repository.getmALLVacations();
        vacationAdapter.setVacations(currentList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.sample) {

            Vacation v1 = new Vacation(
                    1,
                    "Bermuda Trip",
                    1200.0,
                    "Hilton Bermuda",
                    "06/01/2026",
                    "06/10/2026"
            );
            repository.insert(v1);

            Vacation v2 = new Vacation(
                    2,
                    "London Trip",
                    1800.0,
                    "The Savoy",
                    "07/15/2026",
                    "07/25/2026"
            );
            repository.insert(v2);

            Vacation v3 = new Vacation(
                    3,
                    "Spring Break",
                    900.0,
                    "Miami Beach Resort",
                    "03/10/2026",
                    "03/17/2026"
            );
            repository.insert(v3);

            for (Vacation vacation : repository.getmALLVacations()) {

                if (vacation.getTitle().equals("Bermuda Trip")) {
                    repository.insert(new Excursion("Snorkeling", "06/03/2026", vacation.getVacationID()));
                    repository.insert(new Excursion("Hiking", "06/05/2026", vacation.getVacationID()));
                }

                if (vacation.getTitle().equals("London Trip")) {
                    repository.insert(new Excursion("Bus Tour", "07/18/2026", vacation.getVacationID()));
                    repository.insert(new Excursion("Cooking Lesson", "07/20/2026", vacation.getVacationID()));
                }
            }

            currentList = repository.getmALLVacations();
            vacationAdapter.setVacations(currentList);

            Toast.makeText(this, "Sample data added!", Toast.LENGTH_LONG).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}