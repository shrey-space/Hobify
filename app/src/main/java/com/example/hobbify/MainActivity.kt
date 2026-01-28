package com.example.hobbify

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.hobbify.model.DayModel
import com.example.hobbify.utils.Prefs
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val startCal = Calendar.getInstance()
    private val endCal = Calendar.getInstance()

    private lateinit var edtHobby: EditText
    private lateinit var btnStartDate: Button
    private lateinit var btnEndDate: Button
    private lateinit var btnStartChallenge: Button

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If challenge already active → skip main
        if (Prefs.isChallengeActive(this)) {
            startActivity(Intent(this, ChallengeActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        // Bind views
        edtHobby = findViewById(R.id.edtHobby)
        btnStartDate = findViewById(R.id.btnStartDate)
        btnEndDate = findViewById(R.id.btnEndDate)
        btnStartChallenge = findViewById(R.id.btnStartChallenge)

        btnStartDate.setOnClickListener {
            pickDate(true)
        }

        btnEndDate.setOnClickListener {
            pickDate(false)
        }

        btnStartChallenge.setOnClickListener {

            // Basic validation
            if (edtHobby.text.toString().trim().isEmpty()) {
                edtHobby.error = "Please enter hobby name"
                return@setOnClickListener
            }

            if (startCal.after(endCal)) {
                android.widget.Toast.makeText(
                    this,
                    "Start date must be before end date",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val days = generateDays()
            Prefs.saveDays(this, days)
            Prefs.setChallengeActive(this, true)

            val intent = Intent(this, ChallengeActivity::class.java)
            intent.putExtra("HOBBY_NAME", edtHobby.text.toString().trim())
            startActivity(intent)
            finish()
        }
    }

    private fun pickDate(isStart: Boolean) {
        val cal = if (isStart) startCal else endCal

        DatePickerDialog(
            this,
            { _, year, month, day ->
                cal.set(year, month, day)

                val selectedDate = formatter.format(cal.time)

                if (isStart) {
                    btnStartDate.text = "Start: $selectedDate"
                } else {
                    btnEndDate.text = "End: $selectedDate"
                }
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun generateDays(): MutableList<DayModel> {
        val list = mutableListOf<DayModel>()

        val temp = startCal.clone() as Calendar
        while (!temp.after(endCal)) {
            list.add(DayModel(formatter.format(temp.time)))
            temp.add(Calendar.DAY_OF_MONTH, 1)
        }

        return list
    }
}
