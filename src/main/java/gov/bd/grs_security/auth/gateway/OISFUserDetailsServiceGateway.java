package gov.bd.grs_security.auth.gateway;

import gov.bd.grs_security.auth.model.User;
import gov.bd.grs_security.auth.payload.UserInformation;
import gov.bd.grs_security.auth.payload.doptor.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OISFUserDetailsServiceGateway {

    public UserInformation getUserInformation(UserInfo userInfo) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8088/grs_server/api/oisfuser/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserInfo> requestEntity = new HttpEntity<>(userInfo, headers);

        ResponseEntity<UserInformation> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                UserInformation.class
        );
        return responseEntity.getBody();
    }

    public UserInformation getUserInformationFromUser(User user) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8088/grs_server/api/oisfuser/getUserInfoFromUser";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<UserInformation> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                UserInformation.class
        );
        return responseEntity.getBody();
    }
}
