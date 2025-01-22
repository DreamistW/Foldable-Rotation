package com.dreamist.foldablerotation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val isServiceEnabled = sharedPref.getBoolean("service_enabled", false)
            if (isServiceEnabled) {
                val serviceIntent = Intent(context, FoldableStateService::class.java)
                context.startService(serviceIntent) // For Android below O
                Log.d("BootCompletedReceiver", "Service started after boot")
            }
        }
    }
}
