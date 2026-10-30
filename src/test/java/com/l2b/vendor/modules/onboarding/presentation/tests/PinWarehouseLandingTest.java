package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.modules.onboarding.presentation.pages.PinWarehousePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Pin warehouse landing — 9000000013")
public class PinWarehouseLandingTest extends RegistrationBaseTest {

    @Test(priority = 1, description = "PW-L1: opens")
    @Severity(SeverityLevel.BLOCKER)
    public void opens() {
        assertThat(reachPinWarehouse().isDisplayedNow()).isTrue();
    }

    @Test(priority = 2, description = "PW-L2: map unavailable or map")
    @Severity(SeverityLevel.CRITICAL)
    public void mapChrome() {
        PinWarehousePage page = reachPinWarehouse();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(page.isMapUnavailableVisible() || page.isHintVisible()).isTrue();
        softly.assertAll();
    }

    @Test(priority = 3, description = "PW-L3: move-map hint")
    @Severity(SeverityLevel.NORMAL)
    public void hint() {
        assertThat(reachPinWarehouse().isHintVisible()).isTrue();
    }

    @Test(priority = 4, description = "PW-L4: coords chip")
    @Severity(SeverityLevel.CRITICAL)
    public void coords() {
        assertThat(reachPinWarehouse().isCoordsVisible()).isTrue();
    }

    @Test(priority = 5, description = "PW-L5: Confirm")
    @Severity(SeverityLevel.CRITICAL)
    public void confirm() {
        assertThat(reachPinWarehouse().isConfirmVisible()).isTrue();
    }

    @Test(priority = 6, description = "PW-L6: Close")
    @Severity(SeverityLevel.CRITICAL)
    public void close() {
        assertThat(reachPinWarehouse().isCloseVisible()).isTrue();
    }

    @Test(priority = 7, description = "PW-L7: Use current desc")
    @Severity(SeverityLevel.NORMAL)
    public void useCurrentDesc() {
        assertThat(reachPinWarehouse().isUseCurrentDescVisible()).isTrue();
    }

    @Test(priority = 8, description = "PW-L8: package")
    @Severity(SeverityLevel.NORMAL)
    public void pkg() {
        reachPinWarehouse();
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 9, description = "PW-L9: no Accept/Decline/Log Out")
    @Severity(SeverityLevel.CRITICAL)
    public void noDanger() {
        reachPinWarehouse();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(hasText("Decline")).isFalse();
        softly.assertThat(noAcceptVisible()).isTrue();
        softly.assertThat(hasText("Log Out?")).isFalse();
        softly.assertAll();
    }

    @Test(priority = 10, description = "PW-L10: not launcher")
    @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() {
        reachPinWarehouse();
        assertThat(classifyNow()).isNotEqualTo("launcher");
    }
}
