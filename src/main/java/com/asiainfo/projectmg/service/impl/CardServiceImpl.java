package com.asiainfo.projectmg.service.impl;

import com.asiainfo.projectmg.common.BootstrapMessage;
import com.asiainfo.projectmg.common.Message;
import com.asiainfo.projectmg.model.CardInfo;
import com.asiainfo.projectmg.repository.CardRepository;
import com.asiainfo.projectmg.service.CardService;
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
import java.util.ArrayList;
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
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CardServiceImpl implements CardService {


    @Autowired
    private CardRepository cardRepository;

    @Override
    public void save(CardInfo card) {
        if (card != null && card.getId() != null) {
            CardInfo cardinfo = cardRepository.getOne(card.getId());
            if (cardinfo != null) {
                card.setCreateTime(cardinfo.getCreateTime());
            } else {
                card.setCreateTime(new Date());
            }
        } else {
            card.setCreateTime(new Date());
        }
        card.setUpdateTime(new Date());
        cardRepository.save(card);
    }

    @Override
    public void saveList(List<CardInfo> cardList) {
        if (CollectionUtils.isNotEmpty(cardList)) {
            for (CardInfo cardInfo : cardList) {
                CardInfo card = cardRepository.getByUserNameAndDate(cardInfo.getUserName(), cardInfo.getDate());
                if (card != null) {
                    log.info("更新打卡记录");
                    cardInfo.setCreateTime(card.getCreateTime());
                    cardInfo.setUpdateTime(new Date());
                    cardInfo.setUserId(card.getUserId());
                    cardInfo.setId(card.getId());
                    cardInfo.setSurHours(card.getSurHours());
                } else {
                    cardInfo.setCreateTime(new Date());
                    cardInfo.setSurHours(cardInfo.getHours());
                }
                save(cardInfo);
            }
        }
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        cardRepository.deleteCardInfosByIdIn(ids);
    }

    @Override
    public void delete(Long id) {
        cardRepository.deleteById(id);
    }

    @Override
    public List<CardInfo> getListByCardIds(List<Long> cardIds) {
        return cardRepository.findByIdIn(cardIds);
    }

    @Override
    public Message<CardInfo> getList(final CardInfo card, Pageable pageable) {
        BootstrapMessage<CardInfo> message = new BootstrapMessage<>();
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "userId"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "updateTime"));
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(orders));
        Page<CardInfo> cardInfoPage = cardRepository.findAll(new Specification<CardInfo>() {
            @Override
            public Predicate toPredicate(Root<CardInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> userName = root.get("userName");
                Path<Date> date = root.get("date");
                Path<String> surHours = root.get("surHours");

                List<Predicate> wherePredicate = new ArrayList<>();
                if (card != null) {
                    if (StringUtils.isNoneBlank(card.getUserName())) {
                        wherePredicate.add(cb.like(userName, "%" + card.getUserName() + "%"));
                    }
                    if (card.getDate() != null) {
                        wherePredicate.add(cb.equal(date, card.getDate()));
                    }
                    if (card.getSurHours() != null && card.getSurHours().equals("0")) {
                        wherePredicate.add(cb.equal(surHours, "0"));
                    }
                    if (card.getSurHours() != null && card.getSurHours().equals("1")) {
                        wherePredicate.add(cb.greaterThan(surHours, "0"));
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
}
