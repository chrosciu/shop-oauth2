package eu.chrost.shop.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty("product.scheduler.enabled")
@RequiredArgsConstructor
@Slf4j
class ProductScheduler {
    private final ProductService productService;

    @Scheduled(fixedRate = 5000)
    public void displayAllProducts() {
        User user = new User("shop-admin-client", "",
                List.<GrantedAuthority>of(new SimpleGrantedAuthority("ROLE_admin")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            log.info("All products: {}", productService.getProducts());
        } finally {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }
}
