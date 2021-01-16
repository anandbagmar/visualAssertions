package com.eot.hackathon;

import io.github.bonigarcia.wdm.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.safari.*;
import org.testng.*;
import org.testng.annotations.*;

import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import static org.testng.Assert.*;

public class HolidayShoppingWithSeTest {

    private static final String BS_USERNAME = "<BS_USERNAME>";
    private static final String BS_AUTOMATE_KEY = "<BS_AUTOMATE_KEY>";
    private static final String REMOTE_URL = "https://" + BS_USERNAME + ":" + BS_AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    private WebDriver driver;
    private final String part1 = "https://demo.applitools.com/tlcHackathonMasterV1.html";
    private final String part2 = "https://demo.applitools.com/tlcHackathonDev.html";
    private final String part3 = "https://demo.applitools.com/tlcHackathonMasterV2.html";

    private final String baseUrl = part3;

    @BeforeSuite
    public void beforeClass() {
        System.out.println("beforeClass");
        WebDriverManager.chromedriver().setup();
        WebDriverManager.firefoxdriver().setup();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest(Method method) throws MalformedURLException {
        System.out.println("Running test: " + method.getName());
//        driver = createSafariDriver();
//        driver = createFirefoxDriver();
        driver = createChromeDriver();
//        driver = createBrowserStackDriver(method);
        driver.manage().window().setSize(new Dimension(1200, 800));
        driver.get(baseUrl);
    }

    private WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1200,800");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
//        options.addArguments("headless");
        return new ChromeDriver(options);
    }

    private WebDriver createBrowserStackDriver(Method method) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("os", "Windows");
        caps.setCapability("os_version", "10");
        caps.setCapability("browser", "Safari");
//        caps.setCapability("browser_version", "80");
        caps.setCapability("name", method.getName());
        driver = new RemoteWebDriver(new URL(REMOTE_URL), caps);
        return driver;
    }

    private WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--window-size=1200,800");
//        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.addArguments("headless");
        driver = new FirefoxDriver(options);
        return driver;
    }

    private SafariDriver createSafariDriver() {
        return new SafariDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest(ITestResult result) {
        System.out.println("Test completed. Result: " + result.getStatus());
        if (null != driver) {
            driver.quit();
        }
    }

    @Test
    public void MainPage() {
        System.out.println("MainPage");
        List<WebElement> productGrid = driver.findElements(By.cssSelector("div.col-6.col-md-4"));
        assertEquals(productGrid.size(), 9, "Number of products is incorrect");
        checkFilters();
        checkHeadersOrFooters("DIV__mainmenu__15", 5, "Number of items in header menu are incorrect");
        WebElement searchBoxTextElement = driver.findElement(By.id("INPUTtext____42"));
        String placeholderText = searchBoxTextElement.getAttribute("placeholder");
        assertEquals("Search over 10,000 shoes!", placeholderText, "Default search text is incorrect ");
        checkHeadersOrFooters("FOOTER____417", 13, "Number of elements in footer is incorrect");
    }

    private void checkHeadersOrFooters(String div__mainmenu__15, int i, String s) {
        WebElement headerElements = driver.findElement(By.id(div__mainmenu__15));
        List<WebElement> headerElementItems = headerElements.findElements(By.xpath(".//li"));
        assertEquals(headerElementItems.size(), i, s);
    }

    private void checkFilters() {
        List<WebElement> listOfTypes = driver.findElement(By.id("UL____77")).findElements(By.className("container_check"));
        assertEquals(listOfTypes.size(), 4, "Number of types is incorrect");
        List<WebElement> listOfColors = driver.findElement(By.id("UL____102")).findElements(By.className("container_check"));
        assertEquals(listOfColors.size(), 5, "Number of colors is incorrect");
        List<WebElement> listOfBrands = driver.findElement(By.id("UL____132")).findElements(By.className("container_check"));
        assertEquals(listOfBrands.size(), 5, "Number of brands is incorrect");
        List<WebElement> listOfPrices = driver.findElement(By.id("UL____162")).findElements(By.className("container_check"));
        assertEquals(listOfPrices.size(), 4, "Number of prices is incorrect");
    }

    @Test
    public void FilteredProductGrid() {
        System.out.println("FilteredProductGrid");

        checkFilters();

        driver.findElement(By.id("SPAN__checkmark__107")).click();
        driver.findElement(By.id("filterBtn")).click();
        List<WebElement> productGrid = driver.findElements(By.cssSelector("div.col-6.col-md-4"));
        assertEquals(productGrid.size(), 2, "Number of products is incorrect");

        List<WebElement> productItems = driver.findElements(By.cssSelector("img.img-fluid "));
        assertEquals(2, productItems.size(), "Number of filtered products are incorrect");

        checkHeadersOrFooters("DIV__mainmenu__15", 5, "Number of items in header menu are incorrect");
        checkHeadersOrFooters("FOOTER____417", 13, "Number of elements in footer is incorrect");
    }

    @Test
    public void ProductDetails() {
        System.out.println("ProductDetails");
        driver.findElement(By.id("H3____218")).click();
        checkHeadersOrFooters("DIV__mainmenu__15", 5, "Number of items in header menu are incorrect");
        checkHeadersOrFooters("FOOTER____115", 13, "Number of elements in footer is incorrect");

        List<WebElement> numberOfStars = driver.findElements(By.cssSelector("i.icon-star.voted"));
        assertEquals(4, numberOfStars.size(), "Rating is incorrect");

        List<WebElement> numberOfStarsNotSelected = driver.findElements(By.xpath("//i[@class=\"icon-star\"]"));
        assertEquals(1, numberOfStarsNotSelected.size(), "Rating is incorrect");

        WebElement numberOfReviews = driver.findElement(By.id("EM____82"));
        assertEquals("4 reviews", numberOfReviews.getText(), "Number of reviews is incorrect");

        WebElement percentageDiscount = driver.findElement(By.cssSelector("span.percentage"));
        String percentageDiscountText = percentageDiscount.getText();
        assertEquals("-30% discount", percentageDiscountText, "Discount is incorrect");

        WebElement skuId = driver.findElement(By.cssSelector("small#SMALL____84"));
        String skuIdText = skuId.getText();
        assertEquals("SKU: MTKRY-001", skuIdText, "SKU number is incorrect");

        WebElement productDescription = driver.findElement(By.id("P____83"));
        String productDescriptionText = productDescription.getText();
        System.out.println("productDescriptionText: " + productDescriptionText);
        String expectedDescription = "SKU: MTKRY-001\n" +
                                             "These boots are comfortable enough to wear all day. Well made. I love the “look”. Best Used For Casual Everyday Walk fearlessly into the cooler months in the Sorel Joan Of Arctic Wedge II Chelsea Boot. Boasting the iconic wedge platform that's as comfortable as it is stylish, this boot features a waterproof upper";
        System.out.println("expectedDescription   : " + expectedDescription);
        assertEquals(expectedDescription, productDescriptionText, "Product description is incorrect");
    }
}