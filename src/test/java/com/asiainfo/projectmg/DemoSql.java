package com.asiainfo.projectmg;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/5
 * Time: 下午4:30
 * Description: No Description
 */
public class DemoSql {

    @Test
    public void sql1() {
        try {
            InputStream inputStream = DemoSql.class.getClassLoader().getResourceAsStream("group_user.txt");
            System.out.println(inputStream.available());
            BufferedInputStream buf = new BufferedInputStream(inputStream);
            InputStreamReader isr = new InputStreamReader(buf);
            BufferedReader bfr = new BufferedReader(isr);
            String line;
            List<String> users = new ArrayList<>();
            while ((line = bfr.readLine()) != null) {
                String[] datas = line.split("\t");
                //System.out.println(Arrays.toString(datas));
                users.add(datas[0].trim());
            }
            //System.out.println(users);

            int i =0;
            for (String user: users){

                //System.out.println("insert into dacp_user_role_map values('6','"+ user+"');");
                System.out.println("insert into `dacp_meta_team_member` (`member_id`, `member_name`, `descr`, `team_name`, `tenant_name`, `user_id`, `create_date`, `is_default`, `role_name`, `state`) values('SDCMTM180626000099"+(i++)+"','T00021-"+user+"',NULL,'T00021',NULL,'"+user+"',NULL,NULL,NULL,NULL);");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
