package com.justcode.hxl.accessibilityservice;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public class DingDingAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //手机的所有操作信息都会通过这个方法回调
    }

    @Override
    public void onInterrupt() {

    }
}
