package Controllers;

import Contracts.CustomerInterface;
import Contracts.ItemInterface;
import Models.CategoryModel;
import Models.CustomerInformation;
import Services.CategoryService;
import Services.ShopService;
import Services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


@Controller
public class ShopController {

    @Autowired
    private ItemInterface itemService;

    @Autowired
    ShopService shopService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CustomerInterface customerService;

    @Autowired
    HttpSession httpSession;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("items", shopService.findFromTo(0, 12));
        model.addAttribute("topSellers", shopService.findFromTo(5, 3));
        model.addAttribute("recentlyViewed", shopService.findFromTo(0, 3));
        model.addAttribute("topNew", shopService.findFromTo(0, 2));
        model.addAttribute("class", "homePage");
        return "shop/homePage";
    }

    @RequestMapping(value = "/aboutus", method = RequestMethod.GET)
    public String aboutus() {
        return "shop/aboutUsPage";
    }

    @RequestMapping(value = "/shop", method = RequestMethod.GET)
    public String shopping(
            @RequestParam(required = false, name = "itemid") String itemid,
            @RequestParam(required = false, name = "catid") Integer categoryId,
            @RequestParam(required = false, name = "page") Integer page,
            @RequestParam(required = false, name = "size") Integer size,
            Model model) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        builder.replaceQueryParam("catid", categoryId);
        builder.replaceQueryParam("page", page);
        builder.replaceQueryParam("size", size);

        JsonNode data = shopService.getItemsByParameter(itemid, categoryId, page, size);
        model.addAttribute("currentUrl", builder.build().toUri());
        model.addAttribute("data", data);
        model.addAttribute("categoryTreeMenu", data.get("categoryTreeMenu").toString());
        model.addAttribute("currentCatId", categoryId);
        if (itemid != null) {
            return "shop/itemDetailPage";
        }
        return "shop/shopPage";
    }

    @RequestMapping(value = "/myaccount", method = RequestMethod.GET)
    public String myaccount(Model model, HttpServletRequest request, HttpServletResponse response) {
        CustomerInformation currentCustomer = customerService.getCustomerInformation(httpSession.getAttribute("username").toString());
        httpSession.setAttribute("customerId", currentCustomer.getId());
        model.addAttribute("customer", currentCustomer);
        if (!this.checkIfCookieExist(request)) {
            Cookie cookie = new Cookie("userEmail", currentCustomer.getEmail());
            cookie.setMaxAge(120);
            response.addCookie(cookie);
        }
        return "shop/myAccountShop";
    }

    @RequestMapping(value = "/myaccount/myinfo", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String updateCustomerPersonalInformaion(
            @ModelAttribute CustomerInformation customerInformation
    ) {
        customerService.updateCustomerInformation(customerInformation, Integer.parseInt(httpSession.getAttribute("customerId").toString()));
        return "redirect:/myaccount";
    }

    @RequestMapping(value = "/myaccount/mycart", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String addToMyCart(
            HttpServletRequest request,
            @RequestParam(value = "itemid", required = true) String itemid,
            @RequestParam(value = "img", defaultValue = "",required = true) String img,
            @RequestParam(value = "count", required = true) short count) {
        System.out.println(request.getHeader("referer"));
        shopService.addToCart(itemid, count, img);
        return "redirect:/myaccount?tab=cart";
    }

    @ModelAttribute("currentPage")
    public String populateTypes(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("categoryList")
    public List<CategoryModel> getCategoryList() {
        Map<Integer, CategoryModel> categoryMap = new HashMap<>();
        List<CategoryModel> categoryList = new ArrayList<>();
        shopService.getCategory(0, categoryList, categoryMap);
        return categoryList;
    }

    @ModelAttribute("username")
    public String homepage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @ModelAttribute("role")
    public String usrRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object[] roles = authentication.getAuthorities().toArray();
        return roles[0].toString();
    }

    boolean checkIfCookieExist(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie[] userEmail = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("userEmail")).toArray(Cookie[]::new);
        if (userEmail.length > 0) {
            System.out.println("this cookie exist -> " + userEmail[0].getValue());
            return true;
        }
        return false;
    }

}
