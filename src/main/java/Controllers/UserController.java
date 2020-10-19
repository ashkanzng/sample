package Controllers;

import Services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {


    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * Create Custom Login
     *
     * @return Thymeleaf login.html
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpSession session) {
        DefaultSavedRequest request = (DefaultSavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        //if (request != null)
        //    System.out.println( "User Controller --> Log" + request.getRequestURI());
        return "login";
    }


    /**
     * User sign-up
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }


    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String setting(@ModelAttribute("username") String username, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String[] roles = {null};
        authentication.getAuthorities().forEach(usrRole -> {
            roles[0] = usrRole.toString();
        });
        model.addAttribute("username", authentication.getName());
        model.addAttribute("usrRole", roles[0]);
        return "react/view";
    }


    /**
     * login user passwordless programmatically for auth2
     * Working on auto signin
     */
    @RequestMapping(value = "/login_test",method = RequestMethod.GET)
    public RedirectView autologin(){
        UserDetails user = userDetailsService.loadUserByUsername("admin");

        /* login with pass */
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user,"admiin",user.getAuthorities());
        Authentication auth = authenticationManager.authenticate(authReq);

        /* login without pass */
        //Authentication authentication = new UsernamePasswordAuthenticationToken(user,null, AuthorityUtils.createAuthorityList("role_ADMIN"));
        //SecurityContextHolder.getContext().setAuthentication(authentication);

        return new RedirectView("/");
    }


}
