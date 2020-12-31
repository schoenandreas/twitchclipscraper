package com.schoen.twitchclipscraper.services;

import com.schoen.twitchclipscraper.services.interfaces.SeleniumService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DefaultSeleniumService implements SeleniumService {

    @Override
    public WebElement getPage(@NonNull String streamerName) {
        WebDriver driver = new ChromeDriver();
        driver.get(TWITCH_URL + streamerName);
        WebElement streamPage = driver.findElement(By.tagName("body"));
        log.info(streamPage.toString());
        return streamPage;
    }

    @Override
    public boolean isOnline(WebElement streamPage) {
       try {
           final WebElement liveElement = streamPage.findElement(By.cssSelector("div.live-indicator-container p.tw-strong"));
           return liveElement.getText().equals("LIVE") ;
       }catch (NoSuchElementException e){
           log.info("Live element not found. Returning FALSE");
           return false;
       }
    }

    @Override
    public List<java.lang.String> getWebElementsContentList(List<WebElement> webElementList) {
        List<java.lang.String> result  = Collections.emptyList();
        webElementList.forEach(webElement -> result.add(webElement.getText()));
        return result;
    }

}
