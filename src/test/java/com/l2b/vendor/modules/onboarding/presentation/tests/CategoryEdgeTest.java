package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.onboarding.presentation.pages.CategoryPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Category edge — Materials 9000000013")
public class CategoryEdgeTest extends RegistrationBaseTest {

    @Test(priority = 1, description = "SC-E1: rotation")
    @Severity(SeverityLevel.NORMAL)
    public void rotation() {
        reachCategory();
        AndroidDriver android = (AndroidDriver) DriverManager.get();
        try {
            android.rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE);
            sleepQuiet(900);
            android.rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT);
            sleepQuiet(700);
        } catch (RuntimeException e) {
            Allure.parameter("rotateFail", e.getClass().getSimpleName());
        }
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 2, description = "SC-E2: background")
    @Severity(SeverityLevel.CRITICAL)
    public void background() {
        reachCategory();
        try {
            ((AndroidDriver) DriverManager.get()).runAppInBackground(java.time.Duration.ofSeconds(2));
        } catch (RuntimeException e) {
            Adb.run("shell", "input", "keyevent", "3");
            sleepQuiet(1200);
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

    @Test(priority = 3, description = "SC-E3: offline")
    @Severity(SeverityLevel.CRITICAL)
    public void offline() {
        CategoryPage page = reachCategory();
        try {
            Adb.run("shell", "cmd", "connectivity", "airplane-mode", "enable");
            sleepQuiet(700);
            assertThat(page.isDisplayedNow()).isTrue();
        } finally {
            try {
                Adb.run("shell", "cmd", "connectivity", "airplane-mode", "disable");
            } catch (RuntimeException ignored) {
            }
            Adb.ensureNetworkReady();
        }
    }

    @Test(priority = 4, description = "SC-E4: not launcher")
    @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() {
        reachCategory();
        assertThat(classifyNow()).isNotEqualTo("launcher");
    }

    @Test(priority = 5, description = "SC-E5: no Accept")
    @Severity(SeverityLevel.CRITICAL)
    public void noAccept() {
        reachCategory();
        assertThat(noAcceptVisible()).isTrue();
    }

    @Test(priority = 6, description = "SC-E6: no Log Out")
    @Severity(SeverityLevel.CRITICAL)
    public void noLogout() {
        reachCategory();
        assertThat(hasText("Log Out?")).isFalse();
    }

    @Test(priority = 7, description = "SC-E7: Materials after Rental")
    @Severity(SeverityLevel.NORMAL)
    public void materialsAfterRental() {
        CategoryPage page = reachCategory();
        page.tapRental();
        sleepQuiet(300);
        page.tapMaterials();
        sleepQuiet(400);
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 8, description = "SC-E8: rapid Continue after Materials")
    @Severity(SeverityLevel.NORMAL)
    public void rapidContinue() {
        CategoryPage page = reachCategory();
        page.tapMaterials();
        for (int i = 0; i < 3; i++) {
            try {
                page.tapContinue();
            } catch (RuntimeException ignored) {
            }
            sleepQuiet(250);
        }
        sleepQuiet(800);
        assertThat(vendorPackage()).contains("l2b");
        if (hasText("Tell us about yourself")) {
            DriverManager.get().navigate().back();
            sleepQuiet(700);
        }
    }

    @Test(priority = 9, description = "SC-E9: package")
    @Severity(SeverityLevel.NORMAL)
    public void pkg() {
        reachCategory();
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 10, description = "SC-E10: recover category")
    @Severity(SeverityLevel.NORMAL)
    public void recover() {
        assertThat(reachCategory().isDisplayedNow()).isTrue();
    }
}
