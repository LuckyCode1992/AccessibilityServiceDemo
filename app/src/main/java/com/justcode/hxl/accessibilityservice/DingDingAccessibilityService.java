package com.justcode.hxl.accessibilityservice;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

public class DingDingAccessibilityService extends AccessibilityService {
    public static boolean isStartSucc = false;
    public static boolean isInWork = false;
    public static boolean start = false;
    public static boolean isClickKao = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //手机的所有操作信息都会通过这个方法回调
        if (!start) {
            return;
        }

        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        //确保跳转到钉钉页面
        if (nodeInfo == null || !"com.alibaba.android.rimet".equals(nodeInfo.getPackageName().toString())) {
            if (!isStartSucc) {
                isStartSucc = Utils.doStartApplicationWithPackageName(getApplicationContext(), "com.alibaba.android.rimet");
            }

        }
        //进入考勤打卡所在的工作页面
        String resId = "com.alibaba.android.rimet:id/home_bottom_tab_button_work";
        AccessibilityNodeInfo info = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = info.findAccessibilityNodeInfosByViewId(resId);
        if (list == null || list.size() == 0) {
            Toast.makeText(getApplicationContext(), "未进入钉钉主页", Toast.LENGTH_LONG).show();
        } else {
            if (!isInWork) {
                isInWork = list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

        //寻找考勤打卡节点

//        if (event.getPackageName().equals("com.alibaba.android.rimet")){
//            List<AccessibilityNodeInfo> listFather =  getRootInActiveWindow().findAccessibilityNodeInfosByText("考勤打卡");
//            listFather.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }

        String resFatherId = "com.alibaba.android.rimet:id/h5_pc_container";
        AccessibilityNodeInfo infoFather = getRootInActiveWindow();
        List<AccessibilityNodeInfo> listFather = infoFather.findAccessibilityNodeInfosByViewId(resFatherId);
        if (listFather == null || listFather.size() == 0) {
            Toast.makeText(getApplicationContext(), "没有数据了", Toast.LENGTH_LONG).show();
        } else {
            AccessibilityNodeInfo nodeInfo1 = listFather.get(0);
            AccessibilityNodeInfo webview1 = nodeInfo1.getChild(0);
            AccessibilityNodeInfo be = webview1.getChild(0);
            AccessibilityNodeInfo webview2 = be.getChild(0);
            AccessibilityNodeInfo view1 = webview2.getChild(0);
            AccessibilityNodeInfo view2 = view1.getChild(3);
            AccessibilityNodeInfo view3 = view2.getChild(2);
            AccessibilityNodeInfo view4 = view3.getChild(0);
            if (!isClickKao)
                isClickKao =  view4.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

    }

    @Override
    public void onInterrupt() {
        isStartSucc = false;
        isInWork = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStartSucc = false;
        isInWork = false;
    }
}
