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

/** Please select the category — Materials / Rental. */
public class CategoryPage extends SplashScreen {

    public static final String TITLE = "Please select the category";
    public static final String MATERIALS = "Materials Vendor";
    public static final String RENTAL = "Rental Vendor";
    public static final String CONTINUE = "Continue";

    @Override
    @Step("Wait for category")
    public void waitUntilLoaded() {
        Waits.until(driver, d -> isDisplayedNow() ? Boolean.TRUE : null,
                "Category screen did not appear", Duration.ofSeconds(20));
    }

    @Step("Check category")
    public boolean isDisplayedNow() {
        return isPresent(ComposeLocators.textView(TITLE))
                || (isPresent(ComposeLocators.textView(MATERIALS))
                && isPresent(ComposeLocators.textView(RENTAL)));
    }

    @Step("Title")
    public boolean isTitleVisible() {
        return isPresent(ComposeLocators.textView(TITLE));
    }

    @Step("Materials Vendor")
    public boolean isMaterialsVisible() {
        return isPresent(ComposeLocators.textView(MATERIALS));
    }

    @Step("Rental Vendor")
    public boolean isRentalVisible() {
        return isPresent(ComposeLocators.textView(RENTAL));
    }

    @Step("Materials blurb")
    public boolean isMaterialsBlurbVisible() {
        return isPresent(ComposeLocators.textViewContains("bulk materials"))
                || isPresent(ComposeLocators.textViewContains("order management"));
    }

    @Step("Continue visible")
    public boolean isContinueVisible() {
        return isPresent(ComposeLocators.textView(CONTINUE));
    }

    @Step("Tap Materials Vendor")
    public void tapMaterials() {
        tapText(MATERIALS);
    }

    @Step("Tap Rental Vendor")
    public void tapRental() {
        tapText(RENTAL);
    }

    @Step("Tap Continue")
    public void tapContinue() {
        List<WebElement> rows = driver.findElements(ComposeLocators.clickableWithText(CONTINUE));
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
        tapText(CONTINUE);
    }

    private void tapText(String text) {
        List<WebElement> els = driver.findElements(ComposeLocators.textView(text));
        if (els.isEmpty()) {
            throw new IllegalStateException(text + " not on category");
        }
        Rectangle r = els.get(0).getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }
}
