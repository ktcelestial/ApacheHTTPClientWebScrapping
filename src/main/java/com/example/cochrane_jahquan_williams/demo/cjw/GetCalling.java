package com.example.cochrane_jahquan_williams.demo.cjw;

import org.apache.http.HttpResponse;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
import java.lang.*;

// for testing purposes
public class GetCalling {

    public void GetCall() throws IOException, URISyntaxException, InterruptedException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String endpoint = "cdsr/reviews/topics";
        ApacheHTTP_Utilization.getPull(endpoint, ApacheHTTP_Utilization.getSSLCustomClient());
        HttpResponse httpResponse = ApacheHTTP_Utilization.pullResponse();

        ApacheHTTP_Utilization.testUserAgentHttpCLient();

        System.out.println(httpResponse.getStatusLine().getStatusCode());
        //output formatting
        Scanner readContext = new Scanner(httpResponse.getEntity().getContent());
        while (readContext.hasNext()) {
            System.out.print(readContext.next());
        }

        return;
    }
}
