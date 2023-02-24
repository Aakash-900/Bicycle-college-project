package com.system.bicyclerentalsystem.Controller;
import com.system.bicyclerentalsystem.Entity.User;
import com.system.bicyclerentalsystem.Pojo.BlogPojo;
import com.system.bicyclerentalsystem.Pojo.ContactPojo;
import com.system.bicyclerentalsystem.Pojo.UserPojo;
import com.system.bicyclerentalsystem.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private final ValidationAutoConfiguration validationAutoConfiguration;

    @GetMapping("/landing")
    public String homePage() {
        return "index";
    }

    @GetMapping("/create")
    public String createUser(Model model) {
        model.addAttribute("user", new UserPojo());
        return "register";
    }

    @PostMapping("/save")
    public String saveUser(@Valid UserPojo userPojo){
        userService.saveUser(userPojo);
        return "redirect:/user/login";
    }

    @GetMapping("/menu")
    public String getlLand(Model model,Principal principal) {
        model.addAttribute("users", userService.findByEmail(principal.getName()));
        return "menu";
    }
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("user",new UserPojo());
            return "register";
        }
        return "redirect:/user/homepage";
    }

    @GetMapping("/contactNow")
    public String getPage(Model model,Principal principal) {
        model.addAttribute("users", userService.findByEmail(principal.getName()));
        return "contact";
    }


    
//    @PostMapping("/send-message")
//    public String submitMessage(@Valid ContactPojo contactPojo) {
//        userService.submitMsg(contactPojo);
//        return "redirect:contact";
//    }

    @GetMapping("/viewBlog")
    public String viewUserBlog(Model model,Principal principal) {
        model.addAttribute("users", userService.findByEmail(principal.getName()));
        return "blog";
    }
    @GetMapping("/userProfile")
    public String user_profile(Model model,Principal principal) {
        model.addAttribute("users", userService.findByEmail(principal.getName()));
        return "profile";
    }

    @GetMapping("/homepage")
    public String getAbout(Principal principal,Model model, Authentication authentication) {
        if (authentication!=null){
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                if (grantedAuthority.getAuthority().equals("Admin")) {
                    return "/admin/index";
                }
            }
        }
        model.addAttribute("users", userService.findByEmail(principal.getName()));
        return "index";
    }

    @GetMapping("/Booknow")
    public String getUserbook() {
        return "/Booking";

    }

    @GetMapping("/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("deleteMsg", "Row delete successfully");
        return "redirect:/admin/userDetails";
    }

    public Map<String, String> validateRequest(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return null;
        }
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return errors;

    }

    @GetMapping("/list/{id}")
    public String getUserDetails(@PathVariable("id") Integer id, Model model){
        User user =userService.fetchById(id);
        model.addAttribute("update", new UserPojo(user));
        model.addAttribute("userdata",userService.fetchById(user.getId()));
        return "profile";}

    @GetMapping("/forgotpassword")
    public String forgot_password(Model model) {
        model.addAttribute("users", new UserPojo());
        return ("forgetPassword");
    }

    @PostMapping("/changepassword")
    public String changepassword(@RequestParam("email") String email, Model model, @Valid UserPojo userPojo){
        userService.processPasswordResetRequest(userPojo.getEmail());
        model.addAttribute("message","Your new password has been sent to your email Please " +
                "check your inbox");
        return "redirect:/login";
    }
}


