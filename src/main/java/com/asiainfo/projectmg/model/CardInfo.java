package com.asiainfo.projectmg.model;

import com.asiainfo.projectmg.excel.ExcelColumn;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/12
 * Time: 下午2:28
 * Description: 打卡记录
 */

@Data
@Entity
@Table(name = "card_info")
public class CardInfo {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 打卡员工ID
     */
    private String userId;

    /**
     * 打卡员工姓名
     */
    @ExcelColumn("姓名")
    private String userName;

    /**
     * 打卡日期
     */
    @ExcelColumn(value = "打卡日期", format = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date date;

    /**
     * 打卡工时
     */
    @ExcelColumn("打卡工时")
    private String hours;

    /**
     * 剩余未报工时
     */
    private String surHours;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
