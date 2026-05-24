package com.scm;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.scm.services.EmailService;

@SpringBootTest
class ApplicationTests {

	void contextLoads() {
	}

	@Autowired
	private EmailService service;

	@Test
	void sendEmailTest() {
		service.sendEmail("26asmi2004@gmail.com", "Just testing email service", "THIS IS JUST TESTING");
		//                 ☝️ put a real email address here
	}
}