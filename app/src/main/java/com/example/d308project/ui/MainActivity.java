package com.example.d308project.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308project.R;

public class MainActivity extends AppCompatActivity {
    public static int numAlert = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, com.example.d308project.ui.VacationList.class);
                startActivity(intent);
            }
        });



        VideoView videoView = findViewById(R.id.videoView);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chicago);
        videoView.setVideoURI(videoUri);

// Ensure video is ready before starting
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true); // optional loop
            videoView.start();
        });
    }
}