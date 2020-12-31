package com.schoen.twitchclipscraper.services.interfaces;

import org.openqa.selenium.WebElement;

import java.util.List;


public interface SeleniumService {

    java.lang.String TWITCH_URL = "https://www.twitch.tv/";

    WebElement getPage(final String streamerName);

    boolean isOnline(final WebElement streamPage);

    List<java.lang.String> getWebElementsContentList (List<WebElement> webElementList);
}
