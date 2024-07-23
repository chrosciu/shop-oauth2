package eu.chrost.shop.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class ProductService {
    private final ProductRepository productRepository;

    @PostAuthorize("returnObject.owner == authentication.name")
    public Product getProduct(long id) {
        return productRepository.findById(id).get();
    }
}
