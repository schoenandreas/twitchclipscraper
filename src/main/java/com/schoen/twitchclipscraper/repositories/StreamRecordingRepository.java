package com.schoen.twitchclipscraper.repositories;

import com.schoen.twitchclipscraper.models.StreamRecordingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StreamRecordingRepository extends JpaRepository<StreamRecordingModel, UUID> {
}
