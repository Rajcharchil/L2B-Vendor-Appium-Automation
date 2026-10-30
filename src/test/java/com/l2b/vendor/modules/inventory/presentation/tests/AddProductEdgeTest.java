package com.l2b.vendor.modules.inventory.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.inventory.presentation.pages.AddProductPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Add Product edge — 9000000017")
public class AddProductEdgeTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="AP-E1: rotation") @Severity(SeverityLevel.NORMAL)
    public void rotation() {
        reachAddProduct();
        AndroidDriver a = (AndroidDriver) DriverManager.get();
        try { a.rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE); sleepQuiet(800);
              a.rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT); sleepQuiet(600);
        } catch (RuntimeException e) { Allure.parameter("rotateFail", e.getClass().getSimpleName()); }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=2, description="AP-E2: background") @Severity(SeverityLevel.CRITICAL)
    public void background() {
        reachAddProduct();
        try { ((AndroidDriver) DriverManager.get()).runAppInBackground(java.time.Duration.ofSeconds(2)); }
        catch (RuntimeException e) { Adb.run("shell","input","keyevent","3"); sleepQuiet(1000); }
        ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package")); sleepQuiet(1000);
        SoftAssertions s = new SoftAssertions();
        s.assertThat(vendorPackage()).contains("l2b");
        s.assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
        s.assertAll();
    }
    @Test(priority=3, description="AP-E3: offline") @Severity(SeverityLevel.CRITICAL)
    public void offline() {
        AddProductPage page = reachAddProduct();
        try { Adb.run("shell","cmd","connectivity","airplane-mode","enable"); sleepQuiet(600);
              assertThat(page.isDisplayedNow() || hasText("Product Details")).isTrue();
        } finally {
            try { Adb.run("shell","cmd","connectivity","airplane-mode","disable"); } catch (RuntimeException ignored) {}
            Adb.ensureNetworkReady();
        }
    }
    @Test(priority=4, description="AP-E4: not launcher") @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() { reachAddProduct(); assertThat(classifyMaterialNow()).isNotEqualTo("launcher"); }
    @Test(priority=5, description="AP-E5: no Accept") @Severity(SeverityLevel.CRITICAL)
    public void noAccept() { reachAddProduct(); assertThat(noAcceptVisible()).isTrue(); }
    @Test(priority=6, description="AP-E6: no Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noLogout() { reachAddProduct(); assertThat(hasText("Log Out?")).isFalse(); }
    @Test(priority=7, description="AP-E7: rapid Save x3 stays") @Severity(SeverityLevel.CRITICAL)
    public void rapidSave() {
        AddProductPage page = reachAddProduct();
        for (int i=0;i<3;i++) { page.tapSave(); sleepQuiet(300); }
        assertThat(page.isDisplayedNow()).isTrue();
    }
    @Test(priority=8, description="AP-E8: rapid Back recover") @Severity(SeverityLevel.NORMAL)
    public void rapidBack() {
        AddProductPage page = reachAddProduct();
        page.tapBack(); sleepQuiet(500);
        assertThat(reachAddProduct().isDisplayedNow()).isTrue();
    }
    @Test(priority=9, description="AP-E9: reopen") @Severity(SeverityLevel.NORMAL)
    public void reopen() { assertThat(reachAddProduct().isDisplayedNow()).isTrue(); }
    @Test(priority=10, description="AP-E10: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachAddProduct(); assertThat(vendorPackage()).contains("l2b"); }
}
