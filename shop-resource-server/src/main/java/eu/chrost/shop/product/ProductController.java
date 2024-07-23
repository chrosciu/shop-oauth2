package eu.chrost.shop.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable long id) {
        return productService.getProduct(id);
    }
}
