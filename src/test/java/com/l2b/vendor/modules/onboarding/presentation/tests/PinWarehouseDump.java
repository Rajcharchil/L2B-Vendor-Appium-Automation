package com.l2b.vendor.modules.onboarding.presentation.tests;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.onboarding.presentation.pages.PinWarehousePage;
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
@Feature("Pin warehouse dump — 9000000013")
public class PinWarehouseDump extends RegistrationBaseTest {

    private static final Path DUMP_DIR = Path.of("/tmp/l2b-pin-warehouse-0013-suite");
    private static final Pattern TEXT = Pattern.compile("text=\"([^\"]{1,200})\"");
    private static final Pattern DESC = Pattern.compile("content-desc=\"([^\"]{1,160})\"");

    @Test(description = "Dump-1: Pin warehouse Confirm / Close")
    public void dumpPinWarehouse0013() throws IOException {
        Files.createDirectories(DUMP_DIR);
        PinWarehousePage page = reachPinWarehouse();
        writeDump("00-pin", classifyNow());
        page.tapConfirm();
        sleepQuiet(1200);
        writeDump("01-after-confirm", classifyNow());
        if (!page.isDisplayedNow()) {
            reachPinWarehouse();
        }
        writeDump("02-pin-again", classifyNow());
        page.tapClose();
        sleepQuiet(900);
        writeDump("03-after-close", classifyNow());
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
