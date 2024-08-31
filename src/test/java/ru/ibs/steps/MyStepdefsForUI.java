package ru.ibs.steps;
import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class MyStepdefsForUI {

    @И("открытие модального окна")
    public void openModelWindow() {
        WebElement bntAdd = Hooks.getDriver().findElement(By.xpath("//button[contains(.,'Добавить')]"));
        bntAdd.click();

        WebDriverWait wait = new WebDriverWait(Hooks.getDriver(), Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(., 'Добавление товара')]")));
        } catch (TimeoutException e) {
            Assertions.fail("Failed to open modal window.");
        }
    }

    @И("найти айди только что созданого продукта")
    public void findProductId(){
        String idCurrentWebElemStr = findCurrentProductId();
    }

    @И("заполнение поле Наименование:{string}")
    public void fillProductName(String productName) {
        Hooks.getDriver().findElement(By.id("name")).sendKeys(productName);
        String enteredValue = Hooks.getDriver().findElement(By.id("name")).getAttribute("value");
        Assertions.assertEquals(enteredValue, productName,  "Entered value does not match the expected product name.");
    }

    @И("выбор из выпадающего списка типа:{string}")
    public void fillProductType(String productType) {
        WebElement typeDropdown = Hooks.getDriver().findElement(By.id("type"));
        typeDropdown.click();
        WebElement fruitOption = new WebDriverWait(Hooks.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"type\"]/option[contains(.,'" + productType + "')]")));
        Assertions.assertNotNull(fruitOption, "Fruit option not found.");
        fruitOption.click();
    }

    @И("выбор экзотичности {string}")
    public void fillExotic(String exoticCheckboxStr) {
        WebElement exoticCheckboxElement = Hooks.getDriver().findElement(By.id("exotic"));
        Assertions.assertNotNull(exoticCheckboxElement, "Exotic checkbox not found.");
        if (!exoticCheckboxElement.isSelected() && exoticCheckboxStr.equals("true")) {
            exoticCheckboxElement.click();
        }
    }

    @И("нажатие на кнопку сохранить")
    public void saveProduct(){
        Hooks.getDriver().findElement(By.id("save")).click();
    }

    @И("получение id только что добавленного продукта для проверки")
    public String findCurrentProductId() {
        try {
            Thread.sleep(500);
            return Hooks.getDriver().findElement(By.xpath("(//tr)[last()]/th")).getText();
        } catch (Exception e) {
            Assertions.fail("Invalid id value - cannot be converted to int.");
            return "-1";
        }
    }

    @И("проверка значения айди {string}")
    public void validateProductId(String idCurrentWebElemStr) {
        int idCurrentWebElem = Integer.parseInt(idCurrentWebElemStr);
        Assertions.assertEquals(Hooks.findPreviousProductId(), idCurrentWebElem , "The id (#) value is incorrect.");
    }

    @И("проверка значения наименования {string}")
    public void validateProductName(String expectedName) {
        String productName = Hooks.getDriver().findElement(By.xpath("(//tr)[last()]/td[1]")).getText();
        Assertions.assertEquals(expectedName, productName, "The name of the product is incorrect.");
    }

    @И("проверка значения типа продукта {string}")
    public void validateProductType(String expectedType) {
        String productType = Hooks.getDriver().findElement(By.xpath("(//tr)[last()]/td[2]")).getText();
        Assertions.assertEquals(expectedType, productType, "The type of the product is incorrect.");
    }

    @И("проверка экзотичности продукта {string}")
    public void validateProductExotic(String expectedExoticStr) {
        boolean expectedExotic = Boolean.parseBoolean(expectedExoticStr);
        boolean exoticCheckboxBool = false;
        try {
            exoticCheckboxBool = Boolean.parseBoolean(Hooks.getDriver().findElement(
                    By.xpath("(//tr)[last()]/td[3]")).getText());
        } catch (Exception e) {
            Assertions.fail("The checkbox value could not be parsed.");
        }

        Assertions.assertEquals(expectedExotic, exoticCheckboxBool, "The checkbox value is incorrect.");
    }


}
