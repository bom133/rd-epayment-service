package th.go.rd.rdepaymentservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

// @RunWith(SpringRunner.class)
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorizationControllerTest{

    private static Logger logger = LoggerFactory.getLogger(AuthorizationControllerTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    // @Test
    public void testList(){
        logger.info(" ================= Test Get Authorization List ================= ");
        String url = "http://localhost:" + port + "/epay/api-auth/lists/";
        logger.info("URL: " + url);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Accept", "application/json");
        headers.add("Accept-Language", "en");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers),
                String.class);
                logger.info(response.toString());
        String res = response.getBody();
        logger.info(" ================= RESPONSE ================= ");
        logger.info(res);
        assertThat(null != res);
        logger.info(" ================= End Get Authorization List ================= ");
    }

    // @Test
    public void testGet(){
        logger.info(" ================= Test Get Authorization Get ================= ");
        String url = "http://localhost:" + port + "/epay/api-auth/";
        String id = "1";
        url = url + id;
        logger.info("URL: " + url);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Accept", "application/json");
        headers.add("Accept-Language", "en");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers),
                String.class);
                logger.info(response.toString());
        String res = response.getBody();
        logger.info(" ================= RESPONSE ================= ");
        logger.info(res);
        assertThat(null != res);
        logger.info(" ================= End Get Authorization Get ================= ");
    }

    @Test
    public void testSearch(){

    }

    @Test
    public void testDelete(){

    }

    @Test
    public void testCreate(){
        
    }
}