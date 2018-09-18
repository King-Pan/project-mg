package com.asiainfo.projectmg.service.impl;

import com.asiainfo.projectmg.common.BootstrapMessage;
import com.asiainfo.projectmg.common.Message;
import com.asiainfo.projectmg.model.AllotInfo;
import com.asiainfo.projectmg.model.CardInfo;
import com.asiainfo.projectmg.model.Demand;
import com.asiainfo.projectmg.model.form.AllotForm;
import com.asiainfo.projectmg.repository.DemandRepository;
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
 * Date: 2018/9/5
 * Time: 上午10:14
 * Description: No Description
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class DemandServiceImpl implements DemandService {

    @Autowired
    private DemandRepository demandRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private AllotInfoService allotInfoService;

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
    public void allotHours(AllotForm allotForm) {
        String type = allotForm.getType();
        List<CardInfo> cardInfoList = cardService.getListByCardIds(allotForm.getCardIds());
        Demand demand = demandRepository.getOne(Long.parseLong(allotForm.getDemandId()));
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal surHours = new BigDecimal(demand.getSurHours());
        if ("0".equals(type)) {
            log.info("定额分配");
            //定额分配
            for (CardInfo cardInfo : cardInfoList) {
                if (Double.parseDouble(cardInfo.getSurHours()) < Double.parseDouble(allotForm.getHour())) {
                    throw new RuntimeException("员工:" + cardInfo.getUserName() + ",剩余工时不足.");
                }
            }
            sum = new BigDecimal(allotForm.getHour()).multiply(new BigDecimal(cardInfoList.size() + ""));
            log.info("定额分配： 总工时->{}", sum);
        } else if ("1".equals(type)) {
            //余额分配
            log.info("余额分配");
            for (CardInfo cardInfo : cardInfoList) {
                sum = sum.add(new BigDecimal(cardInfo.getSurHours()));
            }
            log.info("余额分配： 总工时->{}", sum);
        } else {
            throw new RuntimeException("type must equals 0 or 1");
        }

        if (surHours.doubleValue() < sum.doubleValue()) {
            throw new RuntimeException("需要分配的总工时为->" + sum + ",剩余工时为->" + demand.getSurHours() + ",工时不足");
        }

        //1. 生成分配记录
        List<AllotInfo> allotInfoList = new ArrayList<>(20);
        AllotInfo allotInfo;
        for (CardInfo cardInfo : cardInfoList) {
            allotInfo = new AllotInfo();
            allotInfo.setCardId(cardInfo.getId());
            allotInfo.setDate(cardInfo.getDate());
            allotInfo.setDemandId(demand.getId());
            allotInfo.setDemandCode(demand.getCode());
            allotInfo.setDemandName(demand.getName());
            allotInfo.setUserName(cardInfo.getUserName());
            if ("0".equals(type)) {
                allotInfo.setHour(allotForm.getHour());
                //扣减打卡记录剩余工时
                cardInfo.setSurHours(new BigDecimal(cardInfo.getSurHours()).
                        subtract(new BigDecimal(allotForm.getHour())).toString());
            } else if ("1".equals(type)) {
                allotInfo.setHour(cardInfo.getSurHours());
                //余额分配
                cardInfo.setSurHours("0");
            }
            allotInfoList.add(allotInfo);
        }
        allotInfoService.saveList(allotInfoList);

        //2. 扣减打卡记录剩余工时
        cardService.saveList(cardInfoList);
        //3. 扣减需求剩余工时
        demand.setSurHours(surHours.subtract(sum).toString());
        //4. 已填工时 + 扣减工时
        demand.setAlHours(new BigDecimal(demand.getAlHours()).add(sum).toString());
        demandRepository.save(demand);
    }

    @Override
    public void saveList(List<Demand> demandList) {

        if (CollectionUtils.isNotEmpty(demandList)) {
            for (Demand demand : demandList) {
                Demand d = demandRepository.getByCode(demand.getCode());
                if (d != null) {
                    log.info("更新需求");
                    demand.setUpdateTime(new Date());
                    demand.setCreateTime(d.getCreateTime());
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
