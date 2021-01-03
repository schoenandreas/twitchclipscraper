package com.schoen.twitchclipscraper.services;

import com.schoen.twitchclipscraper.models.RecordingState;
import com.schoen.twitchclipscraper.repositories.StreamerRepository;
import com.schoen.twitchclipscraper.services.interfaces.RecordingService;
import com.schoen.twitchclipscraper.services.threads.RecordingThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultRecordingService implements RecordingService {

    private final StreamerRepository streamerRepository;
    private final RecordingThread recordingThread;
    private final Map<String, CompletableFuture<String>> threads;

    private void record(final Collection<String> streamerNames) {
            RecordingThread.recordingEnabled = true;
            int startCounter = 0;
            int alreadyRunningCounter = 0;
            for(String streamerName : streamerNames){
                if(threads.get(streamerName) == null || threads.get(streamerName).isDone()){
                    threads.put(streamerName, recordingThread.recordStreamerThread(streamerName));
                    startCounter++;
                }else{
                    alreadyRunningCounter++;
                }
            }
            log.info("{} RecordingThreads started. {} ignored because already running.", startCounter, alreadyRunningCounter);
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
            record(List.of(streamerName));
        }
    }

    @Override
    public void createClip() {

    }

    @Override
    public void stopRecordingAll(){
        RecordingThread.recordingEnabled = false;
        log.info("RecordingThreads will be killed...");
    }

}
