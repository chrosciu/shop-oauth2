package eu.chrost.shop.info;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
class InfoService {
    @PreAuthorize("isAuthenticated()")
    public String getProtectedMethodInfo() {
        return "Protected method info";
    }
}
