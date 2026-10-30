package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsMaterialPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Tell us Materials landing — 9000000013")
public class TellUsMaterialLandingTest extends RegistrationBaseTest {

    @Test(priority = 1, description = "TM-L1: opens")
    @Severity(SeverityLevel.BLOCKER)
    public void opens() {
        assertThat(reachTellUsMaterial().isDisplayedNow()).isTrue();
    }

    @Test(priority = 2, description = "TM-L2: prefill name/email")
    @Severity(SeverityLevel.CRITICAL)
    public void prefill() {
        TellUsMaterialPage page = reachTellUsMaterial();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(page.isNamePrefillVisible() || hasText("Your name")).as("name").isTrue();
        softly.assertThat(page.isEmailPrefillVisible() || hasText("Your email")).as("email").isTrue();
        softly.assertAll();
    }

    @Test(priority = 3, description = "TM-L3: Type of company")
    @Severity(SeverityLevel.CRITICAL)
    public void companyType() {
        assertThat(reachTellUsMaterial().isCompanyTypeVisible()).isTrue();
    }

    @Test(priority = 4, description = "TM-L4: Warehouse")
    @Severity(SeverityLevel.CRITICAL)
    public void warehouse() {
        assertThat(reachTellUsMaterial().isWarehouseVisible()).isTrue();
    }

    @Test(priority = 5, description = "TM-L5: Use current + Pin")
    @Severity(SeverityLevel.CRITICAL)
    public void locationActions() {
        TellUsMaterialPage page = reachTellUsMaterial();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(page.isUseCurrentVisible()).isTrue();
        softly.assertThat(page.isPinMapVisible()).isTrue();
        softly.assertAll();
    }

    @Test(priority = 6, description = "TM-L6: Continue")
    @Severity(SeverityLevel.CRITICAL)
    public void continueVisible() {
        assertThat(reachTellUsMaterial().isContinueVisible()).isTrue();
    }

    @Test(priority = 7, description = "TM-L7: Back")
    @Severity(SeverityLevel.CRITICAL)
    public void back() {
        assertThat(reachTellUsMaterial().isBackVisible()).isTrue();
    }

    @Test(priority = 8, description = "TM-L8: package")
    @Severity(SeverityLevel.NORMAL)
    public void pkg() {
        reachTellUsMaterial();
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 9, description = "TM-L9: no Accept/Decline/Log Out")
    @Severity(SeverityLevel.CRITICAL)
    public void noDanger() {
        reachTellUsMaterial();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(hasText("Decline")).isFalse();
        softly.assertThat(noAcceptVisible()).isTrue();
        softly.assertThat(hasText("Log Out?")).isFalse();
        softly.assertAll();
    }

    @Test(priority = 10, description = "TM-L10: Continue incomplete stays")
    @Severity(SeverityLevel.CRITICAL)
    public void continueStays() {
        TellUsMaterialPage page = reachTellUsMaterial();
        page.tapContinue();
        sleepQuiet(700);
        assertThat(page.isDisplayedNow()).isTrue();
    }
}
