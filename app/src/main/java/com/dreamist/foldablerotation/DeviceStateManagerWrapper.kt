package com.dreamist.foldablerotation

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager

class DeviceStateManagerWrapper(private val context: Context) {

    private var isListening = false

    // 每隔1000毫秒检查折叠状态
    suspend fun startListening(callback: (Boolean) -> Unit) {
        isListening = true
        while (isListening) {
            val isFolded = checkFoldState() // Replace with actual API
            Log.d("DeviceStateManagerWrapper", "isFolded: $isFolded")
            callback(isFolded)
            kotlinx.coroutines.delay(1000) // Polling interval
        }
    }

    suspend fun stopListening() {
        isListening = false
    }

    private suspend fun checkFoldState(): Boolean {
        // 检查屏幕折叠状态
        // 获取屏幕分辨率，计算宽高比判定当前处于折叠态还是展开态
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels.toFloat()
        val screenHeight = displayMetrics.heightPixels.toFloat()
        Log.d("DeviceStateManagerWrapper", "screenWidth: $screenWidth, screenHeight: $screenHeight")

        // 计算宽高比
        val aspectRatio = screenHeight / screenWidth

        // 判断折叠状态（宽高比接近 21:9）
        val isFolded = (aspectRatio in 1.8..2.4)

        // 判断展开状态（宽高比接近 1:1）
        val isExpanded = (aspectRatio in 0.9..1.2)

        // 返回结果
        return when {
            isFolded -> {
                // 折叠状态
                true
            }
            isExpanded -> {
                // 展开状态
                false
            }
            else -> {
                // 其他未知状态，默认为折叠
                true
            }
        }
    }
}