package com.coffeebliss.app.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var loggedInMemberId: Long?
        get() {
            val id = prefs.getLong(KEY_MEMBER_ID, -1L)
            return if (id == -1L) null else id
        }
        set(value) {
            prefs.edit().apply {
                if (value == null) {
                    remove(KEY_MEMBER_ID)
                } else {
                    putLong(KEY_MEMBER_ID, value)
                }
            }.apply()
        }

    fun logout() {
        loggedInMemberId = null
    }

    companion object {
        private const val PREFS_NAME = "coffee_bliss_session"
        private const val KEY_MEMBER_ID = "member_id"
    }
}
