package com.wqs.root.mapper;

import com.github.pagehelper.Page;
import com.wqs.root.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserMapper {
    Page<User> findUserPage();
    void insertUser(User user);
    List<User> selectAllUser();
}
