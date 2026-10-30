package com.l2b.vendor.modules.orders.presentation.pages;

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

/**
 * Material order card expanded via {@code View More Details} (inline — not a separate route).
 * Collapse with {@code View Less Details}.
 */
public class OrderDetailsPage extends SplashScreen {

    @Override
    @Step("Wait for expanded order details")
    public void waitUntilLoaded() {
        Waits.until(driver, d -> isDisplayedNow() ? Boolean.TRUE : null,
                "Order details did not appear", Duration.ofSeconds(12));
    }

    @Step("Check expanded order details (Bill Summary / View Less)")
    public boolean isDisplayedNow() {
        return isPresent(ComposeLocators.textView("View Less Details"))
                || isPresent(ComposeLocators.textView("Bill Summary"))
                || (isPresent(ComposeLocators.textView("Total Bill"))
                && isPresent(ComposeLocators.textView("Taxes")))
                || (isPresent(ComposeLocators.textView("Materials Item Total"))
                && isPresent(ComposeLocators.textViewContains("Units")));
    }

    @Step("Collapse control visible (View Less / Back)")
    public boolean isBackVisible() {
        return isPresent(ComposeLocators.textView("View Less Details"))
                || !driver.findElements(By.xpath("//*[@content-desc='Back']")).isEmpty();
    }

    @Step("Collapse details (View Less Details)")
    public void tapBack() {
        List<WebElement> less = driver.findElements(ComposeLocators.textView("View Less Details"));
        if (!less.isEmpty()) {
            Rectangle r = less.get(0).getRect();
            driver.executeScript("mobile: clickGesture",
                    Map.of("x", r.x + Math.max(1, r.width / 2),
                            "y", r.y + Math.max(1, r.height / 2)));
            return;
        }
        List<WebElement> back = driver.findElements(By.xpath("//*[@content-desc='Back']"));
        if (!back.isEmpty()) {
            Rectangle r = back.get(0).getRect();
            driver.executeScript("mobile: clickGesture",
                    Map.of("x", r.x + Math.max(1, r.width / 2),
                            "y", r.y + Math.max(1, r.height / 2)));
            return;
        }
        driver.navigate().back();
    }
}
