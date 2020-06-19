package com.accountapi.controller;

import com.accountapi.AccountApiApplication;
import com.accountapi.Service.AccountApitService;
import com.accountapi.model.AccountUser;
import com.accountapi.model.UserEM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.Collections;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    AccountApitService accountService;

    @RequestMapping("/hello")
    public String sayHello(){
        return "Hello";
    }

    //insert
    @RequestMapping(method = RequestMethod.POST,value = "/addUser")
    public ResponseEntity<Map<String,String>> addUser(@RequestBody AccountUser user){
        ResponseEntity<String> result=accountService.addUser(user);
        return new ResponseEntity<>(Collections.singletonMap("result",result.getBody()),result.getStatusCode());
    }

    //delete
    @RequestMapping(method=RequestMethod.DELETE,value="/deleteUser")
    public ResponseEntity<Map<String,String>> deleteUser(@RequestBody UserEM userEM){
        ResponseEntity<String> result= accountService.deleteUser(userEM);
        return new ResponseEntity<>(Collections.singletonMap("result",result.getBody()),result.getStatusCode());
    }

    //update
    @RequestMapping(method = RequestMethod.PATCH,value="/updateRecord/{email}/{mobileNum}")
    public ResponseEntity<Map<String,String>> updateRecord(@PathVariable("email") String email, @PathVariable("mobileNum") String mobileNum, @RequestBody AccountUser accountUser){
        UserEM userEM=new UserEM(mobileNum,email);
        ResponseEntity<String> result= accountService.updateUserRecord(userEM,accountUser);
        return new ResponseEntity<>(Collections.singletonMap("result",result.getBody()),result.getStatusCode());
    }

    //withdraw
    @RequestMapping(method = RequestMethod.PUT,value="/withdrawBalance/{email}/{mobileNum}/{amount}")
    public ResponseEntity<Map<String,String>> withdrawBalance(@PathVariable("email") String email, @PathVariable("mobileNum") String mobileNum, @PathVariable("amount") Double amount){
        UserEM userEM=new UserEM(mobileNum,email);
        ResponseEntity<String> result= accountService.updateBalance(userEM,amount,"debit");
        return new ResponseEntity<>(Collections.singletonMap("result",result.getBody()),result.getStatusCode());
    }

    //withdraw by accNum
    @RequestMapping(method = RequestMethod.PUT,value="/withdrawBalanceById/{accNum}/{amount}")
    public ResponseEntity<Map<String,String>> withdrawBalanceById(@PathVariable("accNum") String accNum, @PathVariable("amount") Double amount){
        ResponseEntity<String> result= accountService.updateBalanceById(Long.valueOf(accNum),amount,"debit");
        return new ResponseEntity<>(Collections.singletonMap("result",result.getBody()),result.getStatusCode());
    }

    //view
    @RequestMapping(method=RequestMethod.GET,value="/viewRecord/{email}/{mobileNum}")
    public ResponseEntity<Map<String,AccountUser>> viewRecord(@PathVariable("email") String email, @PathVariable("mobileNum") String mobileNum){
        UserEM userEM=new UserEM(mobileNum,email);
        ResponseEntity<AccountUser> result= accountService.viewUserRecord(userEM);
        return new ResponseEntity<>(Collections.singletonMap("Data",result.getBody()),result.getStatusCode());
    }

    //view By Id
    @RequestMapping(method=RequestMethod.GET,value="/viewRecordById/{accNum}")
    public ResponseEntity<Map<String,AccountUser>> viewRecordById(@PathVariable("accNum") String accNum){
        ResponseEntity<AccountUser> result= accountService.viewUserRecord(Long.valueOf(accNum));
        return new ResponseEntity<>(Collections.singletonMap("Data",result.getBody()),result.getStatusCode());
    }

    //deposit
    @RequestMapping(method = RequestMethod.PUT,value="/depositBalance/{email}/{mobileNum}/{amount}")
    public ResponseEntity<Map<String,String>> depositBalance(@PathVariable("email") String email, @PathVariable("mobileNum") String mobileNum, @PathVariable("amount") Double amount){
        UserEM userEM=new UserEM(mobileNum,email);
        ResponseEntity<String> result= accountService.updateBalance(userEM,amount,"credit");
        return new ResponseEntity<>(Collections.singletonMap("result",result.getBody()),result.getStatusCode());
    }

    //deposit by accNum
    @RequestMapping(method = RequestMethod.PUT,value="/depositeBalanceById/{accNum}/{amount}")
    public ResponseEntity<Map<String,String>> depositBalanceById(@PathVariable("accNum") String accNum, @PathVariable("amount") Double amount){
        ResponseEntity<String> result= accountService.updateBalanceById(Long.valueOf(accNum),amount,"credit");
        return new ResponseEntity<>(Collections.singletonMap("result",result.getBody()),result.getStatusCode());
    }

}
