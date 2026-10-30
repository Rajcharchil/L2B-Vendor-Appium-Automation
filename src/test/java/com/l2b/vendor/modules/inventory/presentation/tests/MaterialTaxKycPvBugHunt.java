package com.l2b.vendor.modules.inventory.presentation.tests;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.modules.kyc.presentation.pages.KycPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.LanguagePage;
import com.l2b.vendor.modules.onboarding.presentation.pages.OtpPage;
import com.l2b.vendor.modules.profileverification.presentation.pages.ProfileVerificationPage;
import com.l2b.vendor.modules.taxation.presentation.pages.TaxationPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;

/**
 * Bug-hunt dump for Material 9000000017 — Language → Tax / KYC / PV.
 * Records functional + a11y defects; never Accept / Decline / Log Out Confirm / paid submit.
 */
@Epic("Vendor app")
@Feature("Bug hunt — Tax / KYC / PV on 9000000017")
public class MaterialTaxKycPvBugHunt extends MaterialSurfaceBaseTest {

    private static final Path DUMP = Path.of("/tmp/l2b-bug-hunt-tax-kyc-pv-0017");
    private static final Pattern TEXT = Pattern.compile("text=\"([^\"]{1,200})\"");
    private final List<String> bugs = new ArrayList<>();

    @Test(description = "BUG-HUNT: Language + Taxation + KYC + PV on 9000000017")
    @Description("Probe Language Get started, login, drawer Taxation/KYC/PV. Log open bugs.")
    public void huntTaxKycPvBugs0017() throws IOException {
        Files.createDirectories(DUMP);

        // --- Language: Get started without selecting a row ---
        LanguagePage language = new LanguagePage();
        if (language.isDisplayedNow()) {
            write("00-language-landing");
            boolean englishChecked = !DriverManager.get()
                    .findElements(By.xpath(
                            "//android.widget.TextView[@text='English']/ancestor::android.view.View[@clickable='true'][1]"
                                    + "//android.widget.CheckBox[@checked='true']"))
                    .isEmpty();
            if (!englishChecked) {
                note("BUG-LANG-1", "Medium",
                        "Language: English shows label Default but checkbox checked=false; "
                                + "no language pre-selected in a11y tree.");
            }
            try {
                language.tapGetStarted();
                sleepQuiet(1200);
            } catch (RuntimeException ignored) {
            }
            write("01-after-getstarted-no-select");
            if (language.isDisplayedNow()) {
                note("BUG-LANG-2", "High",
                        "Language: Get started is enabled/clickable with no language selected, "
                                + "but tap is a no-op — user stays on Welcome to L2B. "
                                + "Expected: disable Get started until a language is selected, "
                                + "or auto-select Default (English).");
            }
        }

        // --- Login ---
        ensureHomeWithProfile();
        write("02-home-after-login");
        OtpPage otpProbe = new OtpPage();
        if (otpProbe.isDisplayedNow() && otpProbe.otpEditTextCount() == 0) {
            note("BUG-OTP-1", "High",
                    "OTP: title Verify your OTP visible but no android.widget.EditText in hierarchy "
                            + "(otpEditTextCount=0). User/automation cannot enter OTP via standard field. "
                            + "Expected: focusable EditText (or labelled OTP boxes) exposed to a11y.");
            write("02b-otp-no-edittext");
        }

        // --- Taxation ---
        TaxationPage tax = reachTaxation();
        write("10-taxation");
        probeTaxation(tax);
        tax.tapBack();
        sleepQuiet(700);

        // --- KYC ---
        KycPage kyc = reachMaterialKyc();
        write("20-kyc");
        probeKyc(kyc);
        try {
            kyc.tapBack();
        } catch (RuntimeException e) {
            DriverManager.get().navigate().back();
        }
        sleepQuiet(700);

        // --- PV ---
        ProfileVerificationPage pv = tryReachProfileVerification();
        write("30-pv");
        probePv(pv);

        Files.writeString(DUMP.resolve("BUGS.txt"), String.join("\n", bugs) + "\n",
                StandardCharsets.UTF_8);
        Allure.parameter("bugsFound", String.valueOf(bugs.size()));
        Allure.parameter("dumpDir", DUMP.toString());
        for (String b : bugs) {
            Allure.addAttachment("bug", "text/plain", b);
        }
    }

    private void probeTaxation(TaxationPage tax) {
        if (!tax.isDisplayedNow()) {
            note("BUG-TAX-1", "High", "Taxation: drawer → Taxation did not open / chrome missing.");
            return;
        }
        if (!tax.isGstMonthlyVisible() || !tax.isOnDemandVisible() || !tax.isGstr1Visible()) {
            note("BUG-TAX-2", "Medium",
                    "Taxation: one or more report cards missing "
                            + "(Monthly=" + tax.isGstMonthlyVisible()
                            + ", OnDemand=" + tax.isOnDemandVisible()
                            + ", GSTR1=" + tax.isGstr1Visible() + ").");
        }
        if (tax.isDownloadVisible()) {
            tax.tapDownloadReport();
            sleepQuiet(1200);
            writeQuiet("11-after-download");
            String src = safeSrc();
            if (src.contains("Something went wrong") || src.contains("unexpected error")) {
                note("BUG-TAX-3", "High",
                        "Taxation: Download Report shows error empty state without recoverable report.");
            }
            if (!new TaxationPage().isDisplayedNow() && !src.contains("Taxation")) {
                // external app / lost vendor — note then recover
                note("BUG-TAX-4", "Medium",
                        "Taxation: Download Report left Taxation chrome (package/screen changed).");
                recoverVendorForeground();
                try {
                    reachTaxation();
                } catch (RuntimeException ignored) {
                }
            }
        }
        if (tax.isRequestVisible()) {
            tax.tapRequestReport();
            sleepQuiet(1200);
            writeQuiet("12-after-request");
            String src = safeSrc();
            if (src.contains("Something went wrong") || src.contains("unexpected error")) {
                note("BUG-TAX-5", "High",
                        "Taxation: Request Report shows error empty state.");
            }
            if (!new TaxationPage().isDisplayedNow()) {
                DriverManager.get().navigate().back();
                sleepQuiet(700);
            }
        }
        if (tax.isReportsEmptyVisible()) {
            Allure.parameter("taxReportsEmpty", "true");
        }
    }

    private void probeKyc(KycPage kyc) {
        if (!kyc.isDisplayedNow()) {
            note("BUG-KYC-1", "High", "KYC: drawer → KYC did not open.");
            return;
        }
        if (kyc.isErrorVisible()) {
            note("BUG-KYC-2", "High",
                    "KYC: opens with error empty state (Something went wrong / unexpected error). "
                            + "Retry=" + kyc.isRetryVisible() + ". Expected: Document/Bank details "
                            + "for completed material vendor 9000000017.");
            if (kyc.isRetryVisible()) {
                try {
                    kyc.tapRetry();
                    sleepQuiet(1500);
                    writeQuiet("21-kyc-after-retry");
                    if (kyc.isErrorVisible()) {
                        note("BUG-KYC-3", "High",
                                "KYC: Retry does not recover Document/Bank details — still error.");
                    }
                } catch (RuntimeException ignored) {
                }
            }
            return;
        }
        if (!kyc.isFullChromeVisible()) {
            note("BUG-KYC-4", "Medium",
                    "KYC: title present but Document Details / Bank Details chrome missing.");
        }
        String src = safeSrc();
        long dashes = src.split("text=\"—\"", -1).length - 1
                + src.split("text=\"–\"", -1).length - 1;
        if (dashes >= 4) {
            note("BUG-KYC-5", "High",
                    "KYC: multiple em-dash (—) values on Document/Bank fields — blank shell "
                            + "looking like filled KYC (same pattern as rental #54).");
        }
    }

    private void probePv(ProfileVerificationPage pv) {
        boolean onPv = pv.isDisplayedNow() || hasText("Profile Verification")
                || hasText("KYC & Payment");
        if (!onPv) {
            // May be unreachable on warm completed 0017 — note as gap, not always a bug
            note("BUG-PV-1", "Medium",
                    "Profile Verification: not reachable from Material drawer on 9000000017 "
                            + "(no Profile Verification / KYC & Payment row after open drawer). "
                            + "If PV is required for material vendors, this is a product gap.");
            writeQuiet("31-drawer-no-pv");
            return;
        }
        String src = safeSrc();
        if (src.contains("Something went wrong") || src.contains("unexpected error")) {
            note("BUG-PV-2", "High",
                    "Profile Verification: error empty state instead of verification steps.");
        }
        if (src.contains("Verify") && !src.contains("Continue") && !src.contains("Submit")) {
            Allure.parameter("pvChromePartial", "true");
        }
    }

    private void note(String id, String severity, String desc) {
        String line = id + " [" + severity + "] " + desc;
        bugs.add(line);
        Allure.parameter(id, severity);
    }

    private void write(String name) {
        writeQuiet(name);
    }

    private void writeQuiet(String name) {
        try {
            String src = safeSrc();
            Files.writeString(DUMP.resolve(name + ".xml"), src, StandardCharsets.UTF_8);
            Files.write(DUMP.resolve(name + ".png"),
                    ((TakesScreenshot) DriverManager.get()).getScreenshotAs(OutputType.BYTES));
            Set<String> texts = new LinkedHashSet<>();
            Matcher tm = TEXT.matcher(src);
            while (tm.find()) {
                if (!tm.group(1).isBlank()) {
                    texts.add(tm.group(1));
                }
            }
            Files.writeString(DUMP.resolve(name + ".txt"),
                    "named=" + classifyMaterialNow() + "\ntexts=" + texts + "\n",
                    StandardCharsets.UTF_8);
        } catch (IOException | RuntimeException e) {
            Allure.parameter("dumpWarn-" + name, e.getClass().getSimpleName());
        }
    }

    private String safeSrc() {
        try {
            String s = DriverManager.get().getPageSource();
            return s == null ? "" : s;
        } catch (RuntimeException e) {
            return "";
        }
    }
}
