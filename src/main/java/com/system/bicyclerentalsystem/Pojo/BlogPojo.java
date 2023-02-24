package com.system.bicyclerentalsystem.Pojo;

import com.system.bicyclerentalsystem.Entity.Blog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogPojo {
    private Integer id;
    private  String author;
    private  String title;
    private Date date;
    private  String phoneNum;
    private  String content;
    private MultipartFile image;


    public BlogPojo(Blog blog) {
        this.id=blog.getId();
        this.author = blog.getAuthor();
        this.title=blog.getTitle();
        this.content=blog.getContent();
        this.date=blog.getDate();

    }
}