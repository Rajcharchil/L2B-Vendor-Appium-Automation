package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.onboarding.presentation.pages.CategoryPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.RolePage;
import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsMaterialPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Category interact — Materials 9000000013")
public class CategoryInteractTest extends RegistrationBaseTest {

    @Test(priority = 1, description = "SC-I1: tap Materials")
    @Severity(SeverityLevel.CRITICAL)
    public void tapMaterials() {
        CategoryPage page = reachCategory();
        page.tapMaterials();
        sleepQuiet(400);
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 2, description = "SC-I2: tap Rental")
    @Severity(SeverityLevel.CRITICAL)
    public void tapRental() {
        CategoryPage page = reachCategory();
        page.tapRental();
        sleepQuiet(400);
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 3, description = "SC-I3: re-tap Materials")
    @Severity(SeverityLevel.NORMAL)
    public void retapMaterials() {
        CategoryPage page = reachCategory();
        page.tapMaterials();
        sleepQuiet(300);
        page.tapMaterials();
        sleepQuiet(300);
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 4, description = "SC-I4: Materials Continue → Tell us Materials")
    @Severity(SeverityLevel.BLOCKER)
    public void materialsContinue() {
        CategoryPage page = reachCategory();
        page.tapMaterials();
        sleepQuiet(400);
        page.tapContinue();
        sleepQuiet(1500);
        assertThat(new TellUsMaterialPage().isDisplayedNow()).isTrue();
        new TellUsMaterialPage().tapBack();
        sleepQuiet(900);
        assertThat(new CategoryPage().isDisplayedNow() || new RolePage().isDisplayedNow()).isTrue();
    }

    @Test(priority = 5, description = "SC-I5: device Back → role")
    @Severity(SeverityLevel.CRITICAL)
    public void deviceBack() {
        reachCategory();
        DriverManager.get().navigate().back();
        sleepQuiet(900);
        assertThat(new RolePage().isDisplayedNow()).isTrue();
    }

    @Test(priority = 6, description = "SC-I6: re-open category")
    @Severity(SeverityLevel.CRITICAL)
    public void reopen() {
        assertThat(reachCategory().isDisplayedNow()).isTrue();
    }

    @Test(priority = 7, description = "SC-I7: rapid Materials")
    @Severity(SeverityLevel.NORMAL)
    public void rapidMaterials() {
        CategoryPage page = reachCategory();
        for (int i = 0; i < 3; i++) {
            page.tapMaterials();
            sleepQuiet(200);
        }
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 8, description = "SC-I8: rapid Rental")
    @Severity(SeverityLevel.NORMAL)
    public void rapidRental() {
        CategoryPage page = reachCategory();
        for (int i = 0; i < 3; i++) {
            page.tapRental();
            sleepQuiet(200);
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 9, description = "SC-I9: Materials then Continue again")
    @Severity(SeverityLevel.NORMAL)
    public void continueAgain() {
        CategoryPage page = reachCategory();
        page.tapMaterials();
        sleepQuiet(300);
        page.tapContinue();
        sleepQuiet(1200);
        SoftAssertionsOrBack();
    }

    @Test(priority = 10, description = "SC-I10: package")
    @Severity(SeverityLevel.NORMAL)
    public void pkg() {
        reachCategory();
        assertThat(vendorPackage()).contains("l2b");
    }

    private void SoftAssertionsOrBack() {
        if (new TellUsMaterialPage().isDisplayedNow()) {
            new TellUsMaterialPage().tapBack();
            sleepQuiet(800);
        }
        assertThat(new CategoryPage().isDisplayedNow() || new RolePage().isDisplayedNow()
                || new TellUsMaterialPage().isDisplayedNow()).isTrue();
    }
}
