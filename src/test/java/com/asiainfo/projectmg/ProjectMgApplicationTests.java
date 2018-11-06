package com.asiainfo.projectmg;

import com.asiainfo.projectmg.model.AllotInfo;
import com.asiainfo.projectmg.repository.AllotInfoRepository;
import com.asiainfo.projectmg.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectMgApplicationTests {


	@Autowired
	private AllotInfoRepository allotInfoRepository;

	@Test
	public void contextLoads() {
		AllotInfo allotInfo = allotInfoRepository.getByAndDemandCodeAndDateAndAndUserName("JYKF20180524002", DateUtil.getDateFromFormat("2018-11-05"),"wx");
		System.out.println(allotInfo);
	}

}
