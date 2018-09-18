package com.asiainfo.projectmg.repository;

import com.asiainfo.projectmg.model.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/5
 * Time: 上午9:57
 * Description: No Description
 */

public interface DemandRepository extends JpaRepository<Demand, Long>, QuerydslPredicateExecutor<Demand>, JpaSpecificationExecutor<Demand> {
    /**
     * 通过需求编码判断该需求是否存在
     * @param code
     * @return
     */
    Demand getByCode(String code);


}
