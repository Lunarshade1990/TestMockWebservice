package com.lunarshade.webservice.controller;

import com.lunarshade.webservice.handler.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockServiceController {

    @Autowired
    private RequestHandler requestHandler;

    @PostMapping(value = "/", produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<String> sendResponse(@RequestBody String request) {
        requestHandler.setRequest(request);
        return requestHandler.getResponse();
    }

}
