package com.example.controller;

import com.example.common.Result;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

/**
 * add, delete, change, search of User
 */
@RestController
@RequestMapping("/user")
public class UserController {


  @Resource
  UserService userService;

  /**
   * add
   */
  @PostMapping("/add")
  public Result add(@RequestBody User user) {
    userService.add(user);
    return Result.success();
  }

  /**
   * delete
   */
  @DeleteMapping("/delete/{id}")
  public Result deleteById(@PathVariable Integer id) {
    userService.deleteById(id);
    return Result.success();
  }

  /**
   * batch delete
   */
  @DeleteMapping("/delete/batch")
  public Result deleteBatch(@RequestBody List<Integer> ids) {
    userService.deleteBatch(ids);
    return Result.success();
  }

  /**
   * change user info
   */
  @PutMapping("/update")
  public Result updateById(@RequestBody User user) {
    userService.updateById(user);
    return Result.success();
  }

  /**
   * search by id
   */
  @GetMapping("/selectById/{id}")
  public Result selectById(@PathVariable Integer id) {
    User user = userService.selectById(id);
    return Result.success(user);
  }

  /**
   * search all users by username or search all
   */
  @GetMapping("/selectAll")
  public Result selectAll(User user) {
    List<User> list = userService.selectAll(user);
    return Result.success(list);
  }

  /**
   * search by page, default size is 10, page is 1.
   */
  @GetMapping("/selectPage")
  public Result selectPage(User user,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
    PageInfo<User> page = userService.selectPage(user, pageNum, pageSize);
    return Result.success(page);
  }
}
