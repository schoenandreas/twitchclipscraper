package com.schoen.twitchclipscraper.repositories;

import com.schoen.twitchclipscraper.models.StreamerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StreamerRepository extends JpaRepository<StreamerModel, UUID> {

    StreamerModel findByStreamerName(String streamerName);
}
