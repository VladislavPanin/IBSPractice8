package ru.ibs.steps;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.util.Properties;


public class Hooks {
    private static WebDriver driver;

    private static Properties props = new Properties();
    private static int idPreviousProduct;
    @BeforeAll
    public static void setup() {
        try (FileInputStream input = new FileInputStream("path/to/application.properties")) {
            props.load(input);
            String driverType = props.getProperty("type.driver");
            String selenoidUrl = props.getProperty("selenoid.url");

            if("remote".equalsIgnoreCase(driverType)) {
                initRemoteDriver(selenoidUrl);
            } else {
                ChromeOptions co = new ChromeOptions();
                co.setBinary("D:\\opt\\chrome-win64.chrome.exe");

                driver = new ChromeDriver();
                System.setProperty("webdriver.chromedriver.diver",
                        "\\auto-testing-qualit-sandbox\\src\\test\\resources\\chromedriver.exe");

                driver.manage().window().maximize();
                //driver.get("http://localhost:8080/food");
                driver.get("http://149.154.71.152:8080/food");
            }
        } catch (Exception e){

        }


    }

    static int findPreviousProductId() {
        try {
            Thread.sleep(500);
            idPreviousProduct = Integer.parseInt(driver.findElement(By.xpath(
                    "(//tr)[last()]/th")).getText());
            return idPreviousProduct;
        } catch (Exception e) {
            Assertions.fail("Invalid id value - cannot be converted to int.");
            return 0;
        }
    }

    public static int getIdPreviousProduct(){
        return idPreviousProduct;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void initRemoteDriver(String selenoidUrl){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setVersion("109.0");
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", false);
    }

    @AfterAll
    public static void endTesting() {
        driver.quit();
    }


}
