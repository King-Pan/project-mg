package com.asiainfo.projectmg.web.controller;

import com.asiainfo.projectmg.common.ServerResponse;
import com.asiainfo.projectmg.model.Demand;
import com.asiainfo.projectmg.model.form.AllotForm;
import com.asiainfo.projectmg.service.DemandService;
import com.asiainfo.projectmg.util.DownloadTemplateUtil;
import com.asiainfo.projectmg.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/5
 * Time: 上午10:18
 * Description: No Description
 */
@Slf4j
@RestController
@RequestMapping("/demand")
public class DemandController {

    @Autowired
    private DemandService demandService;

    @GetMapping("/")
    public ModelAndView page() {
        return new ModelAndView("demand");
    }

    @GetMapping("/list")
    public Object list(Demand demand, @PageableDefault Pageable pageable) {
        return demandService.getList(demand, pageable);
    }

    /**
     * 实现文件上传
     */
    @RequestMapping("fileUpload")
    @ResponseBody
    public ServerResponse fileUpload(HttpServletRequest request, @RequestParam("fileName") MultipartFile file) {
        ServerResponse serverResponse;
        if (file.isEmpty()) {
            serverResponse = ServerResponse.createByErrorMessage("上传失败，文件为空");
        } else {
            String fileName = file.getOriginalFilename();
            int size = (int) file.getSize();
            log.info("文件名:{},大小:{}", fileName, size);
            try {
                List<Demand> list = ExcelUtil.readExcel(file, Demand.class);
                demandService.saveList(list);
                serverResponse = ServerResponse.createBySuccessMessage("上传成功");
            } catch (IllegalStateException e) {
                log.error(e.getMessage(), e);
                serverResponse = ServerResponse.createByErrorMessage("上传失败: " + e.getMessage());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                serverResponse = ServerResponse.createByErrorMessage("上传失败: " + e.getMessage());
            }
        }
        return serverResponse;
    }

    @RequestMapping("/downloadExcelTemplate")
    public void downloadExcelTemplate(final HttpServletResponse response) {
        String path = "excel/demand.xlsx";
        DownloadTemplateUtil.downloadTeplate(response, path, "demand_info_");
    }


    @RequestMapping("/allotHours")
    public ServerResponse allotHours(AllotForm allotForm, @RequestParam("ids[]") Long[] ids) {
        ServerResponse serverResponse;
        try {
            allotForm.setCardIds(Arrays.asList(ids));
            demandService.allotHours(allotForm);
            serverResponse = ServerResponse.createBySuccessMessage("分配成功");
        } catch (Exception e) {
            e.printStackTrace();
            serverResponse = ServerResponse.createByErrorMessage("分配失败:\n" + e.getMessage());
        }
        return serverResponse;
    }

    @RequestMapping("/save")
    public ServerResponse update(Demand demand) {
        ServerResponse serverResponse;
        String msg = "";
        log.info("修改参数: {}", demand);
        try {
            if (demand.getId() != null) {
                msg = "修改需求";
            } else {
                msg = "新增需求";
            }
            demandService.save(demand);
            serverResponse = ServerResponse.createBySuccessMessage(msg + "成功");
        } catch (Exception e) {
            log.error(msg + "失败:" + e.getMessage(), e);
            serverResponse = ServerResponse.createByErrorMessage(msg + "失败:\n" + e.getMessage());
        }
        return serverResponse;
    }


    @DeleteMapping("/")
    public ServerResponse batchDelete(@RequestParam("ids[]") Long[] ids) {
        log.info("需要删除的需求ID： " + Arrays.toString(ids));
        ServerResponse serverResponse;
        try {
            demandService.deleteBatch(Arrays.asList(ids));
            serverResponse = ServerResponse.createBySuccessMessage("删除需求成功");
        } catch (Exception e) {
            log.error("删除需求失败\n", e);
            serverResponse = ServerResponse.createByErrorMessage("删除需求失败\n " + e.getMessage());
        }
        return serverResponse;
    }
}
