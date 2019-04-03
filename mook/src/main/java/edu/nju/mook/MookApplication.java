package edu.nju.mook;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.nju.mook.app.dao")
public class MookApplication {

	public static void main(String[] args) {
		SpringApplication.run(MookApplication.class, args);
	}

}
