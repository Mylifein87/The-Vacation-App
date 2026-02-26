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

        System.out.println(getIntent().getStringExtra("test"));

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

            Vacation vacation=new Vacation("Bermuda Trip", 2000.0);
            repository.insert(vacation);
            vacation=new Vacation("London Trip", 4000.0);
            repository.insert(vacation);
            vacation=new Vacation("Spring Break", 1700.0);
            repository.insert(vacation);


            Excursion excursion= new Excursion(vacationID: 0, excursionName: "Snorkeling", price: 100.0 );
            repository.insert(excursion);
            excursion= new Excursion(vacationID: 0, excursionName: "Hiking", price: 15.0 );
            repository.insert(excursion);
            excursion= new Excursion(vacationID: 0, excursionName: "Bus Tour", price: 25.0 );
            repository.insert(excursion);
            excursion= new Excursion(vacationID: 0, excursionName: "Cooking Lesson", price: 50.0 );
            repository.insert(excursion);





            VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(this);



            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return false;
    }

}