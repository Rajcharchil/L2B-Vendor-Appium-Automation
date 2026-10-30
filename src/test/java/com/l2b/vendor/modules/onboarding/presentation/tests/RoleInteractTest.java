package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.onboarding.presentation.pages.CategoryPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.RolePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Role interact — 9000000013 Materials path")
public class RoleInteractTest extends RegistrationBaseTest {

    @Test(priority = 1, description = "SR-I1: tap Vendor stays role")
    @Severity(SeverityLevel.CRITICAL)
    public void tapVendor() {
        RolePage page = reachRole();
        page.tapVendor();
        sleepQuiet(500);
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 2, description = "SR-I2: tap Operator stays role")
    @Severity(SeverityLevel.CRITICAL)
    public void tapOperator() {
        RolePage page = reachRole();
        page.tapOperator();
        sleepQuiet(500);
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 3, description = "SR-I3: re-tap Vendor")
    @Severity(SeverityLevel.NORMAL)
    public void retapVendor() {
        RolePage page = reachRole();
        page.tapVendor();
        sleepQuiet(300);
        page.tapVendor();
        sleepQuiet(400);
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 4, description = "SR-I4: rapid Vendor ×3")
    @Severity(SeverityLevel.NORMAL)
    public void rapidVendor() {
        RolePage page = reachRole();
        for (int i = 0; i < 3; i++) {
            page.tapVendor();
            sleepQuiet(200);
        }
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 5, description = "SR-I5: rapid Operator ×3")
    @Severity(SeverityLevel.NORMAL)
    public void rapidOperator() {
        RolePage page = reachRole();
        for (int i = 0; i < 3; i++) {
            page.tapOperator();
            sleepQuiet(200);
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 6, description = "SR-I6: Vendor then Next → category")
    @Severity(SeverityLevel.BLOCKER)
    public void vendorNextToCategory() {
        RolePage page = reachRole();
        page.tapVendor();
        sleepQuiet(400);
        page.tapNext();
        sleepQuiet(1200);
        assertThat(new CategoryPage().isDisplayedNow()).isTrue();
        DriverManager.get().navigate().back();
        sleepQuiet(900);
        assertThat(new RolePage().isDisplayedNow()).isTrue();
    }

    @Test(priority = 7, description = "SR-I7: Back from category → role")
    @Severity(SeverityLevel.CRITICAL)
    public void backFromCategory() {
        reachCategory();
        DriverManager.get().navigate().back();
        sleepQuiet(900);
        assertThat(new RolePage().isDisplayedNow()).isTrue();
    }

    @Test(priority = 8, description = "SR-I8: Operator then Vendor")
    @Severity(SeverityLevel.NORMAL)
    public void operatorThenVendor() {
        RolePage page = reachRole();
        page.tapOperator();
        sleepQuiet(300);
        page.tapVendor();
        sleepQuiet(400);
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 9, description = "SR-I9: Next after Vendor leaves role")
    @Severity(SeverityLevel.CRITICAL)
    public void nextLeavesRole() {
        RolePage page = reachRole();
        page.tapVendor();
        sleepQuiet(400);
        page.tapNext();
        sleepQuiet(1200);
        assertThat(new CategoryPage().isDisplayedNow()).isTrue();
        DriverManager.get().navigate().back();
        sleepQuiet(800);
    }

    @Test(priority = 10, description = "SR-I10: stay Vendor package")
    @Severity(SeverityLevel.NORMAL)
    public void packageOk() {
        reachRole();
        assertThat(vendorPackage()).contains("l2b");
    }
}
