package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Slide;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SlideMapper {

    //Insert
    @Insert("INSERT INTO slide (slide_file_id, submitter_id, slide_title, slide_file_path, commit_time, last_modified)" +
            "VALUE (#{slideFileID}, #{submitterID}, #{slideTitle}, #{slideFilePath}, #{commitTime}, #{lastModified})")
    void createSlide(Slide slide);

    //Update
    @Insert("UPDATE slide SET slide_title = #{slideTitle}, slide_file_path = #{slideFilePath}, " +
            "commit_time = #{commitTime} WHERE slide_file_id = #{slideFileID}")
    void editSlideInfo(Slide slide);

    //Select
    @Select("SELECT * FROM slide WHERE slide_file_id = #{slideFileID}")
    @Results(id = "resultMap", value = {
            @Result(column = "slide_file_id", property = "slideFileID"),
            @Result(column = "submitter_id", property = "submitterID"),
            @Result(column = "slide_title", property = "slideTitle"),
            @Result(column = "slide_file_path", property = "slideFilePath"),
            @Result(column = "commit_time", property = "commitTime"),
            @Result(column = "last_modified", property = "lastModified")
    })
    Slide getSlideByFileID(String slideFileID);

    @Select("SELECT * FROM slide WHERE submitter_id = #{submitterID}")
    @ResultMap(value = "resultMap")
    List<Slide> getSlideBySubmitter(String submitterID);

    @Select("<script>" +
            "SELECT * FROM slide WHERE 1 = 1" +
            "<if test='slideTitle != \"ignore\"'>AND slide_title LIKE CONCAT('%', #{slideTitle}, '%')</if>" +
            "<if test='commitTime != \"ignore\"'>AND commitTime LIKE CONCAT(#{commitTime}, '%')</if>" +
            "ORDER BY last_modified" +
            "</script>")
    @ResultMap(value = "resultMap")
    List<Slide> getSlides(@Param("slideTitle") String slideTitle, @Param("commitTime") String commitTime);

    //Delete
    @Delete("DELETE FROM slide WHERE slide_file_id = #{slideFileID}")
    void deleteSlide(String slideFileID);
}
