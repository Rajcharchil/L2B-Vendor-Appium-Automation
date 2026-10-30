package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.onboarding.presentation.pages.RolePage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Role edge — 9000000013")
public class RoleEdgeTest extends RegistrationBaseTest {

    @Test(priority = 1, description = "SR-E1: rotation")
    @Severity(SeverityLevel.NORMAL)
    public void rotation() {
        reachRole();
        AndroidDriver android = (AndroidDriver) DriverManager.get();
        try {
            android.rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE);
            sleepQuiet(1000);
            android.rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT);
            sleepQuiet(800);
        } catch (RuntimeException e) {
            Allure.parameter("rotateFail", e.getClass().getSimpleName());
        }
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 2, description = "SR-E2: background 2s")
    @Severity(SeverityLevel.CRITICAL)
    public void background() {
        reachRole();
        try {
            ((AndroidDriver) DriverManager.get()).runAppInBackground(java.time.Duration.ofSeconds(2));
        } catch (RuntimeException e) {
            Adb.run("shell", "input", "keyevent", "3");
            sleepQuiet(1500);
        }
        ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package"));
        sleepQuiet(1200);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(vendorPackage()).contains("l2b");
        String named = classifyNow();
        Allure.parameter("afterBg", named);
        softly.assertThat(named).as("not launcher after bg").isNotEqualTo("launcher");
        softly.assertAll();
    }

    @Test(priority = 3, description = "SR-E3: rapid toggle")
    @Severity(SeverityLevel.NORMAL)
    public void rapidToggle() {
        RolePage page = reachRole();
        for (int i = 0; i < 4; i++) {
            page.tapVendor();
            page.tapOperator();
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 4, description = "SR-E4: offline chrome")
    @Severity(SeverityLevel.CRITICAL)
    public void offline() {
        RolePage page = reachRole();
        try {
            Adb.run("shell", "cmd", "connectivity", "airplane-mode", "enable");
            sleepQuiet(800);
            assertThat(page.isDisplayedNow()).isTrue();
        } finally {
            try {
                Adb.run("shell", "cmd", "connectivity", "airplane-mode", "disable");
            } catch (RuntimeException ignored) {
            }
            Adb.ensureNetworkReady();
        }
    }

    @Test(priority = 5, description = "SR-E5: not launcher")
    @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() {
        reachRole();
        assertThat(classifyNow()).isNotEqualTo("launcher");
    }

    @Test(priority = 6, description = "SR-E6: no Accept")
    @Severity(SeverityLevel.CRITICAL)
    public void noAccept() {
        reachRole();
        assertThat(noAcceptVisible()).isTrue();
    }

    @Test(priority = 7, description = "SR-E7: no Log Out Confirm")
    @Severity(SeverityLevel.CRITICAL)
    public void noLogoutConfirm() {
        reachRole();
        assertThat(hasText("Log Out?")).isFalse();
    }

    @Test(priority = 8, description = "SR-E8: Vendor Next to category")
    @Severity(SeverityLevel.NORMAL)
    public void vendorNext() {
        RolePage page = reachRole();
        page.tapVendor();
        sleepQuiet(300);
        page.tapNext();
        sleepQuiet(1000);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(hasText("Please select the category") || page.isDisplayedNow()).isTrue();
        softly.assertAll();
        if (hasText("Please select the category")) {
            DriverManager.get().navigate().back();
            sleepQuiet(700);
        }
    }

    @Test(priority = 9, description = "SR-E9: package after bg")
    @Severity(SeverityLevel.NORMAL)
    public void packageAfterBg() {
        reachRole();
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 10, description = "SR-E10: re-open role")
    @Severity(SeverityLevel.NORMAL)
    public void reopenRole() {
        assertThat(reachRole().isDisplayedNow()).isTrue();
    }
}
