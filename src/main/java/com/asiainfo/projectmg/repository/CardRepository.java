package com.asiainfo.projectmg.repository;

import com.asiainfo.projectmg.model.CardInfo;
import com.asiainfo.projectmg.model.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/12
 * Time: 下午2:37
 * Description: No Description
 */
public interface CardRepository extends JpaRepository<CardInfo, Long>, QuerydslPredicateExecutor<CardInfo>, JpaSpecificationExecutor<CardInfo> {
    CardInfo getByUserNameAndDate(String userName, Date date);

    /**
     * 通过cardId获取cardinfo集合
     * @param cardIds
     * @return
     */
    List<CardInfo> findByIdIn(List<Long> cardIds);
}
