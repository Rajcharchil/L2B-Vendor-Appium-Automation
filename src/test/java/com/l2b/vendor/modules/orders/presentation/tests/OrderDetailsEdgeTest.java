package com.l2b.vendor.modules.orders.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.orders.presentation.pages.OrderDetailsPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Order details edge — 9000000017")
public class OrderDetailsEdgeTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="OD-E1: rotation") @Severity(SeverityLevel.NORMAL)
    public void rotation() {
        reachOrderDetails();
        AndroidDriver a = (AndroidDriver) DriverManager.get();
        try { a.rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE); sleepQuiet(700);
              a.rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT); sleepQuiet(500);
        } catch (RuntimeException e) { Allure.parameter("rotateFail", e.getClass().getSimpleName()); }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=2, description="OD-E2: background") @Severity(SeverityLevel.CRITICAL)
    public void background() {
        reachOrderDetails();
        try { ((AndroidDriver) DriverManager.get()).runAppInBackground(java.time.Duration.ofSeconds(2)); }
        catch (RuntimeException e) { Adb.run("shell","input","keyevent","3"); sleepQuiet(1000); }
        ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package")); sleepQuiet(1000);
        SoftAssertions s = new SoftAssertions();
        s.assertThat(vendorPackage()).contains("l2b");
        s.assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
        s.assertAll();
    }
    @Test(priority=3, description="OD-E3: offline") @Severity(SeverityLevel.CRITICAL)
    public void offline() {
        OrderDetailsPage page = reachOrderDetails();
        try { Adb.run("shell","cmd","connectivity","airplane-mode","enable"); sleepQuiet(500);
              SoftAssertions s = new SoftAssertions();
              s.assertThat(vendorPackage()).contains("l2b");
              s.assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
              s.assertAll();
              Allure.parameter("odChromeSurvived", String.valueOf(page.isDisplayedNow()));
        } finally {
            try { Adb.run("shell","cmd","connectivity","airplane-mode","disable"); } catch (RuntimeException ignored) {}
            Adb.ensureNetworkReady();
        }
    }
    @Test(priority=4, description="OD-E4: not launcher") @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() { reachOrderDetails(); assertThat(classifyMaterialNow()).isNotEqualTo("launcher"); }
    @Test(priority=5, description="OD-E5: no Accept") @Severity(SeverityLevel.CRITICAL)
    public void noAccept() { reachOrderDetails(); assertThat(noAcceptVisible()).isTrue(); }
    @Test(priority=6, description="OD-E6: no Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noLogout() { reachOrderDetails(); assertThat(hasText("Log Out?")).isFalse(); }
    @Test(priority=7, description="OD-E7: rapid Back/reopen") @Severity(SeverityLevel.NORMAL)
    public void rapidBack() {
        for (int i=0;i<2;i++) {
            OrderDetailsPage p = reachOrderDetails();
            p.tapBack(); sleepQuiet(500);
        }
        assertThat(reachOrderDetails().isDisplayedNow()).isTrue();
    }
    @Test(priority=8, description="OD-E8: no Decline") @Severity(SeverityLevel.CRITICAL)
    public void noDecline() { reachOrderDetails(); assertThat(hasText("Decline")).isFalse(); }
    @Test(priority=9, description="OD-E9: reopen") @Severity(SeverityLevel.NORMAL)
    public void reopen() { assertThat(reachOrderDetails().isDisplayedNow()).isTrue(); }
    @Test(priority=10, description="OD-E10: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachOrderDetails(); assertThat(vendorPackage()).contains("l2b"); }
}
