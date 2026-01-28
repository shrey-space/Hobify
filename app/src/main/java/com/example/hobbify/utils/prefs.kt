package com.example.hobbify.utils

import android.content.Context
import com.example.hobbify.model.DayModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Prefs {

    private const val PREF_NAME = "hobby_prefs"
    private const val KEY_ACTIVE = "challenge_active"
    private const val KEY_DAYS = "days_data"

    fun isChallengeActive(context: Context): Boolean =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ACTIVE, false)

    fun setChallengeActive(context: Context, value: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ACTIVE, value)
            .apply()
    }

    fun saveDays(context: Context, list: List<DayModel>) {
        val json = Gson().toJson(list)
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_DAYS, json)
            .apply()
    }

    fun getDays(context: Context): MutableList<DayModel> {
        val json = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_DAYS, null) ?: return mutableListOf()

        val type = object : TypeToken<MutableList<DayModel>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}
