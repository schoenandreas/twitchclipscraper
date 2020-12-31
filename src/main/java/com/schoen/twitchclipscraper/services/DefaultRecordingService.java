package com.schoen.twitchclipscraper.services;

import com.schoen.twitchclipscraper.models.RecordEntryModel;
import com.schoen.twitchclipscraper.models.StreamRecordingModel;
import com.schoen.twitchclipscraper.models.StreamerModel;
import com.schoen.twitchclipscraper.repositories.RecordEntryRepository;
import com.schoen.twitchclipscraper.repositories.StreamRecordingRepository;
import com.schoen.twitchclipscraper.repositories.StreamerRepository;
import com.schoen.twitchclipscraper.services.interfaces.RecordingService;
import com.schoen.twitchclipscraper.services.interfaces.SeleniumService;
import com.schoen.twitchclipscraper.services.threads.RecordingThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultRecordingService implements RecordingService {

    private static  int INTERVAL_REC_SEC = 10;
    private static  int INTERVAL_WAIT_SEC = 5;
    private volatile boolean recordingEnabled = true;

    private final SeleniumService seleniumService;
    private final RecordEntryRepository recordEntryRepository;
    private final StreamerRepository streamerRepository;
    private final StreamRecordingRepository streamRecordingRepository;
    private final RecordingThread recordingThread;

    @Override
    public void record(final Collection<String> streamerNames) {
            recordingEnabled = true;
            for(String streamerName : streamerNames){
                  recordingThread.recordStreamerThread(streamerName);
            }
            log.info("{} RecordingThreads started.", streamerNames.size());
    }

    @Override
    public void recordAll() {
            Collection<String> streamerNames = new ArrayList<>();
            streamerRepository.findAll().forEach(streamerModel -> streamerNames.add(streamerModel.getStreamerName()));
            record(streamerNames);
    }

    @Override
    public void record(final String streamerName) {
        if(streamerRepository.findByStreamerName(streamerName) == null){
            log.info("Streamer {} does not exist in DB and can't be recorded.", streamerName);
        }else{
            recordingEnabled = true;
                recordStreamerThread(streamerName);
                log.info("1 RecordingThreads started.");
        }
    }



    @Override
    public void createClip() {

    }

    @Async
    public CompletableFuture<Void> recordStreamerThread(final String streamerName) {
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
        return new CompletableFuture<>();
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


    public void stopRecording(){
        recordingEnabled = false;
        log.debug("RecordingThreads will be killed...");
    }
}
