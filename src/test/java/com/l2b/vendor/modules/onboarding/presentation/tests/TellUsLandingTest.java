package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

/**
 * Tell us landing — 9000000013. Catalog {@code docs/tell-us-about-yourself-edge-cases.md}.
 *
 * <p><b>Happy Path (TU-L1–L10)</b>
 */
@Epic("Vendor app")
@Feature("Tell us landing — fresh 9000000013")
public class TellUsLandingTest extends TellUsBaseTest {

    @Test(priority = 1, description = "TU-L1: land on Tell us after 0013")
    @Severity(SeverityLevel.BLOCKER)
    public void tellUsOpens() {
        TellUsPage page = reachTellUs();
        page.attachScreenshot("tu-l1");
        assertThat(page.isDisplayedNow()).as("Tell us open").isTrue();
    }

    @Test(priority = 2, description = "TU-L2: title + Back")
    @Severity(SeverityLevel.CRITICAL)
    public void titleAndBack() {
        TellUsPage page = reachTellUs();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(page.isTitleVisible()).as("title").isTrue();
        softly.assertThat(page.isBackVisible()).as("Back").isTrue();
        softly.assertAll();
    }

    @Test(priority = 3, description = "TU-L3: prefill name and email")
    @Severity(SeverityLevel.CRITICAL)
    public void prefillNameEmail() {
        TellUsPage page = reachTellUs();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(page.isNamePrefillVisible()).as("waardian tech").isTrue();
        softly.assertThat(page.isEmailPrefillVisible()).as("manju@gmail.com").isTrue();
        softly.assertAll();
    }

    @Test(priority = 4, description = "TU-L4: TPI upload row + hint")
    @Severity(SeverityLevel.CRITICAL)
    public void tpiRow() {
        TellUsPage page = reachTellUs();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(page.isTpiRowVisible()).as("TPI").isTrue();
        softly.assertThat(page.isTpiHintVisible()).as("6mb jpeg/pdf").isTrue();
        softly.assertAll();
    }

    @Test(priority = 5, description = "TU-L5: company checkbox copy")
    @Severity(SeverityLevel.CRITICAL)
    public void companyCopy() {
        assertThat(reachTellUs().isCompanyCopyVisible()).isTrue();
    }

    @Test(priority = 6, description = "TU-L6: Continue visible")
    @Severity(SeverityLevel.CRITICAL)
    public void continueVisible() {
        assertThat(reachTellUs().isContinueVisible()).isTrue();
    }

    @Test(priority = 7, description = "TU-L7: package stays Vendor")
    @Severity(SeverityLevel.NORMAL)
    public void stayVendorPackage() {
        reachTellUs();
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 8, description = "TU-L8: operator_basic_info root")
    @Severity(SeverityLevel.NORMAL)
    public void progressRoot() {
        TellUsPage page = reachTellUs();
        Allure.parameter("root", String.valueOf(page.isDisplayedNow()));
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 9, description = "TU-L9: no Accept/Decline/Log Out")
    @Severity(SeverityLevel.CRITICAL)
    public void noAcceptDeclineLogout() {
        reachTellUs();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(hasText("Decline")).as("no Decline").isFalse();
        softly.assertThat(noAcceptVisible()).as("no Accept").isTrue();
        softly.assertThat(hasText("Log Out?")).as("no Log Out?").isFalse();
        softly.assertAll();
    }

    @Test(priority = 10, description = "TU-L10: Continue without TPI stays Tell us")
    @Severity(SeverityLevel.CRITICAL)
    public void continueWithoutTpiStays() {
        TellUsPage page = reachTellUs();
        Allure.parameter("continueOuterEnabled", String.valueOf(page.isContinueOuterEnabled()));
        page.tapContinueOuter();
        sleepQuiet(700);
        assertThat(page.isDisplayedNow()).as("still Tell us").isTrue();
    }
}
