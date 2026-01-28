package com.example.hobbify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hobbify.adapter.DayAdapter
import com.example.hobbify.utils.Prefs

class ChallengeActivity : AppCompatActivity() {
    private lateinit var txtHobbyName: TextView

    private lateinit var recyclerDays: RecyclerView
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)
        txtHobbyName = findViewById(R.id.txtHobbyName)

        val hobbyName = intent.getStringExtra("HOBBY_NAME") ?: "My Hobby"
        txtHobbyName.text = hobbyName

        recyclerDays = findViewById(R.id.recyclerDays)
        btnCancel = findViewById(R.id.btnCancel)

        recyclerDays.layoutManager = GridLayoutManager(this, 3)
        recyclerDays.adapter = DayAdapter(this, Prefs.getDays(this))

        btnCancel.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Quit Challenge?")
                .setMessage("Do you really want to quit the challenge?")
                .setPositiveButton("Yes") { _, _ ->
                    Prefs.clear(this)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}
