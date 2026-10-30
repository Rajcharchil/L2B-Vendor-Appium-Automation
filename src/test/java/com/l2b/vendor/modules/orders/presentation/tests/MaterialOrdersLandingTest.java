package com.l2b.vendor.modules.orders.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.orders.presentation.pages.MaterialOrdersPage;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Material Orders landing — 9000000017")
public class MaterialOrdersLandingTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="MO-L1: opens") @Severity(SeverityLevel.BLOCKER)
    public void opens() { assertThat(reachOrders().isDisplayedNow()).isTrue(); }
    @Test(priority=2, description="MO-L2: tabs Upcoming/Active/Completed") @Severity(SeverityLevel.CRITICAL)
    public void tabs() { assertThat(reachOrders().areTabsVisible()).isTrue(); }
    @Test(priority=3, description="MO-L3: card chrome or empty") @Severity(SeverityLevel.CRITICAL)
    public void cards() {
        MaterialOrdersPage page = reachOrdersWithCardsIfAny();
        assertThat(page.isCardChromeVisible() || page.isEmptyStateVisible()).isTrue();
    }
    @Test(priority=4, description="MO-L4: Get Direction or empty") @Severity(SeverityLevel.CRITICAL)
    public void getDirection() {
        MaterialOrdersPage page = reachOrdersWithCardsIfAny();
        assertThat(page.isGetDirectionVisible() || page.isEmptyStateVisible()).isTrue();
    }
    @Test(priority=5, description="MO-L5: View More Details or empty") @Severity(SeverityLevel.CRITICAL)
    public void viewMore() {
        MaterialOrdersPage page = reachOrdersWithCardsIfAny();
        assertThat(page.isViewMoreVisible() || page.isEmptyStateVisible()).isTrue();
    }
    @Test(priority=6, description="MO-L6: Help") @Severity(SeverityLevel.NORMAL)
    public void help() { assertThat(reachOrders().isHelpVisible() || hasText("Help")).isTrue(); }
    @Test(priority=7, description="MO-L7: material tabs") @Severity(SeverityLevel.CRITICAL)
    public void bottomTabs() { assertThat(reachOrders().areMaterialTabsVisible()).isTrue(); }
    @Test(priority=8, description="MO-L8: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachOrders(); assertThat(vendorPackage()).contains("l2b"); }
    @Test(priority=9, description="MO-L9: no Accept/Decline") @Severity(SeverityLevel.CRITICAL)
    public void noAcceptDecline() {
        reachOrders();
        SoftAssertions s = new SoftAssertions();
        s.assertThat(noAcceptVisible()).isTrue();
        s.assertThat(hasText("Decline")).isFalse();
        s.assertAll();
    }
    @Test(priority=10, description="MO-L10: no Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noLogout() { reachOrders(); assertThat(hasText("Log Out?")).isFalse(); }
}
