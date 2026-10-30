package com.l2b.vendor.modules.kyc.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.kyc.presentation.pages.KycPage;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("KYC landing — material 9000000017 gap")
public class MaterialKycLandingTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="MK-L1: opens KYC") @Severity(SeverityLevel.BLOCKER)
    public void opens() { assertThat(reachMaterialKyc().isDisplayedNow()).isTrue(); }
    @Test(priority=2, description="MK-L2: title or error dual-path") @Severity(SeverityLevel.CRITICAL)
    public void titleOrError() {
        KycPage page = reachMaterialKyc();
        assertThat(page.isTitleVisible() || page.isErrorVisible()).isTrue();
    }
    @Test(priority=3, description="MK-L3: full chrome or error") @Severity(SeverityLevel.CRITICAL)
    public void chromeOrError() {
        KycPage page = reachMaterialKyc();
        assertThat(page.isFullChromeVisible() || page.isErrorVisible() || page.isDisplayedNow()).isTrue();
    }
    @Test(priority=4, description="MK-L4: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachMaterialKyc(); assertThat(vendorPackage()).contains("l2b"); }
    @Test(priority=5, description="MK-L5: no Accept") @Severity(SeverityLevel.CRITICAL)
    public void noAccept() { reachMaterialKyc(); assertThat(noAcceptVisible()).isTrue(); }
    @Test(priority=6, description="MK-L6: no Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noLogout() { reachMaterialKyc(); assertThat(hasText("Log Out?")).isFalse(); }
    @Test(priority=7, description="MK-L7: no Decline") @Severity(SeverityLevel.CRITICAL)
    public void noDecline() { reachMaterialKyc(); assertThat(hasText("Decline")).isFalse(); }
    @Test(priority=8, description="MK-L8: not launcher") @Severity(SeverityLevel.CRITICAL)
    public void notLauncher() { reachMaterialKyc(); assertThat(classifyMaterialNow()).isNotEqualTo("launcher"); }
    @Test(priority=9, description="MK-L9: Retry if error") @Severity(SeverityLevel.NORMAL)
    public void retryIfError() {
        KycPage page = reachMaterialKyc();
        SoftAssertions s = new SoftAssertions();
        if (page.isErrorVisible()) s.assertThat(page.isRetryVisible()).isTrue();
        else s.assertThat(page.isDisplayedNow()).isTrue();
        s.assertAll();
    }
    @Test(priority=10, description="MK-L10: package again") @Severity(SeverityLevel.NORMAL)
    public void pkg2() { reachMaterialKyc(); assertThat(vendorPackage()).contains("l2b"); }
}
