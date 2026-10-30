package com.l2b.vendor.modules.onboarding.presentation.tests;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.core.locators.ComposeLocators;
import com.l2b.vendor.core.ui.BaseTest;
import com.l2b.vendor.core.wait.Waits;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.onboarding.domain.RoleEnvironment;
import com.l2b.vendor.modules.onboarding.presentation.pages.CategoryPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.LanguagePage;
import com.l2b.vendor.modules.onboarding.presentation.pages.OnboardingCarouselPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.OtpPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.PinWarehousePage;
import com.l2b.vendor.modules.onboarding.presentation.pages.RolePage;
import com.l2b.vendor.modules.onboarding.presentation.pages.SignUpPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsMaterialPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.lang.reflect.Method;
import java.time.Duration;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeSuite;

/**
 * Shared warm path for registration leftovers on 9000000013.
 * Materials path: Role → Vendor → Category → Materials → Tell us Materials.
 * Never Log Out Confirm / Accept / Decline / force-stop.
 */
public abstract class RegistrationBaseTest extends BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void prepareRegistrationEnvironment() {
        RoleEnvironment.prepareSuite();
    }

    @Override
    protected boolean noReset() {
        return RoleEnvironment.noReset();
    }

    @Override
    protected boolean newSessionPerMethod() {
        return RoleEnvironment.newSessionPerMethod();
    }

    @Override
    protected void beforeCreateDriver(Method method) {
        try {
            Adb.ensureNetworkReady();
        } catch (RuntimeException e) {
            Allure.parameter("networkWarn", e.getMessage());
        }
    }

    @Step("Activate Vendor app")
    protected void activateVendor() {
        try {
            ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package"));
        } catch (RuntimeException ignored) {
        }
        sleepQuiet(600);
    }

    @Step("Reach Select your role")
    protected RolePage reachRole() {
        activateVendor();
        dismissToRole();
        RolePage page = new RolePage();
        if (!page.isDisplayedNow()) {
            restoreLoginIfNeeded();
            dismissToRole();
        }
        page.waitUntilLoaded();
        Allure.parameter("after", classifyNow());
        return page;
    }

    @Step("Reach category via Vendor / Owner")
    protected CategoryPage reachCategory() {
        RolePage role = reachRole();
        role.tapVendor();
        sleepQuiet(500);
        role.tapNext();
        sleepQuiet(1200);
        CategoryPage page = new CategoryPage();
        page.waitUntilLoaded();
        Allure.parameter("after", classifyNow());
        return page;
    }

    @Step("Reach Tell us Materials")
    protected TellUsMaterialPage reachTellUsMaterial() {
        if (new PinWarehousePage().isDisplayedNow()) {
            try {
                new PinWarehousePage().tapClose();
            } catch (RuntimeException e) {
                DriverManager.get().navigate().back();
            }
            sleepQuiet(800);
        }
        if (new TellUsMaterialPage().isDisplayedNow()) {
            Allure.parameter("after", classifyNow());
            return new TellUsMaterialPage();
        }
        CategoryPage cat = reachCategory();
        cat.tapMaterials();
        sleepQuiet(500);
        cat.tapContinue();
        sleepQuiet(1500);
        TellUsMaterialPage page = new TellUsMaterialPage();
        page.waitUntilLoaded();
        Allure.parameter("after", classifyNow());
        return page;
    }

    @Step("Reach Pin your warehouse")
    protected PinWarehousePage reachPinWarehouse() {
        if (new PinWarehousePage().isDisplayedNow()) {
            Allure.parameter("after", classifyNow());
            return new PinWarehousePage();
        }
        TellUsMaterialPage tellUs = reachTellUsMaterial();
        tellUs.tapPinOnMap();
        sleepQuiet(1500);
        PinWarehousePage page = new PinWarehousePage();
        page.waitUntilLoaded();
        Allure.parameter("after", classifyNow());
        return page;
    }

    protected void dismissToRole() {
        for (int i = 0; i < 8; i++) {
            if (new RolePage().isDisplayedNow()) {
                return;
            }
            if (new PinWarehousePage().isDisplayedNow()) {
                try {
                    new PinWarehousePage().tapClose();
                } catch (RuntimeException e) {
                    DriverManager.get().navigate().back();
                }
                sleepQuiet(800);
                continue;
            }
            if (new TellUsMaterialPage().isDisplayedNow() || new TellUsPage().isDisplayedNow()) {
                try {
                    new TellUsMaterialPage().tapBack();
                } catch (RuntimeException e) {
                    DriverManager.get().navigate().back();
                }
                sleepQuiet(800);
                continue;
            }
            if (new CategoryPage().isDisplayedNow()) {
                DriverManager.get().navigate().back();
                sleepQuiet(800);
                continue;
            }
            if (hasText("Sign Up Completed") && hasText("Register Yourself")) {
                tapText("Register Yourself");
                sleepQuiet(1200);
                continue;
            }
            break;
        }
    }

    private void restoreLoginIfNeeded() {
        if (new RolePage().isDisplayedNow() || new CategoryPage().isDisplayedNow()
                || new TellUsMaterialPage().isDisplayedNow()) {
            return;
        }
        if (new LanguagePage().isTitleEnglish() || hasText("Welcome to L2B")) {
            LanguagePage language = new LanguagePage();
            language.waitUntilLoaded();
            language.tapGetStarted();
            OnboardingCarouselPage carousel = new OnboardingCarouselPage();
            carousel.waitUntilSlideOne();
            carousel.tapNext();
            carousel.waitUntilSlideTwo();
            carousel.tapGetStarted();
        }
        if (new SignUpPage().isDisplayedNow() || hasText("Enter mobile number")) {
            SignUpPage signUp = new SignUpPage();
            signUp.waitUntilLoaded();
            signUp.enterPhone(RoleEnvironment.phone());
            signUp.hideKeyboard();
            signUp.acceptTerms();
            Waits.until(DriverManager.get(),
                    d -> signUp.isGetOtpEnabled() ? Boolean.TRUE : null,
                    "Get OTP stayed disabled", Duration.ofSeconds(8));
            signUp.tapGetOtp();
        }
        if (new OtpPage().isDisplayedNow() || hasText("Verify your OTP")) {
            OtpPage otp = new OtpPage();
            otp.waitUntilLoaded();
            otp.focusOtpField();
            if (!otp.otpFieldText().isEmpty()) {
                otp.clearOtp();
            }
            otp.pressDigitKeys(RoleEnvironment.otp());
            sleepQuiet(800);
            try {
                if (otp.isContinueEnabled()) {
                    otp.tapContinue();
                }
            } catch (RuntimeException ignored) {
            }
            sleepQuiet(1500);
        }
        if (hasText("Sign Up Completed") && hasText("Register Yourself")) {
            tapText("Register Yourself");
            sleepQuiet(1200);
        }
        Waits.until(DriverManager.get(),
                d -> new RolePage().isDisplayedNow() || new CategoryPage().isDisplayedNow()
                        || new TellUsMaterialPage().isDisplayedNow() ? Boolean.TRUE : null,
                "Registration screen not after OTP (named=" + classifyNow() + ")",
                Duration.ofSeconds(25));
    }

    protected String classifyNow() {
        if (launcherNow()) {
            return "launcher";
        }
        if (new PinWarehousePage().isDisplayedNow()) {
            return "pin-warehouse";
        }
        if (new TellUsMaterialPage().isDisplayedNow()) {
            return "tellus-materials";
        }
        if (new TellUsPage().isDisplayedNow()) {
            return "tellus-operator";
        }
        if (new CategoryPage().isDisplayedNow()) {
            return "category";
        }
        if (new RolePage().isDisplayedNow()) {
            return "role";
        }
        if (hasText("Sign Up Completed")) {
            return "signup-completed";
        }
        if (new OtpPage().isDisplayedNow()) {
            return "otp";
        }
        if (new SignUpPage().isDisplayedNow()) {
            return "signup";
        }
        return "vendor-other";
    }

    protected boolean hasText(String text) {
        return !DriverManager.get().findElements(ComposeLocators.textView(text)).isEmpty();
    }

    protected void tapText(String text) {
        var els = DriverManager.get().findElements(ComposeLocators.textView(text));
        if (els.isEmpty()) {
            return;
        }
        var r = els.get(0).getRect();
        DriverManager.get().executeScript("mobile: clickGesture",
                java.util.Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }

    protected static boolean launcherNow() {
        String pkg = vendorPackage();
        return pkg != null && pkg.contains("launcher");
    }

    protected static String vendorPackage() {
        return ((AndroidDriver) DriverManager.get()).getCurrentPackage();
    }

    protected static void sleepQuiet(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected boolean noAcceptVisible() {
        return DriverManager.get().findElements(By.xpath("//*[@text='Accept']")).isEmpty();
    }
}
