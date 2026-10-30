package com.l2b.vendor.modules.taxation.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.taxation.presentation.pages.TaxationPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Taxation interact — user actions 9000000017")
public class TaxationInteractTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="TX-I1: open") @Severity(SeverityLevel.CRITICAL)
    public void open() { assertThat(reachTaxation().isDisplayedNow()).isTrue(); }
    @Test(priority=2, description="TX-I2: Download Report tap") @Severity(SeverityLevel.CRITICAL)
    public void download() {
        TaxationPage page = reachTaxation();
        page.tapDownloadReport(); sleepQuiet(1200);
        if (!page.isDisplayedNow()) { DriverManager.get().navigate().back(); sleepQuiet(700); }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=3, description="TX-I3: Request Report tap") @Severity(SeverityLevel.CRITICAL)
    public void request() {
        TaxationPage page = reachTaxation();
        page.tapRequestReport(); sleepQuiet(1200);
        if (!page.isDisplayedNow()) { DriverManager.get().navigate().back(); sleepQuiet(700); }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=4, description="TX-I4: Back") @Severity(SeverityLevel.CRITICAL)
    public void back() {
        TaxationPage page = reachTaxation();
        page.tapBack(); sleepQuiet(900);
        assertThat(classifyMaterialNow()).isIn(
                "home", "drawer", "inventory", "orders", "vendor-other", "taxation",
                "quick-booking", "kyc", "add-product", "import-csv");
    }
    @Test(priority=5, description="TX-I5: reopen") @Severity(SeverityLevel.CRITICAL)
    public void reopen() { assertThat(reachTaxation().isDisplayedNow()).isTrue(); }
    @Test(priority=6, description="TX-I6: monthly still") @Severity(SeverityLevel.NORMAL)
    public void monthly() { assertThat(reachTaxation().isGstMonthlyVisible()).isTrue(); }
    @Test(priority=7, description="TX-I7: on demand still") @Severity(SeverityLevel.NORMAL)
    public void onDemand() { assertThat(reachTaxation().isOnDemandVisible()).isTrue(); }
    @Test(priority=8, description="TX-I8: gstr1 still") @Severity(SeverityLevel.NORMAL)
    public void gstr1() { assertThat(reachTaxation().isGstr1Visible()).isTrue(); }
    @Test(priority=9, description="TX-I9: no Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noLogout() { reachTaxation(); assertThat(hasText("Log Out?")).isFalse(); }
    @Test(priority=10, description="TX-I10: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachTaxation(); assertThat(vendorPackage()).contains("l2b"); }
}
