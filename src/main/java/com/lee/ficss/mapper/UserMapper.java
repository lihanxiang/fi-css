package com.lee.ficss.mapper;

import com.lee.ficss.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    // Insert
    @Insert("INSERT INTO user (user_id, Chinese_name, English_name, password, email, phone, role, salt)" +
            "VALUES(#{userID}, #{ChineseName}, #{EnglishName}, #{password}, #{email}, #{phone}, #{role}, #{salt})")
    void createUser(User user);

    // Update
    @Update("UPDATE user SET Chinese_name = #{ChineseName}, English_name = #{EnglishName}, phone = #{phone}")
    void editUserInfo(User user);

    @Update("UPDATE user SET email = #{email}")
    void changeEmail(User user);

    @Update("UPDATE user SET password = #{password}, salt = #{salt}")
    void changePassword(User user);

    // Select
    @Select("SELECT * FROM user WHERE user_id = #{userID}")
    User getUserByUserID(String userID);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User getUserByEmail(String email);

    @Select("SELECT * FROM user WHERE Chinese_name = #{ChineseName}")
    List<User> getUserByChineseName(String ChineseName);

    @Select("SELECT * FROM user WHERE English_name = #{EnglishName}")
    List<User> getUserByEnglishName(String EnglishName);

    @Select("SELECT * FROM user WHERE role like CONCAT('%', #{role}, '%')")
    List<User> getUsersByRole(String role);

    // Delete
    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteUser(User user);
}
