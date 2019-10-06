package com.lunarshade.webservice.handler;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ResponseBuilder {

    private String response;
    private Map<String, String> replacementList;
    private HttpStatus status;
    private ResponseDelay delay;
    private int preciseTime;
    private int randomTimeFrom;
    private int randomTimeTo;
    private String timeFormat;
    MockResponse mockResponse;

    public static ResponseBuilder getInstance() {
        return new ResponseBuilder();
    }

    public ResponseBuilder withResponse(String response) {
        this.response = response;
        return this;
    }

    public ResponseBuilder withStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public ResponseBuilder withReplacementList(Map<String, String> replacementList) {
        this.replacementList = replacementList;
        return this;
    }

    public ResponseBuilder withDelay(ResponseDelay delay) {
        this.delay = delay;
        return this;
    }

    public ResponseBuilder withPreciseTime(int time) {
        this.preciseTime = time;
        return this;
    }


    public ResponseBuilder withRandomDelayDiapason(int from, int to) {
        this.randomTimeFrom = from;
        this.randomTimeTo = to;
        return this;
    }

    public ResponseBuilder withTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
        return this;
    }

//    public MockResponse buildResponseEntity() {
//        this.mockResponse = MockResponse.getInstance();
//        mockResponse.setResponse(response);
//        mockResponse.setStatus(status);
//        mockResponse.setReplacementList(replacementList);
//        mockResponse.setDelay(delay);
//        mockResponse.setPreciseTime(preciseTime);
//        mockResponse.setRandomTimeFrom(randomTimeFrom);
//        mockResponse.setRandomTimeTo(randomTimeTo);
//        mockResponse.setTimeFormat(timeFormat);
//        return mockResponse;
//
//    }
}
