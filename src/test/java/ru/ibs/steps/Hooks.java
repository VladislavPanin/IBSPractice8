
package ru.ibs.steps;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class Hooks {
    private static WebDriver driver;

    private static Properties props = new Properties();
    private static int idPreviousProduct;
    @BeforeAll
    public static void setup() {
        try (FileInputStream input = new FileInputStream("src/test/resources/application.properties")) {
            props.load(input);
            String driverType = props.getProperty("type.driver");

            if("remote".equalsIgnoreCase(driverType)) {
                initRemoteDriver();
                driver.get("http://149.154.71.152:8080/food");
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
            System.out.println(e.getMessage());
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

    public static void initRemoteDriver(){
        try {

            URL remoteUrl = new URL("http://149.154.71.152:4444/wd/hub");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            Map<String, Object> selenoidOptions = new HashMap<>();
            selenoidOptions.put("browserName", "chrome");
            selenoidOptions.put("browserVersion", "109.0");
            selenoidOptions.put("enableVNC", true);
            selenoidOptions.put("enableVideo", false);
            capabilities.setCapability("selenoid:options",selenoidOptions);

            driver = new RemoteWebDriver(remoteUrl, capabilities);
          //  Thread.sleep(10000);
        }catch (MalformedURLException e) {
            throw new RuntimeException("Invalid remote URL", e);
       // } catch (InterruptedException e) {
      //      throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void endTesting() {
        driver.quit();
    }


}
