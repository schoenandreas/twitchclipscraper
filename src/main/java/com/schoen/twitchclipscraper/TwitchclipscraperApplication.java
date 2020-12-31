package com.schoen.twitchclipscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TwitchclipscraperApplication {

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver","C:\\Users\\User\\IdeaProjects\\twitchclipscraper\\chromedriver.exe");

        SpringApplication.run(TwitchclipscraperApplication.class, args);
    }

}
