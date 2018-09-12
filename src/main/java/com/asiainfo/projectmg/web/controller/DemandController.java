package com.asiainfo.projectmg.web.controller;

import com.asiainfo.projectmg.common.ServerResponse;
import com.asiainfo.projectmg.model.Demand;
import com.asiainfo.projectmg.service.DemandService;
import com.asiainfo.projectmg.util.DownloadTemplateUtil;
import com.asiainfo.projectmg.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    @RequestMapping("/")
    public ModelAndView page() {
        return new ModelAndView("demand");
    }

    @RequestMapping("/list")
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
        String path = "excel" + File.separator + "demand.xlsx";
        DownloadTemplateUtil.downloadTeplate(response, path);
    }
}
