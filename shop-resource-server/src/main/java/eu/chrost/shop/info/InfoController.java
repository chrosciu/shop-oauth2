package eu.chrost.shop.info;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/info")
@RequiredArgsConstructor
public class InfoController {
    private final InfoService infoService;

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

    @GetMapping("/protected-method")
    public String getProtectedMethodInfo() {
        return infoService.getProtectedMethodInfo();
    }
}
