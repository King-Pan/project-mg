package com.asiainfo.projectmg.service.impl;

import com.asiainfo.projectmg.common.BootstrapMessage;
import com.asiainfo.projectmg.common.Message;
import com.asiainfo.projectmg.model.AllotInfo;
import com.asiainfo.projectmg.model.CardInfo;
import com.asiainfo.projectmg.model.Demand;
import com.asiainfo.projectmg.repository.AllotInfoRepository;
import com.asiainfo.projectmg.service.AllotInfoService;
import com.asiainfo.projectmg.service.CardService;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
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
@Transactional(rollbackFor = RuntimeException.class)
public class AllotInfoServiceImpl implements AllotInfoService {

    @Autowired
    private AllotInfoRepository allotInfoRepository;


    @Autowired
    private CardService cardService;


    @Autowired
    private DemandService demandService;

    @Override
    public void save(AllotInfo allotInfo) {
        allotInfoRepository.save(allotInfo);
    }

    @Override
    public void saveList(List<AllotInfo> allotInfoList) {
        if (CollectionUtils.isNotEmpty(allotInfoList)) {
            for (AllotInfo allotInfo : allotInfoList) {
                AllotInfo info = allotInfoRepository.getByAndDemandCodeAndDateAndAndUserName(allotInfo.getDemandCode(),allotInfo.getDate(),allotInfo.getUserName());
                if(info==null){
                    save(allotInfo);
                }else{
                    BigDecimal bigDecimal = new BigDecimal(info.getHour());
                    bigDecimal = bigDecimal.add(new BigDecimal(allotInfo.getHour()));
                    info.setHour(bigDecimal.toString());
                    save(info);
                }
            }
        }
    }

    @Override
    public void delete(Long id) {
        allotInfoRepository.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        AllotInfo info;
        CardInfo cardInfo;
        Demand demand;
        List<CardInfo> cardInfoList = new ArrayList<>(10);
        List<Demand> demandList = new ArrayList<>(10);
        for (Long id:ids){
            info = allotInfoRepository.getOne(id);
            cardInfo =  cardService.findById(info.getCardId());
            demand = demandService.findByDemandCode(info.getDemandCode());
            BigDecimal supHour = new BigDecimal(cardInfo.getSurHours());
            supHour = supHour.add(new BigDecimal(info.getHour()));
            cardInfo.setSurHours(supHour.toString());

            //以报工时
            BigDecimal alHours = new BigDecimal(demand.getAlHours());
            alHours = alHours.subtract(new BigDecimal(info.getHour()));

            //未报工时
            BigDecimal surHours = new BigDecimal(demand.getSurHours());
            surHours = surHours.add(new BigDecimal(info.getHour()));

            demand.setAlHours(alHours.toString());
            demand.setSurHours(surHours.toString());

            cardInfoList.add(cardInfo);
            demandList.add(demand);
        }
        cardService.saveList(cardInfoList);
        demandService.saveList(demandList);
        allotInfoRepository.deleteAllotInfoByIdIn(ids);
    }

    @Override
    public Message<AllotInfo> getList(final AllotInfo allotInfo, Pageable pageable) {
        BootstrapMessage<AllotInfo> message = new BootstrapMessage<>();
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "userName"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "date"));
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(orders));
        Page<AllotInfo> cardInfoPage = allotInfoRepository.findAll(new Specification<AllotInfo>() {
            @Override
            public Predicate toPredicate(Root<AllotInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> userName = root.get("userName");
                Path<String> demandName = root.get("demandName");
                Path<String> demandCode = root.get("demandCode");
                Path<Date> date = root.get("date");

                List<Predicate> wherePredicate = new ArrayList<>();
                if (allotInfo != null) {
                    if (StringUtils.isNoneBlank(allotInfo.getUserName())) {
                        wherePredicate.add(cb.like(userName, "%" + allotInfo.getUserName() + "%"));
                    }
                    if (StringUtils.isNoneBlank(allotInfo.getDemandName())) {
                        wherePredicate.add(cb.like(demandName, "%" + allotInfo.getDemandName() + "%"));
                    }
                    if (StringUtils.isNoneBlank(allotInfo.getDemandCode())) {
                        wherePredicate.add(cb.like(demandCode, "%" + allotInfo.getDemandCode() + "%"));
                    }
                    if (allotInfo.getDate() != null) {
                        wherePredicate.add(cb.greaterThanOrEqualTo(date.as(Date.class), allotInfo.getDate()));
                    }
                    if (allotInfo.getEndDate() != null) {
                        wherePredicate.add(cb.lessThanOrEqualTo(date.as(Date.class), allotInfo.getEndDate()));
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
