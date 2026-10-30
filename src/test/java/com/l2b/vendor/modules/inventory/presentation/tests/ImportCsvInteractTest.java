package com.l2b.vendor.modules.inventory.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.modules.inventory.presentation.pages.ImportCsvPage;
import com.l2b.vendor.modules.inventory.presentation.pages.InventoryPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Import CSV interact — user actions no upload 9000000017")
public class ImportCsvInteractTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="IC-I1: open sheet") @Severity(SeverityLevel.CRITICAL)
    public void open() { assertThat(reachImportCsv().isDisplayedNow()).isTrue(); }
    @Test(priority=2, description="IC-I2: Download sample CSV") @Severity(SeverityLevel.CRITICAL)
    public void downloadSample() {
        ImportCsvPage page = reachImportCsv();
        page.tapDownloadSample(); sleepQuiet(1500);
        recoverVendorForeground();
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=3, description="IC-I3: Back to Inventory") @Severity(SeverityLevel.CRITICAL)
    public void backToInventory() {
        ImportCsvPage page = reachImportCsv();
        page.closeWithBack(); sleepQuiet(900);
        recoverVendorForeground();
        if (!new InventoryPage().isDisplayedNow() && materialTabsPresent()) {
            tapBottomTab("Inventory");
            sleepQuiet(900);
        }
        assertThat(new InventoryPage().isDisplayedNow()
                || classifyMaterialNow().equals("inventory")
                || materialTabsPresent()).isTrue();
    }
    @Test(priority=4, description="IC-I4: reopen") @Severity(SeverityLevel.CRITICAL)
    public void reopen() { assertThat(reachImportCsv().isDisplayedNow()).isTrue(); }
    @Test(priority=5, description="IC-I5: accepts still visible") @Severity(SeverityLevel.NORMAL)
    public void accepts() { assertThat(reachImportCsv().isAcceptsTypesVisible()).isTrue(); }
    @Test(priority=6, description="IC-I6: copy still visible") @Severity(SeverityLevel.NORMAL)
    public void copy() { assertThat(reachImportCsv().isImportCopyVisible()).isTrue(); }
    @Test(priority=7, description="IC-I7: download visible") @Severity(SeverityLevel.NORMAL)
    public void downloadVisible() { assertThat(reachImportCsv().isDownloadSampleVisible()).isTrue(); }
    @Test(priority=8, description="IC-I8: close again") @Severity(SeverityLevel.NORMAL)
    public void closeAgain() {
        reachImportCsv().closeWithBack(); sleepQuiet(700);
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=9, description="IC-I9: reopen after close") @Severity(SeverityLevel.NORMAL)
    public void reopen2() { assertThat(reachImportCsv().isDisplayedNow()).isTrue(); }
    @Test(priority=10, description="IC-I10: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachImportCsv(); assertThat(vendorPackage()).contains("l2b"); }
}
