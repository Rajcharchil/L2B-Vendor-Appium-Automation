package com.l2b.vendor.modules.inventory.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.modules.inventory.presentation.pages.AddProductPage;
import com.l2b.vendor.modules.inventory.presentation.pages.InventoryPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Add Product interact — user actions no Save persist 9000000017")
public class AddProductInteractTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="AP-I1: open from Inventory") @Severity(SeverityLevel.CRITICAL)
    public void open() { assertThat(reachAddProduct().isDisplayedNow()).isTrue(); }
    @Test(priority=2, description="AP-I2: incomplete Save stays") @Severity(SeverityLevel.CRITICAL)
    public void incompleteSave() {
        AddProductPage page = reachAddProduct();
        page.tapSave(); sleepQuiet(800);
        assertThat(page.isDisplayedNow()).isTrue();
    }
    @Test(priority=3, description="AP-I3: Back to Inventory") @Severity(SeverityLevel.CRITICAL)
    public void backToInventory() {
        AddProductPage page = reachAddProduct();
        page.tapBack(); sleepQuiet(900);
        assertThat(new InventoryPage().isDisplayedNow()).isTrue();
    }
    @Test(priority=4, description="AP-I4: reopen Add Product") @Severity(SeverityLevel.CRITICAL)
    public void reopen() { assertThat(reachAddProduct().isDisplayedNow()).isTrue(); }
    @Test(priority=5, description="AP-I5: Category chrome visible after reopen") @Severity(SeverityLevel.NORMAL)
    public void categoryAgain() { assertThat(reachAddProduct().isCategoryVisible()).isTrue(); }
    @Test(priority=6, description="AP-I6: Product Name chrome") @Severity(SeverityLevel.NORMAL)
    public void nameAgain() { assertThat(reachAddProduct().isProductNameVisible()).isTrue(); }
    @Test(priority=7, description="AP-I7: Brand chrome") @Severity(SeverityLevel.NORMAL)
    public void brandAgain() { assertThat(reachAddProduct().isBrandVisible()).isTrue(); }
    @Test(priority=8, description="AP-I8: Stock chrome") @Severity(SeverityLevel.NORMAL)
    public void stockAgain() { assertThat(reachAddProduct().isStockVisible()).isTrue(); }
    @Test(priority=9, description="AP-I9: Specs chrome") @Severity(SeverityLevel.NORMAL)
    public void specsAgain() { assertThat(reachAddProduct().isSpecsVisible()).isTrue(); }
    @Test(priority=10, description="AP-I10: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachAddProduct(); assertThat(vendorPackage()).contains("l2b"); }
}
