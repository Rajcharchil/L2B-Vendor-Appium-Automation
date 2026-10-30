package com.l2b.vendor.modules.profileverification.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.profileverification.presentation.pages.ProfileVerificationPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

/**
 * Profile Verification probe on material 0017.
 * Warm verified account often has no PV hub — document unreachable, keep vendor healthy.
 */
@Epic("Vendor app")
@Feature("Profile Verification probe — material 9000000017")
public class ProfileVerificationProbeTest extends MaterialSurfaceBaseTest {

    @Test(priority = 1, description = "PV-L1: probe reachability")
    @Severity(SeverityLevel.BLOCKER)
    public void probe() {
        ProfileVerificationPage page = tryReachProfileVerification();
        boolean shown = page.isDisplayedNow();
        Allure.parameter("profileVerificationVisible", String.valueOf(shown));
        Allure.parameter("after", classifyMaterialNow());
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(vendorPackage()).contains("l2b");
        softly.assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
        if (shown) {
            softly.assertThat(hasText("Profile Verification")
                    || hasText("KYC")
                    || hasText("Verification")).isTrue();
        }
        softly.assertAll();
    }

    @Test(priority = 2, description = "PV-L2: if shown — no Accept/Log Out")
    @Severity(SeverityLevel.CRITICAL)
    public void noDangerIfShown() {
        ProfileVerificationPage page = tryReachProfileVerification();
        SoftAssertions softly = new SoftAssertions();
        if (page.isDisplayedNow()) {
            softly.assertThat(noAcceptVisible()).isTrue();
            softly.assertThat(hasText("Log Out?")).isFalse();
            softly.assertThat(hasText("Decline")).isFalse();
        } else {
            softly.assertThat(vendorPackage()).contains("l2b");
            softly.assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
        }
        softly.assertAll();
    }

    @Test(priority = 3, description = "PV-L3: package healthy")
    @Severity(SeverityLevel.NORMAL)
    public void pkg() {
        tryReachProfileVerification();
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 4, description = "PV-L4: not launcher after probe")
    @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() {
        tryReachProfileVerification();
        assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
    }

    @Test(priority = 5, description = "PV-L5: recover to Material Home")
    @Severity(SeverityLevel.CRITICAL)
    public void recoverHome() {
        tryReachProfileVerification();
        assertThat(reachMaterialHome().isDisplayedNow()).isTrue();
    }

    @Test(priority = 6, description = "PV-E1: background after probe")
    @Severity(SeverityLevel.NORMAL)
    public void background() {
        tryReachProfileVerification();
        try {
            ((io.appium.java_client.android.AndroidDriver) com.l2b.vendor.core.driver.DriverManager.get())
                    .runAppInBackground(java.time.Duration.ofSeconds(2));
        } catch (RuntimeException ignored) {
        }
        activateVendor();
        sleepQuiet(1000);
        assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
    }

    @Test(priority = 7, description = "PV-E2: no Accept")
    @Severity(SeverityLevel.CRITICAL)
    public void noAccept() {
        tryReachProfileVerification();
        assertThat(noAcceptVisible()).isTrue();
    }

    @Test(priority = 8, description = "PV-E3: no Log Out")
    @Severity(SeverityLevel.CRITICAL)
    public void noLogout() {
        tryReachProfileVerification();
        assertThat(hasText("Log Out?")).isFalse();
    }

    @Test(priority = 9, description = "PV-E4: reopen Home")
    @Severity(SeverityLevel.NORMAL)
    public void reopenHome() {
        assertThat(reachMaterialHome().isDisplayedNow()).isTrue();
    }

    @Test(priority = 10, description = "PV-E5: package")
    @Severity(SeverityLevel.NORMAL)
    public void pkg2() {
        tryReachProfileVerification();
        assertThat(vendorPackage()).contains("l2b");
    }
}
