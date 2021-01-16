package com.eot.hackathon;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import com.applitools.eyes.visualgrid.model.*;
import com.applitools.eyes.visualgrid.services.*;
import io.github.bonigarcia.wdm.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.testng.*;
import org.testng.annotations.*;

import java.lang.reflect.*;

public class HolidayShoppingWithEyesTest {

    private final String appName = "AppliFashionHackathon";
    private final RectangleSize viewportSize = new RectangleSize(1200, 800);
    private WebDriver driver;
    private final String part1 = "https://demo.applitools.com/tlcHackathonMasterV1.html";
    private final String part2 = "https://demo.applitools.com/tlcHackathonDev.html";
    private final String part3 = "https://demo.applitools.com/tlcHackathonMasterV2.html";

    private final String baseUrl = part3;
    private Eyes eyes;
    private static final BatchInfo batchInfo = new BatchInfo("Holiday Shopping");
    private EyesRunner visualGridRunner;

    @BeforeSuite
    public void beforeClass() {
        System.out.println("beforeClass");
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest(Method method) {
        System.out.println("Running test: " + method.getName());
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
//        options.addArguments("headless");
        driver = new ChromeDriver(options);
        driver.get(baseUrl);
        visualGridRunner = new VisualGridRunner();

        eyes = new Eyes(visualGridRunner);
        Configuration config = eyes.getConfiguration();
        config.setBatch(batchInfo);
        config.addBrowser(1200, 1200, BrowserType.CHROME);
        config.addBrowser(1200, 1200, BrowserType.FIREFOX);
        config.addBrowser(1200, 1200, BrowserType.EDGE_CHROMIUM);
        config.addBrowser(1200, 1200, BrowserType.SAFARI);
        config.addBrowser(new IosDeviceInfo(IosDeviceName.iPhone_X));
        eyes.setConfiguration(config);
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest(ITestResult result) {
        System.out.println("Test completed. Result: " + result.getStatus());
        eyes.closeAsync();
        TestResultsSummary allTestResults = visualGridRunner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        System.out.println("Number of results for test - " + result.getMethod().getMethodName() + ": " + results.length);
        boolean mismatchFound = false;
        for (TestResultContainer eachResult : results) {
            TestResults testResult = eachResult.getTestResults();
            mismatchFound = mismatchFound || handleTestResults(testResult);
        }
        System.out.println("Overall mismatchFound: " + mismatchFound);
        if (null != driver) {
            driver.quit();
        }
    }

    protected boolean handleTestResults(TestResults result) {
        System.out.println("\t\t" + result);
        System.out.printf("\t\tName = '%s', \nDuration = %d, matched = %d, mismatched = %d, missing = %d, aborted = %s\n",
                result.getName(),
                result.getDuration(),
                result.getMatches(),
                result.getMismatches(),
                result.getMissing(),
                (result.isAborted() ? "aborted" : "no"));
        System.out.println("Results available here: " + result.getUrl());
        boolean hasMismatches = result.getMismatches() != 0 || result.isAborted();
        System.out.println("result: has mismatches or was aborted: " + hasMismatches);
        return hasMismatches;
    }

    @Test
    public void MainPage() {
        System.out.println("MainPage");
        eyes.open(driver, appName, "Test 1", viewportSize);
        eyes.checkWindow("main page");
    }

    @Test
    public void FilteredProductGrid() {
        System.out.println("FilteredProductGrid");
        eyes.open(driver, appName, "Test 2", viewportSize);
        driver.findElement(By.id("SPAN__checkmark__107")).click();
        driver.findElement(By.id("filterBtn")).click();
        eyes.checkRegion(By.id("product_grid"), "filter by color");
    }

    @Test
    public void ProductDetails() {
        System.out.println("ProductDetails");
        eyes.open(driver, appName, "Test 3", viewportSize);
        driver.findElement(By.id("H3____218")).click();
        eyes.checkWindow("product details");
    }
}