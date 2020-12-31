package com.schoen.twitchclipscraper.services.threads;

import com.schoen.twitchclipscraper.models.RecordEntryModel;
import com.schoen.twitchclipscraper.models.StreamRecordingModel;
import com.schoen.twitchclipscraper.models.StreamerModel;
import com.schoen.twitchclipscraper.repositories.StreamRecordingRepository;
import com.schoen.twitchclipscraper.repositories.StreamerRepository;
import com.schoen.twitchclipscraper.services.interfaces.SeleniumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class RecordingThread {


    private static  int INTERVAL_REC_SEC = 10;
    private static  int INTERVAL_WAIT_SEC = 5;
    private volatile boolean recordingEnabled = true;

    private final SeleniumService seleniumService;
    private final StreamerRepository streamerRepository;
    private final StreamRecordingRepository streamRecordingRepository;

    @Async("threadPoolTaskExecutor")
    public void recordStreamerThread(final String streamerName) {
        log.info("{}: recordingThread for Streamer {} started.",Timestamp.valueOf(LocalDateTime.now()), streamerName);

        final StreamerModel streamerModel = streamerRepository.findByStreamerName(streamerName);
        final WebElement page = seleniumService.getPage(streamerName);
        while(recordingEnabled) {
            final List<RecordEntryModel> entries = new ArrayList<>();
            StreamRecordingModel streamRecordingModel = null;
            RecordEntryModel lastEntry = null;
            while(recordingEnabled && seleniumService.isOnline(page) && false){
                if(streamRecordingModel == null){
                    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                    log.info("{}: Recording for Streamer {} started.",now, streamerName);
                    streamRecordingModel = StreamRecordingModel.builder().streamer(streamerModel).startDate(now).intervalSeconds(INTERVAL_REC_SEC).entries(entries).build();
                }
                final RecordEntryModel entry = createRecordEntry(streamRecordingModel, page, lastEntry);
                lastEntry = entry;
                entries.add(entry);
                streamRecordingRepository.save(streamRecordingModel);
                try{
                    Thread.sleep(INTERVAL_REC_SEC *1000);
                }catch (InterruptedException e){}

            }
            if(streamRecordingModel != null){
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                streamRecordingModel.setEndDate(now);
                streamRecordingRepository.save(streamRecordingModel);
                log.info("{}: Recording for Streamer {} finished.",now, streamerName);
            }
            log.info("RecordingThread for Streamer {} is waiting {} sec.",streamerName,INTERVAL_WAIT_SEC);
            try{
                Thread.sleep(INTERVAL_WAIT_SEC *1000);
            }catch (InterruptedException e){}

        }
        log.info("{}: recordingThread for Streamer {} finished.",Timestamp.valueOf(LocalDateTime.now()), streamerName);
    }

    private RecordEntryModel createRecordEntry(final StreamRecordingModel recordingModel, final WebElement page, final RecordEntryModel lastEntry){
        final int oldMsgLength = lastEntry == null ? 0:lastEntry.getMessages().size();
        final List<WebElement> msgList = page.findElements(By.cssSelector("div.chat-line__message"));
        final List<String> msgListNewString = seleniumService.getWebElementsContentList(msgList.subList(msgList.size()-oldMsgLength, msgList.size()));
        int numberOfViewers = 0;
        try {
            numberOfViewers = Integer.valueOf(page.findElement(By.cssSelector("span.tw-animated-number.tw-animated-number--monospaced")).getText());
        }catch (NoSuchElementException e){}

        //add clipping later
        final String clipURL= null;

        return RecordEntryModel.builder().recording(recordingModel).messages(msgListNewString).numberOfViewers(numberOfViewers).entryTime(Timestamp.valueOf(LocalDateTime.now())).build();
    }
}
