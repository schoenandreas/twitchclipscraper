package com.schoen.twitchclipscraper.controllers;

import com.schoen.twitchclipscraper.models.StreamerModel;
import com.schoen.twitchclipscraper.services.interfaces.StreamerModelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
@RequestMapping("/streamer")
public class StreamerController {

    StreamerModelService streamerModelService;

    @PostMapping("/create")
    public ResponseEntity<StreamerModel> createStreamer(@RequestParam String streamerName){
        StreamerModel streamerModel = streamerModelService.createStreamer(streamerName);
        if(streamerModel != null){
            return new ResponseEntity<StreamerModel>(streamerModel, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<StreamerModel>(streamerModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
