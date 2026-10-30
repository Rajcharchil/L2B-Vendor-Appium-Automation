package com.l2b.vendor.modules.taxation.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.taxation.presentation.pages.TaxationPage;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Taxation landing — 9000000017")
public class TaxationLandingTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="TX-L1: opens") @Severity(SeverityLevel.BLOCKER)
    public void opens() { assertThat(reachTaxation().isDisplayedNow()).isTrue(); }
    @Test(priority=2, description="TX-L2: GST Monthly") @Severity(SeverityLevel.CRITICAL)
    public void monthly() { assertThat(reachTaxation().isGstMonthlyVisible()).isTrue(); }
    @Test(priority=3, description="TX-L3: On Demand") @Severity(SeverityLevel.CRITICAL)
    public void onDemand() { assertThat(reachTaxation().isOnDemandVisible()).isTrue(); }
    @Test(priority=4, description="TX-L4: GSTR-1") @Severity(SeverityLevel.CRITICAL)
    public void gstr1() { assertThat(reachTaxation().isGstr1Visible()).isTrue(); }
    @Test(priority=5, description="TX-L5: Download Report") @Severity(SeverityLevel.CRITICAL)
    public void download() { assertThat(reachTaxation().isDownloadVisible()).isTrue(); }
    @Test(priority=6, description="TX-L6: Request Report") @Severity(SeverityLevel.CRITICAL)
    public void request() { assertThat(reachTaxation().isRequestVisible()).isTrue(); }
    @Test(priority=7, description="TX-L7: Reports empty") @Severity(SeverityLevel.NORMAL)
    public void empty() { assertThat(reachTaxation().isReportsEmptyVisible() || hasText("Reports")).isTrue(); }
    @Test(priority=8, description="TX-L8: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachTaxation(); assertThat(vendorPackage()).contains("l2b"); }
    @Test(priority=9, description="TX-L9: no Accept/Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noDanger() {
        reachTaxation();
        SoftAssertions s = new SoftAssertions();
        s.assertThat(noAcceptVisible()).isTrue();
        s.assertThat(hasText("Log Out?")).isFalse();
        s.assertAll();
    }
    @Test(priority=10, description="TX-L10: title Taxation") @Severity(SeverityLevel.CRITICAL)
    public void title() { reachTaxation(); assertThat(hasText("Taxation")).isTrue(); }
}
