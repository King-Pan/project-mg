package com.asiainfo.projectmg.web.controller;

import com.asiainfo.projectmg.common.ServerResponse;
import com.asiainfo.projectmg.model.CardInfo;
import com.asiainfo.projectmg.service.CardService;
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
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/12
 * Time: 下午2:23
 * Description: No Description
 */
@Slf4j
@RequestMapping("/card")
@RestController
public class CardController {


    @Autowired
    private CardService cardService;

    @RequestMapping("/")
    public ModelAndView page() {
        return new ModelAndView("card");
    }

    @RequestMapping("/list")
    public Object list(CardInfo cardInfo, @PageableDefault Pageable pageable) {
        return cardService.getList(cardInfo, pageable);
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
            log.info("上传打卡信息文件名:{},大小:{}", fileName, size);
            try {
                List<CardInfo> list = ExcelUtil.readExcel(file, CardInfo.class);
                cardService.saveList(list);
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
        String path = "excel" + File.separator + "card.xlsx";
        DownloadTemplateUtil.downloadTeplate(response, path);
    }

}
