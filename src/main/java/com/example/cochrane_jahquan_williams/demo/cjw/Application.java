package com.example.cochrane_jahquan_williams.demo.cjw;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.DocFlavor;
import javax.print.attribute.standard.PresentationDirection;
import java.awt.image.TileObserver;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.*;

@SpringBootApplication (scanBasePackages = "com.example")
public class Application {

	// Apache HTTPClient Version 4.5+

	public static void main(String[] args) throws IOException, InterruptedException, Exception {
		SpringApplication.run(Application.class, args);

		// Apache Http Client -> Jsoup parsing
		JSoupImplementation jsoupIMPL = new JSoupImplementation();
		jsoupIMPL.outputLinks();


		// jsoupIMPL.initTextFile(); // Creates a text file for output
		jsoupIMPL.eachLinkAnalysis();
		// jsoupIMPL.ensureFileClose(); // Closes file stream after processing is finished

	}



}
