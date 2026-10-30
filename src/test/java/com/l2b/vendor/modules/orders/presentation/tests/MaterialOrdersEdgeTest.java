package com.l2b.vendor.modules.orders.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.orders.presentation.pages.MaterialOrdersPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Material Orders edge — 9000000017")
public class MaterialOrdersEdgeTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="MO-E1: rotation") @Severity(SeverityLevel.NORMAL)
    public void rotation() {
        reachOrders();
        AndroidDriver a = (AndroidDriver) DriverManager.get();
        try { a.rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE); sleepQuiet(700);
              a.rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT); sleepQuiet(500);
        } catch (RuntimeException e) { Allure.parameter("rotateFail", e.getClass().getSimpleName()); }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=2, description="MO-E2: background") @Severity(SeverityLevel.CRITICAL)
    public void background() {
        reachOrders();
        try { ((AndroidDriver) DriverManager.get()).runAppInBackground(java.time.Duration.ofSeconds(2)); }
        catch (RuntimeException e) { Adb.run("shell","input","keyevent","3"); sleepQuiet(1000); }
        ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package")); sleepQuiet(1000);
        recoverVendorForeground();
        SoftAssertions s = new SoftAssertions();
        s.assertThat(vendorPackage()).contains("l2b");
        s.assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
        s.assertAll();
    }
    @Test(priority=3, description="MO-E3: offline") @Severity(SeverityLevel.CRITICAL)
    public void offline() {
        MaterialOrdersPage page = reachOrders();
        try { Adb.run("shell","cmd","connectivity","airplane-mode","enable"); sleepQuiet(500);
              SoftAssertions s = new SoftAssertions();
              s.assertThat(vendorPackage()).contains("l2b");
              s.assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
              s.assertAll();
              Allure.parameter("ordersChromeSurvived", String.valueOf(page.isDisplayedNow()));
        } finally {
            try { Adb.run("shell","cmd","connectivity","airplane-mode","disable"); } catch (RuntimeException ignored) {}
            Adb.ensureNetworkReady();
            recoverVendorForeground();
        }
    }
    @Test(priority=4, description="MO-E4: not launcher") @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() {
        recoverVendorForeground();
        reachOrders();
        assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
    }
    @Test(priority=5, description="MO-E5: no Accept") @Severity(SeverityLevel.CRITICAL)
    public void noAccept() { reachOrders(); assertThat(noAcceptVisible()).isTrue(); }
    @Test(priority=6, description="MO-E6: no Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noLogout() { reachOrders(); assertThat(hasText("Log Out?")).isFalse(); }
    @Test(priority=7, description="MO-E7: rapid tab switch") @Severity(SeverityLevel.CRITICAL)
    public void rapidTabs() {
        MaterialOrdersPage page = reachOrders();
        for (int i=0;i<4;i++) {
            page.tapActive(); sleepQuiet(200);
            page.tapCompleted(); sleepQuiet(200);
            page.tapUpcoming(); sleepQuiet(200);
        }
        assertThat(page.isDisplayedNow()).isTrue();
    }
    @Test(priority=8, description="MO-E8: no Decline") @Severity(SeverityLevel.CRITICAL)
    public void noDecline() { reachOrders(); assertThat(hasText("Decline")).isFalse(); }
    @Test(priority=9, description="MO-E9: reopen") @Severity(SeverityLevel.NORMAL)
    public void reopen() { assertThat(reachOrders().isDisplayedNow()).isTrue(); }
    @Test(priority=10, description="MO-E10: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachOrders(); assertThat(vendorPackage()).contains("l2b"); }
}
