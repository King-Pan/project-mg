package com.asiainfo.projectmg.util;

import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/12
 * Time: 下午4:36
 * Description: No Description
 */
public class DownloadTemplateUtil {

    public static synchronized void downloadTeplate(final HttpServletResponse response, String path, String name) {
        try {
            String fileName = name + System.currentTimeMillis() + ".xlsx";
            InputStream stream = DownloadTemplateUtil.class.getClassLoader().getResourceAsStream(path);
            File targetFile = new File(fileName);
            FileUtils.copyInputStreamToFile(stream, targetFile);

            InputStream is = new FileInputStream(targetFile);

            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.addHeader("Content-Length", "" + is.available());
            response.setContentType("application/octet-stream;charset=UTF-8");
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] datas = new byte[is.available()];
            is.read(datas);
            outputStream.write(datas);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
