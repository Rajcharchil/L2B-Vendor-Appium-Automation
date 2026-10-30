package com.l2b.vendor.modules.inventory.presentation.pages;

import com.l2b.vendor.core.locators.ComposeLocators;
import com.l2b.vendor.core.ui.SplashScreen;
import com.l2b.vendor.core.wait.Waits;
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

/** Add Product form — Inventory → + Add Product. Never tap Save. */
public class AddProductPage extends SplashScreen {

    public static final String TITLE = "Product Details";

    @Override
    @Step("Wait for Add Product")
    public void waitUntilLoaded() {
        Waits.until(driver, d -> isDisplayedNow() ? Boolean.TRUE : null,
                "Add Product did not appear", Duration.ofSeconds(12));
    }

    @Step("Check Add Product")
    public boolean isDisplayedNow() {
        return isPresent(ComposeLocators.textView(TITLE))
                || (isPresent(ComposeLocators.textView("Category *"))
                && isPresent(ComposeLocators.textView("Product Name *")));
    }

    @Step("Category")
    public boolean isCategoryVisible() {
        return isPresent(ComposeLocators.textView("Category *"));
    }

    @Step("Product Name")
    public boolean isProductNameVisible() {
        return isPresent(ComposeLocators.textView("Product Name *"));
    }

    @Step("Brand")
    public boolean isBrandVisible() {
        return isPresent(ComposeLocators.textView("Brand *"));
    }

    @Step("Pricing")
    public boolean isPricingVisible() {
        return isPresent(ComposeLocators.textView("Pricing"))
                || isPresent(ComposeLocators.textView("Selling Price *"));
    }

    @Step("Stock Available")
    public boolean isStockVisible() {
        return isPresent(ComposeLocators.textView("Stock Available *"));
    }

    @Step("Specifications")
    public boolean isSpecsVisible() {
        return isPresent(ComposeLocators.textView("Specifications"))
                || isPresent(ComposeLocators.textView("Grade"));
    }

    @Step("Save CTA")
    public boolean isSaveVisible() {
        return isPresent(ComposeLocators.textView("Save"));
    }

    @Step("Back")
    public boolean isBackVisible() {
        return !driver.findElements(By.xpath("//*[@content-desc='Back']")).isEmpty();
    }

    @Step("Tap Back (no Save)")
    public void tapBack() {
        List<WebElement> els = driver.findElements(By.xpath("//*[@content-desc='Back']"));
        if (!els.isEmpty()) {
            Rectangle r = els.get(0).getRect();
            driver.executeScript("mobile: clickGesture",
                    Map.of("x", r.x + Math.max(1, r.width / 2),
                            "y", r.y + Math.max(1, r.height / 2)));
            return;
        }
        driver.navigate().back();
    }

    @Step("Tap Save (probe only — expect stay)")
    public void tapSave() {
        List<WebElement> rows = driver.findElements(ComposeLocators.clickableWithText("Save"));
        if (!rows.isEmpty()) {
            Rectangle r = rows.get(0).getRect();
            driver.executeScript("mobile: clickGesture",
                    Map.of("x", r.x + Math.max(1, r.width / 2),
                            "y", r.y + Math.max(1, r.height / 2)));
            return;
        }
        List<WebElement> els = driver.findElements(ComposeLocators.textView("Save"));
        if (!els.isEmpty()) {
            Rectangle r = els.get(0).getRect();
            driver.executeScript("mobile: clickGesture",
                    Map.of("x", r.x + Math.max(1, r.width / 2),
                            "y", r.y + Math.max(1, r.height / 2)));
        }
    }
}
