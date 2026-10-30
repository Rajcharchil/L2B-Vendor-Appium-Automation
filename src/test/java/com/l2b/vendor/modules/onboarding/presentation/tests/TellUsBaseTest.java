package com.l2b.vendor.modules.onboarding.presentation.tests;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.core.locators.ComposeLocators;
import com.l2b.vendor.core.ui.BaseTest;
import com.l2b.vendor.core.wait.Waits;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.onboarding.domain.TellUsEnvironment;
import com.l2b.vendor.modules.onboarding.presentation.pages.LanguagePage;
import com.l2b.vendor.modules.onboarding.presentation.pages.OnboardingCarouselPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.OtpPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.SignUpPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.lang.reflect.Method;
import java.time.Duration;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeSuite;

/**
 * Stay-logged-in Tell us (9000000013). Never force-stop, never Log Out Confirm,
 * never Take Photo / Gallery confirm, never Accept/Decline.
 */
public abstract class TellUsBaseTest extends BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void prepareTellUsEnvironment() {
        TellUsEnvironment.prepareSuite();
    }

    @Override
    protected boolean noReset() {
        return TellUsEnvironment.noReset();
    }

    @Override
    protected boolean newSessionPerMethod() {
        return TellUsEnvironment.newSessionPerMethod();
    }

    @Override
    protected void beforeCreateDriver(Method method) {
        try {
            Adb.ensureNetworkReady();
        } catch (RuntimeException e) {
            Allure.parameter("networkWarn", e.getMessage());
        }
    }

    @Step("Reach Tell us about yourself (0013)")
    protected TellUsPage reachTellUs() {
        try {
            ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package"));
        } catch (RuntimeException ignored) {
        }
        sleepQuiet(600);
        dismissUploadSheetIfPresent();
        TellUsPage page = new TellUsPage();
        if (page.isDisplayedNow()) {
            Allure.parameter("after", classifyTellUsNow());
            return page;
        }
        restoreLogin0013IfNeeded();
        dismissUploadSheetIfPresent();
        page.waitUntilLoaded();
        Allure.parameter("after", classifyTellUsNow());
        return page;
    }

    private void restoreLogin0013IfNeeded() {
        if (new TellUsPage().isDisplayedNow()) {
            return;
        }
        if (hasText("Current Earning") || hasText("Kasim Pathan") || hasText("Upcoming Booking")) {
            throw new IllegalStateException(
                    "Rental Home on 0001 — log out and log in 9000000013 before Tell us");
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
            signUp.enterPhone(TellUsEnvironment.phone());
            signUp.hideKeyboard();
            signUp.acceptTerms();
            Waits.until(DriverManager.get(),
                    d -> signUp.isGetOtpEnabled() ? Boolean.TRUE : null,
                    "Get OTP stayed disabled for 0013",
                    Duration.ofSeconds(8));
            signUp.tapGetOtp();
        }
        if (new OtpPage().isDisplayedNow() || hasText("Verify your OTP")) {
            OtpPage otp = new OtpPage();
            otp.waitUntilLoaded();
            otp.focusOtpField();
            if (!otp.otpFieldText().isEmpty()) {
                otp.clearOtp();
            }
            otp.pressDigitKeys(TellUsEnvironment.otp());
            sleepQuiet(800);
            // Fresh install: Continue may stay until outer View enables after 4 digits.
            try {
                Waits.until(DriverManager.get(),
                        d -> otp.isContinueEnabled() ? Boolean.TRUE : null,
                        "OTP Continue stayed disabled",
                        Duration.ofSeconds(8));
                otp.tapContinue();
            } catch (RuntimeException e) {
                Allure.parameter("otpContinue", e.getMessage());
                try {
                    otp.tapContinue();
                } catch (RuntimeException ignored) {
                }
            }
            sleepQuiet(1500);
        }
        Waits.until(DriverManager.get(),
                d -> new TellUsPage().isDisplayedNow() ? Boolean.TRUE : null,
                "Tell us did not appear after 0013 OTP (named=" + classifyTellUsNow() + ")",
                Duration.ofSeconds(25));
    }

    protected void dismissUploadSheetIfPresent() {
        TellUsPage page = new TellUsPage();
        if (page.isUploadSheetVisible()) {
            page.tapSheetCancel();
            sleepQuiet(700);
        }
    }

    protected String classifyTellUsNow() {
        if (launcherNow()) {
            return "launcher";
        }
        if (new TellUsPage().isUploadSheetVisible()) {
            return "tellus-upload-sheet";
        }
        if (new TellUsPage().isDisplayedNow()) {
            return "tellus";
        }
        if (new OtpPage().isDisplayedNow()) {
            return "otp";
        }
        if (new SignUpPage().isDisplayedNow()) {
            return "signup";
        }
        if (hasText("Welcome to L2B") || new LanguagePage().isTitleEnglish()) {
            return "language";
        }
        return "vendor-other";
    }

    protected boolean hasText(String text) {
        return !DriverManager.get().findElements(ComposeLocators.textView(text)).isEmpty();
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
