package com.asiainfo.projectmg.repository;

import com.asiainfo.projectmg.model.CardInfo;
import com.asiainfo.projectmg.model.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/12
 * Time: 下午2:37
 * Description: No Description
 */
public interface CardRepository extends JpaRepository<CardInfo, String>, QuerydslPredicateExecutor<CardInfo>, JpaSpecificationExecutor<CardInfo> {
    CardInfo getByUserNameAndDate(String userName, Date date);
}
