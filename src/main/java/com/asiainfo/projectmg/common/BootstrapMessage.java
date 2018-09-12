package com.asiainfo.projectmg.common;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/12
 * Time: 上午10:18
 * Description: No Description
 */
@Data
public class BootstrapMessage<T> implements Message {
    private List<T> rows;
    private Integer start;
    private Integer limit;
    private Long total;
}
