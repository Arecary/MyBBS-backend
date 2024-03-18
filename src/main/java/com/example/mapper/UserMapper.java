package com.example.mapper;

import com.example.entity.User;

import java.util.List;

public interface UserMapper {

  /**
   * To insert User in database.
   *
   * @param user user obj
   */
  void insert(User user);

  /**
   * To determine if this user is existed in database.
   * @param username username
   * @return User obj
   */
  User selectByUsername(String username);

  void deleteById(Integer id);

  void updateById(User user);

  User selectById(Integer id);

  List<User> selectAll(User user);
}
