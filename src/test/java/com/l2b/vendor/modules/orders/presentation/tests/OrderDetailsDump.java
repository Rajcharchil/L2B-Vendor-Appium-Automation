package com.l2b.vendor.modules.orders.presentation.tests;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.orders.presentation.pages.OrderDetailsPage;
import io.qameta.allure.*;
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
@Feature("Order details dump — 9000000017")
public class OrderDetailsDump extends MaterialSurfaceBaseTest {
    private static final Path DUMP_DIR = Path.of("/tmp/l2b-order-details-0017-suite");
    private static final Pattern TEXT = Pattern.compile("text=\"([^\"]{1,200})\"");

    @Test(description = "Dump-1: View More Details then Back")
    public void dumpOrderDetails0017() throws IOException {
        Files.createDirectories(DUMP_DIR);
        OrderDetailsPage page = reachOrderDetails();
        write("00-details");
        page.tapBack(); sleepQuiet(900);
        write("01-after-back");
        Allure.parameter("dumpDir", DUMP_DIR.toString());
    }

    private void write(String name) throws IOException {
        String src = DriverManager.get().getPageSource();
        Files.writeString(DUMP_DIR.resolve(name + ".xml"), src, StandardCharsets.UTF_8);
        Files.write(DUMP_DIR.resolve(name + ".png"),
                ((TakesScreenshot) DriverManager.get()).getScreenshotAs(OutputType.BYTES));
        Set<String> texts = new LinkedHashSet<>();
        Matcher tm = TEXT.matcher(src);
        while (tm.find()) { if (!tm.group(1).isBlank()) texts.add(tm.group(1)); }
        Files.writeString(DUMP_DIR.resolve(name + ".txt"),
                "named=" + classifyMaterialNow() + "\ntexts=" + texts + "\n");
    }
}
