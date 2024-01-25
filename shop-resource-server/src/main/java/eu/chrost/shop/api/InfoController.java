package eu.chrost.shop.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/info")
public class InfoController {
    @GetMapping
    public String getInfo() {
        return "Example Shop v 1.0";
    }
}
