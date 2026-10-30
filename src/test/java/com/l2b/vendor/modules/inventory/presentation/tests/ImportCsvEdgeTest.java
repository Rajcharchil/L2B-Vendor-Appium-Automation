package com.l2b.vendor.modules.inventory.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.inventory.presentation.pages.ImportCsvPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Import CSV edge — 9000000017")
public class ImportCsvEdgeTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="IC-E1: rotation") @Severity(SeverityLevel.NORMAL)
    public void rotation() {
        reachImportCsv();
        AndroidDriver a = (AndroidDriver) DriverManager.get();
        try { a.rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE); sleepQuiet(700);
              a.rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT); sleepQuiet(500);
        } catch (RuntimeException e) { Allure.parameter("rotateFail", e.getClass().getSimpleName()); }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=2, description="IC-E2: background") @Severity(SeverityLevel.CRITICAL)
    public void background() {
        reachImportCsv();
        try { ((AndroidDriver) DriverManager.get()).runAppInBackground(java.time.Duration.ofSeconds(2)); }
        catch (RuntimeException e) { Adb.run("shell","input","keyevent","3"); sleepQuiet(1000); }
        ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package")); sleepQuiet(1000);
        SoftAssertions s = new SoftAssertions();
        s.assertThat(vendorPackage()).contains("l2b");
        s.assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
        s.assertAll();
    }
    @Test(priority=3, description="IC-E3: offline") @Severity(SeverityLevel.CRITICAL)
    public void offline() {
        ImportCsvPage page = reachImportCsv();
        try { Adb.run("shell","cmd","connectivity","airplane-mode","enable"); sleepQuiet(500);
              assertThat(page.isDisplayedNow() || hasText("Import CSV")).isTrue();
        } finally {
            try { Adb.run("shell","cmd","connectivity","airplane-mode","disable"); } catch (RuntimeException ignored) {}
            Adb.ensureNetworkReady();
        }
    }
    @Test(priority=4, description="IC-E4: not launcher") @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() { reachImportCsv(); assertThat(classifyMaterialNow()).isNotEqualTo("launcher"); }
    @Test(priority=5, description="IC-E5: no Accept") @Severity(SeverityLevel.CRITICAL)
    public void noAccept() { reachImportCsv(); assertThat(noAcceptVisible()).isTrue(); }
    @Test(priority=6, description="IC-E6: no Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noLogout() { reachImportCsv(); assertThat(hasText("Log Out?")).isFalse(); }
    @Test(priority=7, description="IC-E7: rapid open/close") @Severity(SeverityLevel.NORMAL)
    public void rapidOpenClose() {
        for (int i=0;i<3;i++) {
            ImportCsvPage p = reachImportCsv();
            p.closeWithBack(); sleepQuiet(800);
            recoverVendorForeground();
            if (!new com.l2b.vendor.modules.inventory.presentation.pages.InventoryPage().isDisplayedNow()
                    && materialTabsPresent()) {
                tapBottomTab("Inventory");
                sleepQuiet(700);
            }
        }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=8, description="IC-E8: reopen") @Severity(SeverityLevel.NORMAL)
    public void reopen() { assertThat(reachImportCsv().isDisplayedNow()).isTrue(); }
    @Test(priority=9, description="IC-E9: download still safe") @Severity(SeverityLevel.NORMAL)
    public void downloadSafe() {
        ImportCsvPage p = reachImportCsv();
        if (p.isDownloadSampleVisible()) { p.tapDownloadSample(); sleepQuiet(800); }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=10, description="IC-E10: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachImportCsv(); assertThat(vendorPackage()).contains("l2b"); }
}
