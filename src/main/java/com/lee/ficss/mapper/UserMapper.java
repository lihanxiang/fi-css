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
    @Update("UPDATE user SET cn_name = #{cnName}, en_name = #{enName}, email = #{email}, phone = #{phone}" +
            "WHERE user_id = #{userID}")
    void editUserInfo(@Param("userID") String userID, @Param("cnName") String cnName, @Param("enName") String enName,
                      @Param("email") String email, @Param("phone") String phone);

    @Update("UPDATE user SET email = #{email}")
    void changeEmail(User user);

    @Update("UPDATE user SET password = #{password}, salt = #{salt} WHERE user_id = #{userID}")
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

    @Select("SELECT COUNT(*) FROM user WHERE email = #{email}")
    int checkIfEmailExist(String email);

    @Select("SELECT COUNT(*) FROM user WHERE phone = #{phone}")
    int checkIfPhoneExist(String phone);

    @Select("<script>" +
            "SELECT * FROM user WHERE role = 'candidate' " +
            "<if test='cnName != \"ignore\"'>AND cn_name like CONCAT('%', #{cnName}, '%')</if>" +
            "<if test='enName != \"ignore\"'>AND en_name like CONCAT('%', #{enName}, '%')</if>" +
            "<if test='email != \"ignore\"'>AND email like CONCAT('%', #{email}, '%')</if>" +
            "<if test='phone != \"ignore\"'>AND phone like CONCAT('%', #{phone}, '%')</if>" +
            "</script>")
    @ResultMap(value = "resultMap")
    List<User> getCandidate(@Param("cnName") String cnName, @Param("enName") String enName,
                       @Param("email") String email, @Param("phone") String phone);

    @Select("SELECT COUNT(*) FROM user WHERE role = 'candidate'")
    int getCandidateCount();

    @Select("SELECT * FROM user WHERE role like CONCAT('%', #{role}, '%')")
    @ResultMap(value = "resultMap")
    List<User> getUsersByRole(String role);

    @Select("SELECT * FROM user WHERE user_id IN " +
            "(SELECT submitter_id FROM submission WHERE conference_id = #{conferenceID})")
    @ResultMap(value = "resultMap")
    List<User> getCandidatesInConference(String conferenceID);

    // Delete
    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteUser(User user);
}
