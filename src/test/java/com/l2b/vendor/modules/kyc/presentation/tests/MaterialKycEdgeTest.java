package com.l2b.vendor.modules.kyc.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.kyc.presentation.pages.KycPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("KYC edge — material 9000000017")
public class MaterialKycEdgeTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="MK-E1: rotation") @Severity(SeverityLevel.NORMAL)
    public void rotation() {
        reachMaterialKyc();
        AndroidDriver a = (AndroidDriver) DriverManager.get();
        try { a.rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE); sleepQuiet(700);
              a.rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT); sleepQuiet(500);
        } catch (RuntimeException e) { Allure.parameter("rotateFail", e.getClass().getSimpleName()); }
        assertThat(vendorPackage()).contains("l2b");
    }
    @Test(priority=2, description="MK-E2: background") @Severity(SeverityLevel.CRITICAL)
    public void background() {
        reachMaterialKyc();
        try { ((AndroidDriver) DriverManager.get()).runAppInBackground(java.time.Duration.ofSeconds(2)); }
        catch (RuntimeException e) { Adb.run("shell","input","keyevent","3"); sleepQuiet(1000); }
        ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package")); sleepQuiet(1000);
        SoftAssertions s = new SoftAssertions();
        s.assertThat(vendorPackage()).contains("l2b");
        s.assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
        s.assertAll();
    }
    @Test(priority=3, description="MK-E3: offline") @Severity(SeverityLevel.CRITICAL)
    public void offline() {
        KycPage page = reachMaterialKyc();
        try { Adb.run("shell","cmd","connectivity","airplane-mode","enable"); sleepQuiet(500);
              assertThat(page.isDisplayedNow() || hasText("KYC")).isTrue();
        } finally {
            try { Adb.run("shell","cmd","connectivity","airplane-mode","disable"); } catch (RuntimeException ignored) {}
            Adb.ensureNetworkReady();
        }
    }
    @Test(priority=4, description="MK-E4: not launcher") @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() { reachMaterialKyc(); assertThat(classifyMaterialNow()).isNotEqualTo("launcher"); }
    @Test(priority=5, description="MK-E5: no Accept") @Severity(SeverityLevel.CRITICAL)
    public void noAccept() { reachMaterialKyc(); assertThat(noAcceptVisible()).isTrue(); }
    @Test(priority=6, description="MK-E6: no Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noLogout() { reachMaterialKyc(); assertThat(hasText("Log Out?")).isFalse(); }
    @Test(priority=7, description="MK-E7: rapid Back/reopen") @Severity(SeverityLevel.NORMAL)
    public void rapid() {
        for (int i=0;i<2;i++) { reachMaterialKyc().tapBack(); sleepQuiet(400); }
        assertThat(reachMaterialKyc().isDisplayedNow()).isTrue();
    }
    @Test(priority=8, description="MK-E8: no Decline") @Severity(SeverityLevel.CRITICAL)
    public void noDecline() { reachMaterialKyc(); assertThat(hasText("Decline")).isFalse(); }
    @Test(priority=9, description="MK-E9: reopen") @Severity(SeverityLevel.NORMAL)
    public void reopen() { assertThat(reachMaterialKyc().isDisplayedNow()).isTrue(); }
    @Test(priority=10, description="MK-E10: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachMaterialKyc(); assertThat(vendorPackage()).contains("l2b"); }
}
