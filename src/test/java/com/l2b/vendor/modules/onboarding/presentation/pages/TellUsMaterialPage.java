package com.l2b.vendor.modules.onboarding.presentation.pages;

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
 * Tell us about yourself — Materials vendor.
 * Dump /tmp/l2b-tellus-mat-0013-20261007.
 */
public class TellUsMaterialPage extends SplashScreen {

    public static final String TITLE = "Tell us about yourself ?";
    public static final String COMPANY_TYPE = "Type of company *";
    public static final String WAREHOUSE = "Warehouse location";
    public static final String USE_CURRENT = "Use current";
    public static final String PIN_MAP = "Pin on map";
    public static final String CONTINUE = "Continue";
    public static final String NAME_PREFILL = "waardian tech";
    public static final String EMAIL_PREFILL = "manju@gmail.com";

    @Override
    @Step("Wait for Tell us Materials")
    public void waitUntilLoaded() {
        Waits.until(driver, d -> isDisplayedNow() ? Boolean.TRUE : null,
                "Tell us Materials did not appear", Duration.ofSeconds(20));
    }

    @Step("Check Tell us Materials")
    public boolean isDisplayedNow() {
        return isPresent(ComposeLocators.textView(TITLE))
                && (isPresent(ComposeLocators.textView(COMPANY_TYPE))
                || isPresent(ComposeLocators.textView(WAREHOUSE))
                || isPresent(ComposeLocators.textViewContains("stock material")));
    }

    @Step("Title")
    public boolean isTitleVisible() {
        return isPresent(ComposeLocators.textView(TITLE));
    }

    @Step("Prefill name")
    public boolean isNamePrefillVisible() {
        return isPresent(ComposeLocators.textView(NAME_PREFILL))
                || hasEditText(NAME_PREFILL)
                || NAME_PREFILL.equals(editTextAt(0));
    }

    @Step("Prefill email")
    public boolean isEmailPrefillVisible() {
        return isPresent(ComposeLocators.textView(EMAIL_PREFILL))
                || hasEditText(EMAIL_PREFILL)
                || EMAIL_PREFILL.equals(editTextAt(1));
    }

    private boolean hasEditText(String value) {
        return !driver.findElements(
                By.xpath("//android.widget.EditText[@text='" + value.replace("'", "\"") + "']"))
                .isEmpty();
    }

    private String editTextAt(int index) {
        List<WebElement> fields = driver.findElements(By.className("android.widget.EditText"));
        if (fields.size() <= index) {
            return "";
        }
        WebElement el = fields.get(index);
        String raw = el.getText();
        if (raw == null || raw.isBlank() || "null".equalsIgnoreCase(raw)) {
            raw = el.getAttribute("text");
        }
        return raw == null || "null".equalsIgnoreCase(raw) ? "" : raw;
    }

    @Step("Company type")
    public boolean isCompanyTypeVisible() {
        // After a pick, label is replaced by the selected value (e.g. Sole Proprietorship).
        return isPresent(ComposeLocators.textView(COMPANY_TYPE))
                || isPresent(ComposeLocators.textViewContains("Type of company"))
                || isPresent(ComposeLocators.textView("Sole Proprietorship"))
                || isPresent(ComposeLocators.textView("LLP"))
                || isPresent(ComposeLocators.textView("Pvt. Ltd"))
                || isPresent(ComposeLocators.textView("Others"));
    }

    @Step("Warehouse")
    public boolean isWarehouseVisible() {
        return isPresent(ComposeLocators.textView(WAREHOUSE));
    }

    @Step("Use current")
    public boolean isUseCurrentVisible() {
        return isPresent(ComposeLocators.textView(USE_CURRENT));
    }

    @Step("Pin on map")
    public boolean isPinMapVisible() {
        return isPresent(ComposeLocators.textView(PIN_MAP));
    }

    @Step("Continue")
    public boolean isContinueVisible() {
        return isPresent(ComposeLocators.textView(CONTINUE));
    }

    @Step("Back")
    public boolean isBackVisible() {
        return !driver.findElements(By.xpath("//*[@content-desc='Back']")).isEmpty();
    }

    @Step("Company options expanded")
    public boolean isCompanyOptionsVisible() {
        return isPresent(ComposeLocators.textView("Sole Proprietorship"))
                || isPresent(ComposeLocators.textView("LLP"))
                || isPresent(ComposeLocators.textView("Pvt. Ltd"));
    }

    @Step("Tap Type of company")
    public void tapCompanyType() {
        if (isPresent(ComposeLocators.textView(COMPANY_TYPE))) {
            tapText(COMPANY_TYPE);
            return;
        }
        for (String selected : new String[] {"Sole Proprietorship", "LLP", "Pvt. Ltd", "Others"}) {
            if (isPresent(ComposeLocators.textView(selected))) {
                tapText(selected);
                return;
            }
        }
        tapText(COMPANY_TYPE);
    }

    @Step("Tap Sole Proprietorship")
    public void tapSoleProprietorship() {
        tapText("Sole Proprietorship");
    }

    @Step("Tap LLP")
    public void tapLlp() {
        tapText("LLP");
    }

    @Step("Tap Use current")
    public void tapUseCurrent() {
        tapText(USE_CURRENT);
    }

    @Step("Tap Pin on map")
    public void tapPinOnMap() {
        tapText(PIN_MAP);
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

    @Step("Tap Back")
    public void tapBack() {
        List<WebElement> els = driver.findElements(By.xpath("//*[@content-desc='Back']"));
        if (!els.isEmpty()) {
            Rectangle r = els.get(0).getRect();
            // parent clickable often above desc
            driver.executeScript("mobile: clickGesture",
                    Map.of("x", r.x + Math.max(1, r.width / 2),
                            "y", r.y + Math.max(1, r.height / 2)));
            return;
        }
        driver.navigate().back();
    }

    private void tapText(String text) {
        List<WebElement> els = driver.findElements(ComposeLocators.textView(text));
        if (els.isEmpty()) {
            els = driver.findElements(ComposeLocators.textViewContains(text.replace(" *", "")));
        }
        if (els.isEmpty()) {
            throw new IllegalStateException(text + " not on Tell us Materials");
        }
        Rectangle r = els.get(0).getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }
}
