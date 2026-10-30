package com.l2b.vendor.modules.inventory.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.modules.inventory.presentation.pages.ImportCsvPage;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Import CSV landing — 9000000017")
public class ImportCsvLandingTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="IC-L1: opens") @Severity(SeverityLevel.BLOCKER)
    public void opens() { assertThat(reachImportCsv().isDisplayedNow()).isTrue(); }
    @Test(priority=2, description="IC-L2: import copy") @Severity(SeverityLevel.CRITICAL)
    public void copy() { assertThat(reachImportCsv().isImportCopyVisible()).isTrue(); }
    @Test(priority=3, description="IC-L3: accepts types") @Severity(SeverityLevel.CRITICAL)
    public void accepts() { assertThat(reachImportCsv().isAcceptsTypesVisible()).isTrue(); }
    @Test(priority=4, description="IC-L4: Download sample") @Severity(SeverityLevel.CRITICAL)
    public void download() { assertThat(reachImportCsv().isDownloadSampleVisible()).isTrue(); }
    @Test(priority=5, description="IC-L5: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachImportCsv(); assertThat(vendorPackage()).contains("l2b"); }
    @Test(priority=6, description="IC-L6: no Accept") @Severity(SeverityLevel.CRITICAL)
    public void noAccept() { reachImportCsv(); assertThat(noAcceptVisible()).isTrue(); }
    @Test(priority=7, description="IC-L7: no Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noLogout() { reachImportCsv(); assertThat(hasText("Log Out?")).isFalse(); }
    @Test(priority=8, description="IC-L8: Import CSV label") @Severity(SeverityLevel.NORMAL)
    public void importLabel() { reachImportCsv(); assertThat(hasText("Import CSV")).isTrue(); }
    @Test(priority=9, description="IC-L9: still Inventory underneath or sheet") @Severity(SeverityLevel.NORMAL)
    public void stillVendor() { reachImportCsv(); assertThat(vendorPackage()).contains("l2b"); }
    @Test(priority=10, description="IC-L10: no Decline") @Severity(SeverityLevel.CRITICAL)
    public void noDecline() {
        reachImportCsv();
        SoftAssertions s = new SoftAssertions();
        s.assertThat(hasText("Decline")).isFalse();
        s.assertThat(noAcceptVisible()).isTrue();
        s.assertAll();
    }
}
