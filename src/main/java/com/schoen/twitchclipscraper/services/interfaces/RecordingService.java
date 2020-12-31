package com.schoen.twitchclipscraper.services.interfaces;


import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface RecordingService{

    void record(Collection<String> strings);

    void record(final String streamerName);

    void recordAll();

    void createClip();

    CompletableFuture<Void> recordStreamerThread(String streamerName) throws InterruptedException;

    void stopRecording();
}
