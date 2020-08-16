package Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class CustomErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error")
    public RedirectView errorRedirectView (HttpSession session, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object status2 = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
           if(statusCode == HttpStatus.FORBIDDEN.value()) {
                return new RedirectView("/forbidden");
            }
        }
        return new RedirectView("/");
    }
}
