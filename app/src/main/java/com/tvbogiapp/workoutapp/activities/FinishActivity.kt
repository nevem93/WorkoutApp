package com.tvbogiapp.workoutapp.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tvbogiapp.workoutapp.R
import com.tvbogiapp.workoutapp.model.SqliteOpenHelper
import kotlinx.android.synthetic.main.activity_finish.*
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        setSupportActionBar(toolbar_finish_activity)
        val actionBar = supportActionBar //actionbar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true) //lset back button
        }
        toolbar_finish_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        btnFinish.setOnClickListener {
            finish()
        }

        addDateToDb()

    }

    private fun addDateToDb(){
        val calendar= Calendar.getInstance()
        val dateTime = calendar.time
        Log.i("DATE: ", "" + dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)

        val dbHandler =
            SqliteOpenHelper(this, null)
        dbHandler.addDate(date)
        Log.i("DATE: ", "Added")

    }

}
