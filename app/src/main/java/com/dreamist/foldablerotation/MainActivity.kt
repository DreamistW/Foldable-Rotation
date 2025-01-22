package com.dreamist.foldablerotation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 界面控件
        val toggleServiceSwitch: Switch = findViewById(R.id.switch_toggle_service)
        val disableBatteryOptimizationButton: Button = findViewById(R.id.btn_disable_battery_optimization)
        val disableBatteryOptimizationText: TextView = findViewById(R.id.tv_battery_optimization_status)
        val versionText: TextView = findViewById(R.id.tv_app_version)
        // 配置信息
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        // 获取和显示版本号
        val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0);
        versionText.text = getString(R.string.app_version, packageInfo.versionName)

        // 初始化开关状态
        val isServiceEnabled = sharedPref.getBoolean("service_enabled", false)
        toggleServiceSwitch.isChecked = isServiceEnabled
        // 自动启动服务
        handleServiceState(isServiceEnabled, sharedPref)

        // 监听服务开关状态变化
        toggleServiceSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (Settings.System.canWrite(this)) {
                handleServiceState(isChecked, sharedPref)
            } else {
                toggleServiceSwitch.isChecked = !isChecked
                showGrantPermissionDialog()
            }
        }

        // 监听禁用电池优化按钮点击事件
        disableBatteryOptimizationButton.setOnClickListener {
            showDisableBatteryOptimizationDialog()
        }

        // 检查电池优化权限，更新按钮状态
        updateBatteryOptimizationButton(disableBatteryOptimizationButton, disableBatteryOptimizationText)
    }

    // 应用程序时从后台切到前台后检查权限
    override fun onResume() {
        super.onResume()
        val disableBatteryOptimizationButton: Button = findViewById(R.id.btn_disable_battery_optimization)
        val disableBatteryOptimizationText: TextView = findViewById(R.id.tv_battery_optimization_status)
        updateBatteryOptimizationButton(disableBatteryOptimizationButton, disableBatteryOptimizationText)
    }

    // 开启或停止服务
    private fun handleServiceState(isChecked: Boolean, sharedPref: android.content.SharedPreferences) {
        // 更新配置信息
        sharedPref.edit().putBoolean("service_enabled", isChecked).apply()
        val message = if (isChecked) {
            startService(Intent(this, FoldableStateService::class.java))
            getString(R.string.start_service_msg)
        } else {
            stopService(Intent(this, FoldableStateService::class.java))
            getString(R.string.stop_service_msg)
        }
        Snackbar.make(findViewById(R.id.switch_toggle_service), message, Snackbar.LENGTH_SHORT).show()
    }

    // 显示更改系统设置授权提示
//    private fun showGrantPermissionSnackbar() {
//        Snackbar.make(
//            findViewById(R.id.switch_toggle_service),
//            getString(R.string.settings_permission_hint),
//            Snackbar.LENGTH_LONG
//        ).setAction(getString(R.string.grant_system_settings_btn)) {
//            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
//                data = Uri.parse("package:$packageName")
//            }
//            startActivity(intent)
//        }.show()
//    }

    // 显示或者隐藏禁用电池优化按钮
    private fun updateBatteryOptimizationButton(button: Button, text: TextView) {
        val powerManager = getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
        val batteryOpStatus = powerManager.isIgnoringBatteryOptimizations(packageName)
        button.visibility = if (batteryOpStatus) Button.GONE else Button.VISIBLE
        text.text = if(batteryOpStatus) getString(R.string.disable_battery_op_hint_true) else getString(R.string.disable_battery_op_hint_false)
    }

    // 显示禁用电池优化弹框
    private fun showDisableBatteryOptimizationDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.disable_battery_op_dialog_title))
            setMessage(getString(R.string.disable_battery_op_dialog_content))
            setPositiveButton(getString(R.string.ok_btn)) { _, _ ->
                val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                startActivity(intent)
            }
            setNegativeButton(getString(R.string.cancel_btn), null)
            create()
            show()
        }
    }

    // 显示修改系统设置授权弹框
    private fun showGrantPermissionDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.grant_permission_dialog_title))
            setMessage(getString(R.string.grant_permission_dialog_content))
            setPositiveButton(getString(R.string.ok_btn)) { _, _ ->
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
            }
            setNegativeButton(getString(R.string.cancel_btn), null)
            create()
            show()
        }
    }
}
