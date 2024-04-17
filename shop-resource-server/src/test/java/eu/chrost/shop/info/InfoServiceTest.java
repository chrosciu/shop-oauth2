package eu.chrost.shop.info;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class InfoServiceTest {
    //required if tests are run not with disabled web layer - WebEnvironment.NONE
    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private InfoService infoService;

    @Test
    @WithAnonymousUser
    void givenRequestIsAnonymous_whenGetProtectedMethodInfo_thenUnauthorized() {
        assertThatExceptionOfType(AccessDeniedException.class)
                .isThrownBy(() -> infoService.getProtectedMethodInfo());
    }

    @Test
    @WithMockUser
    void givenRequestIsAuthenticated_whenGetProtectedMethodInfo_thenOk() throws Exception {
        assertThat(infoService.getProtectedMethodInfo()).isEqualTo("Protected method info");
    }

}
