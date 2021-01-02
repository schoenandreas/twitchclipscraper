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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DefaultSeleniumService implements SeleniumService {

    @Override
    public WebDriver getDriver() {
        return new ChromeDriver();
    }

    @Override
    public WebElement getPage(@NonNull final String streamerName, @NonNull final WebDriver driver) {
        driver.get(TWITCH_URL + streamerName);
        WebElement streamPage = driver.findElement(By.tagName("body"));
        log.info(streamPage.toString());
        return streamPage;
    }

    @Override
    public boolean isOnline(final WebElement streamPage, final String streamerName) {
       try {
           final WebElement liveElement = streamPage.findElement(By.cssSelector("a[href=\"/"+streamerName+"\"] div.live-indicator-container p.tw-strong"));
           boolean isLive = liveElement.getText().equals("LIVE");
           log.debug(streamerName+" is LIVE");
           return isLive ;
       }catch (NoSuchElementException e){
           log.debug("Live element not found. {} is offline or selector is broken.", streamerName);
           return false;
       }
    }

    @Override
    public List<String> getWebElementsContentList(final List<WebElement> webElementList) {
        final List<String> result  = new ArrayList<>();
        webElementList.forEach(webElement -> result.add(webElement.getText()));
        return result;
    }

    //TODO correct parsing of chat messages with emotes etc
    private String parseChatMessage(final WebElement element){
        final WebElement innerElement = element.findElement(By.cssSelector("div.chat-line__no-background.tw-inline"));
        final List<WebElement> msgElements = element.findElements(By.cssSelector("*"));
        StringBuilder builder = new StringBuilder();
        msgElements.forEach(msgElement -> builder.append(handleMsgElement(msgElement)));


        return null;
    }

    private String handleMsgElement ( final WebElement msgElement){
        final String tag = msgElement.getTagName();
        String returnValue;
        switch (tag){
            case "div":
                //TODO
                break;
            default:
                returnValue = "";
        }
        return null;
    }

}
