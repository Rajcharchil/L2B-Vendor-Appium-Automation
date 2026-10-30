package com.l2b.vendor.modules.inventory.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.inventory.presentation.pages.InventoryPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Inventory edge — material 9000000017")
public class InventoryEdgeTest extends MaterialSurfaceBaseTest {

    @Test(priority = 1, description = "INV-E1: rotation")
    @Severity(SeverityLevel.NORMAL)
    public void rotation() {
        reachInventory();
        AndroidDriver android = (AndroidDriver) DriverManager.get();
        try {
            android.rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE);
            sleepQuiet(900);
            android.rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT);
            sleepQuiet(700);
        } catch (RuntimeException e) {
            Allure.parameter("rotateFail", e.getClass().getSimpleName());
        }
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 2, description = "INV-E2: background")
    @Severity(SeverityLevel.CRITICAL)
    public void background() {
        reachInventory();
        try {
            ((AndroidDriver) DriverManager.get()).runAppInBackground(java.time.Duration.ofSeconds(2));
        } catch (RuntimeException e) {
            Adb.run("shell", "input", "keyevent", "3");
            sleepQuiet(1200);
        }
        ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package"));
        sleepQuiet(1200);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(vendorPackage()).contains("l2b");
        Allure.parameter("afterBg", classifyMaterialNow());
        softly.assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
        softly.assertAll();
    }

    @Test(priority = 3, description = "INV-E3: offline")
    @Severity(SeverityLevel.CRITICAL)
    public void offline() {
        InventoryPage page = reachInventory();
        try {
            Adb.run("shell", "cmd", "connectivity", "airplane-mode", "enable");
            sleepQuiet(700);
            assertThat(page.isDisplayedNow() || hasText("Inventory")).isTrue();
        } finally {
            try {
                Adb.run("shell", "cmd", "connectivity", "airplane-mode", "disable");
            } catch (RuntimeException ignored) {
            }
            Adb.ensureNetworkReady();
        }
    }

    @Test(priority = 4, description = "INV-E4: not launcher")
    @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() {
        reachInventory();
        assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
    }

    @Test(priority = 5, description = "INV-E5: no Accept")
    @Severity(SeverityLevel.CRITICAL)
    public void noAccept() {
        reachInventory();
        assertThat(noAcceptVisible()).isTrue();
    }

    @Test(priority = 6, description = "INV-E6: no Log Out")
    @Severity(SeverityLevel.CRITICAL)
    public void noLogout() {
        reachInventory();
        assertThat(hasText("Log Out?")).isFalse();
    }

    @Test(priority = 7, description = "INV-E7: rapid Filter")
    @Severity(SeverityLevel.NORMAL)
    public void rapidFilter() {
        InventoryPage page = reachInventory();
        for (int i = 0; i < 3; i++) {
            if (page.isFilterVisible()) {
                try {
                    page.tapFilter();
                    sleepQuiet(250);
                    DriverManager.get().navigate().back();
                    sleepQuiet(250);
                } catch (RuntimeException ignored) {
                }
            }
        }
        assertThat(reachInventory().isDisplayedNow()).isTrue();
    }

    @Test(priority = 8, description = "INV-E8: empty Search")
    @Severity(SeverityLevel.NORMAL)
    public void emptySearch() {
        InventoryPage page = reachInventory();
        page.typeSearch("zzzznomatchesl2b");
        sleepQuiet(700);
        page.clearSearch();
        sleepQuiet(400);
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 9, description = "INV-E9: reopen Inventory")
    @Severity(SeverityLevel.NORMAL)
    public void reopen() {
        assertThat(reachInventory().isDisplayedNow()).isTrue();
    }

    @Test(priority = 10, description = "INV-E10: material tabs stay")
    @Severity(SeverityLevel.CRITICAL)
    public void tabsStay() {
        assertThat(reachInventory().areMaterialTabsVisible()).isTrue();
    }
}
