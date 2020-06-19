package com.cardapi.dao;

import com.cardapi.model.DebitCard;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface DebitCardApiRepository extends CrudRepository<DebitCard, Long> {

    @Query("select dc from DebitCard dc where dc.carNum=:carNum")
    Optional<DebitCard> getByCardNum(@Param("carNum") Long cardNum);

    @Transactional
    @Modifying
    @Query("update DebitCard dc set dc.accNum=:accNum where dc.carNum=:carNum")
    void updateAccNumByCardNum(@Param("accNum") Long accNum,@Param("carNum") Long cardNum);
}
