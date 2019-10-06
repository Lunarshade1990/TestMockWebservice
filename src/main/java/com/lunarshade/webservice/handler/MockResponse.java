package com.lunarshade.webservice.handler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MockResponse {

    private String response;
    private boolean plainXml = false;
    private Map<String, String> replacementList;
    private HttpStatus status;
    private ResponseDelay delay;
    private int preciseTime;
    private int randomTimeFrom;
    private int randomTimeTo;
    private String timeFormat;
    private Builder builder;

    private int responseDelay;

    public static MockResponse getInstance() {
        return new MockResponse();
    }

    public ResponseEntity<String> getResponse() {
        setDelay();
        paramReplacement();
        deleteLineBreakersInXml();

        try {
            Thread.sleep(responseDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>(response, status);
    };

    public void setDelay() {
        switch (delay) {
            case RANDOM:
                responseDelay = getRandomDelay();
                break;
            case EXACTLY:
                responseDelay = preciseTime;
                break;
            case LONG:
                responseDelay = 2000;
                break;
            case QUICKLY:
                responseDelay = 100;

        }
    }

    public String paramReplacement() {
        if (replacementList != null) {
            replacementList.forEach((param, replacement) -> response = response.replaceAll(param, replacement));
        }
        return response;
    }

    private String deleteLineBreakersInXml() {
        if (plainXml) response = response.replaceAll("[>]\n\\s*[<]", "><");
            return response;

    }

    public int getRandomDelay() {
        return  (int)  (Math.random() * (randomTimeTo-randomTimeFrom) + randomTimeFrom);
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private MockResponse mockResponse;

        private Builder() {
            mockResponse = new MockResponse();
        }

        public Builder withResponse(String response) {
            mockResponse.response = response;
            return this;
        }

        public Builder withStatus(HttpStatus status) {
            mockResponse.status = status;
            return this;
        }

        public Builder withReplacementList(Map<String, String> replacementList) {
            mockResponse.replacementList = replacementList;
            return this;
        }

        public Builder withDelay(ResponseDelay delay) {
            mockResponse.delay = delay;
            return this;
        }

        public Builder withPreciseTime(int time) {
            mockResponse.preciseTime = time;
            return this;
        }


        public Builder withRandomDelayDiapason(int from, int to) {
            mockResponse.randomTimeFrom = from;
            mockResponse.randomTimeTo = to;
            return this;
        }

        public Builder withTimeFormat(String timeFormat) {
            mockResponse.timeFormat = timeFormat;
            return this;
        }

        public MockResponse build() {
            return this.mockResponse;
        }

    }



}
