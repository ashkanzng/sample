package Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.model.IModel;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;

@Controller
public class UserController {

    /**
     * Create Custom Login
     * @return Thymeleaf login.html
     */
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(HttpSession session) {
        return "login";
    }

    @RequestMapping(value = "/robots.txt")
    public RedirectView robotsRedirectView () {
        return new RedirectView("home");
    }

    @RequestMapping(value = "/users",method = RequestMethod.GET)
    public String setting(@ModelAttribute("username") String username, Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username",authentication.getName());
        return "react/view";
    }
}
