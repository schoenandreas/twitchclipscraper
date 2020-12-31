package com.schoen.twitchclipscraper.services;

import com.schoen.twitchclipscraper.repositories.StreamerRepository;
import com.schoen.twitchclipscraper.services.interfaces.RecordingService;
import com.schoen.twitchclipscraper.services.threads.RecordingThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultRecordingService implements RecordingService {

    private final StreamerRepository streamerRepository;
    private final RecordingThread recordingThread;

    @Override
    public void record(final Collection<String> streamerNames) {
        RecordingThread.recordingEnabled = true;
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
            RecordingThread.recordingEnabled = true;
            recordingThread.recordStreamerThread(streamerName);
            log.info("1 RecordingThreads started.");
        }
    }

    @Override
    public void createClip() {

    }

    public void stopRecording(){
        RecordingThread.recordingEnabled = false;
        log.debug("RecordingThreads will be killed...");
    }
}
