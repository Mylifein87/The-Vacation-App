package com.example.d308project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308project.R;
import com.example.d308project.database.Repository;
import com.example.d308project.database.VacationDatabaseBuilder;
import com.example.d308project.entities.Excursion;
import com.example.d308project.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VacationList extends AppCompatActivity {
private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationList.this, com.example.d308project.ui.VacationDetails.class);
                startActivity(intent);
            }

        });

        RecyclerView recyclerView=findViewById(R.id.VacationListRecyclerView);

        //System.out.println(getIntent().getStringExtra("test"));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sample) {

            repository=new Repository(getApplication());

            Vacation v1 = new Vacation(
                    "Bermuda Trip",
                    "Hilton Bermuda",
                    "06/01/2026",
                    "06/10/2026"
            );
            repository.insert(v1);

            Vacation v2 = new Vacation(
                    "London Trip",
                    "The Savoy",
                    "07/15/2026",
                    "07/25/2026"
            );
            repository.insert(v2);

            Vacation v3 = new Vacation(
                    "Spring Break",
                    "Miami Beach Resort",
                    "03/10/2026",
                    "03/17/2026"
            );
            repository.insert(v3);


            for (Vacation vacation : repository.getAllVacations()) {

                if (vacation.getTitle().equals("Bermuda Trip")) {

                    repository.insert(new Excursion(
                            "Snorkeling",
                            "06/03/2026",
                            vacation.getVacationID()
                    ));

                    repository.insert(new Excursion(
                            "Hiking",
                            "06/05/2026",
                            vacation.getVacationID()
                    ));
                }

                if (vacation.getTitle().equals("London Trip")) {

                    repository.insert(new Excursion(
                            "Bus Tour",
                            "07/18/2026",
                            vacation.getVacationID()
                    ));

                    repository.insert(new Excursion(
                            "Cooking Lesson",
                            "07/20/2026",
                            vacation.getVacationID()
                    ));
                }
            }

            Toast.makeText(this, "Sample data added!", Toast.LENGTH_LONG).show();

            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return false;
    }

}