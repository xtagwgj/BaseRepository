package com.xtagwgj.base.utils.permissionpage;

import android.content.Context;
import android.os.Build;

/**
 * 跳转到权限配置页面的工具
 *
 * @author xtagwgj
 */
public class PermissionPageUtil {

    public void jump2Page(Context context) {
        String brand = Build.BRAND;

        BrandPermission brandPermission;

        if (brand == null) {
            brandPermission = new NormalPermission();
        } else if (brand.equalsIgnoreCase("redmi") || brand.equalsIgnoreCase("xiaomi")) {
            brandPermission = new MIUIPermission();
        } else if (brand.equalsIgnoreCase("meizu")) {
            brandPermission = new MeizuPermission();
        } else if (brand.equalsIgnoreCase("huawei") || brand.equalsIgnoreCase("honor")) {
            brandPermission = new HuaweiPermission();
        } else {
            brandPermission = new NormalPermission();
        }

        try {
            brandPermission.go2PermissionPage(context);
        } catch (Exception e) {
            e.printStackTrace();
            new NormalPermission().go2PermissionPage(context);
        }

    }

}
