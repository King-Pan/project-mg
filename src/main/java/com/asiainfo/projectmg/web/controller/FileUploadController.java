package com.asiainfo.projectmg.web.controller;

import com.asiainfo.projectmg.model.Demand;
import com.asiainfo.projectmg.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/4
 * Time: 下午4:48
 * Description: No Description
 */
@Slf4j
@Controller
public class FileUploadController {


    @Value("${uploadPath}")
    private String uploadPath;

    /**
     * 实现文件上传
     */
    @RequestMapping("fileUpload")
    @ResponseBody
    public Object fileUpload(HttpServletRequest request, @RequestParam("fileName") MultipartFile file) {
        if (file.isEmpty()) {
            return "false";
        }
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);
        File dest = new File(uploadPath + File.separator + fileName);
        log.info("上文文件路径:{},大小{}", dest.getAbsolutePath(), size);
        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            //保存文件
           // file.transferTo(dest);
            List<Demand> list = ExcelUtil.readExcel(file, Demand.class);
            return list;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            e.printStackTrace();
            return "false";
        }
    }
}
