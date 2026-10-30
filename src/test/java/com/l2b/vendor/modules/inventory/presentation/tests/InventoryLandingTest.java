package com.l2b.vendor.modules.inventory.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.modules.inventory.presentation.pages.InventoryPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Inventory landing — material 9000000017")
public class InventoryLandingTest extends MaterialSurfaceBaseTest {

    @Test(priority = 1, description = "INV-L1: bottom Inventory opens")
    @Severity(SeverityLevel.BLOCKER)
    public void opens() {
        assertThat(reachInventory().isDisplayedNow()).isTrue();
    }

    @Test(priority = 2, description = "INV-L2: + Add Product")
    @Severity(SeverityLevel.CRITICAL)
    public void addProduct() {
        assertThat(reachInventory().isAddProductVisible()).isTrue();
    }

    @Test(priority = 3, description = "INV-L3: Import CSV")
    @Severity(SeverityLevel.CRITICAL)
    public void importCsv() {
        assertThat(reachInventory().isImportCsvVisible()).isTrue();
    }

    @Test(priority = 4, description = "INV-L4: Search Item")
    @Severity(SeverityLevel.CRITICAL)
    public void search() {
        assertThat(reachInventory().isSearchVisible()).isTrue();
    }

    @Test(priority = 5, description = "INV-L5: All Material Inventory")
    @Severity(SeverityLevel.CRITICAL)
    public void allInventory() {
        assertThat(reachInventory().isAllInventoryVisible()).isTrue();
    }

    @Test(priority = 6, description = "INV-L6: stock row chrome")
    @Severity(SeverityLevel.CRITICAL)
    public void stockRow() {
        assertThat(reachInventory().isStockRowVisible()).isTrue();
    }

    @Test(priority = 7, description = "INV-L7: material bottom tabs stay")
    @Severity(SeverityLevel.CRITICAL)
    public void bottomTabs() {
        assertThat(reachInventory().areMaterialTabsVisible()).isTrue();
    }

    @Test(priority = 8, description = "INV-L8: package")
    @Severity(SeverityLevel.NORMAL)
    public void pkg() {
        reachInventory();
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 9, description = "INV-L9: no Accept / Log Out")
    @Severity(SeverityLevel.CRITICAL)
    public void noDanger() {
        reachInventory();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(noAcceptVisible()).isTrue();
        softly.assertThat(hasText("Log Out?")).isFalse();
        softly.assertThat(hasText("Decline")).isFalse();
        softly.assertAll();
    }

    @Test(priority = 10, description = "INV-L10: drawer Your Inventory")
    @Severity(SeverityLevel.CRITICAL)
    public void drawerYourInventory() {
        assertThat(reachInventoryViaDrawer().isDisplayedNow()).isTrue();
    }
}
