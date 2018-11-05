package com.asiainfo.projectmg.web.controller;

import com.asiainfo.projectmg.common.ServerResponse;
import com.asiainfo.projectmg.model.AllotInfo;
import com.asiainfo.projectmg.service.AllotInfoService;
import com.asiainfo.projectmg.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/18
 * Time: 下午1:53
 * Description: No Description
 */
@Slf4j
@RestController
@RequestMapping("/allot")
public class AllotController {

    @Autowired
    private AllotInfoService allotInfoService;

    @RequestMapping("/")
    public ModelAndView page() {
        return new ModelAndView("allot");
    }


    @GetMapping("/list")
    public Object list(AllotInfo allotInfo, @PageableDefault Pageable pageable) {
        return allotInfoService.getList(allotInfo, pageable);
    }

    @GetMapping("/download")
    public void exportExcel(AllotInfo allotInfo, final HttpServletResponse response) {
        try {
            String fileName = "allot_info_" + System.currentTimeMillis();
            List<AllotInfo> allotInfoList = allotInfoService.getList(allotInfo);
            ExportExcelUtil.exportWithResponse("工时分配信息", "工时分配信息", fileName,
                    5, new int[]{10, 10, 20, 50, 10}
                    , new String[]{"员工姓名", "打卡日期", "需求编码", "需求名称", "分配工时"},
                    allotInfoList,
                    response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @DeleteMapping("/")
    public ServerResponse batchDelete(@RequestParam("ids[]") Long[] ids) {
        log.info("需要删除的打卡记录ID： " + Arrays.toString(ids));
        ServerResponse serverResponse;
        try {
            allotInfoService.deleteBatch(Arrays.asList(ids));
            serverResponse = ServerResponse.createBySuccessMessage("删除分配记录成功");
        } catch (Exception e) {
            log.error("删除打卡记录失败\n", e);
            serverResponse = ServerResponse.createByErrorMessage("删除分配记录失败\n " + e.getMessage());
        }
        return serverResponse;
    }


}
