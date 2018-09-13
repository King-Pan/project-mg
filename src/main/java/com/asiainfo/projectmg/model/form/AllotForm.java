package com.asiainfo.projectmg.model.form;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/13
 * Time: 下午5:23
 * Description: No Description
 */
@Data
public class AllotForm {
    private String demandId;
    private String type;
    private String hour;
    private List<Long> cardIds;
}
