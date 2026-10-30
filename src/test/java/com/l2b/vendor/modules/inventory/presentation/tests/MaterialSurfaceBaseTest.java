package com.l2b.vendor.modules.inventory.presentation.tests;

import com.l2b.vendor.core.driver.DriverManager;
import com.l2b.vendor.core.locators.ComposeLocators;
import com.l2b.vendor.environment.Adb;
import com.l2b.vendor.environment.Config;
import com.l2b.vendor.modules.bookings.presentation.pages.QuickBookingPage;
import com.l2b.vendor.modules.home.presentation.pages.HomePage;
import com.l2b.vendor.modules.home.presentation.tests.HomeBaseTest;
import com.l2b.vendor.modules.inventory.domain.InventoryEnvironment;
import com.l2b.vendor.modules.inventory.presentation.pages.AddProductPage;
import com.l2b.vendor.modules.inventory.presentation.pages.ImportCsvPage;
import com.l2b.vendor.modules.inventory.presentation.pages.InventoryPage;
import com.l2b.vendor.modules.kyc.presentation.pages.KycPage;
import com.l2b.vendor.modules.onboarding.presentation.pages.LanguagePage;
import com.l2b.vendor.modules.onboarding.presentation.pages.SignUpPage;
import com.l2b.vendor.modules.orders.presentation.pages.MaterialOrdersPage;
import com.l2b.vendor.modules.orders.presentation.pages.OrderDetailsPage;
import com.l2b.vendor.modules.profileverification.presentation.pages.ProfileVerificationPage;
import com.l2b.vendor.modules.taxation.presentation.pages.TaxationPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeSuite;

/**
 * Warm Material 9000000017 surface. Never Accept / Decline / Log Out Confirm / force-stop.
 */
public abstract class MaterialSurfaceBaseTest extends HomeBaseTest {

    private static volatile boolean suiteLoggedIn;

    @BeforeSuite(alwaysRun = true)
    public void prepareMaterialSurface() {
        InventoryEnvironment.prepareSuite();
        suiteLoggedIn = false;
    }

    @Override
    protected boolean noReset() {
        return InventoryEnvironment.noReset();
    }

    @Override
    protected boolean newSessionPerMethod() {
        return InventoryEnvironment.newSessionPerMethod();
    }

    @Override
    protected void beforeCreateDriver(Method method) {
        try {
            Adb.ensureNetworkReady();
        } catch (RuntimeException e) {
            Allure.parameter("networkWarn", e.getMessage());
        }
    }

    @Step("Activate Vendor app")
    protected void activateVendor() {
        try {
            ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package"));
        } catch (RuntimeException ignored) {
        }
        sleepQuiet(700);
    }

    @Step("Reach Material Home (close QB if needed)")
    protected HomePage reachMaterialHome() {
        recoverVendorForeground();
        dismissQuickBookingIfPresent();
        dismissSheetsTowardInventory();
        HomePage home = new HomePage();
        if (home.isDisplayedNow()) {
            suiteLoggedIn = true;
            Allure.parameter("after", classifyMaterialNow());
            return home;
        }
        if (materialTabsPresent()) {
            tapBottomTab("Home");
            sleepQuiet(900);
            dismissQuickBookingIfPresent();
            home = new HomePage();
            if (home.isDisplayedNow() || materialTabsPresent()) {
                suiteLoggedIn = true;
                if (!home.isDisplayedNow()) {
                    // Tabs mean warm session — wait briefly for Home chrome
                    sleepQuiet(800);
                    home = new HomePage();
                }
                Allure.parameter("after", classifyMaterialNow());
                return home;
            }
        }
        // Cold OTP only when Language / SignUp / OTP is actually on screen — never Guess from "Get started".
        if (clearlyLoggedOut() && !materialTabsPresent()) {
            Allure.parameter("bootstrap", "otp-login-0017");
            Adb.forceStop(Config.get("app.package"));
            sleepQuiet(600);
            try {
                Adb.run("shell", "am", "start", "-n",
                        Config.get("app.package") + "/" + Config.get("app.activity"));
            } catch (RuntimeException e) {
                try {
                    ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package"));
                } catch (RuntimeException ignored) {
                }
            }
            sleepQuiet(2500);
            dismissAnrIfPresent();
            home = new HomePage();
            if (home.isDisplayedNow() || materialTabsPresent()) {
                if (!home.isDisplayedNow()) {
                    tapBottomTab("Home");
                    sleepQuiet(900);
                    home = new HomePage();
                }
            } else if (clearlyLoggedOut()) {
                try {
                    home = loginToHomeOrQueue();
                } catch (RuntimeException loginFail) {
                    Allure.parameter("loginFail", loginFail.getClass().getSimpleName());
                    recoverVendorForeground();
                    home = new HomePage();
                    if (materialTabsPresent() && !home.isDisplayedNow()) {
                        tapBottomTab("Home");
                        sleepQuiet(900);
                        home = new HomePage();
                    }
                }
            }
            dismissQuickBookingIfPresent();
            home = new HomePage();
            if (home.isDisplayedNow() || materialTabsPresent()) {
                suiteLoggedIn = true;
            }
        } else if (launcherNow() && !materialTabsPresent() && !suiteLoggedIn) {
            // Soft relaunch without OTP — token usually still warm
            Allure.parameter("bootstrap", "activate-warm");
            activateVendor();
            sleepQuiet(1500);
            home = new HomePage();
            if (materialTabsPresent() && !home.isDisplayedNow()) {
                tapBottomTab("Home");
                sleepQuiet(900);
                home = new HomePage();
            }
            if (home.isDisplayedNow() || materialTabsPresent()) {
                suiteLoggedIn = true;
            }
        }
        if (!home.isDisplayedNow() && materialTabsPresent()) {
            tapBottomTab("Home");
            sleepQuiet(900);
            dismissQuickBookingIfPresent();
            home = new HomePage();
        }
        if (!home.isDisplayedNow()) {
            // Prefer soft fail path via tabs over Language timeout cascade
            if (materialTabsPresent()) {
                Allure.parameter("after", "tabs-without-home-chrome");
                suiteLoggedIn = true;
                return home;
            }
            home.waitUntilLoaded();
        }
        Allure.parameter("after", classifyMaterialNow());
        return home;
    }

    @Step("Reach Inventory via bottom tab")
    protected InventoryPage reachInventory() {
        if (new InventoryPage().isDisplayedNow()) {
            Allure.parameter("after", classifyMaterialNow());
            return new InventoryPage();
        }
        dismissSheetsTowardInventory();
        reachMaterialHome();
        tapBottomTab("Inventory");
        sleepQuiet(1000);
        InventoryPage page = new InventoryPage();
        page.waitUntilLoaded();
        Allure.parameter("after", classifyMaterialNow());
        return page;
    }

    @Step("Reach Inventory via drawer Your Inventory")
    protected InventoryPage reachInventoryViaDrawer() {
        HomePage home = reachMaterialHome();
        home.tapDesc("Profile");
        sleepQuiet(800);
        tapText("Your Inventory");
        sleepQuiet(1000);
        InventoryPage page = new InventoryPage();
        page.waitUntilLoaded();
        Allure.parameter("entry", "drawer");
        return page;
    }

    @Step("Reach Add Product")
    protected AddProductPage reachAddProduct() {
        if (new AddProductPage().isDisplayedNow()) {
            return new AddProductPage();
        }
        InventoryPage inv = reachInventory();
        inv.tapAddProduct();
        sleepQuiet(1200);
        AddProductPage page = new AddProductPage();
        page.waitUntilLoaded();
        return page;
    }

    @Step("Reach Import CSV sheet")
    protected ImportCsvPage reachImportCsv() {
        recoverVendorForeground();
        if (new ImportCsvPage().isDisplayedNow()) {
            return new ImportCsvPage();
        }
        InventoryPage inv = reachInventory();
        inv.tapImportCsv();
        sleepQuiet(1200);
        ImportCsvPage page = new ImportCsvPage();
        try {
            page.waitUntilLoaded();
        } catch (RuntimeException first) {
            // Sheet may need a second open after download / rapid close
            recoverVendorForeground();
            inv = reachInventory();
            inv.tapImportCsv();
            sleepQuiet(1400);
            page = new ImportCsvPage();
            page.waitUntilLoaded();
        }
        return page;
    }

    @Step("Reach Material Orders")
    protected MaterialOrdersPage reachOrders() {
        recoverVendorForeground();
        if (new MaterialOrdersPage().isDisplayedNow()) {
            return new MaterialOrdersPage();
        }
        dismissSheetsTowardInventory();
        // Prefer bottom Orders tab when warm — do not require Home chrome first
        if (materialTabsPresent()) {
            tapBottomTab("Orders");
            sleepQuiet(1200);
            MaterialOrdersPage direct = new MaterialOrdersPage();
            if (direct.isDisplayedNow()) {
                suiteLoggedIn = true;
                return direct;
            }
        }
        try {
            reachMaterialHome();
        } catch (RuntimeException homeFail) {
            Allure.parameter("ordersHomeWarn", homeFail.getClass().getSimpleName());
            recoverVendorForeground();
        }
        if (!materialTabsPresent()) {
            recoverVendorForeground();
        }
        if (materialTabsPresent()) {
            tapBottomTab("Orders");
            sleepQuiet(1200);
        }
        MaterialOrdersPage page = new MaterialOrdersPage();
        page.waitUntilLoaded();
        return page;
    }

    /** Prefer a tab that still has order cards (Upcoming may be empty). */
    protected MaterialOrdersPage reachOrdersWithCardsIfAny() {
        MaterialOrdersPage page = reachOrders();
        if (page.isCardChromeVisible() || page.isViewMoreVisible()) {
            return page;
        }
        page.tapActive();
        sleepQuiet(700);
        if (page.isCardChromeVisible() || page.isViewMoreVisible()) {
            return page;
        }
        page.tapCompleted();
        sleepQuiet(700);
        if (page.isCardChromeVisible() || page.isViewMoreVisible()) {
            return page;
        }
        page.tapUpcoming();
        sleepQuiet(500);
        return page;
    }

    @Step("Reach Order details (expand View More Details inline)")
    protected OrderDetailsPage reachOrderDetails() {
        recoverVendorForeground();
        if (new OrderDetailsPage().isDisplayedNow()) {
            return new OrderDetailsPage();
        }
        MaterialOrdersPage orders = reachOrders();
        if (!orders.isViewMoreVisible() && !hasText("View Less Details")) {
            orders.tapUpcoming();
            sleepQuiet(700);
        }
        if (hasText("View Less Details") || new OrderDetailsPage().isDisplayedNow()) {
            OrderDetailsPage already = new OrderDetailsPage();
            if (already.isDisplayedNow()) {
                return already;
            }
        }
        for (int attempt = 0; attempt < 3; attempt++) {
            if (new OrderDetailsPage().isDisplayedNow()) {
                return new OrderDetailsPage();
            }
            if (orders.isViewMoreVisible() || hasText("View More Details")) {
                orders.tapViewMoreDetails();
                sleepQuiet(1600);
            } else {
                try {
                    orders.tapUpcoming();
                } catch (RuntimeException ignored) {
                }
                sleepQuiet(700);
            }
            if (new OrderDetailsPage().isDisplayedNow()) {
                return new OrderDetailsPage();
            }
        }
        OrderDetailsPage page = new OrderDetailsPage();
        page.waitUntilLoaded();
        return page;
    }

    /** Open Material profile drawer without requiring full Home chrome wait. */
    protected void openMaterialDrawer() {
        recoverVendorForeground();
        dismissSheetsTowardInventory();
        if (profileDrawerNow() || hasText("Taxation") || hasText("Your Inventory")
                || (hasText("KYC") && hasText("Account"))) {
            return;
        }
        ensureHomeWithProfile();
        dismissQuickBookingIfPresent();
        tapProfileHeader();
        sleepQuiet(900);
        if (!profileDrawerNow() && !hasText("Taxation") && !hasText("KYC") && !hasText("Account")) {
            try {
                ensureHomeWithProfile();
                tapProfileHeader();
                sleepQuiet(900);
            } catch (RuntimeException ignored) {
            }
        }
    }

    /** Land on Material Home with Profile affordance — OTP bootstrap if Language/SignUp. */
    protected void ensureHomeWithProfile() {
        if (profileControlPresent()) {
            return;
        }
        if (clearlyLoggedOut()) {
            Allure.parameter("drawerBootstrap", "otp-login-0017");
            try {
                HomePage home = loginToHomeOrQueue();
                dismissQuickBookingIfPresent();
                if (!home.isDisplayedNow() && materialTabsPresent()) {
                    tapBottomTab("Home");
                    sleepQuiet(800);
                }
            } catch (RuntimeException loginFail) {
                Allure.parameter("drawerLoginFail", loginFail.getClass().getSimpleName());
                recoverVendorForeground();
                if (clearlyLoggedOut()) {
                    Adb.forceStop(Config.get("app.package"));
                    sleepQuiet(600);
                    try {
                        Adb.run("shell", "am", "start", "-n",
                                Config.get("app.package") + "/" + Config.get("app.activity"));
                    } catch (RuntimeException ignored) {
                        activateVendor();
                    }
                    sleepQuiet(2000);
                    loginToHomeOrQueue();
                    dismissQuickBookingIfPresent();
                }
            }
            suiteLoggedIn = true;
        } else if (materialTabsPresent()) {
            try {
                tapBottomTab("Home");
                sleepQuiet(700);
            } catch (RuntimeException ignored) {
            }
            dismissQuickBookingIfPresent();
        } else {
            try {
                reachMaterialHome();
                dismissQuickBookingIfPresent();
            } catch (RuntimeException homeFail) {
                Allure.parameter("drawerHomeWarn", homeFail.getClass().getSimpleName());
                recoverVendorForeground();
                if (clearlyLoggedOut()) {
                    loginToHomeOrQueue();
                    dismissQuickBookingIfPresent();
                    suiteLoggedIn = true;
                }
            }
        }
        if (!profileControlPresent() && materialTabsPresent()) {
            tapBottomTab("Home");
            sleepQuiet(800);
            dismissQuickBookingIfPresent();
        }
        if (!profileControlPresent()) {
            throw new IllegalStateException("Profile control missing after home bootstrap");
        }
    }

    protected boolean profileControlPresent() {
        return !DriverManager.get().findElements(By.xpath("//*[@content-desc='Profile']")).isEmpty();
    }

    protected void tapProfileHeader() {
        if (!profileControlPresent()) {
            ensureHomeWithProfile();
        }
        List<WebElement> els = DriverManager.get().findElements(By.xpath("//*[@content-desc='Profile']"));
        if (els.isEmpty()) {
            els = DriverManager.get().findElements(By.xpath(
                    "//*[@clickable='true'][@content-desc='Profile']"
                            + " | //android.view.View[@clickable='true'][.//*[@content-desc='Profile']]"));
        }
        if (els.isEmpty()) {
            try {
                new HomePage().tapDesc("Profile");
                return;
            } catch (RuntimeException e) {
                throw new IllegalStateException("Profile control missing");
            }
        }
        WebElement best = els.get(0);
        int minY = best.getRect().y;
        for (WebElement el : els) {
            int y = el.getRect().y;
            if (y >= 0 && y < minY) {
                minY = y;
                best = el;
            }
        }
        Rectangle r = best.getRect();
        DriverManager.get().executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }

    @Step("Reach Taxation")
    protected TaxationPage reachTaxation() {
        recoverVendorForeground();
        if (new TaxationPage().isDisplayedNow()) {
            return new TaxationPage();
        }
        openMaterialDrawer();
        tapText("Taxation");
        sleepQuiet(1200);
        TaxationPage page = new TaxationPage();
        page.waitUntilLoaded();
        return page;
    }

    @Step("Reach KYC via Material drawer")
    protected KycPage reachMaterialKyc() {
        recoverVendorForeground();
        if (new KycPage().isDisplayedNow()) {
            return new KycPage();
        }
        openMaterialDrawer();
        tapText("KYC");
        sleepQuiet(1200);
        KycPage page = new KycPage();
        page.waitUntilLoaded();
        return page;
    }

    @Step("Try Profile Verification (may be unreachable on 0017)")
    protected ProfileVerificationPage tryReachProfileVerification() {
        recoverVendorForeground();
        if (new ProfileVerificationPage().isDisplayedNow()) {
            return new ProfileVerificationPage();
        }
        openMaterialDrawer();
        sleepQuiet(500);
        if (hasText("Profile Verification") || hasText("KYC & Payment")) {
            tapText(hasText("Profile Verification") ? "Profile Verification" : "KYC & Payment");
            sleepQuiet(1000);
        } else if (hasText("KYC")) {
            tapText("KYC");
            sleepQuiet(1000);
            if (hasText("Profile Verification")) {
                tapText("Profile Verification");
                sleepQuiet(1000);
            }
        }
        return new ProfileVerificationPage();
    }

    protected void recoverVendorForeground() {
        dismissAnrIfPresent();
        activateVendor();
        String pkg = vendorPackage();
        if (pkg == null || !pkg.contains("l2b")) {
            // Download / Maps / DocumentsUI — force vendor front
            try {
                Adb.run("shell", "am", "force-stop", "com.google.android.apps.maps");
            } catch (RuntimeException ignored) {
            }
            for (int i = 0; i < 3; i++) {
                try {
                    DriverManager.get().navigate().back();
                } catch (RuntimeException ignored) {
                }
                sleepQuiet(400);
                activateVendor();
                pkg = vendorPackage();
                if (pkg != null && pkg.contains("l2b")) {
                    break;
                }
            }
            if (pkg == null || !pkg.contains("l2b")) {
                try {
                    ((AndroidDriver) DriverManager.get()).activateApp(Config.get("app.package"));
                } catch (RuntimeException ignored) {
                }
                sleepQuiet(800);
            }
        }
        dismissAnrIfPresent();
    }

    protected void dismissAnrIfPresent() {
        try {
            if (hasText("isn't responding") || hasText("Close app") || hasText("Wait")) {
                if (hasText("Wait")) {
                    tapText("Wait");
                    sleepQuiet(500);
                } else if (hasText("Close app")) {
                    tapText("Close app");
                    sleepQuiet(500);
                    activateVendor();
                }
            }
        } catch (RuntimeException ignored) {
        }
    }

    protected void dismissQuickBookingIfPresent() {
        QuickBookingPage qb = new QuickBookingPage();
        if (qb.isDisplayedNow() || hasText("Quick Booking")) {
            try {
                if (qb.isCloseVisible()) {
                    qb.tapClose();
                    sleepQuiet(1000);
                }
            } catch (RuntimeException ignored) {
            }
        }
    }

    protected void dismissSheetsTowardInventory() {
        for (int i = 0; i < 5; i++) {
            if (new InventoryPage().isDisplayedNow() || new HomePage().isDisplayedNow()
                    || new MaterialOrdersPage().isDisplayedNow()) {
                return;
            }
            if (new AddProductPage().isDisplayedNow() || new ImportCsvPage().isDisplayedNow()
                    || new OrderDetailsPage().isDisplayedNow()
                    || new TaxationPage().isDisplayedNow()
                    || new KycPage().isDisplayedNow()
                    || new ProfileVerificationPage().isDisplayedNow()) {
                DriverManager.get().navigate().back();
                sleepQuiet(700);
                continue;
            }
            if (profileDrawerNow()) {
                DriverManager.get().navigate().back();
                sleepQuiet(600);
                continue;
            }
            String pkg = vendorPackage();
            if (pkg != null && !pkg.contains("l2b")) {
                recoverVendorForeground();
                continue;
            }
            break;
        }
    }

    protected String classifyMaterialNow() {
        if (launcherNow()) {
            return "launcher";
        }
        if (new ProfileVerificationPage().isDisplayedNow()) {
            return "profile-verification";
        }
        if (new KycPage().isDisplayedNow()) {
            return "kyc";
        }
        if (new TaxationPage().isDisplayedNow()) {
            return "taxation";
        }
        if (new OrderDetailsPage().isDisplayedNow()) {
            return "order-details";
        }
        if (new MaterialOrdersPage().isDisplayedNow()) {
            return "orders";
        }
        if (new AddProductPage().isDisplayedNow()) {
            return "add-product";
        }
        if (new ImportCsvPage().isDisplayedNow()) {
            return "import-csv";
        }
        if (new InventoryPage().isDisplayedNow()) {
            return "inventory";
        }
        if (profileDrawerNow()) {
            return "drawer";
        }
        if (new QuickBookingPage().isDisplayedNow()) {
            return "quick-booking";
        }
        if (new HomePage().isDisplayedNow()) {
            return "home";
        }
        return "vendor-other";
    }

    protected boolean needsColdLogin() {
        return clearlyLoggedOut();
    }

    /** Strict logged-out detection — avoid false positives from Maps/ANR chrome. */
    protected boolean clearlyLoggedOut() {
        return new LanguagePage().isTitleEnglish()
                || new SignUpPage().isDisplayedNow()
                || hasText("Enter mobile number")
                || hasText("Welcome to L2B")
                || hasText("Verify your OTP");
    }

    protected boolean materialTabsPresent() {
        return !DriverManager.get().findElements(By.xpath("//*[@content-desc='Home']")).isEmpty()
                && (!DriverManager.get().findElements(By.xpath("//*[@content-desc='Inventory']")).isEmpty()
                || !DriverManager.get().findElements(By.xpath("//*[@content-desc='Orders']")).isEmpty());
    }

    protected void tapBottomTab(String desc) {
        List<WebElement> els = DriverManager.get().findElements(By.xpath("//*[@content-desc='" + desc + "']"));
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
        Rectangle r = best.getRect();
        DriverManager.get().executeScript("mobile: clickGesture",
                Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }

    protected boolean hasText(String text) {
        return !DriverManager.get().findElements(ComposeLocators.textView(text)).isEmpty();
    }

    protected void tapText(String text) {
        var els = DriverManager.get().findElements(ComposeLocators.textView(text));
        if (els.isEmpty()) {
            els = DriverManager.get().findElements(ComposeLocators.clickableWithText(text));
        }
        if (els.isEmpty()) {
            throw new IllegalStateException(text + " not visible");
        }
        var r = els.get(0).getRect();
        DriverManager.get().executeScript("mobile: clickGesture",
                java.util.Map.of("x", r.x + Math.max(1, r.width / 2),
                        "y", r.y + Math.max(1, r.height / 2)));
    }

    protected boolean noAcceptVisible() {
        return DriverManager.get().findElements(By.xpath("//*[@text='Accept']")).isEmpty();
    }

    protected static void sleepQuiet(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
