package eu.chrost.shop.product;

import eu.chrost.shop.config.ChainedPermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
class ProductPermissionEvaluator implements ChainedPermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        Product product = (Product) targetDomainObject;
        return authentication.getName().equals(product.getOwner());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new IllegalStateException("Should not be called");
    }

    @Override
    public boolean supports(Object targetDomainObject) {
        return targetDomainObject instanceof Product;
    }

    @Override
    public boolean supports(String targetType) {
        //currently we do not support fetching products to verify permissions
        return false;
    }
}
