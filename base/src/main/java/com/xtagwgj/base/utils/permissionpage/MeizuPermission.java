package com.xtagwgj.base.utils.permissionpage;

import android.content.Context;
import android.content.Intent;

public class MeizuPermission extends BrandPermission {

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     *
     * @param context
     * @throws Exception
     */
    @Override
    void go2PermissionPage(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", context.getPackageName());
        context.startActivity(intent);
    }
}
