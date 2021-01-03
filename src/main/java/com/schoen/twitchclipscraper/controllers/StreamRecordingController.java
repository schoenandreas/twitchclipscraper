package com.schoen.twitchclipscraper.controllers;

import com.schoen.twitchclipscraper.services.interfaces.RecordingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@AllArgsConstructor
@Controller
@RequestMapping("/recording")
public class StreamRecordingController {

    private RecordingService recordingService;

    @PostMapping("/start")
    @ResponseBody
    public ResponseEntity<String> recordStreamer(@RequestParam String streamerName){
        recordingService.record(streamerName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/startAll")
    @ResponseBody
    public ResponseEntity<String> recordAll(){
        recordingService.recordAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/stopAll")
    @ResponseBody
    public ResponseEntity<String> stopRecording(){
        recordingService.stopRecordingAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
