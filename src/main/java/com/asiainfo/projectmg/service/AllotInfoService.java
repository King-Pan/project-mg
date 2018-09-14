package com.asiainfo.projectmg.service;

import com.asiainfo.projectmg.common.Message;
import com.asiainfo.projectmg.model.AllotInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/14
 * Time: 上午9:30
 * Description: No Description
 */
public interface AllotInfoService {
    /**
     * 保存需求
     *
     * @param allotInfo
     */
    void save(AllotInfo allotInfo);

    /**
     * 保存需求列表
     *
     * @param allotInfoList
     */
    void saveList(List<AllotInfo> allotInfoList);

    /**
     * 通过ID删除需求
     *
     * @param id
     */
    void delete(Long id);


    /**
     * 分页获取需求列表
     *
     * @param allotInfo     查询参数
     * @param pageable 分页参数
     * @return
     */
    Message<AllotInfo> getList(AllotInfo allotInfo, Pageable pageable);
}
