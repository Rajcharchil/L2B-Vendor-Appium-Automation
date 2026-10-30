package com.l2b.vendor.modules.onboarding.presentation.pages;

import com.l2b.vendor.core.locators.ComposeLocators;
import com.l2b.vendor.core.ui.SplashScreen;
import com.l2b.vendor.core.wait.Waits;
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

/** Pin your warehouse — from Tell us Materials → Pin on map. Dump /tmp/l2b-pin-warehouse-0013-20261007. */
public class PinWarehousePage extends SplashScreen {

    public static final String TITLE = "Pin your warehouse";
    public static final String MAP_UNAVAILABLE = "Map unavailable";
    public static final String MAP_HINT = "Move the map so the pin sits on your warehouse.";
    public static final String CONFIRM = "Confirm location";
    private static final Pattern COORDS = Pattern.compile("-?\\d+\\.\\d+,\\s*-?\\d+\\.\\d+");

    @Override
    @Step("Wait for Pin your warehouse")
    public void waitUntilLoaded() {
        Waits.until(driver, d -> isDisplayedNow() ? Boolean.TRUE : null,
                "Pin your warehouse did not appear", Duration.ofSeconds(15));
    }

    @Step("Check pin warehouse")
    public boolean isDisplayedNow() {
        return isPresent(ComposeLocators.textView(TITLE))
                || (isPresent(ComposeLocators.textView(CONFIRM))
                && isPresent(ComposeLocators.textViewContains("warehouse")));
    }

    @Step("Title")
    public boolean isTitleVisible() {
        return isPresent(ComposeLocators.textView(TITLE));
    }

    @Step("Map unavailable")
    public boolean isMapUnavailableVisible() {
        return isPresent(ComposeLocators.textView(MAP_UNAVAILABLE))
                || isPresent(ComposeLocators.textViewContains("map could not be loaded"));
    }

    @Step("Move-map hint")
    public boolean isHintVisible() {
        return isPresent(ComposeLocators.textView(MAP_HINT))
                || isPresent(ComposeLocators.textViewContains("Move the map"));
    }

    @Step("Lat/lng chip")
    public boolean isCoordsVisible() {
        String src = driver.getPageSource();
        return COORDS.matcher(src).find();
    }

    @Step("Confirm location")
    public boolean isConfirmVisible() {
        return isPresent(ComposeLocators.textView(CONFIRM));
    }

    @Step("Close")
    public boolean isCloseVisible() {
        return !driver.findElements(By.xpath("//*[@content-desc='Close']")).isEmpty();
    }

    @Step("Use current desc")
    public boolean isUseCurrentDescVisible() {
        return !driver.findElements(By.xpath("//*[@content-desc='Use current']")).isEmpty();
    }

    @Step("Tap Confirm location")
    public void tapConfirm() {
        tapText(CONFIRM);
    }

    @Step("Tap Close")
    public void tapClose() {
        List<WebElement> els = driver.findElements(By.xpath("//*[@content-desc='Close']"));
        if (els.isEmpty()) {
            driver.navigate().back();
            return;
        }
        Rectangle r = els.get(0).getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }

    @Step("Tap Use current on sheet")
    public void tapUseCurrentDesc() {
        List<WebElement> els = driver.findElements(By.xpath("//*[@content-desc='Use current']"));
        if (els.isEmpty()) {
            throw new IllegalStateException("Use current not on pin sheet");
        }
        Rectangle r = els.get(0).getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }

    private void tapText(String text) {
        List<WebElement> els = driver.findElements(ComposeLocators.textView(text));
        if (els.isEmpty()) {
            els = driver.findElements(ComposeLocators.clickableWithText(text));
        }
        if (els.isEmpty()) {
            throw new IllegalStateException(text + " not on Pin warehouse");
        }
        Rectangle r = els.get(0).getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }
}
