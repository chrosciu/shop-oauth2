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

    @GetMapping("/protected")
    public String getProtectedInfo() {
        return "Protected info";
    }

    @GetMapping("/admin")
    public String getAdminInfo() {
        return "Admin info";
    }
}
