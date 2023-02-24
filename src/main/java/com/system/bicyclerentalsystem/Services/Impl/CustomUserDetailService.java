package com.system.bicyclerentalsystem.Services.Impl;



import com.system.bicyclerentalsystem.Repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String user_name) throws UsernameNotFoundException {
        return userRepo.findByEmail(user_name).orElseThrow(()->new EntityNotFoundException("User Not Found"));
    }
}


