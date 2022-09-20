package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding : ActivityExerciseBinding? = null

    private var restTimer : CountDownTimer? = null
    private var restProgress : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setupRestView()
    }

    private fun setupRestView() {
        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                Toast.makeText(
                    this@ExerciseActivity,
                    "Timer is finished",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        binding = null
    }
}