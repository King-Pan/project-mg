package com.asiainfo.projectmg.service.impl;

import com.asiainfo.projectmg.common.BootstrapMessage;
import com.asiainfo.projectmg.common.Message;
import com.asiainfo.projectmg.model.Demand;
import com.asiainfo.projectmg.repository.DemandRepository;
import com.asiainfo.projectmg.service.DemandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/5
 * Time: 上午10:14
 * Description: No Description
 */
@Slf4j
@Service
public class DemandServiceImpl implements DemandService {

    @Autowired
    private DemandRepository demandRepository;

    @Override
    public void save(Demand demand) {
        demandRepository.save(demand);
    }

    @Override
    public Message<Demand> getList(final Demand demand, Pageable pageable) {
        BootstrapMessage<Demand> message = new BootstrapMessage<>();
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "updateTime"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "id"));
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(orders));
        Page<Demand> demandPage = demandRepository.findAll(new Specification<Demand>() {
            @Override
            public Predicate toPredicate(Root<Demand> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> code = root.get("code");
                Path<String> name = root.get("name");

                List<Predicate> wherePredicate = new ArrayList<>();
                if (demand != null) {
                    if (StringUtils.isNoneBlank(demand.getCode())) {
                        wherePredicate.add(cb.like(code, "%" + demand.getCode() + "%"));
                    }
                    if (StringUtils.isNoneBlank(demand.getName())) {
                        wherePredicate.add(cb.like(name, "%" + demand.getName() + "%"));
                    }
                }
                Predicate[] predicates = new Predicate[]{};
                //这里可以设置任意条查询条件
                if (CollectionUtils.isNotEmpty(wherePredicate)) {
                    query.where(wherePredicate.toArray(predicates));
                }
                return null;
            }
        }, pageRequest);
        message.setLimit(demandPage.getSize());
        message.setRows(demandPage.getContent());
        message.setTotal(demandPage.getTotalElements());
        message.setStart(demandPage.getNumberOfElements());
        return message;
    }

    @Override
    public void saveList(List<Demand> demandList) {

        if (CollectionUtils.isNotEmpty(demandList)) {
            for (Demand demand : demandList) {
                Demand d = demandRepository.getByCode(demand.getCode());
                if (d != null) {
                    log.info("更新需求");
                    demand.setUpdateTime(new Date());
                    demand.setId(d.getId());
                } else {
                    demand.setCreateTime(new Date());
                }
                save(demand);
            }
        }
    }

    @Override
    public void delete(Long id) {
        demandRepository.deleteById(id);
    }

    @Override
    public List<Demand> getList() {
        return demandRepository.findAll();
    }
}
