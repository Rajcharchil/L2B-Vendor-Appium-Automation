package com.l2b.vendor.modules.inventory.presentation.pages;

import com.l2b.vendor.core.locators.ComposeLocators;
import com.l2b.vendor.core.ui.SplashScreen;
import com.l2b.vendor.core.wait.Waits;
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

/** Import CSV sheet on Inventory. Never confirm file upload. */
public class ImportCsvPage extends SplashScreen {

    @Override
    @Step("Wait for Import CSV")
    public void waitUntilLoaded() {
        Waits.until(driver, d -> isDisplayedNow() ? Boolean.TRUE : null,
                "Import CSV did not appear", Duration.ofSeconds(10));
    }

    @Step("Check Import CSV sheet")
    public boolean isDisplayedNow() {
        return isPresent(ComposeLocators.textViewContains("import your inventory CSV"))
                || isPresent(ComposeLocators.textView("Download a sample CSV"))
                || (isPresent(ComposeLocators.textView("Import CSV"))
                && isPresent(ComposeLocators.textViewContains("Accepts")));
    }

    @Step("Import copy")
    public boolean isImportCopyVisible() {
        return isPresent(ComposeLocators.textViewContains("import your inventory CSV"));
    }

    @Step("Accepts types")
    public boolean isAcceptsTypesVisible() {
        return isPresent(ComposeLocators.textViewContains(".csv"))
                || isPresent(ComposeLocators.textViewContains("Accepts"));
    }

    @Step("Download sample")
    public boolean isDownloadSampleVisible() {
        return isPresent(ComposeLocators.textView("Download a sample CSV"));
    }

    @Step("Tap Download sample CSV")
    public void tapDownloadSample() {
        List<WebElement> els = driver.findElements(ComposeLocators.textView("Download a sample CSV"));
        if (els.isEmpty()) {
            throw new IllegalStateException("Download a sample CSV missing");
        }
        Rectangle r = els.get(0).getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }

    @Step("Close via device Back")
    public void closeWithBack() {
        driver.navigate().back();
    }
}
