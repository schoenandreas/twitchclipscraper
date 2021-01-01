package com.schoen.twitchclipscraper.services;

import com.schoen.twitchclipscraper.services.interfaces.SeleniumService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class DefaultSeleniumServiceTest {

    @Autowired
    SeleniumService systemUnderTest;

    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver.exe");
    }

    @Test
    void getPage() {
        systemUnderTest.getPage("sweet_anita");
    }

    @Test
    void chatMsgTest() throws InterruptedException {
        WebElement page = systemUnderTest.getPage("sweet_anita");
        List<WebElement> msgList = page.findElements(By.cssSelector("div.chat-line__message"));
        log.info(java.lang.String.valueOf(msgList.size()));
        Thread.sleep(5000);
        msgList = page.findElements(By.cssSelector("div.chat-line__message"));
        log.info(java.lang.String.valueOf(msgList.size()));
        Thread.sleep(5000);
        msgList = page.findElements(By.cssSelector("div.chat-line__message"));
        log.info(java.lang.String.valueOf(msgList.size()));
        Thread.sleep(5000);
        msgList = page.findElements(By.cssSelector("div.chat-line__message"));
        log.info(java.lang.String.valueOf(msgList.size()));
    }
    @Test
    void getViewers() throws InterruptedException {
        WebElement page = systemUnderTest.getPage("sweet_anita");
        Thread.sleep(20000);
        final int numberOfViewers = Integer.valueOf(page.findElement(By.cssSelector("span.tw-animated-number.tw-animated-number--monospaced")).getText());
        log.info(""+numberOfViewers);
    }

    @Test
    void isOnlineTest() throws InterruptedException {
        String streamerName = "mizkif";
        WebElement page = systemUnderTest.getPage(streamerName);
        Thread.sleep(500);
        String result = page.findElement(By.cssSelector("a[href=\"/"+streamerName+"\"] div.live-indicator-container p.tw-strong")).getText();
        log.info(streamerName+" is "+result);
    }
}
