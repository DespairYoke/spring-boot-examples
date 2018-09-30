package com.example.zwd.propertyexample;

import com.example.zwd.propertyexample.properties.UserProperties;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PropertyExampleApplicationTests {

	private static final Log log = LogFactory.getLog(PropertyExampleApplicationTests.class);
	@Autowired
	private UserProperties userProperties;

	@Test
	public void contextLoads() {

		log.info("name: "+userProperties.getUsername());
		log.info("password: "+userProperties.getPassword());
		log.info("content: "+userProperties.getContent());
		log.info("随机字符串："+userProperties.getIsString());
		log.info("随机int: "+userProperties.getNumber());


	}

}
