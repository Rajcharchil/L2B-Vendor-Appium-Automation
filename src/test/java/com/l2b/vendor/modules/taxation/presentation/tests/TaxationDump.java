package com.l2b.vendor.modules.taxation.presentation.tests;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.inventory.presentation.tests.MaterialSurfaceBaseTest;
import com.l2b.vendor.modules.taxation.presentation.pages.TaxationPage;
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
@Feature("Taxation dump — 9000000017")
public class TaxationDump extends MaterialSurfaceBaseTest {
    private static final Path DUMP_DIR = Path.of("/tmp/l2b-taxation-0017-suite");
    private static final Pattern TEXT = Pattern.compile("text=\"([^\"]{1,200})\"");

    @Test(description = "Dump-1: Taxation cards + Download/Request probe + Back")
    public void dumpTaxation0017() throws IOException {
        Files.createDirectories(DUMP_DIR);
        TaxationPage page = reachTaxation();
        write("00-taxation");
        if (page.isDownloadVisible()) { page.tapDownloadReport(); sleepQuiet(1000); write("01-download"); }
        if (!page.isDisplayedNow()) { DriverManager.get().navigate().back(); sleepQuiet(700); page = reachTaxation(); }
        if (page.isRequestVisible()) { page.tapRequestReport(); sleepQuiet(1000); write("02-request"); }
        if (!new TaxationPage().isDisplayedNow()) { DriverManager.get().navigate().back(); sleepQuiet(700); }
        page = new TaxationPage();
        if (page.isDisplayedNow()) { page.tapBack(); sleepQuiet(800); }
        write("03-after-back");
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
