package eu.chrost.shop.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/info")
public class InfoController {
    @GetMapping("/public")
    public String getPublicInfo() {
        return "Public info";
    }

    @GetMapping("/authorized")
    public String getAuthorizedUserInfo() {
        return "Authorized user info";
    }

    @GetMapping("/admin")
    public String getAdminInfo() {
        return "Admin info";
    }
}
