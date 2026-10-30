package com.l2b.vendor.modules.onboarding.presentation.tests;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsMaterialPage;
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
@Feature("Tell us Materials dump — 9000000013")
public class TellUsMaterialDump extends RegistrationBaseTest {

    private static final Path DUMP_DIR = Path.of("/tmp/l2b-tellus-mat-0013-suite");
    private static final Pattern TEXT = Pattern.compile("text=\"([^\"]{1,200})\"");

    @Test(description = "Dump-1: Tell us Materials company type + Continue probe")
    public void dumpTellUsMaterial0013() throws IOException {
        Files.createDirectories(DUMP_DIR);
        TellUsMaterialPage page = reachTellUsMaterial();
        writeDump("00-tellus-mat");
        page.tapCompanyType();
        sleepQuiet(900);
        writeDump("01-company-options");
        if (page.isCompanyOptionsVisible()) {
            page.tapSoleProprietorship();
            sleepQuiet(600);
            writeDump("02-sole");
        }
        page.tapContinue();
        sleepQuiet(700);
        writeDump("03-after-continue");
        Allure.parameter("stillTellUs", String.valueOf(page.isDisplayedNow()));
        Allure.parameter("dumpDir", DUMP_DIR.toString());
    }

    private void writeDump(String name) throws IOException {
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
        Files.writeString(DUMP_DIR.resolve(name + ".txt"),
                "named=" + classifyNow() + "\ntexts=" + texts + "\n", StandardCharsets.UTF_8);
    }
}
