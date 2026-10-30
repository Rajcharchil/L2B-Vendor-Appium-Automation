package com.l2b.vendor.modules.onboarding.presentation.tests;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.onboarding.presentation.pages.TellUsPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
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
@Feature("Tell us dump — fresh 9000000013")
public class TellUsDump extends TellUsBaseTest {

    private static final Path DUMP_DIR = Path.of("/tmp/l2b-tellus-0013-20261006-suite");
    private static final Pattern TEXT = Pattern.compile("text=\"([^\"]{1,200})\"");
    private static final Pattern DESC = Pattern.compile("content-desc=\"([^\"]{1,160})\"");

    @Test(description = "Dump-1: Tell us chrome + company + TPI sheet Cancel")
    @Description("9000000013. Stay on Tell us. Never Take Photo/Gallery/Log Out Confirm.")
    public void dumpTellUs0013() throws IOException {
        Files.createDirectories(DUMP_DIR);
        TellUsPage page = reachTellUs();
        writeDump("00-tellus", classifyTellUsNow());
        Allure.parameter("name", page.nameFieldText());
        Allure.parameter("email", page.emailFieldText());
        Allure.parameter("continueOuterEnabled", String.valueOf(page.isContinueOuterEnabled()));

        page.tapCompanyCheckbox();
        sleepQuiet(700);
        writeDump("01-company", classifyTellUsNow());

        page.tapCompanyCheckbox();
        sleepQuiet(700);
        writeDump("02-company-toggle", classifyTellUsNow());

        page.tapUploadTpi();
        sleepQuiet(900);
        writeDump("03-upload-sheet", classifyTellUsNow());
        if (page.isUploadSheetVisible()) {
            page.tapSheetCancel();
            sleepQuiet(800);
        }
        writeDump("04-after-cancel", classifyTellUsNow());

        page.tapContinueOuter();
        sleepQuiet(600);
        writeDump("05-after-continue", classifyTellUsNow());
        Allure.parameter("stillTellUs", String.valueOf(page.isDisplayedNow()));
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
