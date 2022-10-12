package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View
    }

    private var binding : ActivityBmiBinding? = null
    private var currentVisibleView : String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarBmiActivity)

        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener {
            _, checkedId : Int ->
            if(checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener {
            caculateUnits()
        }
    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.GONE
        binding?.tilMetricUnitHeightFeet?.visibility = View.GONE
        binding?.tilMetricUnitHeightInch?.visibility = View.GONE

        binding?.etMetricUnitWeight?.text!!.clear()
        binding?.etMetricUnitHeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilMetricUnitHeightInch?.visibility = View.VISIBLE

        binding?.etUsMetricUnitWeight?.text!!.clear()
        binding?.etMetricUnitHeightFeet?.text!!.clear()
        binding?.etMetricUnitHeightInch?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi : Float) {
        val bmiLabel : String
        val bmiDescription : String

        if(bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "OOps! you really need to take better care of yourself! Eat more!"
        } else if(bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "OOps! you really need to take better care of yourself! Eat more!"
        } else if(bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "OOps! you really need to take better care of yourself! Eat more!"
        } else if(bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if(bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "OOps! you really need to take better care of yourself! Workout maybe!"
        } else if(bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "OOps! you really need to take better care of yourself! Workout maybe!"
        } else if(bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OOps! you really need to take better care of yourself! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OOps! you really need to take better care of yourself! Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription
    }

    private fun validateMetricUnits() : Boolean {
        var isValid = true

        if(binding?.etMetricUnitWeight?.text.toString().isEmpty() || binding?.etMetricUnitHeight?.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }

    private fun caculateUnits() {
        if(currentVisibleView == METRIC_UNITS_VIEW) {
            if(validateMetricUnits()) {
                val heightValue : Float = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
                val weightValue : Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                val bmi = weightValue / heightValue / heightValue

                displayBMIResult(bmi)
            } else {
                Toast.makeText(this@BMIActivity,
                    "Plaese enter valid vlues.",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            if(validateUsUnits()) {
                val usUnitHeightValueFeet: Float =
                    binding?.etMetricUnitHeightFeet?.text.toString().toFloat()
                val usUnitHeightValueInch : Float =
                    binding?.etMetricUnitHeightInch?.text.toString().toFloat()
                val usUnitWeightValue : Float =
                    binding?.etUsMetricUnitWeight?.text.toString().toFloat()

                val heightValue = usUnitHeightValueFeet * 12 + usUnitHeightValueInch

                val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                displayBMIResult(bmi)
            } else {
                Toast.makeText(this@BMIActivity,
                    "Plaese enter valid vlues.",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun validateUsUnits() : Boolean {
        var isValid = true

        when {
            binding?.etUsMetricUnitWeight?.text.toString().isEmpty() -> isValid = false
            binding?.etMetricUnitHeightFeet?.text.toString().isEmpty() -> isValid = false
            binding?.etMetricUnitHeightInch?.text.toString().isEmpty() -> isValid = false
        }

        return isValid
    }
}