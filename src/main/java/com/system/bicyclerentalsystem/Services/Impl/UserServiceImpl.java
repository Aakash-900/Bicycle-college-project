
    package com.system.bicyclerentalsystem.Services.Impl;

    import com.system.bicyclerentalsystem.Entity.User;
    import com.system.bicyclerentalsystem.Pojo.UserPojo;
    import com.system.bicyclerentalsystem.Repo.UserRepo;
    import com.system.bicyclerentalsystem.Services.UserService;

    import com.system.bicyclerentalsystem.config.PasswordEncoderUtil;
    import freemarker.template.Configuration;
    import freemarker.template.Template;
    import jakarta.mail.internet.MimeMessage;
    import lombok.RequiredArgsConstructor;


    import org.springframework.mail.javamail.JavaMailSender;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.mail.MailSender;
    import org.springframework.mail.SimpleMailMessage;
    import org.springframework.mail.javamail.MimeMessageHelper;
    import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

    import java.nio.charset.StandardCharsets;
    import java.util.*;


    @Service
    @RequiredArgsConstructor
    public class UserServiceImpl implements UserService {
        private final UserRepo userRepo;
        private final JavaMailSender mailSender;
        private final ThreadPoolTaskExecutor taskExecutor;

        @Autowired
        @Qualifier("emailConfigBean")
        private Configuration emailConfig;

        @Override
        public User fetchById(Integer id){
            return userRepo.findById(id).orElseThrow(()-> new RuntimeException("Not Found"));
        }
        @Override
        public String saveUser(UserPojo userPojo) {
            User user = new User();
            if(userPojo.getId()!= null){
                user.setId(userPojo.getId());
            }
            user.setName(userPojo.getName());
            user.setEmail(userPojo.getEmail());
//            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            String encodedPassword = passwordEncoder.encode(userPojo.getPassword());
            user.setPassword(PasswordEncoderUtil.getInstance().encode(userPojo.getPassword()));
            userRepo.save(user);

            return "created";
        }

        public List<User> fetchAll() {
            return userRepo.findAll();
        }

        @Override
        public User findByEmail(String email) {
            return userRepo.findByEmail(email).orElseThrow();
        }

        public void deleteById(Integer id){
            userRepo.deleteById(id);
        }


        @Override
        public String updateResetPassword(String email) {
            User user = (User) userRepo.findByEmail(email)
                    .orElseThrow(()-> new RuntimeException("Invalid User email"));
            String updated_password = generatePassword();
            try {
                userRepo.updatePassword(updated_password, email);
                return "CHANGED";
            } catch (Exception e){
                e.printStackTrace();
            }
            return "ds";
        }

        public String generatePassword() {
            int length = 8;
            String password = "";
            Random r = new Random();
            for (int i = 0; i < length; i++) {
                int randomChar = (int)(r.nextInt(94) + 33);
                char c = (char)randomChar;
                password += c;
            }
            return password;
        }

        private void sendPassword(String email, String password ){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your new password is:");
            message.setText(password);
            mailSender.send(message);
        }
        @Override
        public void processPasswordResetRequest(String email){
            Optional<User> optionalUser = userRepo.findByEmail(email);
            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                String password = generatePassword();
                sendPassword(email, password);
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodePassword = passwordEncoder.encode(password);
                user.setPassword(encodePassword);
                userRepo.save(user);
            }
        }
        @Override
        public void sendEmail() {
            try {
                Map<String, String> model = new HashMap<>();
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                Template template = emailConfig.getTemplate("emailTemp.ftl");
                String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

                mimeMessageHelper.setTo("sendfrom@yopmail.com");
                mimeMessageHelper.setText(html, true);
                mimeMessageHelper.setSubject("Registration");
                mimeMessageHelper.setFrom("sendTo@yopmail.com");

                taskExecutor.execute(new Thread() {
                    public void run() {
                        mailSender.send(message);
                    }
                });
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }







