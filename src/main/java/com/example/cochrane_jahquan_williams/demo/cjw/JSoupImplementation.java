package com.example.cochrane_jahquan_williams.demo.cjw;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import java.io.File;
import java.io.IOException;

public class JSoupImplementation {

    private List<String> list = new ArrayList<String>();
    private List<String> topicPageLinks = new ArrayList<>();
    private Document jDoc;
    private String jDocString;
    public FileWriter writeToFileOutput;
    private final String filename_export = "filename.txt"; // this will not be changed
    private String[] reviewProfile = new String[5];


    public JSoupImplementation() throws IOException {
        System.out.println("Constructor initialized");

        try {
            // Apache
            jDocString = ApacheHTTP_Utilization.testUserAgentHttpCLient();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Jsoup utilization here
        jDoc = Jsoup.parse(jDocString);
        Elements links = jDoc.select("a[href]");
        for (Element link : links) {
            if (link.attr("abs:href").contains(/* paste link URL here */ "https://www.google.com/"))
            {

                list.add(link.attr("abs:href"));
            };
        }
        return;
    }

    public void initTextFile() throws IOException {
        writeToFileOutput = new FileWriter(filename_export, false); // overwrite the file, do not append
        System.out.println("'filename_export' was created and written to successfully. '...\\demo.cjw\\filename.txt'");

    }

    public void ensureFileClose() throws IOException {
        writeToFileOutput.close();
        System.out.println("'filename_export' was closed successfully.");
    }

    // must be executed after "findRelevantTags()" method, try/catch may be needed
    public void findTopicNumberOfPages() {
        topicPageLinks.clear(); //reset for the next set of links
        Elements pageItems = jDoc.getElementsByClass("pagination-page-list-item");
        for (Element page : pageItems) {
            Elements links = page.select("a");
            String href = links.attr("href");
            topicPageLinks.add(href);
        }

        for (String link : topicPageLinks) {
            System.out.println("Page # of Topic: " + link);
        }
    }

    public void outputLinks() {
        for (String link : list) {
            System.out.println(link);
        }
    }

    public void eachLinkAnalysis() throws IOException, URISyntaxException {
        System.out.println("eachLinkAnalysis()");
        int link_count = 1;
        String html = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            for (String link : list) {
                System.out.println("Visiting: " + link);
                HttpPost post = new HttpPost(link);

                // browser simulation
                post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
                post.setHeader("Content-Type", "application/x-www-form-urlencoded");

                // setEntity? May not be necessary
                try (CloseableHttpResponse response = httpClient.execute(post)) {
                    CloseableHttpResponse responses;
                    HttpPost tpPostRequest;
                    html = EntityUtils.toString(response.getEntity());

                    if (html != null) {
                        // jDoc set to start off the program, first post request
                        jDoc = Jsoup.parse(html);

                        findTopicNumberOfPages();

                        for (String tpLink : topicPageLinks) {
                            System.out.println("TP LINK: " + tpLink);
                            tpPostRequest = new HttpPost(tpLink);
                            tpPostRequest.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
                            tpPostRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
                            responses = httpClient.execute(tpPostRequest);
                            html = EntityUtils.toString(responses.getEntity());
                            jDoc = Jsoup.parse(html);

                            perReview();
                        }
                    }
                } finally {

                }
            }
        }
    }

    public void perReview() throws IOException {

        // Currently going through every (37) topic
        // Allergy & Intolerance has 3 pages of reviews currently
        Elements resultBody = jDoc.getElementsByClass("search-results-item-body");
        for (Element review : resultBody) {
            updateReviewModelURL(fetchReqURLfromDoc(review));
            updateReviewModelTopicName(fetchTopicfromDoc());
            updateReviewModelTitle(fetchTitlefromDoc(review));
            updateReviewModelAuthor(fetchAuthorsfromDoc(review));
            updateReviewModelDate(fetchDatefromDoc(review));

            // for system (testing) output:
            System.out.println(reviewProfile[0] + " | "
                    + reviewProfile[1] + " | "
                    + reviewProfile[2] + " | "
                    + reviewProfile[3] + " | "
                    + reviewProfile[4] + '\n');

            // for file output (equivalent)
            writeToFileOutput.write(reviewProfile[0] + " | "
                    + reviewProfile[1] + " | "
                    + reviewProfile[2] + " | "
                    + reviewProfile[3] + " | "
                    + reviewProfile[4] + "\n\n");
        }
    }

    public String fetchReqURLfromDoc(Element currReview) {
        Elements aElements = currReview.select("a");
        String href = aElements.attr("href");
        return /* paste link to fetch here */"https://www.google.com" + href;
    }

    public String fetchTopicfromDoc() {
        Elements spansTopic = jDoc.select("span" /*add .id to fetch specific data*/);

        return spansTopic.text();
    }

    public String fetchTitlefromDoc(Element currReview) {
        Elements h3Title = currReview.select("h3"/*add .id to fetch specific data*/);
        return h3Title.text();
    }

    public String fetchAuthorsfromDoc(Element currReview) {
        Elements divAuthorElements = currReview.select("div" );
        Element divAuthorElement = null;
        if (divAuthorElements != null) {
            divAuthorElement = divAuthorElements.first().selectFirst("div"/*add .id to fetch specific data*/);
        }
        return divAuthorElement.text();
    }
    public String fetchDatefromDoc(Element currReview) {
        Elements divDate = currReview.select("div" /*add .id to fetch specific data*/);

        return divDate.text();
    }


    // Review Model. Using one array and updating the values per reviews, saving space
    // Check: if empty list index is empty, .add, if not, = to update
    private void updateReviewModelURL(String URL) {
        try {
            reviewProfile[0] = URL;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void updateReviewModelTopicName(String topic) {
        try {
            reviewProfile[1] = topic;
        } catch (Exception exception) {
            System.out.println("Topic is being added to the list: " + topic + " --" + exception.getMessage());
        }
    }

    private void updateReviewModelTitle(String revTitle) {
        try {
            reviewProfile[2] = revTitle;
        } catch (Exception exception) {
            System.out.println("Title is being added to the list: " + revTitle + " --" + exception.getMessage());
        }
    }

    private void updateReviewModelAuthor(String authors) {
        try {
            reviewProfile[3] = authors;
        } catch (Exception exception) {
            System.out.println("Author(s) is being added to the list: " + authors + " --" + exception.getMessage());
        }
    }

    private void updateReviewModelDate(String date) {
        try {
            reviewProfile[4] = date;
        } catch (Exception exception) {
            System.out.println("Date is being added to the list: " + date + " --" + exception.getMessage());
        }
    }



}
