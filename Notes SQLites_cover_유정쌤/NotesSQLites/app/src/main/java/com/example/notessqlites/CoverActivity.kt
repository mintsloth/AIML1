package com.example.notessqlites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CoverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cover)

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            // 시작하기 버튼을 누르면 MainActivity로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // 커버 페이지는 필요없으니 종료
        }
    }
}
