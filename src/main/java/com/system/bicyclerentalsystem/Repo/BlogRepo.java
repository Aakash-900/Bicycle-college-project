package com.system.bicyclerentalsystem.Repo;

import com.system.bicyclerentalsystem.Entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface BlogRepo extends JpaRepository<Blog, Integer> {
    Optional<Blog> findBlogByTitle(String answer);
}
