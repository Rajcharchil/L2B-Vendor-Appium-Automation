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

/** Material Orders list — Upcoming / Active / Completed. */
public class MaterialOrdersPage extends SplashScreen {

    @Override
    @Step("Wait for Material Orders")
    public void waitUntilLoaded() {
        Waits.until(driver, d -> isDisplayedNow() ? Boolean.TRUE : null,
                "Material Orders did not appear", Duration.ofSeconds(15));
    }

    @Step("Check Orders list")
    public boolean isDisplayedNow() {
        // Tabs alone are enough — Upcoming may be empty (no cards / View More).
        return isPresent(ComposeLocators.textView("Upcoming"))
                && isPresent(ComposeLocators.textView("Active"))
                && isPresent(ComposeLocators.textView("Completed"));
    }

    /**
     * True only for explicit empty-list copy. Tabs alone must NOT count as empty —
     * that caused F2 false-passes when the list was loading, errored, or still had
     * non-Confirmed chrome without View More / Get Direction.
     */
    @Step("Empty / no-card Orders state")
    public boolean isEmptyStateVisible() {
        return isPresent(ComposeLocators.textViewContains("No order"))
                || isPresent(ComposeLocators.textViewContains("no order"))
                || isPresent(ComposeLocators.textViewContains("Nothing here"))
                || isPresent(ComposeLocators.textViewContains("No upcoming"))
                || isPresent(ComposeLocators.textViewContains("No orders right now"));
    }

    /** Intermittent load-error chrome (BUGS_FOUND #60) — not a valid empty pass. */
    @Step("Orders load-error empty")
    public boolean isLoadErrorVisible() {
        return isPresent(ComposeLocators.textViewContains("Could not load"))
                || isPresent(ComposeLocators.textViewContains("Check your connection"))
                || (areTabsVisible()
                        && isPresent(ComposeLocators.textView("Retry"))
                        && !isCardChromeVisible()
                        && !isEmptyStateVisible());
    }

    @Step("Tabs")
    public boolean areTabsVisible() {
        return isPresent(ComposeLocators.textView("Upcoming"))
                && isPresent(ComposeLocators.textView("Active"))
                && isPresent(ComposeLocators.textView("Completed"));
    }

    @Step("Card chrome")
    public boolean isCardChromeVisible() {
        return isPresent(ComposeLocators.textViewContains("Confirmed"))
                || isPresent(ComposeLocators.textViewContains("Picked up"))
                || isPresent(ComposeLocators.textView("Get Direction"));
    }

    @Step("Get Direction")
    public boolean isGetDirectionVisible() {
        return isPresent(ComposeLocators.textView("Get Direction"));
    }

    @Step("View More Details")
    public boolean isViewMoreVisible() {
        return isPresent(ComposeLocators.textView("View More Details"));
    }

    @Step("Help")
    public boolean isHelpVisible() {
        return isPresent(ComposeLocators.textView("Help"));
    }

    @Step("Material bottom tabs")
    public boolean areMaterialTabsVisible() {
        return isPresent(By.xpath("//*[@content-desc='Orders']"))
                && isPresent(By.xpath("//*[@content-desc='Home']"));
    }

    @Step("Tap Upcoming")
    public void tapUpcoming() {
        tapText("Upcoming");
    }

    @Step("Tap Active")
    public void tapActive() {
        tapText("Active");
    }

    @Step("Tap Completed")
    public void tapCompleted() {
        tapText("Completed");
    }

    @Step("Tap View More Details (topmost card)")
    public void tapViewMoreDetails() {
        List<WebElement> els = driver.findElements(ComposeLocators.textView("View More Details"));
        if (els.isEmpty()) {
            els = driver.findElements(ComposeLocators.clickableWithText("View More Details"));
        }
        if (els.isEmpty()) {
            throw new IllegalStateException("View More Details not on Orders");
        }
        // Prefer the highest on-screen control — DOM order is unreliable for expand.
        WebElement best = els.get(0);
        int minY = best.getRect().y;
        for (WebElement el : els) {
            int y = el.getRect().y;
            if (y >= 0 && y < minY) {
                minY = y;
                best = el;
            }
        }
        Rectangle r = best.getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }

    @Step("Tap Get Direction")
    public void tapGetDirection() {
        tapText("Get Direction");
    }

    @Step("Tap Help")
    public void tapHelp() {
        tapText("Help");
    }

    private void tapText(String text) {
        List<WebElement> els = driver.findElements(ComposeLocators.textView(text));
        if (els.isEmpty()) {
            els = driver.findElements(ComposeLocators.clickableWithText(text));
        }
        if (els.isEmpty()) {
            throw new IllegalStateException(text + " not on Orders");
        }
        Rectangle r = els.get(0).getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }
}
