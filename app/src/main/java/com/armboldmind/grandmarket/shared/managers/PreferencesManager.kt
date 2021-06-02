package com.armboldmind.grandmarket.shared.managers

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class PreferencesManager(private val context: Context) {
    companion object {
        private val PREF_NAME = "PREF_CONFIG"
    }

    val instance: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun <T> saveByKey(key: String, value: T) = when (value) {
        is String -> instance.edit()
            .putString(key, value)
            .apply()
        is Int -> instance.edit()
            .putInt(key, value)
            .apply()
        is Long -> instance.edit()
            .putLong(key, value)
            .apply()
        is Float -> instance.edit()
            .putFloat(key, value)
            .apply()
        is Boolean -> instance.edit()
            .putBoolean(key, value)
            .apply()
        else -> instance.edit()
            .putString(key, Gson().toJson(value))
            .apply()
    }

    inline fun <reified T> findByKey(key: String): T {
        return when (T::class) {
            Int::class -> instance.getInt(key, -1) as T
            String::class -> instance.getString(key, "") as T
            Boolean::class -> instance.getBoolean(key, false) as T
            Float::class -> instance.getFloat(key, -1f) as T
            Long::class -> instance.getLong(key, -1) as T
            else -> Gson().fromJson(instance.getString(key, ""), T::class.java)
        }
    }

    fun remove(key: String) {
        instance.edit()
            .remove(key)
            .apply()
    }

    fun clear() {
        instance.edit()
            .clear()
            .apply()
    }
}