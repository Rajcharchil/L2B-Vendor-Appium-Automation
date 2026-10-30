package com.l2b.vendor.modules.profileverification.presentation.pages;

import com.l2b.vendor.core.locators.ComposeLocators;
import com.l2b.vendor.core.ui.SplashScreen;
import io.qameta.allure.Step;

/** Profile Verification hub — often registration-only; may be absent on warm 0017. */
public class ProfileVerificationPage extends SplashScreen {

    @Step("Check Profile Verification")
    public boolean isDisplayedNow() {
        return isPresent(ComposeLocators.textView("Profile Verification"))
                || isPresent(ComposeLocators.textViewContains("KYC & Payment"))
                || isPresent(ComposeLocators.textViewContains("24h"))
                || (isPresent(ComposeLocators.textViewContains("Verification"))
                && isPresent(ComposeLocators.textViewContains("KYC")));
    }

    @Override
    public void waitUntilLoaded() {
        // optional screen — callers check isDisplayedNow
    }
}
