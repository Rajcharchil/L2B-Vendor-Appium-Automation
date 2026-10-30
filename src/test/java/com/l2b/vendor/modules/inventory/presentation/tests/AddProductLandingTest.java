package com.l2b.vendor.modules.inventory.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.modules.inventory.presentation.pages.AddProductPage;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Add Product landing — every field chrome 9000000017")
public class AddProductLandingTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="AP-L1: opens") @Severity(SeverityLevel.BLOCKER)
    public void opens() { assertThat(reachAddProduct().isDisplayedNow()).isTrue(); }
    @Test(priority=2, description="AP-L2: Category *") @Severity(SeverityLevel.CRITICAL)
    public void category() { assertThat(reachAddProduct().isCategoryVisible()).isTrue(); }
    @Test(priority=3, description="AP-L3: Product Name *") @Severity(SeverityLevel.CRITICAL)
    public void productName() { assertThat(reachAddProduct().isProductNameVisible()).isTrue(); }
    @Test(priority=4, description="AP-L4: Brand *") @Severity(SeverityLevel.CRITICAL)
    public void brand() { assertThat(reachAddProduct().isBrandVisible()).isTrue(); }
    @Test(priority=5, description="AP-L5: Pricing / Selling Price") @Severity(SeverityLevel.CRITICAL)
    public void pricing() { assertThat(reachAddProduct().isPricingVisible()).isTrue(); }
    @Test(priority=6, description="AP-L6: Stock Available") @Severity(SeverityLevel.CRITICAL)
    public void stock() { assertThat(reachAddProduct().isStockVisible()).isTrue(); }
    @Test(priority=7, description="AP-L7: Specifications") @Severity(SeverityLevel.NORMAL)
    public void specs() { assertThat(reachAddProduct().isSpecsVisible()).isTrue(); }
    @Test(priority=8, description="AP-L8: Save CTA visible") @Severity(SeverityLevel.CRITICAL)
    public void saveVisible() { assertThat(reachAddProduct().isSaveVisible()).isTrue(); }
    @Test(priority=9, description="AP-L9: Back") @Severity(SeverityLevel.CRITICAL)
    public void back() { assertThat(reachAddProduct().isBackVisible()).isTrue(); }
    @Test(priority=10, description="AP-L10: no Accept/Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noDanger() {
        reachAddProduct();
        SoftAssertions s = new SoftAssertions();
        s.assertThat(noAcceptVisible()).isTrue();
        s.assertThat(hasText("Log Out?")).isFalse();
        s.assertAll();
    }
}
