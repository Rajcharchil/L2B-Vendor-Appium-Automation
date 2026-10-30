package com.l2b.vendor.modules.orders.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.home.presentation.pages.HomePage;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.orders.presentation.pages.MaterialOrdersPage;
import io.qameta.allure.*;
import org.testng.SkipException;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Material Orders interact — every tab/action 9000000017")
public class MaterialOrdersInteractTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="MO-I1: tap Active") @Severity(SeverityLevel.CRITICAL)
    public void active() {
        MaterialOrdersPage page = reachOrders();
        page.tapActive(); sleepQuiet(700);
        assertThat(page.areTabsVisible()).isTrue();
    }
    @Test(priority=2, description="MO-I2: tap Completed") @Severity(SeverityLevel.CRITICAL)
    public void completed() {
        MaterialOrdersPage page = reachOrders();
        page.tapCompleted(); sleepQuiet(700);
        assertThat(page.areTabsVisible()).isTrue();
    }
    @Test(priority=3, description="MO-I3: tap Upcoming") @Severity(SeverityLevel.CRITICAL)
    public void upcoming() {
        MaterialOrdersPage page = reachOrders();
        page.tapUpcoming(); sleepQuiet(700);
        assertThat(page.isDisplayedNow()).isTrue();
    }
    @Test(priority=4, description="MO-I4: Get Direction then Back") @Severity(SeverityLevel.CRITICAL)
    public void getDirectionBack() {
        MaterialOrdersPage page = reachOrders();
        if (page.isGetDirectionVisible()) {
            page.tapGetDirection(); sleepQuiet(2000);
            recoverVendorForeground();
            try { DriverManager.get().navigate().back(); } catch (RuntimeException ignored) {}
            sleepQuiet(800);
            recoverVendorForeground();
        }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=5, description="MO-I5: Help then Back") @Severity(SeverityLevel.NORMAL)
    public void helpBack() {
        MaterialOrdersPage page = reachOrders();
        if (page.isHelpVisible()) {
            page.tapHelp(); sleepQuiet(1200);
            recoverVendorForeground();
            try { DriverManager.get().navigate().back(); } catch (RuntimeException ignored) {}
            sleepQuiet(800);
            recoverVendorForeground();
        }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=6, description="MO-I6: Home See all") @Severity(SeverityLevel.CRITICAL)
    public void homeSeeAll() {
        recoverVendorForeground();
        reachMaterialHome();
        HomePage home = new HomePage();
        if (hasText("See all")) {
            tapText("See all");
            sleepQuiet(1000);
            assertThat(new MaterialOrdersPage().isDisplayedNow())
                    .as("See all navigates to Orders list")
                    .isTrue();
        } else if (home.isEmptyOrdersCopyVisible()) {
            Allure.parameter("seeAllInapplicable", "emptyHome");
            throw new SkipException("MO-I6 inapplicable: empty Home — See all not present");
        } else {
            throw new AssertionError(
                    "Material Home: expected See all or empty Orders copy ('No orders right now.'), found neither");
        }
    }
    @Test(priority=7, description="MO-I7: reopen Orders tab") @Severity(SeverityLevel.CRITICAL)
    public void reopen() { assertThat(reachOrders().isDisplayedNow()).isTrue(); }
    @Test(priority=8, description="MO-I8: card chrome or empty after reopen") @Severity(SeverityLevel.NORMAL)
    public void cards() {
        MaterialOrdersPage page = reachOrdersWithCardsIfAny();
        assertThat(page.isCardChromeVisible() || page.isEmptyStateVisible()).isTrue();
    }
    @Test(priority=9, description="MO-I9: View More or empty") @Severity(SeverityLevel.NORMAL)
    public void viewMore() {
        MaterialOrdersPage page = reachOrdersWithCardsIfAny();
        assertThat(page.isViewMoreVisible() || page.isEmptyStateVisible()).isTrue();
    }
    @Test(priority=10, description="MO-I10: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachOrders(); assertThat(vendorPackage()).contains("l2b"); }
}
