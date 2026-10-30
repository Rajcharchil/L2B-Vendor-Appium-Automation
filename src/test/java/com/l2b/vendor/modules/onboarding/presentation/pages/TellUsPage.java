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
 * Registration leftover — Tell us about yourself (9000000013).
 * Dump {@code /tmp/l2b-tellus-0013-20261006}. Root {@code operator_basic_info_root}.
 * Continue enabled lives on the clickable outer View (inner TextView/Button stay
 * {@code enabled=true} decoys). Never tap Take Photo / Gallery / Log Out Confirm.
 */
public class TellUsPage extends SplashScreen {

    public static final String TITLE = "Tell us about yourself ?";
    public static final String ROOT_ID = "operator_basic_info_root";
    public static final String NAME_PREFILL = "waardian tech";
    public static final String EMAIL_PREFILL = "manju@gmail.com";
    public static final String TPI = "Upload TPI certificate document *";
    public static final String TPI_HINT = "( upto 6mb jpeg, pdf )";
    public static final String COMPANY = "Are you associated with a company?";
    public static final String CONTINUE = "Continue";

    @Override
    @Step("Wait for Tell us about yourself")
    public void waitUntilLoaded() {
        Waits.until(driver, d -> isDisplayedNow() ? Boolean.TRUE : null,
                "Tell us about yourself did not appear", Duration.ofSeconds(20));
    }

    @Step("Check Tell us identity")
    public boolean isDisplayedNow() {
        return isTitleVisible()
                || isPresent(By.id(ROOT_ID))
                || isPresent(ComposeLocators.textView(TPI));
    }

    @Step("Check title")
    public boolean isTitleVisible() {
        return isPresent(ComposeLocators.textView(TITLE))
                || isPresent(ComposeLocators.textViewContains("Tell us about yourself"));
    }

    @Step("Check Back")
    public boolean isBackVisible() {
        return !driver.findElements(By.xpath(
                "//*[@content-desc='Back' or @content-desc='Navigate up']")).isEmpty();
    }

    @Step("Check prefill name")
    public boolean isNamePrefillVisible() {
        return NAME_PREFILL.equals(nameFieldText());
    }

    @Step("Check prefill email")
    public boolean isEmailPrefillVisible() {
        return EMAIL_PREFILL.equals(emailFieldText());
    }

    @Step("Read name EditText")
    public String nameFieldText() {
        List<WebElement> fields = driver.findElements(By.className("android.widget.EditText"));
        if (fields.isEmpty()) {
            return "";
        }
        String raw = fields.get(0).getAttribute("text");
        return raw == null || "null".equalsIgnoreCase(raw) ? "" : raw;
    }

    @Step("Read email EditText")
    public String emailFieldText() {
        List<WebElement> fields = driver.findElements(By.className("android.widget.EditText"));
        if (fields.size() < 2) {
            return "";
        }
        String raw = fields.get(1).getAttribute("text");
        return raw == null || "null".equalsIgnoreCase(raw) ? "" : raw;
    }

    @Step("TPI upload row")
    public boolean isTpiRowVisible() {
        return isPresent(ComposeLocators.textView(TPI))
                || isPresent(ComposeLocators.textViewContains("TPI certificate"));
    }

    @Step("TPI size hint")
    public boolean isTpiHintVisible() {
        return isPresent(ComposeLocators.textView(TPI_HINT))
                || isPresent(ComposeLocators.textViewContains("6mb"))
                || isPresent(ComposeLocators.textViewContains("jpeg"));
    }

    @Step("Company checkbox copy")
    public boolean isCompanyCopyVisible() {
        return isPresent(ComposeLocators.textView(COMPANY));
    }

    @Step("Continue visible")
    public boolean isContinueVisible() {
        return isPresent(ComposeLocators.textView(CONTINUE));
    }

    /**
     * Outer Continue View. Dump: {@code enabled=false} without TPI; inner TextView
     * stays enabled=true (decoy).
     */
    @Step("Continue outer View enabled")
    public boolean isContinueOuterEnabled() {
        List<WebElement> rows = driver.findElements(ComposeLocators.clickableWithText(CONTINUE));
        if (rows.isEmpty()) {
            return false;
        }
        return Boolean.parseBoolean(rows.get(0).getAttribute("enabled"));
    }

    @Step("Tap company checkbox")
    public void tapCompanyCheckbox() {
        List<WebElement> boxes = driver.findElements(By.className("android.widget.CheckBox"));
        if (boxes.isEmpty()) {
            throw new IllegalStateException("Company CheckBox not on Tell us");
        }
        Rectangle r = boxes.get(0).getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }

    @Step("Tap Upload TPI row")
    public void tapUploadTpi() {
        List<WebElement> rows = driver.findElements(ComposeLocators.clickableWithText(TPI));
        if (rows.isEmpty()) {
            List<WebElement> texts = driver.findElements(ComposeLocators.textView(TPI));
            if (texts.isEmpty()) {
                throw new IllegalStateException("TPI row not on Tell us");
            }
            Rectangle r = texts.get(0).getRect();
            driver.executeScript("mobile: clickGesture",
                    Map.of("x", r.x + Math.max(1, r.width / 2),
                            "y", r.y + Math.max(1, r.height / 2)));
            return;
        }
        Rectangle r = rows.get(0).getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }

    @Step("Upload sheet visible")
    public boolean isUploadSheetVisible() {
        return isPresent(ComposeLocators.textView("Add Document Photo"))
                || isPresent(ComposeLocators.textView("Take Photo"))
                || isPresent(ComposeLocators.textView("Choose from Gallery"));
    }

    @Step("Tap sheet Cancel")
    public void tapSheetCancel() {
        List<WebElement> cancel = driver.findElements(ComposeLocators.textView("Cancel"));
        if (!cancel.isEmpty()) {
            Rectangle r = cancel.get(0).getRect();
            driver.executeScript("mobile: clickGesture",
                    Map.of("x", r.x + Math.max(1, r.width / 2),
                            "y", r.y + Math.max(1, r.height / 2)));
            return;
        }
        driver.navigate().back();
    }

    @Step("Tap Continue outer (may be disabled)")
    public void tapContinueOuter() {
        List<WebElement> rows = driver.findElements(ComposeLocators.clickableWithText(CONTINUE));
        if (rows.isEmpty()) {
            List<WebElement> texts = driver.findElements(ComposeLocators.textView(CONTINUE));
            if (texts.isEmpty()) {
                return;
            }
            Rectangle r = texts.get(0).getRect();
            driver.executeScript("mobile: clickGesture",
                    Map.of("x", r.x + Math.max(1, r.width / 2),
                            "y", r.y + Math.max(1, r.height / 2)));
            return;
        }
        try {
            rows.get(0).click();
        } catch (RuntimeException e) {
            Rectangle r = rows.get(0).getRect();
            driver.executeScript("mobile: clickGesture",
                    Map.of("x", r.x + Math.max(1, r.width / 2),
                            "y", r.y + Math.max(1, r.height / 2)));
        }
    }

    @Step("Tap Add profile photo chip")
    public void tapAddProfilePhoto() {
        List<WebElement> els = driver.findElements(By.xpath(
                "//*[@content-desc='Add profile photo']/.."));
        if (els.isEmpty()) {
            els = driver.findElements(By.xpath("//*[@content-desc='Add profile photo']"));
        }
        if (els.isEmpty()) {
            return;
        }
        Rectangle r = els.get(0).getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }

    @Step("Set name field (restore after probes)")
    public void setName(String value) {
        List<WebElement> fields = driver.findElements(By.className("android.widget.EditText"));
        if (fields.isEmpty()) {
            return;
        }
        WebElement field = fields.get(0);
        field.click();
        field.clear();
        field.sendKeys(value);
    }

    @Step("Set email field (restore after probes)")
    public void setEmail(String value) {
        List<WebElement> fields = driver.findElements(By.className("android.widget.EditText"));
        if (fields.size() < 2) {
            return;
        }
        WebElement field = fields.get(1);
        field.click();
        field.clear();
        field.sendKeys(value);
    }
}
