package com.example.cochrane_jahquan_williams.demo.cjw;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;


import javax.net.ssl.HostnameVerifier;
import java.awt.*;
import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class ApacheHTTP_Utilization {

    private static HttpResponse response;
    private static String baseU = "www.google.com" /* base url here, modify this value */;
    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";

    // ----
    public static void getPull(String endpoint, CloseableHttpClient closeableHttpClient) {
        try {
            System.out.println(baseU + endpoint);

            URI uri = new URIBuilder(baseU + endpoint).build();
            HttpGet httpGet = new HttpGet(uri);

            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);

            if (httpResponse != null) {
                response = httpResponse;
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    // ----  be sure to call this after the majority of processing is done
    public static HttpResponse pullResponse() {
        return response;
    }

    // ----  The usual default closeable client create
    public static CloseableHttpClient getDefaultClient() {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        return closeableHttpClient;
    }

    public static String dynamicHttpGet(String dynURL) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(dynURL);
        //request.addHeader("User-Agent", userAgent);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
        //System.out.println(content);
        return content;
    }

    // ----- Standard HttpClient with User Agent occurrence
    public static String testUserAgentHttpCLient() throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(baseU+/*Url extension for internal HTML links: */ "");
        String content = "";
        request.addHeader("User-Agent", userAgent);
        CloseableHttpResponse response = client.execute(request);
        try {
            HttpEntity entity = response.getEntity();
            content = EntityUtils.toString(entity);
            //System.out.println("User-Agent fix: " + content);
        } finally {
            response.close();
        }


        return content;

    }

    public static CloseableHttpClient getSSLCustomClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setSSLSocketFactory(getSSLContext());
        CloseableHttpClient client = clientBuilder.build();

        return client;
    }


    public static CloseableHttpClient getConCurrClient(int threadPoolCount) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        HttpClientBuilder clientBuilder = HttpClients.custom();
        connectionManager.setMaxTotal(threadPoolCount);
        clientBuilder.setConnectionManager(connectionManager);
        CloseableHttpClient client = clientBuilder.build();

        return client;
    }

    public static SSLConnectionSocketFactory getSSLContext() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustStrategy trustStrategy = new TrustStrategy() {
            @Override // auto gen
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                return false;
            }
        };


        HostnameVerifier allVerifier = new NoopHostnameVerifier();
        SSLConnectionSocketFactory connectionSocketFactory = null;

        try {
            connectionSocketFactory = new SSLConnectionSocketFactory(SSLContextBuilder.create().loadTrustMaterial(trustStrategy).build(), allVerifier);
        } catch (KeyManagementException exception) {
            exception.printStackTrace();
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        } catch (KeyStoreException exception) {
            exception.printStackTrace();
        }

        return connectionSocketFactory;

    }

}
