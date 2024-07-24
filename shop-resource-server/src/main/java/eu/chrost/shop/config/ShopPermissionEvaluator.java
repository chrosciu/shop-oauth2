package eu.chrost.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
class ShopPermissionEvaluator implements PermissionEvaluator {
    private final List<ChainedPermissionEvaluator> permissionEvaluators;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        for (ChainedPermissionEvaluator permissionEvaluator : permissionEvaluators) {
            if (permissionEvaluator.supports(targetDomainObject)) {
                return permissionEvaluator.hasPermission(authentication, targetDomainObject, permission);
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        for (ChainedPermissionEvaluator permissionEvaluator : permissionEvaluators) {
            if (permissionEvaluator.supports(targetType)) {
                return permissionEvaluator.hasPermission(authentication, targetId, targetType, permission);
            }
        }
        return false;
    }
}
