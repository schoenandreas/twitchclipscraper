package com.schoen.twitchclipscraper.services.interfaces;

import com.schoen.twitchclipscraper.models.StreamerModel;

public interface StreamerModelService {

    StreamerModel createStreamer(String streamerName);

    void deleteStreamer(String streamerName);
}
