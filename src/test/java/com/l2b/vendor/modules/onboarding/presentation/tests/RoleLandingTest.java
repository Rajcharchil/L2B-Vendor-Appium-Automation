package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.modules.onboarding.presentation.pages.RolePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Role landing — 9000000013")
public class RoleLandingTest extends RegistrationBaseTest {

    @Test(priority = 1, description = "SR-L1: role opens")
    @Severity(SeverityLevel.BLOCKER)
    public void roleOpens() {
        assertThat(reachRole().isDisplayedNow()).isTrue();
    }

    @Test(priority = 2, description = "SR-L2: Vendor card")
    @Severity(SeverityLevel.CRITICAL)
    public void vendorCard() {
        assertThat(reachRole().isVendorVisible()).isTrue();
    }

    @Test(priority = 3, description = "SR-L3: Operator card")
    @Severity(SeverityLevel.CRITICAL)
    public void operatorCard() {
        assertThat(reachRole().isOperatorVisible()).isTrue();
    }

    @Test(priority = 4, description = "SR-L4: Next visible")
    @Severity(SeverityLevel.CRITICAL)
    public void nextVisible() {
        assertThat(reachRole().isNextVisible()).isTrue();
    }

    @Test(priority = 5, description = "SR-L5: package Vendor")
    @Severity(SeverityLevel.NORMAL)
    public void stayVendor() {
        reachRole();
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 6, description = "SR-L6: no Accept/Decline")
    @Severity(SeverityLevel.CRITICAL)
    public void noAcceptDecline() {
        reachRole();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(hasText("Decline")).isFalse();
        softly.assertThat(noAcceptVisible()).isTrue();
        softly.assertAll();
    }

    @Test(priority = 7, description = "SR-L7: no Log Out?")
    @Severity(SeverityLevel.CRITICAL)
    public void noLogout() {
        reachRole();
        assertThat(hasText("Log Out?")).isFalse();
    }

    @Test(priority = 8, description = "SR-L8: Vendor subtitle")
    @Severity(SeverityLevel.NORMAL)
    public void vendorSubtitle() {
        assertThat(reachRole().isVendorSubtitleVisible()).isTrue();
    }

    @Test(priority = 9, description = "SR-L9: Operator subtitle")
    @Severity(SeverityLevel.NORMAL)
    public void operatorSubtitle() {
        assertThat(reachRole().isOperatorSubtitleVisible()).isTrue();
    }

    @Test(priority = 10, description = "SR-L10: title")
    @Severity(SeverityLevel.CRITICAL)
    public void title() {
        RolePage page = reachRole();
        assertThat(page.isTitleVisible()).isTrue();
    }
}
