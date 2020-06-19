package com.accountapi.dao;

import com.accountapi.model.AccountUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface AccountApiRepository extends CrudRepository<AccountUser, Long> {


    @Query("select au from AccountUser au where au.email=:email and au.mobileNum=:mobileNum")
    Optional<AccountUser> getByEmailMobile(@Param("email") String email, @Param("mobileNum") String mobileNum);

    @Query("select au from AccountUser au where au.accNum=:accNum")
    Optional<AccountUser> getById(@Param("accNum") Long accNum);

    @Transactional
    @Modifying
    @Query("delete from AccountUser au where au.accNum=:id")
    void deleteWithID(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update AccountUser au set au.name=:aname, au.email=:aemail, au.mobileNum=:amobileNum where au.email=:pemail and au.mobileNum=:pmobileNum")
    void update(@Param("pmobileNum")String mobileNum,@Param("pemail") String email,@Param("aname") String name,@Param("amobileNum") String mobileNum1,@Param("aemail") String email1);

    @Transactional
    @Modifying
    @Query("update AccountUser au set au.balance=:balance where au.accNum=:accNum")
    void updateBalance(@Param("accNum") Long accNum,@Param("balance") double balance);
}
