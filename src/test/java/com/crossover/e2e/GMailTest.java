package com.crossover.e2e;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.testng.annotations.*;
//import org.testng.Assert;


//public class GMailTest{
public class GMailTest extends TestCase{
    private WebDriver driver;
    private Properties properties = new Properties();

    //@BeforeTest
    public void setUp() throws Exception {
        
        properties.load(new FileReader(new File("src/test/resources/test.properties")));
        //Dont Change below line. Set this value in test.properties file incase you need to change it..
        System.setProperty("webdriver.chrome.driver",properties.getProperty("webdriver.chrome.driver") );
        driver = new ChromeDriver();
    }

    //@AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }

    /*
     * Please focus on completing the task
     * 
     */
    @Test
    public void testSendEmail() throws Exception {
    	driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("https://mail.google.com/");
       
        //username
        WebElement userElement = driver.findElement(By.id("identifierId"));
        userElement.sendKeys(properties.getProperty("username"));
        driver.findElement(By.id("identifierNext")).click();

        //password
        WebElement passwordElement = driver.findElement(By.name("password"));
        passwordElement.sendKeys(properties.getProperty("password"));
        driver.findElement(By.id("passwordNext")).click();

        //compose
        //WebElement composeElement = driver.findElement(By.xpath("//*[@role='button' and @class='T-I J-J5-Ji T-I-KE L3']"));
        WebElement composeElement = driver.findElement(By.xpath("//*[@role='button' and contains(text(),'Compose')]"));
        composeElement.click();    

        //fill send to
        driver.findElement(By.name("to")).clear();
        driver.findElement(By.name("to")).sendKeys(String.format("%s@gmail.com", properties.getProperty("username")));
        
        //emailSubject and emailbody to be used in this unit test.
        String emailSubject = properties.getProperty("email.subject");
        String emailBody = properties.getProperty("email.body"); 
        
        //fill subject and body
        driver.findElement(By.name("subjectbox")).sendKeys(emailSubject);
        driver.findElement(By.xpath("//*[@role='textbox']")).sendKeys(emailBody);
        
        //label as social
        driver.findElement(By.xpath("//div[@role='button' and @aria-label='More options']")).click();
        driver.findElement(By.xpath("//*[@role='menuitem' and @aria-haspopup='true']")).click();
        driver.findElement(By.xpath("//*[@role='menuitemcheckbox' and @title='Social']")).click();
        
        //send
        driver.findElement(By.xpath("//*[@role='button' and text()='Send']")).click(); 
        
        Thread.sleep(2000);
        
        //change to social inbox
        driver.findElement(By.xpath("//div[@data-tooltip='Messages from social networks, media-sharing sites, online dating services, and other social websites.']")).click();

        Thread.sleep(2000);
       
        //open email
        driver.findElement(By.xpath(String.format("//div[@role='link']/div/div//span[text()='%s']", emailSubject))).click();
        //driver.findElement(By.xpath(String.format("//span[text()='%s']", emailSubject))).click();
        
        Thread.sleep(2000);
        
        //start it
        driver.findElement(By.xpath("//span[@class='T-KT']")).click();
        Thread.sleep(3000);
        
        //check labels
		driver.findElement(By.xpath("(//div[@class='T-I J-J5-Ji T-I-Js-Gs mA mw T-I-ax7 L3']//div[@class='asa'])[2]")).click();
        
        //check is social label is checked
        String socialChecked = driver.findElement(By.xpath("//div[@role='menuitemcheckbox' and @title='Social']")).getAttribute("aria-checked");
        Assert.assertEquals("true", socialChecked);
        System.out.println(String.format("Social label is %s", socialChecked));		
        
        //verify subject
        String verifSubject = driver.findElement(By.xpath("//h2[@class='hP']")).getText();
        Assert.assertEquals(verifSubject, emailSubject);
        if(emailSubject.equals(verifSubject)) {
        	System.out.println("Subject matches.");
        } else {
        	System.out.println("Subject doesn't match.");
        }
        
        //verify body
        String verifBody = driver.findElement(By.xpath("//div[@class='a3s aXjCH ']//div[@dir='ltr']")).getText();
        Assert.assertEquals(verifBody, emailBody);
        if(verifBody.equals(emailBody)) {
        	System.out.println("Body matches.");
        } else {
        	System.out.println("Body doesn't match.");
        }
        
        Thread.sleep(1000);
    }
}
