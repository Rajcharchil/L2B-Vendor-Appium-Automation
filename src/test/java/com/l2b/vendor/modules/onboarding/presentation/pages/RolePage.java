package com.l2b.vendor.modules.onboarding.presentation.pages;

import com.l2b.vendor.core.locators.ComposeLocators;
import com.l2b.vendor.core.ui.SplashScreen;
import com.l2b.vendor.core.wait.Waits;
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

/** Select your role ? — dump /tmp/l2b-role-mat-0013-20261007. */
public class RolePage extends SplashScreen {

    public static final String TITLE = "Select your role ?";
    public static final String VENDOR = "Vendor / Owner";
    public static final String OPERATOR = "Operator / Driver";
    public static final String NEXT = "Next";

    @Override
    @Step("Wait for Select your role")
    public void waitUntilLoaded() {
        Waits.until(driver, d -> isDisplayedNow() ? Boolean.TRUE : null,
                "Select your role did not appear", Duration.ofSeconds(20));
    }

    @Step("Check role screen")
    public boolean isDisplayedNow() {
        return isPresent(ComposeLocators.textView(TITLE))
                || (isPresent(ComposeLocators.textView(VENDOR))
                && isPresent(ComposeLocators.textView(OPERATOR)));
    }

    @Step("Title")
    public boolean isTitleVisible() {
        return isPresent(ComposeLocators.textView(TITLE))
                || isPresent(ComposeLocators.textViewContains("Select your role"));
    }

    @Step("Vendor card")
    public boolean isVendorVisible() {
        return isPresent(ComposeLocators.textView(VENDOR));
    }

    @Step("Operator card")
    public boolean isOperatorVisible() {
        return isPresent(ComposeLocators.textView(OPERATOR));
    }

    @Step("Vendor subtitle")
    public boolean isVendorSubtitleVisible() {
        return isPresent(ComposeLocators.textViewContains("Manages a business"));
    }

    @Step("Operator subtitle")
    public boolean isOperatorSubtitleVisible() {
        return isPresent(ComposeLocators.textViewContains("operating machinery"));
    }

    @Step("Next visible")
    public boolean isNextVisible() {
        return isPresent(ComposeLocators.textView(NEXT));
    }

    @Step("Next outer enabled")
    public boolean isNextOuterEnabled() {
        List<WebElement> rows = driver.findElements(ComposeLocators.clickableWithText(NEXT));
        if (rows.isEmpty()) {
            return false;
        }
        return Boolean.parseBoolean(rows.get(0).getAttribute("enabled"));
    }

    @Step("Tap Vendor / Owner")
    public void tapVendor() {
        tapText(VENDOR);
    }

    @Step("Tap Operator / Driver")
    public void tapOperator() {
        tapText(OPERATOR);
    }

    @Step("Tap Next")
    public void tapNext() {
        List<WebElement> rows = driver.findElements(ComposeLocators.clickableWithText(NEXT));
        if (!rows.isEmpty()) {
            try {
                rows.get(0).click();
                return;
            } catch (RuntimeException e) {
                Rectangle r = rows.get(0).getRect();
                driver.executeScript("mobile: clickGesture",
                        Map.of("x", r.x + Math.max(1, r.width / 2),
                                "y", r.y + Math.max(1, r.height / 2)));
                return;
            }
        }
        tapText(NEXT);
    }

    private void tapText(String text) {
        List<WebElement> els = driver.findElements(ComposeLocators.textView(text));
        if (els.isEmpty()) {
            throw new IllegalStateException(text + " not on role screen");
        }
        Rectangle r = els.get(0).getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }
}
