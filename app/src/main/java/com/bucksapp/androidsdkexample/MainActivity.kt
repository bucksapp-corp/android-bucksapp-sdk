package com.bucksapp.androidsdkexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.bucksapp.androidsdk.Bucksapp


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            Bucksapp.init(
                this@MainActivity,
                "12TvAswlCh03Qhj5uxiM7w",
                "1c111bf4-7646-4b84-bc4c-4426fb596a87",
            )
        }

    }
}