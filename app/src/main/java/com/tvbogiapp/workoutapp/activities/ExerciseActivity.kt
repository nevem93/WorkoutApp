package com.tvbogiapp.workoutapp.activities

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tvbogiapp.workoutapp.*
import com.tvbogiapp.workoutapp.adapter.ExerciseStatusAdapter
import com.tvbogiapp.workoutapp.model.Constants
import com.tvbogiapp.workoutapp.model.ExerciseModel
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_cutom_back_confirmation.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(),  TextToSpeech.OnInitListener {

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration: Long = 10

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgRess = 0
    private  var exerciseTimerDuration: Long = 30

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null // Variable for TextToSpeech

    private var player: MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        tts = TextToSpeech(this, this)

        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar
        if (actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogForBackButton()
        }


        exerciseList =
            Constants.defaultExerciseList()
        setUpRestView()

        setUpExerciseStatusRecyclerView()


    }

    override fun onDestroy() {
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }
        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgRess=0
        }

        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player != null){
            player!!.stop()
        }

        super.onDestroy()
    }

    private fun setExerciseProgressBar(){

        exercise_progressBar.progress=exerciseProgRess
        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 1000,1000){
            override fun onFinish() {
                //if(currentExercisePosition < exerciseList?.size!! - 1) {
                if(currentExercisePosition < 2) {
                    exerciseList!![currentExercisePosition].setisSelected(false)
                    exerciseList!![currentExercisePosition].setisCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setUpRestView()

                }else{
                    finish()
                    intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)

                }
            }

            override fun onTick(millisUntilFinished: Long) {
                exerciseProgRess++
                exercise_progressBar.progress = (exerciseTimerDuration- exerciseProgRess).toInt()
                exercise_tvTimer.text = (exerciseTimerDuration-exerciseProgRess).toString()

            }

        }.start()
    }
    private fun setUpExerciseView(){
        if(exerciseTimer!=null){
            exerciseTimer!!.cancel()
            exerciseProgRess = 0
        }
        llRestView.visibility = View.GONE
        llExerciseView.visibility=View.VISIBLE
        setExerciseProgressBar()

        ivImage.setImageResource(exerciseList!![currentExercisePosition].getimage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
        speakOut("Start ${tvUpcomingExerciseName.text.toString()}")

    }

    private fun setRestProgressBar(){
        progressBar.progress = restProgress

        restTimer = object: CountDownTimer(restTimerDuration*1000, 1000){
            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setisSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setUpExerciseView()

            }

            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = (restTimerDuration - restProgress).toInt()
                tvTimer.text = (restTimerDuration-restProgress).toString()
            }

        }.start()
    }

    private fun setUpRestView(){

        try{
            player = MediaPlayer.create(applicationContext,
                R.raw.press_start
            )
            player!!.isLooping = false
            player!!.start()

        }catch (e:Exception){
            e.printStackTrace()
        }

        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility = View.GONE

        if(restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0

        }
        tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition + 1].getName()


        speakOut("Up coming exercise is ${tvUpcomingExerciseName.text.toString()}")

        setRestProgressBar()
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            //set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result==TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", " The language is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH,null, "")
    }

    private fun setUpExerciseStatusRecyclerView() {
        rvExerciseStatus.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter =
            ExerciseStatusAdapter(
                exerciseList!!,
                this
            )
        rvExerciseStatus.adapter = exerciseAdapter
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)

        customDialog.setContentView(R.layout.dialog_cutom_back_confirmation)
        customDialog.tvYes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

}
