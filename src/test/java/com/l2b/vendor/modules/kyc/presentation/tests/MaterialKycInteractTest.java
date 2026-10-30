package com.l2b.vendor.modules.kyc.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.kyc.presentation.pages.KycPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("KYC interact — material 9000000017")
public class MaterialKycInteractTest extends MaterialSurfaceBaseTest {
    @Test(priority=1, description="MK-I1: open") @Severity(SeverityLevel.CRITICAL)
    public void open() { assertThat(reachMaterialKyc().isDisplayedNow()).isTrue(); }
    @Test(priority=2, description="MK-I2: Back") @Severity(SeverityLevel.CRITICAL)
    public void back() {
        KycPage page = reachMaterialKyc();
        page.tapBack(); sleepQuiet(900);
        assertThat(classifyMaterialNow()).isNotEqualTo("launcher");
    }
    @Test(priority=3, description="MK-I3: reopen") @Severity(SeverityLevel.CRITICAL)
    public void reopen() { assertThat(reachMaterialKyc().isDisplayedNow()).isTrue(); }
    @Test(priority=4, description="MK-I4: Retry if present") @Severity(SeverityLevel.NORMAL)
    public void retry() {
        KycPage page = reachMaterialKyc();
        if (page.isRetryVisible()) { page.tapRetry(); sleepQuiet(1200); }
        assertThat(page.isDisplayedNow()).isTrue();
    }
    @Test(priority=5, description="MK-I5: title/error after reopen") @Severity(SeverityLevel.NORMAL)
    public void title() {
        KycPage page = reachMaterialKyc();
        assertThat(page.isTitleVisible() || page.isErrorVisible()).isTrue();
    }
    @Test(priority=6, description="MK-I6: no Accept") @Severity(SeverityLevel.CRITICAL)
    public void noAccept() { reachMaterialKyc(); assertThat(noAcceptVisible()).isTrue(); }
    @Test(priority=7, description="MK-I7: no Log Out") @Severity(SeverityLevel.CRITICAL)
    public void noLogout() { reachMaterialKyc(); assertThat(hasText("Log Out?")).isFalse(); }
    @Test(priority=8, description="MK-I8: Back again") @Severity(SeverityLevel.NORMAL)
    public void back2() { reachMaterialKyc().tapBack(); sleepQuiet(700); assertThat(vendorPackage()).contains("l2b"); }
    @Test(priority=9, description="MK-I9: reopen 2") @Severity(SeverityLevel.NORMAL)
    public void reopen2() { assertThat(reachMaterialKyc().isDisplayedNow()).isTrue(); }
    @Test(priority=10, description="MK-I10: package") @Severity(SeverityLevel.NORMAL)
    public void pkg() { reachMaterialKyc(); assertThat(vendorPackage()).contains("l2b"); }
}
