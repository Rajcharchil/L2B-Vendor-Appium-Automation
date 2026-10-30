package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

/**
 * Tell us edge — 9000000013. No force-stop (logs out). Catalog
 * {@code docs/tell-us-about-yourself-edge-cases.md} TU-E1–E10.
 */
@Epic("Vendor app")
@Feature("Tell us edge — fresh 9000000013")
public class TellUsEdgeTest extends TellUsBaseTest {

    @Test(priority = 1, description = "TU-E1: rotate landscape then portrait")
    @Severity(SeverityLevel.NORMAL)
    public void rotation() {
        TellUsPage page = reachTellUs();
        AndroidDriver android = (AndroidDriver) DriverManager.get();
        try {
            android.rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE);
            sleepQuiet(1200);
            android.rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT);
            sleepQuiet(1000);
        } catch (RuntimeException e) {
            Allure.parameter("rotateFail", e.getClass().getSimpleName());
        }
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(vendorPackage()).contains("l2b");
        softly.assertThat(page.isDisplayedNow() || classifyTellUsNow().startsWith("tellus"))
                .isTrue();
        softly.assertAll();
    }

    @Test(priority = 2, description = "TU-E2: background 2s recover Tell us")
    @Severity(SeverityLevel.CRITICAL)
    public void backgroundForeground() {
        TellUsPage page = reachTellUs();
        try {
            ((AndroidDriver) DriverManager.get()).runAppInBackground(java.time.Duration.ofSeconds(2));
        } catch (RuntimeException e) {
            Adb.run("shell", "input", "keyevent", "3");
            sleepQuiet(1500);
            ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package"));
        }
        sleepQuiet(1200);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(vendorPackage()).contains("l2b");
        softly.assertThat(page.isDisplayedNow()).as("Tell us after bg").isTrue();
        softly.assertAll();
    }

    @Test(priority = 3, description = "TU-E3: rapid company checkbox ×3")
    @Severity(SeverityLevel.NORMAL)
    public void rapidCompany() {
        TellUsPage page = reachTellUs();
        for (int i = 0; i < 3; i++) {
            try {
                page.tapCompanyCheckbox();
            } catch (RuntimeException ignored) {
            }
            sleepQuiet(250);
        }
        sleepQuiet(500);
        assertThat(vendorPackage()).contains("l2b");
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 4, description = "TU-E4: rapid open upload ×3")
    @Severity(SeverityLevel.NORMAL)
    public void rapidUpload() {
        TellUsPage page = reachTellUs();
        for (int i = 0; i < 3; i++) {
            try {
                page.tapUploadTpi();
            } catch (RuntimeException ignored) {
            }
            sleepQuiet(300);
        }
        sleepQuiet(800);
        Allure.parameter("afterRapidUpload", classifyTellUsNow());
        assertThat(page.isDisplayedNow() || page.isUploadSheetVisible()).isTrue();
        dismissUploadSheetIfPresent();
        assertThat(new TellUsPage().isDisplayedNow()).isTrue();
    }

    @Test(priority = 5, description = "TU-E5: offline still shows Tell us")
    @Severity(SeverityLevel.CRITICAL)
    public void offlineChrome() {
        TellUsPage page = reachTellUs();
        try {
            Adb.run("shell", "cmd", "connectivity", "airplane-mode", "enable");
            Adb.run("shell", "settings", "put", "global", "airplane_mode_on", "1");
            sleepQuiet(1000);
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(page.isDisplayedNow()).as("Tell us offline").isTrue();
            softly.assertThat(vendorPackage()).contains("l2b");
            softly.assertAll();
        } finally {
            try {
                Adb.run("shell", "cmd", "connectivity", "airplane-mode", "disable");
                Adb.run("shell", "settings", "put", "global", "airplane_mode_on", "0");
            } catch (RuntimeException ignored) {
            }
            Adb.ensureNetworkReady();
        }
    }

    @Test(priority = 6, description = "TU-E6: not launcher")
    @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() {
        reachTellUs();
        assertThat(classifyTellUsNow()).isNotEqualTo("launcher");
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 7, description = "TU-E7: Continue spam stays Tell us")
    @Severity(SeverityLevel.CRITICAL)
    public void continueSpam() {
        TellUsPage page = reachTellUs();
        for (int i = 0; i < 5; i++) {
            page.tapContinueOuter();
            sleepQuiet(200);
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 8, description = "TU-E8: long name paste then restore")
    @Severity(SeverityLevel.NORMAL)
    public void longName() {
        TellUsPage page = reachTellUs();
        page.setName("A".repeat(80));
        sleepQuiet(500);
        page.setName(TellUsPage.NAME_PREFILL);
        sleepQuiet(400);
        assertThat(page.isDisplayedNow()).isTrue();
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 9, description = "TU-E9: invalid email then restore")
    @Severity(SeverityLevel.NORMAL)
    public void invalidEmailRestore() {
        TellUsPage page = reachTellUs();
        page.setEmail("not-an-email");
        sleepQuiet(400);
        page.tapContinueOuter();
        sleepQuiet(400);
        page.setEmail(TellUsPage.EMAIL_PREFILL);
        sleepQuiet(400);
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 10, description = "TU-E10: no Log Out Confirm on Tell us")
    @Severity(SeverityLevel.CRITICAL)
    public void noLogoutConfirm() {
        reachTellUs();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(hasText("Log Out?")).isFalse();
        softly.assertThat(hasText("Are you sure you want to log out")).isFalse();
        softly.assertThat(hasText("Decline")).isFalse();
        softly.assertAll();
    }
}
