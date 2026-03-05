package com.example.d308project.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308project.R;
import com.example.d308project.database.Repository;
import com.example.d308project.entities.Excursion;

public class ExcursionDetailsActivity
        extends AppCompatActivity {

    EditText name;
    EditText date;

    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        repository =
                new Repository(getApplication());

        Button save =
                findViewById(R.id.saveExcursion);

        save.setOnClickListener(v -> {

            Excursion e =
                    new Excursion(
                            name.getText().toString(),
                            date.getText().toString(),
                            vacationID
                    );

            repository.insert(e);
        });
    }
}