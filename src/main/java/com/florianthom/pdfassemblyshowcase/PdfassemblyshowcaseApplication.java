package com.florianthom.pdfassemblyshowcase;

import com.florianthom.pdfassemblyshowcase.properties.GotenbergProperties;
import com.florianthom.pdfassemblyshowcase.properties.SigningProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({SigningProperties.class, GotenbergProperties.class})
public class PdfassemblyshowcaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfassemblyshowcaseApplication.class, args);
	}

}
