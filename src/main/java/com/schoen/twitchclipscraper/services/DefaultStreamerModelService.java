package com.schoen.twitchclipscraper.services;

import com.schoen.twitchclipscraper.models.RecordEntryModel;
import com.schoen.twitchclipscraper.models.StreamRecordingModel;
import com.schoen.twitchclipscraper.models.StreamerModel;
import com.schoen.twitchclipscraper.repositories.StreamerRepository;
import com.schoen.twitchclipscraper.services.interfaces.StreamerModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultStreamerModelService implements StreamerModelService {

    private final StreamerRepository streamerRepository;

    @Override
    public StreamerModel createStreamer(final String streamerName) {

            try{
                final List<StreamRecordingModel> entries = new ArrayList<>();
                final StreamerModel streamerModel = StreamerModel.builder().streamerName(streamerName).recordings(entries).build();
                final StreamerModel streamerModelSaved = streamerRepository.save(streamerModel);
                log.info("Added {} to DB.",streamerModel.getStreamerName());
                return streamerModelSaved;
            }catch (Exception e){
                log.info("Streamer {} could not be added {}.",streamerName,e);
                return null;
            }
    }

    @Override
    public void deleteStreamer(final String streamerName) {
        StreamerModel streamerModel=null;
        try{
            streamerModel = streamerRepository.findByStreamerName(streamerName);
        }catch(Exception e) {
            log.info("Streamer {} does not exist.", streamerName);
            return;
        }
        streamerRepository.delete(streamerModel);
        log.info("Streamer {} deleted.", streamerName);
    }
}
