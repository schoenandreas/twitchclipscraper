package com.schoen.twitchclipscraper.services.interfaces;


import java.util.Collection;

public interface RecordingService{

    void record(final String streamerName);

    void recordAll();

    void createClip();

    void stopRecordingAll();
}
