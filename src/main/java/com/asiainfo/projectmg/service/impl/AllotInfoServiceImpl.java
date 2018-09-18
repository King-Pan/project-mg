package com.asiainfo.projectmg.service.impl;

import com.asiainfo.projectmg.common.BootstrapMessage;
import com.asiainfo.projectmg.common.Message;
import com.asiainfo.projectmg.model.AllotInfo;
import com.asiainfo.projectmg.repository.AllotInfoRepository;
import com.asiainfo.projectmg.service.AllotInfoService;
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
 * Date: 2018/9/14
 * Time: 上午9:37
 * Description: No Description
 */
@Slf4j
@Service
public class AllotInfoServiceImpl implements AllotInfoService {

    @Autowired
    private AllotInfoRepository allotInfoRepository;

    @Override
    public void save(AllotInfo allotInfo) {
        allotInfoRepository.save(allotInfo);
    }

    @Override
    public void saveList(List<AllotInfo> allotInfoList) {
        if (CollectionUtils.isNotEmpty(allotInfoList)) {
            for (AllotInfo allotInfo : allotInfoList) {
                save(allotInfo);
            }
        }
    }

    @Override
    public void delete(Long id) {
        allotInfoRepository.deleteById(id);
    }

    @Override
    public Message<AllotInfo> getList(final AllotInfo allotInfo, Pageable pageable) {
        BootstrapMessage<AllotInfo> message = new BootstrapMessage<>();
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "userName"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "date"));
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(orders));
        Page<AllotInfo> cardInfoPage = allotInfoRepository.findAll(new Specification<AllotInfo>() {
            @Override
            public Predicate toPredicate(Root<AllotInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> userName = root.get("userName");
                Path<String> demandName = root.get("demandName");
                Path<Date> date = root.get("date");

                List<Predicate> wherePredicate = new ArrayList<>();
                if (allotInfo != null) {
                    if (StringUtils.isNoneBlank(allotInfo.getUserName())) {
                        wherePredicate.add(cb.like(userName, "%" + allotInfo.getUserName() + "%"));
                    }
                    if (allotInfo.getDemandName() != null) {
                        wherePredicate.add(cb.like(demandName, "%" + allotInfo.getDemandName() + "%"));
                    }
                    if (allotInfo.getDate() != null) {
                        wherePredicate.add(cb.equal(date, allotInfo.getDate()));
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
        message.setLimit(cardInfoPage.getSize());
        message.setRows(cardInfoPage.getContent());
        message.setTotal(cardInfoPage.getTotalElements());
        message.setStart(cardInfoPage.getNumberOfElements());
        return message;
    }

    @Override
    public List<AllotInfo> getList(AllotInfo allotInfo) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "userName"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "date"));

        List<AllotInfo> allotInfoList = allotInfoRepository.findAll(new Specification<AllotInfo>() {
            @Override
            public Predicate toPredicate(Root<AllotInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> userName = root.get("userName");
                Path<Date> date = root.get("date");
                Path<Date> endDate = root.get("date");
                Path<String> demandName = root.get("demandName");

                List<Predicate> wherePredicate = new ArrayList<>();
                if (allotInfo != null) {
                    if (allotInfo.getDemandName() != null) {
                        wherePredicate.add(cb.like(demandName, "%" + allotInfo.getDemandName() + "%"));
                    }
                    if (allotInfo.getDate() != null) {
                        wherePredicate.add(cb.greaterThanOrEqualTo(date, allotInfo.getDate()));
                    }
                    if (allotInfo.getEndDate() != null) {
                        wherePredicate.add(cb.greaterThanOrEqualTo(date, allotInfo.getEndDate()));
                    }
                    if (StringUtils.isNoneBlank(allotInfo.getUserName())) {
                        wherePredicate.add(cb.like(userName, "%" + allotInfo.getUserName() + "%"));
                    }
                }
                Predicate[] predicates = new Predicate[]{};
                //这里可以设置任意条查询条件
                if (CollectionUtils.isNotEmpty(wherePredicate)) {
                    query.where(wherePredicate.toArray(predicates));
                }
                return null;
            }
        }, new Sort(orders));
        return allotInfoList;
    }
}
