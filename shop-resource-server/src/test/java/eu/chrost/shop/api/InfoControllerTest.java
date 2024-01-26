package eu.chrost.shop.api;

import eu.chrost.shop.config.KeycloakRoleConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class InfoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void givenRequestIsAnonymous_whenGetPublicInfo_thenOk() throws Exception {
        mockMvc.perform(get("/api/info/public"))
                .andExpect(status().isOk());
    }

    @Test
    void givenRequestIsAnonymous_whenGetProtectedInfo_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/info/protected"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenRequestIsAuthenticated_whenGetProtectedInfo_thenOk() throws Exception {
        mockMvc.perform(get("/api/info/protected").with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void givenRequestIsAnonymous_whenGetAdminInfo_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/info/admin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenRequestIsAuthenticated_whenGetAdminInfo_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/info/admin").with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenRequestHasAdminRole_whenGetAdminInfo_thenOk() throws Exception {
        var roles = Collections.singletonList("admin");
        var realmAccess = Collections.singletonMap("roles", roles);
        mockMvc.perform(get("/api/info/admin").with(jwt()
                        .jwt(jwt -> jwt.claim("realm_access", realmAccess))
                        .authorities(new KeycloakRoleConverter())))
                .andExpect(status().isOk());
    }
}
