package com.tvbogiapp.workoutapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tvbogiapp.workoutapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        llStart.setOnClickListener {
            intent= Intent(this,
                ExerciseActivity::class.java)
            startActivity(intent)
        }

        llBMI.setOnClickListener {
            intent= Intent(this,
                BMIActivity::class.java)
            startActivity(intent)
        }
        llHistory.setOnClickListener {
            intent = (Intent(this, HistoryActivity::class.java))
            startActivity(intent)
        }

    }
}
