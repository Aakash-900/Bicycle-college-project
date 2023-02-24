package com.system.bicyclerentalsystem.Pojo;

import com.system.bicyclerentalsystem.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
    public class UserPojo {

        private Integer id;
        private String name;
        private String email;
        private String password;


        public UserPojo(User user){
            this.id= user.getId();
            this.name=user.getName();
            this.email=user.getEmail();
            this.password=user.getPassword();

        }

}
