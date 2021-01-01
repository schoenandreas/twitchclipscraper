package com.schoen.twitchclipscraper.services.interfaces;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;


public interface SeleniumService {

    String TWITCH_URL = "https://www.twitch.tv/";

    WebDriver getDriver();

    WebElement getPage(final String streamerName, final WebDriver webDriver);

    boolean isOnline(final WebElement streamPage,final String streamerName);

    List<java.lang.String> getWebElementsContentList (List<WebElement> webElementList);
}
