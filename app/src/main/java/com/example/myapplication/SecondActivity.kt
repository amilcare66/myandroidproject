package com.example.myapplication;
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val textView = findViewById<TextView>(R.id.textViewReceived)

        val receivedText = intent.getStringExtra(MainActivity.EXTRA_TEXT)

        textView.text = if (receivedText.isNullOrBlank()) {
            "Нет переданного текста"
        } else {
            receivedText
        }
    }
}