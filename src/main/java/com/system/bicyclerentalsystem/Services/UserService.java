package com.system.bicyclerentalsystem.Services;


import com.system.bicyclerentalsystem.Entity.User;
import com.system.bicyclerentalsystem.Pojo.UserPojo;


import java.util.List;

public interface UserService {

    String saveUser(UserPojo userPojo);
    List<User> fetchAll();

    User findByEmail(String email);

    User fetchById(Integer id);

    void deleteById(Integer id);

    String updateResetPassword(String email);

    void processPasswordResetRequest(String email);

    void sendEmail();
}
