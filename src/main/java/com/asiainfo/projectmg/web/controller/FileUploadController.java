package com.asiainfo.projectmg.web.controller;

import com.asiainfo.projectmg.model.Demand;
import com.asiainfo.projectmg.service.DemandService;
import com.asiainfo.projectmg.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private DemandService demandService;




}
