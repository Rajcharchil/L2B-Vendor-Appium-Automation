package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

/**
 * Tell us interact — 9000000013. Never Take Photo / Gallery / Log Out Confirm.
 *
 * <p><b>Happy Path (TU-I1–I10)</b>
 */
@Epic("Vendor app")
@Feature("Tell us interact — fresh 9000000013")
public class TellUsInteractTest extends TellUsBaseTest {

    @Test(priority = 1, description = "TU-I1: open Tell us")
    @Severity(SeverityLevel.BLOCKER)
    public void openTellUs() {
        assertThat(reachTellUs().isDisplayedNow()).isTrue();
    }

    @Test(priority = 2, description = "TU-I2: tap company checkbox stays Tell us")
    @Severity(SeverityLevel.CRITICAL)
    public void tapCompany() {
        TellUsPage page = reachTellUs();
        page.tapCompanyCheckbox();
        sleepQuiet(600);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(page.isDisplayedNow()).isTrue();
        softly.assertThat(vendorPackage()).contains("l2b");
        softly.assertAll();
    }

    @Test(priority = 3, description = "TU-I3: un-tap company stays Tell us")
    @Severity(SeverityLevel.CRITICAL)
    public void untapCompany() {
        TellUsPage page = reachTellUs();
        page.tapCompanyCheckbox();
        sleepQuiet(400);
        page.tapCompanyCheckbox();
        sleepQuiet(500);
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 4, description = "TU-I4: Upload TPI opens sheet")
    @Severity(SeverityLevel.CRITICAL)
    public void uploadOpensSheet() {
        TellUsPage page = reachTellUs();
        page.tapUploadTpi();
        sleepQuiet(900);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(page.isUploadSheetVisible()).as("sheet").isTrue();
        softly.assertThat(hasText("Take Photo") || hasText("Choose from Gallery")).isTrue();
        softly.assertAll();
        page.tapSheetCancel();
        sleepQuiet(700);
    }

    @Test(priority = 5, description = "TU-I5: sheet Cancel returns Tell us")
    @Severity(SeverityLevel.CRITICAL)
    public void sheetCancel() {
        TellUsPage page = reachTellUs();
        page.tapUploadTpi();
        sleepQuiet(800);
        page.tapSheetCancel();
        sleepQuiet(800);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(page.isDisplayedNow()).isTrue();
        softly.assertThat(page.isUploadSheetVisible()).isFalse();
        softly.assertAll();
    }

    @Test(priority = 6, description = "TU-I6: device Back on sheet")
    @Severity(SeverityLevel.NORMAL)
    public void deviceBackOnSheet() {
        TellUsPage page = reachTellUs();
        page.tapUploadTpi();
        sleepQuiet(800);
        DriverManager.get().navigate().back();
        sleepQuiet(800);
        assertThat(page.isDisplayedNow() || page.isUploadSheetVisible()).isTrue();
        dismissUploadSheetIfPresent();
        assertThat(new TellUsPage().isDisplayedNow()).isTrue();
    }

    @Test(priority = 7, description = "TU-I7: edit name then restore")
    @Severity(SeverityLevel.NORMAL)
    public void editNameRestore() {
        TellUsPage page = reachTellUs();
        String original = page.nameFieldText();
        page.setName(original + " ");
        sleepQuiet(400);
        page.setName(TellUsPage.NAME_PREFILL);
        sleepQuiet(400);
        assertThat(page.isDisplayedNow()).isTrue();
        Allure.parameter("nameAfter", page.nameFieldText());
    }

    @Test(priority = 8, description = "TU-I8: rapid Continue ×3 stays Tell us")
    @Severity(SeverityLevel.CRITICAL)
    public void rapidContinue() {
        TellUsPage page = reachTellUs();
        for (int i = 0; i < 3; i++) {
            page.tapContinueOuter();
            sleepQuiet(250);
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 9, description = "TU-I9: photo chip tap does not crash")
    @Severity(SeverityLevel.NORMAL)
    public void photoChip() {
        TellUsPage page = reachTellUs();
        page.tapAddProfilePhoto();
        sleepQuiet(1200);
        Allure.parameter("afterPhoto", classifyTellUsNow());
        try {
            DriverManager.get().navigate().back();
        } catch (RuntimeException ignored) {
        }
        sleepQuiet(700);
        dismissUploadSheetIfPresent();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(vendorPackage()).contains("l2b");
        softly.assertThat(new TellUsPage().isDisplayedNow()
                || hasText("Take Photo")
                || hasText("Allow")
                || hasText("While using the app")).isTrue();
        softly.assertAll();
        if (!new TellUsPage().isDisplayedNow()) {
            try {
                DriverManager.get().navigate().back();
            } catch (RuntimeException ignored) {
            }
            sleepQuiet(600);
        }
    }

    @Test(priority = 10, description = "TU-I10: stay Vendor after toggle")
    @Severity(SeverityLevel.NORMAL)
    public void stayVendor() {
        TellUsPage page = reachTellUs();
        page.tapCompanyCheckbox();
        sleepQuiet(300);
        assertThat(vendorPackage()).contains("l2b");
        assertThat(page.isDisplayedNow()).isTrue();
    }
}
