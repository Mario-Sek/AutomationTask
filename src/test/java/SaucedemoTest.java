import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class SaucedemoTest {

    WebDriver driver;

    @BeforeClass
    public void setUp() {
        FirefoxOptions options = new FirefoxOptions();

        options.addPreference("dom.webnotifications.enabled", false);

        driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void loginTest(){

        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
    }

    @Test(priority = 2)
    public void addToCartTest(){

        WebElement inventory =  driver.findElement(By.className("inventory_list"));

        for (WebElement item : inventory.findElements(By.className("inventory_item"))){
            item.findElement(By.cssSelector("button")).click();
        }

        driver.findElement(By.id("shopping_cart_container")).click();
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertFalse(cartItems.isEmpty(), "Empty cart!");

    }

    @Test(priority = 3)
    public void checkoutTest() {
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("Mario");
        driver.findElement(By.id("last-name")).sendKeys("Shekerovski");
        driver.findElement(By.id("postal-code")).sendKeys("1000");
        driver.findElement(By.id("continue")).click();

        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertFalse(cartItems.isEmpty(), "Empty checkout cart!");

        driver.findElement(By.id("finish")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-complete"), "Checkout not completed!");
    }

    @Test(priority = 4)
    public void logoutTest() {
        driver.findElement(By.id("react-burger-menu-btn")).click();
        driver.findElement(By.id("logout_sidebar_link")).click();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "https://www.saucedemo.com/", "Logout failed!");
    }


    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
