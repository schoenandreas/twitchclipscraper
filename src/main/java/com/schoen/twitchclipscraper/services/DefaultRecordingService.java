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
    private final Map<String, RecordingState> recordingStateMap;
    private final List<CompletableFuture<String>> threads;

    private void record(final Collection<String> streamerNames) {
            RecordingThread.recordingEnabled = true;
            int startCounter = 0;
            int alreadyRunningCounter = 0;
            updateRecordingStateMap();
            for(String streamerName : streamerNames){
                if(recordingStateMap.get(streamerName) == RecordingState.RUNNING){
                    alreadyRunningCounter++;
                }else{
                    threads.add(recordingThread.recordStreamerThread(streamerName));
                    recordingStateMap.put(streamerName,RecordingState.RUNNING);
                    startCounter++;
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
    public void stopRecording(){
        RecordingThread.recordingEnabled = false;
        log.info("RecordingThreads will be killed...");
    }

    private void updateRecordingStateMap(){
        try{
            for(CompletableFuture<String> future : threads){
                if(future.isDone()){
                    try {
                        recordingStateMap.put(future.get(),RecordingState.FINISHED);
                        threads.remove(future);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (ConcurrentModificationException e){
            log.info("Need to wait to update recordingStateMap...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            updateRecordingStateMap();
        }

    }
}
