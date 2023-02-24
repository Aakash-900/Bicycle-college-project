package com.system.bicyclerentalsystem.Controller;

import com.system.bicyclerentalsystem.Entity.Blog;
import com.system.bicyclerentalsystem.Pojo.BlogPojo;
import com.system.bicyclerentalsystem.Services.BlogServices;
import com.system.bicyclerentalsystem.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/blog")
public class blogController {
    private final BlogServices blogServices;
    private  final UserService userServices;

    @GetMapping("/addBlog")
    public String createBlog(Model model) {
        model.addAttribute("blog", new BlogPojo());
        return "/adminBlog";
    }


    @PostMapping("/save")
    public String saveBlog(@Valid BlogPojo blogPojo,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {

        Map<String, String> requestError = validateRequest(bindingResult);
        if (requestError != null) {
            redirectAttributes.addFlashAttribute("requestError", requestError);
            return "redirect:blog/addBlog";
        }

        blogServices.saveBlog(blogPojo);
        redirectAttributes.addFlashAttribute("successMsg", "Blog saved successfully");

        return "redirect:/blog/list";
    }

    @GetMapping("/list")
    public String displayBlog( Model model) {
        List<Blog> blogs = blogServices.fetchAll();
        model.addAttribute("bloglist", blogs.stream().map(blog ->
                Blog.builder()
                        .id(blog.getId())
                        .title(blog.getTitle())
                        .author(blog.getAuthor())
                        .content(blog.getContent())
                        .date(blog.getDate())
                        .imageBase64(getImageBase64(blog.getImage()))
                        .build()

        ));
        return "blogtable";
    }

    @GetMapping("/view")
    public String getViewList(Model model , Principal principal) {
        if (principal != null) {
            model.addAttribute("prof", userServices.findByEmail(principal.getName()));
        }
        List<Blog> blogs = blogServices.fetchAll();
        model.addAttribute("bloglist", blogs.stream().map(blog ->
                Blog.builder()
                        .id(blog.getId())
                        .title(blog.getTitle())
                        .author(blog.getAuthor())
                        .imageBase64(getImageBase64(blog.getImage()))
                        .build()

        ));
        return "blog";
    }

    @GetMapping("/editBlog/{id}")
    public String editBlog(@PathVariable("id") Integer id, Model model) {
        Blog blog = blogServices.fetchById(id);
        model.addAttribute("bloglist", new BlogPojo(blog));
        return "adminBlog";
    }


    @GetMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable("id") Integer id) {
        blogServices.deleteById(id);
        return "redirect:/blog/list";
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

    public String getImageBase64(String fileName) {
        String filePath = System.getProperty("user.dir") + "/cycleimage/";
        File file = new File(filePath + fileName);
        byte[] bytes ;
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return Base64.getEncoder().encodeToString(bytes);
    }


}
