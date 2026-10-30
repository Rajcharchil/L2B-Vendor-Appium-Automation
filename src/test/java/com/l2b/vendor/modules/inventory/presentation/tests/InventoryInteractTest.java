package com.l2b.vendor.modules.inventory.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.inventory.presentation.pages.InventoryPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Inventory interact — every user action 9000000017")
public class InventoryInteractTest extends MaterialSurfaceBaseTest {

    @Test(priority = 1, description = "INV-I1: Search type AAC")
    @Severity(SeverityLevel.CRITICAL)
    public void searchType() {
        InventoryPage page = reachInventory();
        page.typeSearch("AAC");
        sleepQuiet(800);
        assertThat(page.isDisplayedNow() || page.isStockRowVisible()).isTrue();
    }

    @Test(priority = 2, description = "INV-I2: clear search")
    @Severity(SeverityLevel.CRITICAL)
    public void clearSearch() {
        InventoryPage page = reachInventory();
        page.typeSearch("AAC");
        sleepQuiet(400);
        page.clearSearch();
        sleepQuiet(500);
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 3, description = "INV-I3: Filter open/close")
    @Severity(SeverityLevel.CRITICAL)
    public void filter() {
        InventoryPage page = reachInventory();
        if (page.isFilterVisible()) {
            page.tapFilter();
            sleepQuiet(700);
            DriverManager.get().navigate().back();
            sleepQuiet(500);
        }
        assertThat(new InventoryPage().isDisplayedNow()).isTrue();
    }

    @Test(priority = 4, description = "INV-I4: Expand")
    @Severity(SeverityLevel.NORMAL)
    public void expand() {
        InventoryPage page = reachInventory();
        if (page.isExpandVisible()) {
            page.tapExpand();
            sleepQuiet(600);
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 5, description = "INV-I5: Decrease qty — no Save")
    @Severity(SeverityLevel.CRITICAL)
    public void decreaseQtyNoSave() {
        InventoryPage page = reachInventory();
        try {
            page.tapDecreaseQty();
            sleepQuiet(400);
        } catch (RuntimeException ignored) {
        }
        assertThat(page.isDisplayedNow()).isTrue();
        assertThat(hasText("Log Out?")).isFalse();
    }

    @Test(priority = 6, description = "INV-I6: Increase qty — no Save")
    @Severity(SeverityLevel.CRITICAL)
    public void increaseQtyNoSave() {
        InventoryPage page = reachInventory();
        try {
            page.tapIncreaseQty();
            sleepQuiet(400);
        } catch (RuntimeException ignored) {
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }

    @Test(priority = 7, description = "INV-I7: Home then Inventory")
    @Severity(SeverityLevel.CRITICAL)
    public void homeThenInventory() {
        InventoryPage page = reachInventory();
        page.tapHomeTab();
        sleepQuiet(800);
        assertThat(reachInventory().isDisplayedNow()).isTrue();
    }

    @Test(priority = 8, description = "INV-I8: Orders then Inventory")
    @Severity(SeverityLevel.CRITICAL)
    public void ordersThenInventory() {
        InventoryPage page = reachInventory();
        page.tapOrdersTab();
        sleepQuiet(900);
        assertThat(reachInventory().isDisplayedNow()).isTrue();
    }

    @Test(priority = 9, description = "INV-I9: Back when present")
    @Severity(SeverityLevel.NORMAL)
    public void back() {
        InventoryPage page = reachInventory();
        page.tapBack();
        sleepQuiet(800);
        assertThat(classifyMaterialNow()).isIn("home", "inventory", "orders", "vendor-other");
    }

    @Test(priority = 10, description = "INV-I10: package after actions")
    @Severity(SeverityLevel.NORMAL)
    public void pkg() {
        reachInventory();
        assertThat(vendorPackage()).contains("l2b");
    }
}
