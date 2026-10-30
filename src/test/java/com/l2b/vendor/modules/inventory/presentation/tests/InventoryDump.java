package com.l2b.vendor.modules.inventory.presentation.tests;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.home.presentation.pages.HomePage;
import com.l2b.vendor.modules.inventory.presentation.pages.InventoryPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;

@Epic("Vendor app")
@Feature("Inventory dump — material 9000000017")
public class InventoryDump extends MaterialSurfaceBaseTest {

    private static final Path DUMP_DIR = Path.of("/tmp/l2b-inventory-0017-suite");
    private static final Pattern TEXT = Pattern.compile("text=\"([^\"]{1,200})\"");
    private static final Pattern DESC = Pattern.compile("content-desc=\"([^\"]{1,160})\"");

    @Test(description = "Dump-1: Inventory tab + drawer + Filter/Expand/qty (no Save)")
    public void dumpInventory0017() throws IOException {
        Files.createDirectories(DUMP_DIR);
        HomePage home = reachMaterialHome();
        writeDump("00-home", classifyMaterialNow());
        home.tapDesc("Inventory");
        sleepQuiet(1000);
        InventoryPage page = new InventoryPage();
        page.waitUntilLoaded();
        writeDump("01-inventory-tab", classifyMaterialNow());
        if (page.isFilterVisible()) {
            page.tapFilter();
            sleepQuiet(700);
            writeDump("02-filter", classifyMaterialNow());
            DriverManager.get().navigate().back();
            sleepQuiet(500);
        }
        if (page.isExpandVisible()) {
            page.tapExpand();
            sleepQuiet(600);
            writeDump("03-expand", classifyMaterialNow());
        }
        try {
            page.tapDecreaseQty();
            sleepQuiet(400);
            page.tapIncreaseQty();
            sleepQuiet(400);
            writeDump("04-qty-no-save", classifyMaterialNow());
        } catch (RuntimeException e) {
            Allure.parameter("qtySkip", e.getMessage());
        }
        page.tapHomeTab();
        sleepQuiet(700);
        reachInventoryViaDrawer();
        writeDump("05-drawer-your-inventory", classifyMaterialNow());
        Allure.parameter("dumpDir", DUMP_DIR.toString());
    }

    private void writeDump(String name, String named) throws IOException {
        String src = DriverManager.get().getPageSource();
        Files.writeString(DUMP_DIR.resolve(name + ".xml"), src, StandardCharsets.UTF_8);
        Files.write(DUMP_DIR.resolve(name + ".png"),
                ((TakesScreenshot) DriverManager.get()).getScreenshotAs(OutputType.BYTES));
        Set<String> texts = new LinkedHashSet<>();
        Matcher tm = TEXT.matcher(src);
        while (tm.find()) {
            if (!tm.group(1).isBlank()) {
                texts.add(tm.group(1));
            }
        }
        Set<String> descs = new LinkedHashSet<>();
        Matcher dm = DESC.matcher(src);
        while (dm.find()) {
            if (!dm.group(1).isBlank()) {
                descs.add(dm.group(1));
            }
        }
        String summary = "named=" + named + "\ntexts=" + texts + "\ndescs=" + descs + "\n";
        Files.writeString(DUMP_DIR.resolve(name + ".txt"), summary, StandardCharsets.UTF_8);
        Allure.addAttachment(name + "-summary", "text/plain", summary);
        Allure.parameter(name + "Pkg",
                ((AndroidDriver) DriverManager.get()).getCurrentPackage());
    }
}
