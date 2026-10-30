package com.l2b.vendor.modules.orders.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.orders.presentation.pages.OrderDetailsPage;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Order details landing — 9000000017")
public class OrderDetailsLandingTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="OD-L1: opens") @Severity(SeverityLevel.BLOCKER)
    public void opens() { assertThat(reachOrderDetails().isDisplayedNow()).isTrue(); }
    @Test(priority=2, description="OD-L2: View Less / collapse visible") @Severity(SeverityLevel.CRITICAL)
    public void back() { assertThat(reachOrderDetails().isBackVisible()).isTrue(); }
    @Test(priority=3, description="OD-L3: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachOrderDetails(); assertThat(vendorPackage()).contains("l2b"); }
    @Test(priority=4, description="OD-L4: no Accept") @Severity(SeverityLevel.CRITICAL)
    public void noAccept() { reachOrderDetails(); assertThat(noAcceptVisible()).isTrue(); }
    @Test(priority=5, description="OD-L5: no Decline") @Severity(SeverityLevel.CRITICAL)
    public void noDecline() { reachOrderDetails(); assertThat(hasText("Decline")).isFalse(); }
    @Test(priority=6, description="OD-L6: no Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noLogout() { reachOrderDetails(); assertThat(hasText("Log Out?")).isFalse(); }
    @Test(priority=7, description="OD-L7: not launcher") @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() { reachOrderDetails(); assertThat(classifyMaterialNow()).isNotEqualTo("launcher"); }
    @Test(priority=8, description="OD-L8: details or booking chrome") @Severity(SeverityLevel.CRITICAL)
    public void chrome() {
        OrderDetailsPage page = reachOrderDetails();
        SoftAssertions s = new SoftAssertions();
        s.assertThat(page.isDisplayedNow()).isTrue();
        s.assertThat(vendorPackage()).contains("l2b");
        s.assertAll();
    }
    @Test(priority=9, description="OD-L9: classify order-details") @Severity(SeverityLevel.NORMAL)
    public void named() {
        reachOrderDetails();
        assertThat(classifyMaterialNow()).isIn("order-details", "orders", "vendor-other");
    }
    @Test(priority=10, description="OD-L10: package again") @Severity(SeverityLevel.NORMAL)
    public void pkg2() { reachOrderDetails(); assertThat(vendorPackage()).contains("l2b"); }
}
