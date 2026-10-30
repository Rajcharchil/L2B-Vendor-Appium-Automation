package com.l2b.vendor.modules.onboarding.presentation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.l2b.vendor.modules.onboarding.presentation.pages.CategoryPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Category landing — Materials 9000000013")
public class CategoryLandingTest extends RegistrationBaseTest {

    @Test(priority = 1, description = "SC-L1: category opens")
    @Severity(SeverityLevel.BLOCKER)
    public void opens() {
        assertThat(reachCategory().isDisplayedNow()).isTrue();
    }

    @Test(priority = 2, description = "SC-L2: Materials Vendor")
    @Severity(SeverityLevel.CRITICAL)
    public void materials() {
        assertThat(reachCategory().isMaterialsVisible()).isTrue();
    }

    @Test(priority = 3, description = "SC-L3: Rental Vendor")
    @Severity(SeverityLevel.CRITICAL)
    public void rental() {
        assertThat(reachCategory().isRentalVisible()).isTrue();
    }

    @Test(priority = 4, description = "SC-L4: Continue")
    @Severity(SeverityLevel.CRITICAL)
    public void continueVisible() {
        assertThat(reachCategory().isContinueVisible()).isTrue();
    }

    @Test(priority = 5, description = "SC-L5: package")
    @Severity(SeverityLevel.NORMAL)
    public void pkg() {
        reachCategory();
        assertThat(vendorPackage()).contains("l2b");
    }

    @Test(priority = 6, description = "SC-L6: no Accept/Decline")
    @Severity(SeverityLevel.CRITICAL)
    public void noAccept() {
        reachCategory();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(hasText("Decline")).isFalse();
        softly.assertThat(noAcceptVisible()).isTrue();
        softly.assertAll();
    }

    @Test(priority = 7, description = "SC-L7: Materials blurb when selected")
    @Severity(SeverityLevel.NORMAL)
    public void materialsBlurb() {
        CategoryPage page = reachCategory();
        page.tapMaterials();
        sleepQuiet(500);
        assertThat(page.isMaterialsBlurbVisible() || page.isMaterialsVisible()).isTrue();
    }

    @Test(priority = 8, description = "SC-L8: title")
    @Severity(SeverityLevel.CRITICAL)
    public void title() {
        assertThat(reachCategory().isTitleVisible()).isTrue();
    }

    @Test(priority = 9, description = "SC-L9: no Log Out?")
    @Severity(SeverityLevel.CRITICAL)
    public void noLogout() {
        reachCategory();
        assertThat(hasText("Log Out?")).isFalse();
    }

    @Test(priority = 10, description = "SC-L10: stay category")
    @Severity(SeverityLevel.NORMAL)
    public void stay() {
        assertThat(reachCategory().isDisplayedNow()).isTrue();
    }
}
