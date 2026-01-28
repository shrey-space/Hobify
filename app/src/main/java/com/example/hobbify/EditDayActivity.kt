package com.example.hobbify

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hobbify.utils.Prefs

class EditDayActivity : AppCompatActivity() {

    private var index = 0
    private val values = arrayOf(15, 30, 45, 60)

    private lateinit var txtTime: TextView
    private lateinit var btnAdd: Button
    private lateinit var btnRemove: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_day)

        // Bind views
        txtTime = findViewById(R.id.txtTime)
        btnAdd = findViewById(R.id.btnAdd)
        btnRemove = findViewById(R.id.btnRemove)

        index = intent.getIntExtra("index", 0)

        val list = Prefs.getDays(this)
        val day = list[index]

        txtTime.text = "${day.minutes} min"

        btnAdd.setOnLongClickListener {
            showDialog(add = true)
            true
        }

        btnRemove.setOnLongClickListener {
            showDialog(add = false)
            true
        }
    }

    private fun showDialog(add: Boolean) {
        AlertDialog.Builder(this)
            .setTitle(if (add) "Add Time" else "Remove Time")
            .setItems(arrayOf("15 min", "30 min", "45 min", "60 min")) { _, i ->
                val list = Prefs.getDays(this)

                if (add) {
                    list[index].minutes += values[i]
                } else {
                    list[index].minutes =
                        (list[index].minutes - values[i]).coerceAtLeast(0)
                }

                Prefs.saveDays(this, list)
                txtTime.text = "${list[index].minutes} min"
            }
            .show()
    }
}
