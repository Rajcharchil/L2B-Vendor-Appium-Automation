package com.l2b.vendor.modules.inventory.domain;

import com.l2b.vendor.environment.Config;
import com.l2b.vendor.environment.SuiteEnvironment;

/** Material Inventory — 9000000017. */
public final class InventoryEnvironment {

    private InventoryEnvironment() {
    }

    public static void prepareSuite() {
        SuiteEnvironment.prepare();
    }

    public static boolean noReset() {
        return true;
    }

    public static boolean newSessionPerMethod() {
        return false;
    }

    public static String phone() {
        return Config.get("user.material.phone", "9000000017");
    }

    public static String otp() {
        return "1234";
    }
}
