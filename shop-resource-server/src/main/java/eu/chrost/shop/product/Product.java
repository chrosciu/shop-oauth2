package eu.chrost.shop.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
class Product {
    @Id
    private Long id;
    private String name;
    private String owner;
}
