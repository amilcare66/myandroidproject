package com.example.myapplication // <-- ПОМЕНЯЙ НА СВОЙ ПАКЕТ!

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri // Импортируем toUri, чтобы убрать предупреждение

class MainActivity : AppCompatActivity() { // <-- ГЛАВНОЕ ИСПРАВЛЕНИЕ ЗДЕСЬ

    companion object {
        const val EXTRA_TEXT = "extra_text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.editTextInput)
        val btnOpenSecond = findViewById<Button>(R.id.btnOpenSecond)
        val btnCall = findViewById<Button>(R.id.btnCall)
        val btnShare = findViewById<Button>(R.id.btnShare)

        // 1. Явный Intent
        btnOpenSecond.setOnClickListener {
            val text = editText.text.toString().trim()

            if (text.isEmpty()) {
                showToast("Сначала введите текст")
                return@setOnClickListener
            }

            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra(EXTRA_TEXT, text)
            }
            startActivity(intent)
        }

        // 2. Неявный Intent: ACTION_DIAL
        btnCall.setOnClickListener {
            val input = editText.text.toString().trim()

            if (input.isEmpty()) {
                showToast("Введите номер телефона")
                return@setOnClickListener
            }

            val phone = input.replace(Regex("""[\s\-()]"""), "")

            if (!isValidPhone(phone)) {
                showToast("Некорректный номер телефона")
                return@setOnClickListener
            }

            try {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    // Используем .toUri() вместо Uri.parse(), как просит студия
                    data = "tel:$phone".toUri()
                }
                startActivity(dialIntent)
            } catch (e: ActivityNotFoundException) {
                // Используем e, чтобы убрать предупреждение "Parameter is never used"
                showToast("Нет приложения для звонков: ${e.message}")
            }
        }

        // 3. Системный Intent: ACTION_SEND
        btnShare.setOnClickListener {
            val text = editText.text.toString().trim()

            if (text.isEmpty()) {
                showToast("Сначала введите текст")
                return@setOnClickListener
            }

            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }

            try {
                startActivity(Intent.createChooser(sendIntent, "Поделиться через"))
            } catch (e: ActivityNotFoundException) {
                showToast("Нет приложения для отправки: ${e.message}")
            }
        }
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regex("""^\+?\d{6,15}$"""))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}