import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class RecruitmentTaskTests {
    @BeforeTest
    void setUpDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

    }

    @Test
    void firstTask() {
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://xtm.cloud/");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement buyCloud = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Buy XTM Cloud")));
            buyCloud.click();

            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe[src='https://login.xtm.cloud/saas-manager/buy.jsp']")));
            driver.switchTo().frame(iframe);

            WebElement accountDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"new-xtm-subscription-form\"]/div/div[2]/fieldset[1]/div/div[1]/div[1]/div/span[1]/span[1]/span/span[2]")));
            accountDropdown.click();
            WebElement accountType = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/span/span/span[2]/ul/li[2]")));
            accountType.click();

            WebElement usersDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"new-xtm-subscription-form\"]/div/div[2]/fieldset[1]/div/div[1]/div[2]/div/span[1]/span[1]/span")));
            usersDropdown.click();
            WebElement numberOfUsers = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/span/span/span[2]/ul/li[2]")));
            numberOfUsers.click();

            WebElement durationDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"new-xtm-subscription-form\"]/div/div[2]/fieldset[1]/div/div[2]/div[1]/div/span[1]/span[1]/span")));
            durationDropdown.click();
            WebElement durationMonths = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/span/span/span[2]/ul/li[3]")));
            durationMonths.click();

            WebElement wordsDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"new-xtm-subscription-form\"]/div/div[2]/fieldset[1]/div/div[2]/div[2]/div/span[1]/span[1]/span")));
            wordsDropdown.click();
            WebElement noWordsOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/span/span/span[2]/ul/li[1]")));
            noWordsOption.click();

            Thread.sleep(3000);
            WebElement currencyUSD = driver.findElement(By.id("USD"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", currencyUSD);

            WebElement costElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='total-cost']/p/span")));
            String textValue = costElement.getText();
            String expectedCost = "$831,60";

            Assert.assertTrue(textValue.equals(expectedCost));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    //na potrzeby sprawdzania samego kalkulatora ceny mozna poprostu na backendzie (zmienne oczywiscie do sparametryzowania)
    @Test
    void firstTaskRestAssured() {
        WebDriver driver = new ChromeDriver();
        try {
            String fullUrl = "https://login.xtm.cloud/saas-manager/getSubscriptionPrice.serv?newSubscription.firstName=&" +
                    "newSubscription.lastName=&newSubscription.email=&newSubscription.company=&" +
                    "newSubscription.shortCompanyName=&newSubscription.phone1=&newSubscription.title=&" +
                    "newSubscription.promotionCode=&newSubscription.country=&newSubscription.accountType=SMALL_GROUP&" +
                    "newSubscription.numberOfUsers=3&newSubscription.subscriptionLength=12&" +
                    "newSubscription.numberOfWords=30000&reqId=1&type=newSubscription&" +
                    "isNewSubscription=true&isCalculatorPage=false&_=1724510799257";


            Response response = given()
                    .when()
                    .get(fullUrl)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

            Assert.assertTrue(response.getBody().asString().contains("finalCompleteTotalPrice\":\"$831.60\""), "Price in dollars is 831.60");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    @Test
    void secondTask() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);
        //tu zeby bylo porzadnie trzeba by sie pobawic z ustawianiem sciezki pobierania w opcjach
        try {
            driver.get("https://xtm.cloud/");

            WebElement resourcesMenu = driver.findElement(By.xpath("//a[contains(text(),'Resources')]"));
            resourcesMenu.click();
            WebElement documentationLink = driver.findElement(By.linkText("Documentation"));
            documentationLink.click();

            WebElement xtmCloudHelp = driver.findElement(By.xpath("//a[contains(text(),'Visit XTM Online Help')]"));
            xtmCloudHelp.click();

            WebElement h3Element = driver.findElement(By.xpath("//div[@class='portal-single-publication']//h3[text()='XTM Cloud']"));
            h3Element.click();

            WebElement versionLink = driver.findElement(By.xpath("//*[@id=\"UUID-a2533e82-ecce-f16a-502d-f27e59a4c756\"]/div[2]/ul/li[2]/p/a"));
            versionLink.click();
            navigateToTabByName(driver, "XTM Cloud 13.7");

            WebElement stopWordsSection = driver.findElement(By.linkText("Stop words"));
            stopWordsSection.click();

            WebElement downloadLink = driver.findElement(By.linkText("sk – Slovak"));
            downloadLink.click();

            Thread.sleep(5000);

            File file = new File("C:\\Users\\Piotr\\Downloads\\sk.txt");
            if (file.exists()) {
                System.out.println("File downloaded correctly, deleting file...");
                file.delete();
            } else {
                Assert.fail("File was not downloaded correctly");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void navigateToTabByName(WebDriver driver, String tabName) {
        Set<String> allWindows = driver.getWindowHandles();

        boolean found = false;

        for (String windowHandle : allWindows) {
            driver.switchTo().window(windowHandle);

            if (driver.getTitle().equals(tabName)) {
                found = true;
                System.out.println("Switched to the tab with title: " + driver.getTitle());

                driver.get("https://help.xtm.cloud/en/xtm-cloud/13.7/en/additional-information.html");

                break;
            }
        }

        if (!found) {
            Assert.fail("Tab with title" + tabName + "not found.");
        }
    }
}
