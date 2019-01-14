package com.xtagwgj.base.utils.permissionpage;

import android.content.Context;
import android.content.Intent;

public class MIUIPermission extends BrandPermission {

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     *
     * @param context
     * @throws Exception
     */
    @Override
    void go2PermissionPage(Context context) {
        Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.putExtra("extra_pkgname", context.getPackageName());

        try { // MIUI 8
            localIntent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.PermissionsEditorActivity");
            context.startActivity(localIntent);
        } catch (Exception e) {
            // MIUI 5/6/7
            localIntent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            context.startActivity(localIntent);
        }
    }
}
