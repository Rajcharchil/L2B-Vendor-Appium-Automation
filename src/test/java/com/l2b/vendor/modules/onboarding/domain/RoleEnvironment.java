package com.l2b.vendor.modules.onboarding.domain;

import com.l2b.vendor.environment.Config;
import com.l2b.vendor.environment.SuiteEnvironment;

/** Select your role — registration leftover 9000000013. */
public final class RoleEnvironment {

    private RoleEnvironment() {
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
        return Config.get("user.fresh.phone", "9000000013");
    }

    public static String otp() {
        String v = System.getProperty("qa.otp", System.getenv().getOrDefault("L2B_QA_OTP", "1234"));
        if (v == null || v.isBlank()) {
            v = Config.get("qa.otp", "1234");
        }
        return (v == null || v.isBlank()) ? "1234" : v;
    }
}
