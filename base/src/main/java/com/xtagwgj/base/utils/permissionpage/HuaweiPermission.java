package com.xtagwgj.base.utils.permissionpage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class HuaweiPermission extends BrandPermission {

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     *
     * @param context
     * @throws Exception
     */
    @Override
    void go2PermissionPage(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //华为权限管理
        ComponentName comp = new ComponentName("com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainAccessActivity");
        intent.setComponent(comp);
        context.startActivity(intent);
    }
}
