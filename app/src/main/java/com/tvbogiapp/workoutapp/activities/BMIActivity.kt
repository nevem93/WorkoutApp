package com.tvbogiapp.workoutapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.tvbogiapp.workoutapp.R
import kotlinx.android.synthetic.main.activity_b_m_i.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    val METRIC_UNITS_VIEW= "METRIC_UNIT_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"

    var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)

        setSupportActionBar(toolbar_bmi_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setTitle("CALCULATE BMI")
        }

        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }


        btnCalculateUnits.setOnClickListener {
            if (currentVisibleView.equals(METRIC_UNITS_VIEW)){
                if(validateMetricUnits()){
                    calculateBMIMetricUnits()
                } else{
                    Toast.makeText(this@BMIActivity, "Please enter valid values.",
                        Toast.LENGTH_SHORT).show()
                }
            }else{
                if(validateUsUnits()){
                    calculateBMIUsUnits()
                }else{
                    Toast.makeText(this@BMIActivity, "Please enter valid values.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        makeVisibleMetricUnitsView()
        rgUnits.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else {
                makeVisibleUSUnitsView()
            }
        }

    }

    private fun calculateBMIUsUnits() {

            val usUnitHeightValueFeet: String = etUsUnitHeightFeet.text.toString()
            val usUnitHeightValueInch: String = etUsUnitHeightInch.text.toString()
            val usUnitWeightValue: Float = etUsUnitWeight.text.toString().toFloat()

            val usUnitHeightValue = usUnitHeightValueInch.toFloat()+ usUnitHeightValueFeet.toFloat() * 12

            val bmi = 703 * ( usUnitWeightValue / (usUnitHeightValue * usUnitHeightValue))

            displayBMIResult(bmi)
    }

    private fun calculateBMIMetricUnits(){
        val heightValue : Float = etMetricUnitHeight.text.toString().toFloat() / 100
        val weightValue : Float= etMetricUnitWeight.text.toString().toFloat()

        val bmi = weightValue / (heightValue*heightValue)
        displayBMIResult(bmi)
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        llMetricUnitsView.visibility = View.GONE


        etUsUnitWeight.text!!.clear()
        etUsUnitHeightFeet.text!!.clear()
        etUsUnitHeightInch.text!!.clear()

        llUsUnitsView.visibility = View.VISIBLE


        llDisplayBMIResult.visibility = View.INVISIBLE
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        llMetricUnitsView.visibility = View.VISIBLE


        etMetricUnitWeight.text!!.clear()
        etMetricUnitHeight.text!!.clear()

        llUsUnitsView.visibility= View.GONE


        llDisplayBMIResult.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription:String

        if (bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Ooops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Ooops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight"
            bmiDescription = "Ooops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        }else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "Ooops! You really need to of yourself! Workout more!"
        }else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Ooops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        llDisplayBMIResult.visibility=View.VISIBLE

       /* tvYourBMI.visibility = View.VISIBLE
        tvBMIValue.visibility = View.VISIBLE
        tvBMIType.visibility = View.VISIBLE
        tvBMIDescription.visibility = View.VISIBLE*/

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,
            RoundingMode.HALF_EVEN).toString()

        tvBMIValue.text = bmiValue
        tvBMIType.text = bmiLabel
        tvBMIDescription.text = bmiDescription

    }


    private fun validateMetricUnits(): Boolean{
        var isValid = true

        if(etMetricUnitWeight.text.toString().isEmpty() )
            isValid = false
        else if (etMetricUnitHeight.text.toString().isEmpty())
            isValid = false

        return isValid

    }

    private fun validateUsUnits(): Boolean{
        var isValid = true

        when {
            etUsUnitWeight.text.toString().isEmpty() -> isValid = false
            etUsUnitHeightInch.text.toString().isEmpty() -> isValid = false
            etUsUnitHeightFeet.text.toString().isEmpty() -> isValid = false
        }

        return isValid

    }

}
