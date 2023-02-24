package com.system.bicyclerentalsystem.Controller.Admin;

import com.system.bicyclerentalsystem.Entity.User;
import com.system.bicyclerentalsystem.Services.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    @GetMapping("/Dashboard")
    public String getPage(Model model) {
        return "admin/index";
    }

//    @GetMapping("/userList")
//    public String getLand(Model model) {
//        return "admin/userlist";
//    }
    @GetMapping("/list")
    public String getUSerList(Model model) {
        List<User> users = userService.fetchAll();

        model.addAttribute("users", users.stream().map(user ->
                User.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .build()
        ));


        return "admin/userList";
    }

}
