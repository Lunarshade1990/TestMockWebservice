package com.lunarshade.webservice.handler;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Service
public class RequestProcessor {

    @Autowired
    private ResponseProperties responseProperties;
    private String request;
    private long requestIdInt;
    private String requestIdStr;
    private String responseOkString;
    private String responseBadString;


    public ResponseEntity<String> buildResponse(String request)  {

        if (!isOkRequest()) {
            return buildBadResponse(MockResponse.getBuilder()).getResponse();
        } else {
            return buildOkResponse(MockResponse.getBuilder()).getResponse();
        }
    }

    public MockResponse buildOkResponse(MockResponse.Builder builder) {
        String response = null;
        Map<String, String> paramReplacement = new HashMap<>();
        paramReplacement.put("__id__", requestIdStr);
        paramReplacement.put("__date__", new SimpleDateFormat(responseProperties.getDateFormatPattern()).format(new Date()));
        response = getOkString();
        builder.withResponse(response)
                       .withStatus(HttpStatus.OK)
                       .withReplacementList(paramReplacement);
        if(requestIdInt<=555555555L) {
            builder.withDelay(ResponseDelay.EXACTLY)
                           .withPreciseTime(1500);
        } else {
            builder.withDelay(ResponseDelay.RANDOM)
                           .withRandomDelayDiapason(100, 2000);
        }
        return builder.build();
    }

    public MockResponse buildBadResponse(MockResponse.Builder builder) {
        String response = null;
        Map<String, String> paramReplacement = new HashMap<>();
        paramReplacement.put("__id__", requestIdStr);
        response = getBadString();
        builder.withResponse(response)
                .withDelay(ResponseDelay.EXACTLY)
                .withPreciseTime(100)
                .withStatus(HttpStatus.BAD_REQUEST)
                .withReplacementList(paramReplacement);
        return builder.build();
    }

    private String getOkString(){
        if (this.responseOkString==null) {
            try {
                this.responseOkString = new String(Files.readAllBytes(Paths.get(responseProperties.getGoodResponsePath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.responseOkString;
    }

    private String getBadString(){
        if (this.responseBadString==null) {
            try {
                this.responseBadString = new String(Files.readAllBytes(Paths.get(responseProperties.getBadResponsePath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.responseBadString;
    }


    public void setRequestIdFromRequest() {
        this.requestIdStr = xPathParse(request, responseProperties.getIdXpathPattern());
        this.requestIdInt = Long.parseLong(requestIdStr);
    }

    public void setRequest(String request) {
        this.request = request;
        setRequestIdFromRequest();
    }

    public boolean isOkRequest(){
        if (requestIdInt < 0) return false;
        else return true;
    }


    public String xPathParse(String xml, String xPath) {
        xml = deleteLineBreacksFromXml(xml);
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        return findXpathStringFromDoc(getDocfromSoap(is), xPath);
    }


    public String deleteLineBreacksFromXml(String xml) {
        xml = xml.replaceAll("[>]\n\\s*[<]", "><");
        return xml;
    }


    public Document getDocfromSoap(InputStream is) {
        Document doc = null;
        try {
            SOAPMessage soapMessage =  MessageFactory.newInstance().createMessage(null, is);
            SOAPBody body = soapMessage.getSOAPBody();
            doc = body.extractContentAsDocument();
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }


    public String findXpathStringFromDoc(Document doc, String xPath) {
        String result = null;
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
        try {
            expr = xpath.compile(xPath);
            result = expr.evaluate(doc);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

}
