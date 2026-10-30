package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsMaterialPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Tell us Materials edge — 9000000013")
public class TellUsMaterialEdgeTest extends RegistrationBaseTest {

    @Test(priority = 1, description = "TM-E1: rotation")
    @Severity(SeverityLevel.NORMAL)
    public void rotation() {
        reachTellUsMaterial();
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

    @Test(priority = 2, description = "TM-E2: background")
    @Severity(SeverityLevel.CRITICAL)
    public void background() {
        reachTellUsMaterial();
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

    @Test(priority = 3, description = "TM-E3: offline")
    @Severity(SeverityLevel.CRITICAL)
    public void offline() {
        TellUsMaterialPage page = reachTellUsMaterial();
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

    @Test(priority = 4, description = "TM-E4: not launcher")
    @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() {
        reachTellUsMaterial();
        assertThat(classifyNow()).isNotEqualTo("launcher");
    }

    @Test(priority = 5, description = "TM-E5: rapid company type")
    @Severity(SeverityLevel.NORMAL)
    public void rapidCompany() {
        TellUsMaterialPage page = reachTellUsMaterial();
        for (int i = 0; i < 3; i++) {
            try {
                page.tapCompanyType();
            } catch (RuntimeException ignored) {
            }
            sleepQuiet(250);
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 6, description = "TM-E6: rapid Continue")
    @Severity(SeverityLevel.CRITICAL)
    public void rapidContinue() {
        TellUsMaterialPage page = reachTellUsMaterial();
        for (int i = 0; i < 5; i++) {
            page.tapContinue();
            sleepQuiet(200);
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 7, description = "TM-E7: no Log Out")
    @Severity(SeverityLevel.CRITICAL)
    public void noLogout() {
        reachTellUsMaterial();
        assertThat(hasText("Log Out?")).isFalse();
    }

    @Test(priority = 8, description = "TM-E8: no Accept")
    @Severity(SeverityLevel.CRITICAL)
    public void noAccept() {
        reachTellUsMaterial();
        assertThat(noAcceptVisible()).isTrue();
    }

    @Test(priority = 9, description = "TM-E9: map Back recover")
    @Severity(SeverityLevel.NORMAL)
    public void mapBack() {
        TellUsMaterialPage page = reachTellUsMaterial();
        page.tapPinOnMap();
        sleepQuiet(1200);
        DriverManager.get().navigate().back();
        sleepQuiet(900);
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 10, description = "TM-E10: reopen")
    @Severity(SeverityLevel.NORMAL)
    public void reopen() {
        assertThat(reachTellUsMaterial().isDisplayedNow()).isTrue();
    }
}
