package com.asiainfo.projectmg.service;

import com.asiainfo.projectmg.common.Message;
import com.asiainfo.projectmg.model.CardInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/12
 * Time: 下午2:35
 * Description: No Description
 */
public interface CardService {
    /**
     * 保存需求
     *
     * @param card
     */
    void save(CardInfo card);

    /**
     * 保存需求列表
     *
     * @param cardList
     */
    void saveList(List<CardInfo> cardList);

    /**
     * 通过ID删除打卡记录
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 通过cardId 查询CardInfo集合
     *
     * @param cardIds
     * @return
     */
    List<CardInfo> getListByCardIds(List<Long> cardIds);

    /**
     * 通过ID删除需求
     *
     * @param id
     */
    void delete(Long id);


    CardInfo findById(Long id);


    /**
     * 分页获取需求列表
     *
     * @param card     查询参数
     * @param pageable 分页参数
     * @return
     */
    Message<CardInfo> getList(CardInfo card, Pageable pageable);
}
