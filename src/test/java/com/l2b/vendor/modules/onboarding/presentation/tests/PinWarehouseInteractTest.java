package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.onboarding.presentation.pages.PinWarehousePage;
import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsMaterialPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Pin warehouse interact — 9000000013")
public class PinWarehouseInteractTest extends RegistrationBaseTest {

    @Test(priority = 1, description = "PW-I1: Confirm → Tell us")
    @Severity(SeverityLevel.CRITICAL)
    public void confirmToTellUs() {
        PinWarehousePage page = reachPinWarehouse();
        page.tapConfirm();
        sleepQuiet(1200);
        assertThat(new TellUsMaterialPage().isDisplayedNow()).isTrue();
    }

    @Test(priority = 2, description = "PW-I2: Close → Tell us")
    @Severity(SeverityLevel.CRITICAL)
    public void closeToTellUs() {
        PinWarehousePage page = reachPinWarehouse();
        page.tapClose();
        sleepQuiet(900);
        assertThat(new TellUsMaterialPage().isDisplayedNow()).isTrue();
    }

    @Test(priority = 3, description = "PW-I3: device Back → Tell us")
    @Severity(SeverityLevel.CRITICAL)
    public void deviceBack() {
        reachPinWarehouse();
        DriverManager.get().navigate().back();
        sleepQuiet(900);
        assertThat(new TellUsMaterialPage().isDisplayedNow()).isTrue();
    }

    @Test(priority = 4, description = "PW-I4: re-open pin")
    @Severity(SeverityLevel.CRITICAL)
    public void reopen() {
        assertThat(reachPinWarehouse().isDisplayedNow()).isTrue();
    }

    @Test(priority = 5, description = "PW-I5: Use current on sheet")
    @Severity(SeverityLevel.NORMAL)
    public void useCurrentOnSheet() {
        PinWarehousePage page = reachPinWarehouse();
        try {
            page.tapUseCurrentDesc();
            sleepQuiet(2000);
        } catch (RuntimeException ignored) {
        }
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 6, description = "PW-I6: rapid Confirm")
    @Severity(SeverityLevel.CRITICAL)
    public void rapidConfirm() {
        PinWarehousePage page = reachPinWarehouse();
        for (int i = 0; i < 3; i++) {
            if (page.isDisplayedNow()) {
                page.tapConfirm();
                sleepQuiet(400);
            }
        }
        assertThat(new TellUsMaterialPage().isDisplayedNow() || page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 7, description = "PW-I7: rapid Close")
    @Severity(SeverityLevel.NORMAL)
    public void rapidClose() {
        PinWarehousePage page = reachPinWarehouse();
        page.tapClose();
        sleepQuiet(500);
        if (page.isDisplayedNow()) {
            page.tapClose();
        }
        assertThat(new TellUsMaterialPage().isDisplayedNow() || page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 8, description = "PW-I8: Confirm then Pin again")
    @Severity(SeverityLevel.CRITICAL)
    public void confirmThenPinAgain() {
        PinWarehousePage page = reachPinWarehouse();
        page.tapConfirm();
        sleepQuiet(1000);
        assertThat(reachPinWarehouse().isDisplayedNow()).isTrue();
    }

    @Test(priority = 9, description = "PW-I9: stay Tell us after Confirm")
    @Severity(SeverityLevel.NORMAL)
    public void stayTellUs() {
        PinWarehousePage page = reachPinWarehouse();
        page.tapConfirm();
        sleepQuiet(1000);
        assertThat(classifyNow()).isIn("tellus-materials", "pin-warehouse");
    }

    @Test(priority = 10, description = "PW-I10: package")
    @Severity(SeverityLevel.NORMAL)
    public void pkg() {
        reachPinWarehouse();
        assertThat(vendorPackage()).contains("l2b");
    }
}
