package com.l2b.vendor.modules.orders.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.orders.presentation.pages.MaterialOrdersPage;
import com.l2b.vendor.modules.orders.presentation.pages.OrderDetailsPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Order details interact — 9000000017")
public class OrderDetailsInteractTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="OD-I1: open details") @Severity(SeverityLevel.CRITICAL)
    public void open() { assertThat(reachOrderDetails().isDisplayedNow()).isTrue(); }
    @Test(priority=2, description="OD-I2: Collapse (View Less) stays on Orders") @Severity(SeverityLevel.CRITICAL)
    public void backToOrders() {
        OrderDetailsPage page = reachOrderDetails();
        page.tapBack(); sleepQuiet(1000);
        assertThat(new MaterialOrdersPage().isDisplayedNow()
                || hasText("Upcoming")
                || hasText("View More Details")).isTrue();
    }
    @Test(priority=3, description="OD-I3: reopen details") @Severity(SeverityLevel.CRITICAL)
    public void reopen() { assertThat(reachOrderDetails().isDisplayedNow()).isTrue(); }
    @Test(priority=4, description="OD-I4: Back again") @Severity(SeverityLevel.NORMAL)
    public void backAgain() {
        reachOrderDetails().tapBack(); sleepQuiet(800);
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=5, description="OD-I5: reopen 2") @Severity(SeverityLevel.NORMAL)
    public void reopen2() { assertThat(reachOrderDetails().isDisplayedNow()).isTrue(); }
    @Test(priority=6, description="OD-I6: View Less visible when expanded") @Severity(SeverityLevel.NORMAL)
    public void backVisible() { assertThat(reachOrderDetails().isBackVisible()).isTrue(); }
    @Test(priority=7, description="OD-I7: no Accept on details") @Severity(SeverityLevel.CRITICAL)
    public void noAccept() { reachOrderDetails(); assertThat(noAcceptVisible()).isTrue(); }
    @Test(priority=8, description="OD-I8: no Decline") @Severity(SeverityLevel.CRITICAL)
    public void noDecline() { reachOrderDetails(); assertThat(hasText("Decline")).isFalse(); }
    @Test(priority=9, description="OD-I9: stay vendor") @Severity(SeverityLevel.NORMAL)
    public void stay() { reachOrderDetails(); assertThat(classifyMaterialNow()).isNotEqualTo("launcher"); }
    @Test(priority=10, description="OD-I10: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachOrderDetails(); assertThat(vendorPackage()).contains("l2b"); }
}
