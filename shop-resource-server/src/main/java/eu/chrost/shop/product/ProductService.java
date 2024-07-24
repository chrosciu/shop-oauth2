package eu.chrost.shop.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
class ProductService {
    private final ProductRepository productRepository;

    //@PostAuthorize("hasRole('admin') || returnObject.owner == authentication.name")
    @PostAuthorize("hasRole('admin') || hasPermission(returnObject, 'read')")
    public Product getProduct(long id) {
        return productRepository.findById(id).get();
    }

    //@PostFilter("hasRole('admin') || filterObject.owner == authentication.name")
    @PostFilter("hasRole('admin') || hasPermission(filterObject, 'read')")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
