package com.system.bicyclerentalsystem.Services;


import com.system.bicyclerentalsystem.Entity.Blog;
import com.system.bicyclerentalsystem.Pojo.BlogPojo;

import java.io.IOException;
import java.util.List;

public interface BlogServices {
    BlogPojo saveBlog(BlogPojo blogPojo)throws IOException;

    List<Blog> fetchAll();

    Blog fetchById(Integer id);
    void deleteById(Integer id);
}
