package com.springboot.example.restful;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;


@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

		// 	Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
        //         "", 8, 185000, SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        // );
		// Map<String, PasswordEncoder> encoder = new HashedMap();

		// encoder.put("pbkdf2", pbkdf2PasswordEncoder);
		// DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoder);
		// passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);

		// System.out.println(passwordEncoder.encode("admin123"));
		// System.out.println(passwordEncoder.encode("admin1234"));
	}

}
