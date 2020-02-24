package com.lee.ficss.mapper;

import com.lee.ficss.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    // Insert
    @Insert("INSERT INTO user (user_id, cn_name, en_name, password, email, phone, role, salt)" +
            "VALUES(#{userID}, #{cnName}, #{enName}, #{password}, #{email}, #{phone}, #{role}, #{salt})")
    void createUser(User user);

    // Update
    @Update("UPDATE user SET cn_name = #{cnName}, en_name = #{enName}, phone = #{phone}")
    void editUserInfo(User user);

    @Update("UPDATE user SET email = #{email}")
    void changeEmail(User user);

    @Update("UPDATE user SET password = #{password}, salt = #{salt}")
    void changePassword(User user);

    // Select
    @Select("SELECT * FROM user WHERE user_id = #{userID}")
    @Results(id = "resultMap", value = {
            @Result(column = "user_id", property = "userID"),
            @Result(column = "cn_name", property = "cnName"),
            @Result(column = "en_name", property = "enName")
    })
    User getUserByUserID(String userID);

    @Select("SELECT * FROM user WHERE email = #{email}")
    @ResultMap(value = "resultMap")
    User getUserByEmail(String email);

    @Select("<script>" +
            "SELECT * FROM user WHERE role = 'candidate' " +
            "<if test='cnName != \"ignore\"'>AND cn_name = #{cnName}</if>" +
            "<if test='enName != \"ignore\"'>AND en_name = #{enName}</if>" +
            "<if test='email != \"ignore\"'>AND email = #{email}</if>" +
            "<if test='phone != \"ignore\"'>AND phone = #{phone}</if>" +
            "</script>")
    @ResultMap(value = "resultMap")
    List<User> getCandidate(@Param("cnName") String cnName, @Param("enName") String enName,
                       @Param("email") String email, @Param("phone") String phone);


    @Select("SELECT * FROM user WHERE role like CONCAT('%', #{role}, '%')")
    @ResultMap(value = "resultMap")
    List<User> getUsersByRole(String role);

    // Delete
    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteUser(User user);
}
