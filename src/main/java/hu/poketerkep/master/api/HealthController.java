package hu.poketerkep.master.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HealthController {

    @RequestMapping("/health")
    public String health() {
        return "OK";
    }
}
