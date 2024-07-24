package eu.chrost.shop.config;

import org.springframework.security.access.PermissionEvaluator;

public interface ChainedPermissionEvaluator extends PermissionEvaluator {

    boolean supports(Object targetDomainObject);

    boolean supports(String targetType);
}
