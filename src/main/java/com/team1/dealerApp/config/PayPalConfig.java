package com.team1.dealerApp.config;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {

	@Value("${paypal.clientId}")
	private String userId;

	@Value("${paypal.secret}" )
	private String secret;

	@Value("${paypal.mode}")
	private String mode;

	@Bean
	public APIContext apiContext(){
		return new APIContext(userId, secret, mode);
	}
}
