package th.go.rd.rdepaymentservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import com.jayway.jsonpath.JsonPath;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

// @RunWith(SpringRunner.class)
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaxPayerControllerTest {
    private static Logger logger = LoggerFactory.getLogger(TaxPayerControllerTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;


    @Autowired
    //@Test
    public void testDisplayPaymentResult() throws Exception {
        logger.info(" ================= Test Display Payment Result ================= ");
        String paymentId = "ref01";
        String uuid = "640449af-46e8-43fc-9e2a-957ea6b38aef";
        String url = "http://localhost:" + port + "/epay/taxpayer/display-payment-result/";
        String parameter = paymentId + "/" + uuid;
        url = url + parameter;
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
        logger.info(" ================= JSON PATH ================= ");
        logger.info(JsonPath.read(res, "$.taxInfo.agentId"));
        logger.info(JsonPath.read(res, "$.taxInfo.refNo"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(null != res);
        assertThat(paymentId.equals(JsonPath.read(res, "$.taxInfo.refNo")));
        logger.info(" ================= End Test Display Payment Result ================= ");
    }

    // @Test
    public void testDisplaypaymentlinechannel() throws Exception {
        logger.info(" ================= Test Display Payment Line Channel ================= ");
        String uuid = "745dfa00-aed7-4d85-8f03-363e789ae530";
        String paylineCode = "ATM";
        String url = "http://localhost:" + port + "/epay/taxpayer/display-payment-line-channel/";
        String parameter = paylineCode + "/" + uuid;
        url = url + parameter;
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
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(null != res);
        // assertThat(paylineCode.equals(JsonPath.read(res, "$.payLineCode")));
        logger.info(" ================= End Test Display Line Channel ================= ");
    }
}
