package com.udemy.a7minutesworkout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udemy.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNIT_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNIT_VIEW = "US_UNIT_VIEW"
    }

    private var currentVisibleView: String = METRIC_UNIT_VIEW

    private var _binding: ActivityBmiBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarBmiActivity)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding.toolbarBmiActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitView()

        binding.rgUnit.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.rb_metric_unit) makeVisibleMetricUnitView()
            else makeVisibleUsUnitView()
        }

        binding.btnCalculateUnit.setOnClickListener {
            calculateUnit()
        }
    }

    private fun makeVisibleMetricUnitView() {
        currentVisibleView = METRIC_UNIT_VIEW

        binding.tilMetricUnitWeight.visibility = View.VISIBLE
        binding.tilMetricUnitHeight.visibility = View.VISIBLE

        binding.tilUsMetricUnitWeight.visibility = View.GONE
        binding.tilMetricUsUnitHeightFeet.visibility = View.GONE
        binding.tilMetricUsUnitHeightInch.visibility = View.GONE

        binding.etMetricUnitWeight.text!!.clear()
        binding.etMetricUnitHeight.text!!.clear()

        binding.llDisplayBmiResult.visibility = View.INVISIBLE
    }

    private fun makeVisibleUsUnitView() {
        currentVisibleView = US_UNIT_VIEW

        binding.tilMetricUnitWeight.visibility = View.INVISIBLE
        binding.tilMetricUnitHeight.visibility = View.INVISIBLE

        binding.tilUsMetricUnitWeight.visibility = View.VISIBLE
        binding.tilMetricUsUnitHeightFeet.visibility = View.VISIBLE
        binding.tilMetricUsUnitHeightInch.visibility = View.VISIBLE

        binding.etMetricUnitWeight.text!!.clear()
        binding.etUsMetricUnitHeightFeet.text!!.clear()
        binding.etUsMetricUnitHeightInch.text!!.clear()

        binding.llDisplayBmiResult.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding.llDisplayBmiResult.visibility = View.VISIBLE
        binding.tvBmiValue.text = bmiValue
        binding.tvBmiType.text = bmiLabel
        binding.tvBmiDescription.text = bmiDescription
    }

    private fun calculateUnit() {
        if (currentVisibleView == METRIC_UNIT_VIEW) {
            if (validateMetricUnit()) {
                val weightValue: Float = binding.etMetricUnitWeight.text.toString().toFloat()
                val heightValue: Float = binding.etMetricUnitHeight.text.toString().toFloat() / 100
                val bmi = weightValue / (heightValue * heightValue)
                displayBMIResult(bmi)
            } else Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()
        } else {
            if (validateUsUnit()) {
                val usUnitHeightValueFeet: String = binding.etUsMetricUnitHeightFeet.text.toString()
                val usUnitHeightValueInch: String = binding.etUsMetricUnitHeightInch.text.toString()
                val usUnitWeightValue: Float =
                    binding.etUsMetricUnitWeight.text.toString().toFloat()

                val heightValue =
                    usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                displayBMIResult(bmi)
            } else Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateUsUnit(): Boolean {
        var isValid = true

        when {
            binding.etUsMetricUnitWeight.text.toString().isEmpty() -> isValid = false
            binding.etUsMetricUnitHeightFeet.text.toString().isEmpty() -> isValid = false
            binding.etUsMetricUnitHeightInch.text.toString().isEmpty() -> isValid = false
        }
        return isValid
    }

    private fun validateMetricUnit(): Boolean {
        var isValid = true

        if (binding.etMetricUnitWeight.text.toString().isEmpty()) isValid = false
        else if (binding.etMetricUnitHeight.text.toString().isEmpty()) isValid = false

        return isValid
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}