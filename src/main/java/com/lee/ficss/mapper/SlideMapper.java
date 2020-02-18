package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Slide;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SlideMapper {

    //Insert
    @Insert("INSERT INTO slide (slide_file_id, author, slide_title, slide_file_path, commit_time)" +
            "VALUE (#{slideFileID}, #{author}, #{slideTitle}, #{slideFilePath}, #{commitTime})")
    void createSlide(Slide slide);

    //Update
    @Insert("UPDATE slide SET author = #{author}, slide_title = #{slideTitle}, " +
            "slide_file_path = #{slideFilePath}, commit_time = #{commitTime}" +
            "WHERE slide_file_id = #{slideFileID}")
    void editSlideInfo(Slide slide);

    //Select
    @Select("SELECT * FROM slide WHERE slide_file_id = #{slideFileID}")
    Slide getSlideByFileID(String slideFileID);

    @Select("<script>" +
            "SELECT * FROM slide WHERE" +
            "<if test='author != null>author = LIKE CONCAT('%', #{author}, '%')</if>'" +
            "<if test='slideTitle != null>slide_title LIKE CONCAT('%', #{slideTitle}, '%')</if>'" +
            "<if test='commitTime != null>commitTime LIKE CONCAT(#{commitTime}, '%')</if>'" +
            "ORDER BY last_modified" +
            "</script>")
    List<Slide> getSlides(String author, String slideTitle, String commitTime);

    /*
    @Select("SELECT * FROM slide WHERE author = #{author}")
    List<Slide> getSlideByAuthor(String author);

    @Select("SELECT * FROM slide WHERE submit_date = #{submitDate}")
    List<Slide> getSlideByDate(String submitDate);
     */

    //Delete
    @Delete("DELETE FROM slide WHERE slide_file_id = #{slideFileID}")
    void deleteSlide(String slideFileID);
}
