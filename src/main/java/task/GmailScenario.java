package task;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

//Version without using TestNG, report is created using File and PrintWriter classes, for the TestNG version navigate to: ./src/test/java/task/GmailScenarioTest
public class GmailScenario {
    public static void main(String[] args) throws FileNotFoundException {

        //Generating report on path: ./src/main/resources/Report.txt
        File report = new File("./src/main/resources/Report.txt");
        PrintWriter writer = new PrintWriter(report);

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        WebDriver driver = new ChromeDriver(options);

        WebDriverWait wait = new WebDriverWait(driver,10);

        //Navigate to Gmail
        driver.get("https://mail.google.com/ ");

        //Test account is used, credentials: email: tt6463042@gmail.com , password: testing123!
        //Input email address and click Next
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"identifierId\"]"))).sendKeys("tt6463042@gmail.com");
        driver.findElement(By.xpath("//*[@id=\"identifierNext\"]/div/button")).click();

        //Input password and click Next
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"password\"]/div[1]/div/div[1]/input"))).sendKeys("testing123!");
        driver.findElement(By.xpath("//*[@id=\"passwordNext\"]/div/button")).click();

        //Click on 'More' and mark everything as read, this is going to help us identify the new future sent mail
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role=\"button\" and @class=\"T-I J-J5-Ji nf T-I-ax7 L3\"]"))).click();
        driver.findElement(By.xpath("//div[text()=\"Mark all as read\"]")).click();

        //Click on Compose button
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()=\"Compose\" and @role=\"button\"]"))).click();

        //Fill in the To, Subject and Body fields
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@class,\"agP aFw\")]"))).sendKeys("tt6463042@gmail.com");
        driver.findElement(By.name("subjectbox")).sendKeys("Test message!");
        driver.findElement(By.xpath("//div[@class=\"Am Al editable LW-avf tS-tW\"]")).sendKeys("This is a test message!");

        //Click on the Send button
        driver.findElement(By.xpath("//div[text()=\"Send\"]")).click();

        //waiting for the Message Sent pop-up to appear before moving on to make sure than message is properly sent
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\"Message sent\"]")));

        try {
            //Clicking on the received mail, if mail is not received Element will not be found and exception will be thrown which will result in failed tests
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[@class=\"zA zE\"][1]"))).click();
            writer.println("--------------Test Results--------------");
            writer.println("--Test 1: Mail received: [Success]✅\n----------------------------------------");

            //Finding the subject and body of the opened mail
            WebElement findSubject = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@class='hP']")));
            WebElement findBody = driver.findElement(By.xpath("//div[@class='a3s aiL ']/div[1]"));

            //Checking for the correctness of the Subject and Body
            writer.println(findSubject.getText().equals("Test message!") ? "--Test 2: Subject is correct: [Success]✅" : "--Test 2: Subject is correct: [Failed]❌");
            writer.println("----Subject received: " + findSubject.getText() + "\n" + "----------------------------------------");
            writer.println(findBody.getText().equals("This is a test message!") ? "--Test 3: Body is correct: [Success]✅" : "--Test 3: Body is correct: [Failed]❌");
            writer.println("----Body received: " + findBody.getText() + "\n" + "----------------------------------------");

            writer.close();
            driver.quit();

        } catch (Exception e) {
            writer.println("""
                    --------------Test Results--------------
                    --Test 1: Mail received: [Failed]❌
                    --Test 2: Subject is correct: [Blocked]🚫
                    --Test 3: Body is correct: [Blocked]🚫
                    ------------------------------------
                    """ +
                    "Failure reason: " + e.getMessage());

            writer.close();
            driver.quit();
        }
    }
}
