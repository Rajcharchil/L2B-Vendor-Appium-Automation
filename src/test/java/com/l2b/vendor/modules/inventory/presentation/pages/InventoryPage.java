package com.l2b.vendor.modules.inventory.presentation.pages;

import com.l2b.vendor.core.locators.ComposeLocators;
import com.l2b.vendor.core.ui.SplashScreen;
import com.l2b.vendor.core.wait.Waits;
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

/** Material Inventory — dump /tmp/l2b-material-0017-gap-20261007. */
public class InventoryPage extends SplashScreen {

    public static final String TITLE = "Inventory";
    public static final String ADD_PRODUCT = "+ Add Product";
    public static final String IMPORT_CSV = "Import CSV";
    public static final String SEARCH = "Search Item...";
    public static final String ALL_INVENTORY = "All Material Inventory";

    @Override
    @Step("Wait for Inventory")
    public void waitUntilLoaded() {
        Waits.until(driver, d -> isDisplayedNow() ? Boolean.TRUE : null,
                "Inventory did not appear", Duration.ofSeconds(15));
    }

    @Step("Check Inventory")
    public boolean isDisplayedNow() {
        return isPresent(ComposeLocators.textView(TITLE))
                && (isPresent(ComposeLocators.textView(ADD_PRODUCT))
                || isPresent(ComposeLocators.textView(ALL_INVENTORY))
                || isPresent(ComposeLocators.textView(IMPORT_CSV)));
    }

    @Step("Title")
    public boolean isTitleVisible() {
        return isPresent(ComposeLocators.textView(TITLE));
    }

    @Step("Add Product")
    public boolean isAddProductVisible() {
        return isPresent(ComposeLocators.textView(ADD_PRODUCT));
    }

    @Step("Import CSV")
    public boolean isImportCsvVisible() {
        return isPresent(ComposeLocators.textView(IMPORT_CSV));
    }

    @Step("Search")
    public boolean isSearchVisible() {
        return isPresent(ComposeLocators.textView(SEARCH))
                || !driver.findElements(By.className("android.widget.EditText")).isEmpty();
    }

    @Step("All Material Inventory")
    public boolean isAllInventoryVisible() {
        return isPresent(ComposeLocators.textView(ALL_INVENTORY));
    }

    @Step("Stock row chrome")
    public boolean isStockRowVisible() {
        return isPresent(ComposeLocators.textView("In stock"))
                || isPresent(ComposeLocators.textView("Out of stock"))
                || isPresent(ComposeLocators.textViewContains("AAC"));
    }

    @Step("Material bottom tabs")
    public boolean areMaterialTabsVisible() {
        return isPresent(By.xpath("//*[@content-desc='Orders']"))
                && isPresent(By.xpath("//*[@content-desc='Home']"))
                && isPresent(By.xpath("//*[@content-desc='Earning']"))
                && isPresent(By.xpath("//*[@content-desc='Inventory']"));
    }

    @Step("Filter desc")
    public boolean isFilterVisible() {
        return !driver.findElements(By.xpath("//*[@content-desc='Filter']")).isEmpty();
    }

    @Step("Expand desc")
    public boolean isExpandVisible() {
        return !driver.findElements(By.xpath("//*[@content-desc='Expand']")).isEmpty();
    }

    @Step("Tap Add Product")
    public void tapAddProduct() {
        tapText(ADD_PRODUCT);
    }

    @Step("Tap Import CSV")
    public void tapImportCsv() {
        tapText(IMPORT_CSV);
    }

    @Step("Tap Filter")
    public void tapFilter() {
        tapDesc("Filter");
    }

    @Step("Tap Expand")
    public void tapExpand() {
        tapDesc("Expand");
    }

    @Step("Tap Decrease quantity")
    public void tapDecreaseQty() {
        tapDesc("Decrease quantity");
    }

    @Step("Tap Increase quantity")
    public void tapIncreaseQty() {
        tapDesc("Increase quantity");
    }

    @Step("Tap bottom Inventory")
    public void tapInventoryTab() {
        tapBottomDesc("Inventory");
    }

    @Step("Tap bottom Home")
    public void tapHomeTab() {
        tapBottomDesc("Home");
    }

    @Step("Tap bottom Orders")
    public void tapOrdersTab() {
        tapBottomDesc("Orders");
    }

    @Step("Type search")
    public void typeSearch(String q) {
        List<WebElement> fields = driver.findElements(By.className("android.widget.EditText"));
        if (!fields.isEmpty()) {
            WebElement f = fields.get(0);
            f.click();
            try {
                f.clear();
            } catch (RuntimeException ignored) {
            }
            f.sendKeys(q);
            return;
        }
        tapText(SEARCH);
        driver.findElement(By.className("android.widget.EditText")).sendKeys(q);
    }

    @Step("Clear search")
    public void clearSearch() {
        List<WebElement> fields = driver.findElements(By.className("android.widget.EditText"));
        if (fields.isEmpty()) {
            return;
        }
        WebElement f = fields.get(0);
        f.click();
        try {
            f.clear();
        } catch (RuntimeException e) {
            f.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        }
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
            els = driver.findElements(ComposeLocators.clickableWithText(text));
        }
        if (els.isEmpty()) {
            throw new IllegalStateException(text + " not on Inventory");
        }
        gesture(els.get(0));
    }

    private void tapDesc(String desc) {
        List<WebElement> els = driver.findElements(By.xpath("//*[@content-desc='" + desc + "']"));
        if (els.isEmpty()) {
            throw new IllegalStateException(desc + " not on Inventory");
        }
        gesture(els.get(0));
    }

    private void tapBottomDesc(String desc) {
        List<WebElement> els = driver.findElements(By.xpath("//*[@content-desc='" + desc + "']"));
        if (els.isEmpty()) {
            throw new IllegalStateException(desc + " tab missing");
        }
        WebElement best = els.get(0);
        int maxY = best.getRect().y;
        for (WebElement el : els) {
            if (el.getRect().y > maxY) {
                maxY = el.getRect().y;
                best = el;
            }
        }
        gesture(best);
    }

    private void gesture(WebElement el) {
        Rectangle r = el.getRect();
        driver.executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }
}
