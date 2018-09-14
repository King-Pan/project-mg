package com.asiainfo.projectmg.repository;

import com.asiainfo.projectmg.model.AllotInfo;
import com.asiainfo.projectmg.model.CardInfo;
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
public interface AllotInfoRepository extends JpaRepository<AllotInfo, Long>, QuerydslPredicateExecutor<AllotInfo>, JpaSpecificationExecutor<AllotInfo> {
}
