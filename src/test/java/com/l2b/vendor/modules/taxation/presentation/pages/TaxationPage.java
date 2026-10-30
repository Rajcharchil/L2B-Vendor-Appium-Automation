package com.l2b.vendor.modules.taxation.presentation.pages;

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

/** Taxation drawer — GST reports. Never complete paid submit. */
public class TaxationPage extends SplashScreen {

    public static final String TITLE = "Taxation";

    @Override
    @Step("Wait for Taxation")
    public void waitUntilLoaded() {
        Waits.until(driver, d -> isDisplayedNow() ? Boolean.TRUE : null,
                "Taxation did not appear", Duration.ofSeconds(12));
    }

    @Step("Check Taxation")
    public boolean isDisplayedNow() {
        return isPresent(ComposeLocators.textView(TITLE))
                && (isPresent(ComposeLocators.textView("GST Monthly Report"))
                || isPresent(ComposeLocators.textView("GSTR-1 Summary")));
    }

    @Step("GST Monthly")
    public boolean isGstMonthlyVisible() {
        return isPresent(ComposeLocators.textView("GST Monthly Report"));
    }

    @Step("On Demand")
    public boolean isOnDemandVisible() {
        return isPresent(ComposeLocators.textView("GST On Demand Report"));
    }

    @Step("GSTR-1")
    public boolean isGstr1Visible() {
        return isPresent(ComposeLocators.textView("GSTR-1 Summary"));
    }

    @Step("Download Report")
    public boolean isDownloadVisible() {
        return isPresent(ComposeLocators.textView("Download Report"));
    }

    @Step("Request Report")
    public boolean isRequestVisible() {
        return isPresent(ComposeLocators.textView("Request Report"));
    }

    @Step("Reports empty")
    public boolean isReportsEmptyVisible() {
        return isPresent(ComposeLocators.textViewContains("No reports yet"));
    }

    @Step("Tap Download Report")
    public void tapDownloadReport() {
        tapText("Download Report");
    }

    @Step("Tap Request Report")
    public void tapRequestReport() {
        tapText("Request Report");
    }

    @Step("Tap Back")
    public void tapBack() {
        List<WebElement> els = driver.findElements(By.xpath("//*[@content-desc='Back']"));
        if (!els.isEmpty()) {
            gesture(els.get(0));
            return;
        }
        driver.navigate().back();
    }

    private void tapText(String text) {
        List<WebElement> els = driver.findElements(ComposeLocators.textView(text));
        if (els.isEmpty()) {
            throw new IllegalStateException(text + " not on Taxation");
        }
        gesture(els.get(0));
    }

    private void gesture(WebElement el) {
        Rectangle r = el.getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }
}
