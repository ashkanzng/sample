package Controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

@Controller
public class AppController {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    CacheManager cacheManager;

    ObjectMapper mapper = new ObjectMapper();

    /**
     * @RequestMapping(value = "/inventories" , method = RequestMethod.GET)
     * public String inventory(Model model)
     * {
     * model.addAttribute("class","inventory");
     * return "react/view";
     * }
     * replace all routes with simple method
     */
    @RequestMapping(value = "/admin/{component}", method = RequestMethod.GET)
    public String loadPage(@PathVariable String component, Model model, Authentication authentication) {
        model.addAttribute("class", component);
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

    @ResponseBody
    @RequestMapping(value = "/private/img/{imageName}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> loadPrivateImage(@PathVariable String imageName) throws IOException {
        Resource media = resourceLoader.getResource("classpath:resources/privateMedia/" + imageName);
        byte[] image = media.getInputStream().readAllBytes();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @ResponseBody
    @RequestMapping(value = "/site/img/{imageName}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> loadPublicImage(@PathVariable String imageName) throws IOException {
        Resource media = resourceLoader.getResource("classpath:resources/publicMedia/" + imageName);
        byte[] image = media.getInputStream().readAllBytes();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    /*
     * we can create a variable with name of `user` and bind value to a named model attribute and then exposes it to a web view
     * we can access ${user} in home or other pages
     * */
    @ModelAttribute("username")
    public String homepage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @ModelAttribute("usrRole")
    public String usrRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String[] roles = {null};
        authentication.getAuthorities().forEach(usrRole -> {
            roles[0] = usrRole.toString();
        });
        return roles[0];
    }

    @Scheduled(fixedRate = 10000)
    public void scheduleTaskWithFixedRate() {
        /* I am getting additional info from cache that i already saved it from userService */
        Cache userCache = cacheManager.getCache("userAdditionalInfo");
        if (userCache != null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ObjectNode cacheNodes = mapper.convertValue(userCache.getNativeCache(), ObjectNode.class);
            Iterator<String> cacheKeys = cacheNodes.fieldNames();
            while (cacheKeys.hasNext()) {
                String key = cacheKeys.next();
                System.out.println(cacheNodes.get(key));
                //Date currentdate = new Date(timestamp.getTime());
                //Date timeOfDelete = new Date(cacheNodes.get(key).get("timestamp").asLong());
                if (timestamp.getTime() > cacheNodes.get(key).get("timestamp").asLong()) {
                    if (userCache.get(key) != null) {
                        userCache.evict(key); //removing user cache additional information
                    }
                }
            }
        }
    }
}
