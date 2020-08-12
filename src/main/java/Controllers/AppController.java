package Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class AppController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("class", "index");
        return "react/view";
    }

    @RequestMapping(value = "/inventories", method = RequestMethod.GET)
    public String inventory(Model model) {
        model.addAttribute("class", "inventory");
        return "react/view";
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String items(Model model) {
        model.addAttribute("class", "items");
        return "react/view";
    }

    @RequestMapping(value = "/attributes", method = RequestMethod.GET)
    public String attributes(Model model) {
        model.addAttribute("class", "attributes");
        return "react/view";
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public String customers(Model model) {
        model.addAttribute("class", "customers");
        return "react/view";
    }

    @RequestMapping(value = "/vendors", method = RequestMethod.GET)
    public String vendors(Model model) {
        model.addAttribute("class", "vendors");
        return "react/view";
    }

    @RequestMapping(value = "/purchases", method = RequestMethod.GET)
    public String purchases(Model model) {
        model.addAttribute("class", "purchases");
        return "react/view";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String orders(Model model) {
        model.addAttribute("class", "orders");
        return "react/view";
    }

    @RequestMapping(value = "/tracking", method = RequestMethod.GET)
    public String tracking(Model model) {
        model.addAttribute("class", "tracking");
        return "react/view";
    }

    @RequestMapping(value = "/forbidden", method = RequestMethod.GET)
    public String forbidden(Model model) {
        return "forbidden";
    }

    @RequestMapping(value = "/notfound", method = RequestMethod.GET)
    public String notFound(Model model) {
        return "notfound";
    }


    /*
     * we can create a variable with name of `user` and bind value to a named model attribute and then exposes it to a web view
     * we can access ${user} in home or other pages
     * */
    @ModelAttribute("username")
    public String homepage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ;
        //System.out.println(authentication.getName());
        return authentication.getName();
    }
}
