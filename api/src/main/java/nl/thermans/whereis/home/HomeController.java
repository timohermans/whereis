package nl.thermans.whereis.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    @ModelAttribute("timo")
    public String timo() {
        return "duurt heel lang...";
    }

    @GetMapping("/")
    public String handle(Model model) {
        model.addAttribute("message", "Hello, Timo");
        return "index";
    }
}
