package com.example.hobbify.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hobbify.EditDayActivity
import com.example.hobbify.R
import com.example.hobbify.model.DayModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DayAdapter(
    private val context: Context,
    private val list: MutableList<DayModel>
) : RecyclerView.Adapter<DayAdapter.VH>() {

    private val dateFormatter =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // ✅ TODAYDATE defined clearly at class level
    private val todayDate: String =
        dateFormatter.format(Calendar.getInstance().time)

    private fun showAddTimeDialog(position: Int) {
        val options = arrayOf("15 min", "30 min", "45 min", "1 hr")
        val values = arrayOf(15, 30, 45, 60)

        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle("Add Time")
            .setItems(options) { _, which ->
                val listFromPrefs = com.example.hobbify.utils.Prefs.getDays(context)

                // Add time
                listFromPrefs[position].minutes += values[which]

                // Save
                com.example.hobbify.utils.Prefs.saveDays(context, listFromPrefs)

                // Update local list
                list[position].minutes = listFromPrefs[position].minutes

                // 🔥 REAL-TIME UPDATE
                notifyItemChanged(position)
            }
            .show()
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val icon: ImageView = view.findViewById(R.id.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_day, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val day = list[position]

        holder.txtDate.text = day.date
        holder.txtTime.text = "${day.minutes} min"

        // Clear old icon
        holder.icon.setImageDrawable(null)

        // Tick / Cross logic
        when {
            day.minutes > 0 -> {
                holder.icon.setImageResource(R.drawable.ic_tick)
                holder.itemView.findViewById<View>(R.id.iconBox)
                    .setBackgroundResource(R.drawable.status_bg_success)
            }
            day.date < todayDate -> {
                holder.icon.setImageResource(R.drawable.ic_cross)
                holder.itemView.findViewById<View>(R.id.iconBox)
                    .setBackgroundResource(R.drawable.status_bg_error)
            }
            else -> {
                holder.icon.setImageDrawable(null)
                holder.itemView.findViewById<View>(R.id.iconBox)
                    .setBackgroundResource(R.drawable.status_bg_neutral)
            }
        }


        // Highlight today
        if (day.date == todayDate) {
            holder.itemView.setBackgroundResource(R.color.today)
        } else {
            holder.itemView.setBackgroundResource(android.R.color.transparent)
        }

        // Open edit screen for today or past
        holder.itemView.setOnClickListener {
            if (day.date <= todayDate) {
                val intent = Intent(context, EditDayActivity::class.java)
                intent.putExtra("index", position)
                context.startActivity(intent)
            }
        }
        holder.itemView.setOnLongClickListener {
            if (day.date == todayDate) {
                showAddTimeDialog(position)
                true
            } else {
                false
            }
        }

    }
}
