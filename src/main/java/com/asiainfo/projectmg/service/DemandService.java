package com.asiainfo.projectmg.service;

import com.asiainfo.projectmg.common.Message;
import com.asiainfo.projectmg.model.Demand;
import com.asiainfo.projectmg.model.form.AllotForm;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/5
 * Time: 上午10:12
 * Description: No Description
 */
public interface DemandService {

    /**
     * 保存需求
     *
     * @param demand
     */
    void save(Demand demand);

    /**
     * 保存需求列表
     *
     * @param demandList
     */
    void saveList(List<Demand> demandList);

    /**
     * 通过ID删除需求
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 通过需求编码获取需求信息
     * @param code 需求编码
     * @return
     */
    Demand findByDemandCode(String code);


    /**
     * 通过ID删除需求
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 获取需求列表
     *
     * @return
     */
    List<Demand> getList();



    /**
     * 分页获取需求列表
     *
     * @param demand   查询参数
     * @param pageable 分页参数
     * @return
     */
    Message<Demand> getList(Demand demand, Pageable pageable);


    /**
     * 按照需求分配工时
     * @param allotForm 参数
     */
    void allotHours(AllotForm allotForm);
}
