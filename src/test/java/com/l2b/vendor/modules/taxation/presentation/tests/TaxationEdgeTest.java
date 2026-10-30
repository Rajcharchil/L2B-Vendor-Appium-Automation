package com.l2b.vendor.modules.taxation.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.taxation.presentation.pages.TaxationPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Taxation edge — 9000000017")
public class TaxationEdgeTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="TX-E1: rotation") @Severity(SeverityLevel.NORMAL)
    public void rotation() {
        reachTaxation();
        AndroidDriver a = (AndroidDriver) DriverManager.get();
        try { a.rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE); sleepQuiet(700);
              a.rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT); sleepQuiet(500);
        } catch (RuntimeException e) { Allure.parameter("rotateFail", e.getClass().getSimpleName()); }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=2, description="TX-E2: background") @Severity(SeverityLevel.CRITICAL)
    public void background() {
        reachTaxation();
        try { ((AndroidDriver) DriverManager.get()).runAppInBackground(java.time.Duration.ofSeconds(2)); }
        catch (RuntimeException e) { Adb.run("shell","input","keyevent","3"); sleepQuiet(1000); }
        ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package")); sleepQuiet(1000);
        SoftAssertions s = new SoftAssertions();
        s.assertThat(vendorPackage()).contains("l2b");
        s.assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
        s.assertAll();
    }
    @Test(priority=3, description="TX-E3: offline") @Severity(SeverityLevel.CRITICAL)
    public void offline() {
        TaxationPage page = reachTaxation();
        try { Adb.run("shell","cmd","connectivity","airplane-mode","enable"); sleepQuiet(500);
              assertThat(page.isDisplayedNow() || hasText("Taxation")).isTrue();
        } finally {
            try { Adb.run("shell","cmd","connectivity","airplane-mode","disable"); } catch (RuntimeException ignored) {}
            Adb.ensureNetworkReady();
        }
    }
    @Test(priority=4, description="TX-E4: not launcher") @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() { reachTaxation(); assertThat(classifyMaterialNow()).isNotEqualTo("launcher"); }
    @Test(priority=5, description="TX-E5: no Accept") @Severity(SeverityLevel.CRITICAL)
    public void noAccept() { reachTaxation(); assertThat(noAcceptVisible()).isTrue(); }
    @Test(priority=6, description="TX-E6: no Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noLogout() { reachTaxation(); assertThat(hasText("Log Out?")).isFalse(); }
    @Test(priority=7, description="TX-E7: rapid Download") @Severity(SeverityLevel.NORMAL)
    public void rapidDownload() {
        TaxationPage page = reachTaxation();
        for (int i=0;i<3;i++) {
            if (page.isDownloadVisible()) { page.tapDownloadReport(); sleepQuiet(400);
                if (!page.isDisplayedNow()) { DriverManager.get().navigate().back(); sleepQuiet(400); page = reachTaxation(); }
            }
        }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=8, description="TX-E8: reopen") @Severity(SeverityLevel.NORMAL)
    public void reopen() { assertThat(reachTaxation().isDisplayedNow()).isTrue(); }
    @Test(priority=9, description="TX-E9: empty reports still") @Severity(SeverityLevel.NORMAL)
    public void empty() { assertThat(reachTaxation().isReportsEmptyVisible() || hasText("Reports")).isTrue(); }
    @Test(priority=10, description="TX-E10: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachTaxation(); assertThat(vendorPackage()).contains("l2b"); }
}
