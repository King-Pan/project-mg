package com.asiainfo.projectmg.util;

import com.asiainfo.projectmg.web.controller.DemandController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/12
 * Time: 下午4:36
 * Description: No Description
 */
public class DownloadTemplateUtil {

    public static synchronized void downloadTeplate(final HttpServletResponse response,String path,String name){
        try {
            String fileName = name + System.currentTimeMillis() + ".xlsx";
            InputStream inputStream = DemandController.class.getClassLoader().getResourceAsStream(path);
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.addHeader("Content-Length", "" + inputStream.available());
            response.setContentType("application/octet-stream;charset=UTF-8");
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] datas = new byte[inputStream.available()];
            inputStream.read(datas);
            outputStream.write(datas);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
