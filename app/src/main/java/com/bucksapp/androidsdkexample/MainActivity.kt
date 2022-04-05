package com.bucksapp.androidsdkexample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bucksapp.androidsdk.BucksappFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container, BucksappFragment.newInstance(
                        "12TvAswlCh03Qhj5uxiM7w",
                        "1c111bf4-7646-4b84-bc4c-4426fb596a87",
                        "development",
                        "es"
                    )
                )
                .commitNow()
        }

    }
}