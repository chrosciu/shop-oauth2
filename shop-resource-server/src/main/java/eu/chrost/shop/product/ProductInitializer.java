package eu.chrost.shop.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
class ProductInitializer implements CommandLineRunner {
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        var productOne = Product.builder().id(1L).name("Product One").owner("shop-client").build();
        var productTwo = Product.builder().id(2L).name("Product One").owner("shop-admin").build();
        var productThree = Product.builder().id(3L).name("Product One").owner("marcin").build();
        var productFour = Product.builder().id(4L).name("Product One").owner("tomasz").build();
        productRepository.saveAll(List.of(productOne, productTwo, productThree, productFour));
        log.info("Products initialized");
    }
}
