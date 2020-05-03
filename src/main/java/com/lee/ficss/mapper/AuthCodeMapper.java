package com.lee.ficss.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AuthCodeMapper {

    @Insert("INSERT INTO auth_code (email, code) VALUE (#{email}, #{code})")
    void createAuthCode(@Param("email") String email, @Param("code") String code);

    @Update("UPDATE auth_code SET code = #{code} WHERE email = #{email}")
    void updateAuthCode(@Param("email") String email, @Param("code") String code);

    @Select("SELECT code FROM auth_code WHERE email = #{email}")
    String getAuthCode(String email);

    @Delete("DELETE FROM auth_code WHERE email = #{email}")
    void deleteAuthCode(String email);
}
