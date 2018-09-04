package com.asiainfo.projectmg.excel.model;

import com.asiainfo.projectmg.excel.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/4
 * Time: 上午11:54
 * Description: 需求信息
 */
@Data
public class Demand {
    /**
     * 需求编码
     */
    @ExcelColumn("需求编码")
    private String code;
    /**
     * 需求名称
     */
    @ExcelColumn("需求名称")
    private String name;
    /**
     * 预估工作量-天（天/人）
     */
    @ExcelColumn("预估工作量-天")
    private String personDay;
    /**
     * 预估工作量（小时）
     */
    @ExcelColumn("预估工作量-时")
    private String preHours;

    /**
     * 已报工工时（小时）
     */
    @ExcelColumn("已报工工时")
    private String alHours;

    /**
     * 剩报工工时
     */
    @ExcelColumn("剩报工工时")
    private String surHours;

    /**
     * 可报人员
     */
    @ExcelColumn("可报工人员")
    private String persons;
}
