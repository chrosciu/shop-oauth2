package eu.chrost.shop.product;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
class ProductServiceTest {
    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private ProductService productService;

    @Test
    @WithAnonymousUser
    void givenRequestIsAnonymous_whenGetAnyProduct_thenUnauthorized() {
        assertThatExceptionOfType(AccessDeniedException.class)
                .isThrownBy(() -> productService.getProduct(1L));
    }

    @Test
    @WithMockUser("shop-admin-client")
    void givenRequestIsAnonymous_whenGetNotOwnProduct_thenUnauthorized() {
        assertThatExceptionOfType(AccessDeniedException.class)
                .isThrownBy(() -> productService.getProduct(1L));
    }

    @Test
    @WithMockUser("shop-client")
    void givenRequestIsAnonymous_whenGetOwnProduct_thenOk() {
        assertThat(productService.getProduct(1L)).extracting(Product::getId).isEqualTo(1L);
    }
}
