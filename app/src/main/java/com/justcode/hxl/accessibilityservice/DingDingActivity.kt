package com.justcode.hxl.accessibilityservice

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast

class DingDingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ding_ding)
        openAccessSettingOn()
    }

    private fun openAccessSettingOn() {
        if (!isAccessibilitySettingsOn(applicationContext)) {
            Toast.makeText(applicationContext, "请开启辅助服务", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }else{
            DingDingAccessibilityService.start = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DingDingAccessibilityService.start = false
        DingDingAccessibilityService.isInWork = false
        DingDingAccessibilityService.isStartSucc = false
        DingDingAccessibilityService.isClickKao = false
    }

    private fun isAccessibilitySettingsOn(mContext: Context): Boolean {
        var accessibilityEnabled = 0
        // TestService为对应的服务
        val service = packageName + "/" + DingDingAccessibilityService::class.java.canonicalName
        // com.z.buildingaccessibilityservices/android.accessibilityservice.AccessibilityService
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.applicationContext.contentResolver,
                android.provider.Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }

        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')

        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                mContext.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()

                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false

    }
}
