package com.cardapi.Service;

import com.cardapi.dao.DebitCardApiRepository;
import com.cardapi.model.AccountUser;
import com.cardapi.model.CombinedData;
import com.cardapi.model.DebitCard;
import org.aspectj.weaver.Iterators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.HTMLDocument;
import java.io.IOException;
import java.util.*;

@Service
public class DebitCardApiService {

    @Autowired
    DebitCardApiRepository cardApiRepository;

    public ResponseEntity<String> createCard() {
        DebitCard dc=new DebitCard();
        long count = cardApiRepository.count();
        dc.setCarNum(count+1);
        dc.setCvvNum((int)((count+1)%1000));
        cardApiRepository.save(dc);
        return new ResponseEntity<>("Card got Created with Card Number: "+(count+1), HttpStatus.OK);
    }

    public ResponseEntity<String> linkCard(Long cardNum, Long accNum) {
        RestTemplate rt=new RestTemplate();
        rt.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {

            }
        });
        ResponseEntity<AccountUser> reAccUser=rt.getForEntity("http://localhost:9090/viewRecordById/"+accNum, AccountUser.class);

        Optional<DebitCard> debitCardOpt=cardApiRepository.getByCardNum(cardNum);

        if(reAccUser.getStatusCode().value()==HttpStatus.BAD_REQUEST.value())
            return new ResponseEntity<>("Record not available for the account Number: "+accNum,HttpStatus.BAD_REQUEST);
        else if(!debitCardOpt.isPresent())
            return new ResponseEntity<>("Record not available for the card Number: "+cardNum,HttpStatus.BAD_REQUEST);
        else{
            cardApiRepository.updateAccNumByCardNum(accNum,cardNum);
            return new ResponseEntity<>("Card got linked with the Account Number", HttpStatus.OK);
        }
    }

    public ResponseEntity<String> debitAmount(Long carNum, Double amount) {
        Optional<DebitCard> debitCardOpt=cardApiRepository.getByCardNum(carNum);
        if(debitCardOpt.isPresent()){
            if(debitCardOpt.get().getAccNum()==-1)
                return new ResponseEntity<>("No Account Record linked to Card Number: "+carNum,HttpStatus.BAD_REQUEST);
            else{
                RestTemplate rt=new RestTemplate();
                rt.setErrorHandler(new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                        return false;
                    }

                    @Override
                    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {

                    }
                });
                ResponseEntity<Map> result =rt.exchange("http://localhost:9090/withdrawBalanceById/"+debitCardOpt.get().getAccNum()+"/"+amount, HttpMethod.PUT,null,Map.class);
                return new ResponseEntity<>((String)result.getBody().get("result"),result.getStatusCode());
            }
        }
        else
            return new ResponseEntity<>("No Record Corresponding to Card Number: "+carNum,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> creditAmount(Long carNum, Double amount) {
        Optional<DebitCard> debitCardOpt=cardApiRepository.getByCardNum(carNum);
        if(debitCardOpt.isPresent()){
            if(debitCardOpt.get().getAccNum()==-1)
                return new ResponseEntity<>("No Account Record linked to Card Number: "+carNum,HttpStatus.BAD_REQUEST);
            else{
                RestTemplate rt=new RestTemplate();
                ResponseEntity<Map> result =rt.exchange("http://localhost:9090/depositeBalanceById/"+debitCardOpt.get().getAccNum()+"/"+amount, HttpMethod.PUT,null,Map.class);
                return new ResponseEntity<>((String)result.getBody().get("result"),result.getStatusCode());
            }
        }
        else
            return new ResponseEntity<>("No Record Corresponding to Card Number: "+carNum,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<CombinedData> showAllData(Long cardNum) {
        Optional<DebitCard> dcOptional=cardApiRepository.getByCardNum(cardNum);
        if(dcOptional.isPresent()){
            RestTemplate rt=new RestTemplate();
            rt.setErrorHandler(new ResponseErrorHandler() {
                @Override
                public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                    return false;
                }

                @Override
                public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {

                }
            });
            ResponseEntity<Map> accUser=rt.getForEntity("http://localhost:9090/viewRecordById/"+dcOptional.get().getAccNum(), Map.class);
            if(accUser.getStatusCode()==HttpStatus.BAD_REQUEST)
                    return new ResponseEntity<>(new CombinedData(-1L,null,null,null,-1,cardNum,dcOptional.get().getCvvNum()),accUser.getStatusCode());
            Map<String,Object> au=(LinkedHashMap<String,Object>)accUser.getBody().get("Data");
            return new ResponseEntity<>(new CombinedData(dcOptional.get().getAccNum(),(String)au.get("name"),(String)au.get("email"),(String)au.get("mobileNum"),(Double) au.get("balance"),dcOptional.get().getCarNum(),dcOptional.get().getCvvNum()),HttpStatus.OK);

        }
        else return new ResponseEntity<>(new CombinedData(-1L,null,null,null,-1,-1L,-1),HttpStatus.BAD_REQUEST);
    }
}
