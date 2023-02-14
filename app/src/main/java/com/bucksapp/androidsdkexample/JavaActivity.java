package com.bucksapp.androidsdkexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bucksapp.androidsdk.BucksappFragment;

public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        BucksappFragment bucksappFragment = BucksappFragment.Companion.newInstance(
                "API_KEY",
                "USER_UUID",
                "staging",
                "es"
        );

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, bucksappFragment)
                        .commitNow();
            }
        });
    }
}