package example.agora.recording;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
public class AgoraRecordingApplication {

    @Value("${agora.customer.id}")
    private String agoraCustomerId;

    @Value("${agora.customer.certificate}")
    private String agoraCustomerCertificate;

    public static void main(String[] args) {
        SpringApplication.run(AgoraRecordingApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate =restTemplateBuilder.build();

        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor(){
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                // Fill in the Customer ID and Customer Certificate to obtain the plainCredentials value.
                String plainCredentials = agoraCustomerId + ":"+ agoraCustomerCertificate;
                // Fill in the plainCredentials (the base64 encoding of the credential) to obtain the base64Credentials value which is the Authorization parameter you want.
                String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
                request.getHeaders().set("Authorization", "Basic " + base64Credentials);
                return execution.execute(request, body);
            }
        });
        return restTemplate;
    }
}
