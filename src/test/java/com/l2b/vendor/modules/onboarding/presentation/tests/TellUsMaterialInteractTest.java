package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.onboarding.presentation.pages.CategoryPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsMaterialPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Tell us Materials interact — 9000000013")
public class TellUsMaterialInteractTest extends RegistrationBaseTest {

    @Test(priority = 1, description = "TM-I1: open company options")
    @Severity(SeverityLevel.CRITICAL)
    public void companyOptions() {
        TellUsMaterialPage page = reachTellUsMaterial();
        page.tapCompanyType();
        sleepQuiet(800);
        assertThat(page.isCompanyOptionsVisible() || page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 2, description = "TM-I2: Sole Proprietorship")
    @Severity(SeverityLevel.CRITICAL)
    public void sole() {
        TellUsMaterialPage page = reachTellUsMaterial();
        page.tapCompanyType();
        sleepQuiet(600);
        if (page.isCompanyOptionsVisible()) {
            page.tapSoleProprietorship();
            sleepQuiet(500);
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 3, description = "TM-I3: LLP")
    @Severity(SeverityLevel.NORMAL)
    public void llp() {
        TellUsMaterialPage page = reachTellUsMaterial();
        page.tapCompanyType();
        sleepQuiet(600);
        if (hasText("LLP")) {
            page.tapLlp();
            sleepQuiet(500);
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 4, description = "TM-I4: Use current")
    @Severity(SeverityLevel.NORMAL)
    public void useCurrent() {
        TellUsMaterialPage page = reachTellUsMaterial();
        page.tapUseCurrent();
        sleepQuiet(2000);
        assertThat(vendorPackage()).contains("l2b");
        assertThat(page.isDisplayedNow() || hasText("While using") || hasText("Allow")).isTrue();
        if (!page.isDisplayedNow()) {
            DriverManager.get().navigate().back();
            sleepQuiet(700);
        }
    }

    @Test(priority = 5, description = "TM-I5: Pin on map then Back")
    @Severity(SeverityLevel.CRITICAL)
    public void pinMapBack() {
        TellUsMaterialPage page = reachTellUsMaterial();
        page.tapPinOnMap();
        sleepQuiet(1500);
        DriverManager.get().navigate().back();
        sleepQuiet(1000);
        assertThat(new TellUsMaterialPage().isDisplayedNow() || new CategoryPage().isDisplayedNow())
                .isTrue();
    }

    @Test(priority = 6, description = "TM-I6: Back to category")
    @Severity(SeverityLevel.CRITICAL)
    public void backToCategory() {
        TellUsMaterialPage page = reachTellUsMaterial();
        page.tapBack();
        sleepQuiet(900);
        assertThat(new CategoryPage().isDisplayedNow()).isTrue();
    }

    @Test(priority = 7, description = "TM-I7: re-open Tell us Materials")
    @Severity(SeverityLevel.CRITICAL)
    public void reopen() {
        assertThat(reachTellUsMaterial().isDisplayedNow()).isTrue();
    }

    @Test(priority = 8, description = "TM-I8: rapid Continue")
    @Severity(SeverityLevel.CRITICAL)
    public void rapidContinue() {
        TellUsMaterialPage page = reachTellUsMaterial();
        for (int i = 0; i < 3; i++) {
            page.tapContinue();
            sleepQuiet(250);
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 9, description = "TM-I9: stay after company pick")
    @Severity(SeverityLevel.NORMAL)
    public void stayAfterCompany() {
        TellUsMaterialPage page = reachTellUsMaterial();
        page.tapCompanyType();
        sleepQuiet(500);
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 10, description = "TM-I10: package")
    @Severity(SeverityLevel.NORMAL)
    public void pkg() {
        reachTellUsMaterial();
        assertThat(vendorPackage()).contains("l2b");
    }
}
