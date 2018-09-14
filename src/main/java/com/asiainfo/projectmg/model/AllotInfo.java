package com.asiainfo.projectmg.model;

import lombok.Data;
import org.hibernate.exception.DataException;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/13
 * Time: 下午5:51
 * Description: 员工工时分配记录
 */
@Data
@Entity
@Table
public class AllotInfo {

    /**
     * 分配记录ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 员工姓名
     */
    private String userName;
    /**
     * 打卡日期
     */
    private Date date;
    /**
     * 需求id
     */
    private Long demandId;
    /**
     * 需求名称
     */
    private String demandName;
    /**
     * 打卡记录ID
     */
    private Long cardId;

}
