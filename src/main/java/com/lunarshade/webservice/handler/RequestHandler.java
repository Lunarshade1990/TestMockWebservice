package com.lunarshade.webservice.handler;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
public class RequestHandler {

    String request;

    @Autowired
    RequestProcessor responseProcessor;

    public void setRequest(String request) {
        this.request = request;
    }

    public ResponseEntity<String> getResponse() {
        responseProcessor.setRequest(request);
        return responseProcessor.buildResponse(request);
    }


}
