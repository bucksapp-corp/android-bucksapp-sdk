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
                        "API_KEY",
                        "USER_UUID",
                        "development",
                        "es"
                    )
                )
                .commitNow()
        }

    }
}