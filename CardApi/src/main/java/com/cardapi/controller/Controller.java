package com.cardapi.controller;

import com.cardapi.Service.DebitCardApiService;
import com.cardapi.model.CombinedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class Controller {
    //create card

    @Autowired
    DebitCardApiService cardService;

    @RequestMapping(method=RequestMethod.POST,value="/createCard")
    public ResponseEntity<Map<String,String>> createCard(){
        ResponseEntity<String> result=cardService.createCard();
        return new ResponseEntity<>(Collections.singletonMap("result",result.getBody()),result.getStatusCode());
    }

    @RequestMapping(method=RequestMethod.PATCH, value="/linkCard/{cardNum}/{accNum}")
    public ResponseEntity<Map<String,String>> linkCard(@PathVariable("cardNum") String cardNum, @PathVariable("accNum") String accNum){
        ResponseEntity<String> result=cardService.linkCard(Long.valueOf(cardNum),Long.valueOf(accNum));
        return new ResponseEntity<>(Collections.singletonMap("result",result.getBody()),result.getStatusCode());
    }

    @RequestMapping(method=RequestMethod.PUT, value = "/debitAmount/{cardNum}/{amount}")
    public ResponseEntity<Map<String,String>> debitAmount(@PathVariable("cardNum") String cardNum, @PathVariable("amount") String amount){
        ResponseEntity<String> result=cardService.debitAmount(Long.valueOf(cardNum),Double.valueOf(amount));
        return new ResponseEntity<>(Collections.singletonMap("result",result.getBody()),result.getStatusCode());
    }

    @RequestMapping(method=RequestMethod.PUT, value = "/creditAmount/{cardNum}/{amount}")
    public ResponseEntity<Map<String,String>> creditAmount(@PathVariable("cardNum") String cardNum, @PathVariable("amount") String amount){
        ResponseEntity<String> result=cardService.creditAmount(Long.valueOf(cardNum),Double.valueOf(amount));
        return new ResponseEntity<>(Collections.singletonMap("result",result.getBody()),result.getStatusCode());
    }

    @RequestMapping(method=RequestMethod.GET, value="/showAllData/{cardNum}")
    public ResponseEntity<Map<String,CombinedData>> showAllData(@PathVariable("cardNum") String cardNum){
        ResponseEntity<CombinedData> result=cardService.showAllData(Long.valueOf(cardNum));
        return new ResponseEntity<>(Collections.singletonMap("Data",result.getBody()),result.getStatusCode());
    }
}
