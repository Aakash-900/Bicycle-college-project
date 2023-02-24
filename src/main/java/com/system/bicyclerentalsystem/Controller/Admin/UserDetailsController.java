package com.system.bicyclerentalsystem.Controller.Admin;


import com.system.bicyclerentalsystem.Entity.User;
import com.system.bicyclerentalsystem.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserDetailsController {
    private final UserService userService;
    @GetMapping("/userDetails")
    public String getUserDetails(Model model) {
        List<User> users = userService.fetchAll();

        model.addAttribute("userList" ,users.stream().map(user ->
                User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build()
        ));
        return "admin/userList";
    }
}
