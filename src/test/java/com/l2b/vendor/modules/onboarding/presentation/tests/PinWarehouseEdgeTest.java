package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.onboarding.presentation.pages.PinWarehousePage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Pin warehouse edge — 9000000013")
public class PinWarehouseEdgeTest extends RegistrationBaseTest {

    @Test(priority = 1, description = "PW-E1: rotation")
    @Severity(SeverityLevel.NORMAL)
    public void rotation() {
        reachPinWarehouse();
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

    @Test(priority = 2, description = "PW-E2: background")
    @Severity(SeverityLevel.CRITICAL)
    public void background() {
        reachPinWarehouse();
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
        Allure.parameter("afterBg", classifyNow());
        softly.assertThat(classifyNow()).as("not launcher after bg").isNotEqualTo("launcher");
        softly.assertAll();
    }

    @Test(priority = 3, description = "PW-E3: offline")
    @Severity(SeverityLevel.CRITICAL)
    public void offline() {
        PinWarehousePage page = reachPinWarehouse();
        try {
            Adb.run("shell", "cmd", "connectivity", "airplane-mode", "enable");
            sleepQuiet(700);
            assertThat(page.isDisplayedNow() || hasText("Tell us about yourself")).isTrue();
        } finally {
            try {
                Adb.run("shell", "cmd", "connectivity", "airplane-mode", "disable");
            } catch (RuntimeException ignored) {
            }
            Adb.ensureNetworkReady();
        }
    }

    @Test(priority = 4, description = "PW-E4: not launcher")
    @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() {
        reachPinWarehouse();
        assertThat(classifyNow()).isNotEqualTo("launcher");
    }

    @Test(priority = 5, description = "PW-E5: no Accept")
    @Severity(SeverityLevel.CRITICAL)
    public void noAccept() {
        reachPinWarehouse();
        assertThat(noAcceptVisible()).isTrue();
    }

    @Test(priority = 6, description = "PW-E6: no Log Out")
    @Severity(SeverityLevel.CRITICAL)
    public void noLogout() {
        reachPinWarehouse();
        assertThat(hasText("Log Out?")).isFalse();
    }

    @Test(priority = 7, description = "PW-E7: rapid Confirm")
    @Severity(SeverityLevel.NORMAL)
    public void rapidConfirm() {
        PinWarehousePage page = reachPinWarehouse();
        for (int i = 0; i < 4; i++) {
            if (page.isDisplayedNow()) {
                page.tapConfirm();
                sleepQuiet(300);
            } else {
                reachPinWarehouse();
            }
        }
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 8, description = "PW-E8: map unavailable stays usable")
    @Severity(SeverityLevel.CRITICAL)
    public void mapUnavailableOk() {
        PinWarehousePage page = reachPinWarehouse();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(page.isConfirmVisible()).isTrue();
        softly.assertThat(page.isMapUnavailableVisible() || page.isCoordsVisible()).isTrue();
        softly.assertAll();
    }

    @Test(priority = 9, description = "PW-E9: Close recover")
    @Severity(SeverityLevel.NORMAL)
    public void closeRecover() {
        PinWarehousePage page = reachPinWarehouse();
        page.tapClose();
        sleepQuiet(800);
        assertThat(reachPinWarehouse().isDisplayedNow()).isTrue();
    }

    @Test(priority = 10, description = "PW-E10: reopen")
    @Severity(SeverityLevel.NORMAL)
    public void reopen() {
        assertThat(reachPinWarehouse().isDisplayedNow()).isTrue();
    }
}
