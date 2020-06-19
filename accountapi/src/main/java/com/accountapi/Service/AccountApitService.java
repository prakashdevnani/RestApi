package com.accountapi.Service;

import com.accountapi.dao.AccountApiRepository;
import com.accountapi.model.AccountUser;
import com.accountapi.model.UserEM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountApitService {

    @Autowired
    AccountApiRepository accRep;


    public ResponseEntity<String> addUser(AccountUser user) {
        ResponseEntity<String> result=checkInputRecordData(user,"Data Inserted Correctly");
        if(result.getStatusCode()==HttpStatus.OK)
            accRep.save(user);
        return result;
    }

    private Long checkAlreadyPresent(String email, String mobileNum) {
        Optional<AccountUser> accUserTemp=accRep.getByEmailMobile(email,mobileNum);

        if(accUserTemp.isPresent())
            return accUserTemp.get().getAccNum();
        else
            return -1L;
    }

    public ResponseEntity<String> deleteUser(UserEM userEM) {

        String emailPattern="^[a-zA-Z0-9+_.-]+@[a-zA-Z]+\\.[a-zA-Z]+$";
        Pattern pat=Pattern.compile(emailPattern);
        Matcher matcher=pat.matcher(userEM.getEmail());
        if(userEM.getEmail()!=null && !userEM.getEmail().equals("") && matcher.matches())
            if(userEM.getMobileNum()!=null && !userEM.getMobileNum().equals("") && userEM.getMobileNum().matches("^[0-9]*") && userEM.getMobileNum().length()==10)
            {
                Long id=checkAlreadyPresent(userEM.getEmail(),userEM.getMobileNum());
                if(id==-1)
                    return new ResponseEntity<>("Record not present with given inputs",HttpStatus.BAD_REQUEST);
                else{
                    accRep.delete(id);
                    return new ResponseEntity<>("Record got deleted",HttpStatus.OK);
                }
            }
            else
                return new ResponseEntity<>("Enter correct Mobile Number",HttpStatus.BAD_REQUEST);
        else
        return new ResponseEntity<>("Enter correct Email Address",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<AccountUser> viewUserRecord(UserEM userEM) {
        Optional<AccountUser> userTemp=accRep.getByEmailMobile(userEM.getEmail(),userEM.getMobileNum());
        AccountUser user=new AccountUser();
        user.setAccNum(-1L);
        if(userTemp.isPresent())
            return new ResponseEntity<>(userTemp.get(),HttpStatus.OK);
        else
            return new ResponseEntity<>(user,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<AccountUser> viewUserRecord(Long accNum) {
        Optional<AccountUser> userTemp=accRep.getById(accNum);
        AccountUser user=new AccountUser();
        user.setAccNum(-1L);
        if(userTemp.isPresent())
            return new ResponseEntity<>(userTemp.get(),HttpStatus.OK);
        else
            return new ResponseEntity<>(user,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> updateUserRecord(UserEM userEM, AccountUser accountUser) {
        Optional<AccountUser> userTemp=accRep.getByEmailMobile(userEM.getEmail(),userEM.getMobileNum());
        if(userTemp.isPresent()) {
            ResponseEntity<String> result=checkInputRecordData(accountUser, "Data got Updated");
            if(result.getStatusCode()==HttpStatus.OK)
                accRep.update(userEM.getMobileNum(),userEM.getEmail(),accountUser.getName(),accountUser.getMobileNum(),accountUser.getEmail());
            return  result;
        }
        else
            return new ResponseEntity<>("Record not available with the given email id and mobile number",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> updateBalance(UserEM userEM, Double amount, String operation) {
        Optional<AccountUser> userTemp=accRep.getByEmailMobile(userEM.getEmail(),userEM.getMobileNum());
        if(userTemp.isPresent())
            return dataPresent(userTemp.get(),amount,operation);
        else
            return new ResponseEntity<>("Record not available with the given email id and mobile number",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> updateBalanceById(Long accNum, Double amount, String operation) {
        Optional<AccountUser> userTemp=accRep.getById(accNum);
        if(userTemp.isPresent())
            return dataPresent(userTemp.get(),amount,operation);
        else
            return new ResponseEntity<>("Record not available with the given Account Number",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> checkInputRecordData(AccountUser user,String operation){
        String emailPattern="^[a-zA-Z0-9+_.-]+@[a-zA-Z]+\\.[a-zA-Z]+$";
        Pattern pat=Pattern.compile(emailPattern);
        Matcher matcher=pat.matcher(user.getEmail());
        if(user.getName()!=null && !user.getName().equals(""))
            if(user.getEmail()!=null && !user.getEmail().equals("") && matcher.matches())
                if(user.getMobileNum()!=null && !user.getMobileNum().equals("") && user.getMobileNum().matches("^[0-9]*") && user.getMobileNum().length()==10)
                    if(checkAlreadyPresent(user.getEmail(),user.getMobileNum())==-1)
                        return new ResponseEntity<>(operation, HttpStatus.OK);
                    else
                        return new ResponseEntity<>("Data Already Present", HttpStatus.BAD_REQUEST);
                else
                    return new ResponseEntity<>("Enter correct Mobile Number", HttpStatus.BAD_REQUEST);
            else
                return new ResponseEntity<>("Enter correct Email Address", HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>("Enter Name correctly",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> dataPresent(AccountUser au, Double amount, String operation){
        if(operation.equals("debit")){
            if(au.getBalance()<amount)
                return new ResponseEntity<>("Not Enough balance",HttpStatus.BAD_REQUEST);
            au.setBalance(au.getBalance()-amount);
            accRep.updateBalance(au.getAccNum(),au.getBalance());
            return new  ResponseEntity<>("Amount got debited",HttpStatus.OK);
        }
        else if(operation.equals("credit")){
            au.setBalance(au.getBalance()+amount);
            accRep.updateBalance(au.getAccNum(),au.getBalance());
            return new  ResponseEntity<>("Amount got credited",HttpStatus.OK);
        }
        return new  ResponseEntity<>("Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
