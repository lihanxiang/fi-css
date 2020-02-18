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
    @Insert("INSERT INTO slide (slide_file_id, author, slide_name, slide_file_path, submit_date)" +
            "VALUE (#{slideFileID}, #{author}, #{slideName}, #{slideFilePath}, #{submitDate})")
    void createSlide(Slide slide);

    //Update
    @Insert("UPDATE slide SET author = #{author}, slide_name = #{slideName}, " +
            "slide_file_path = #{slideFilePath}, submit_date = #{submitDate}")
    void editSlideInfo(Slide slide);

    //Select
    @Select("SELECT * FROM slide WHERE id = #{id}")
    Slide getSlideByID(int id);

    @Select("SELECT * FROM slide WHERE slide_file_id = #{slideFileID}")
    Slide getSlideByFileID(String slideFileID);

    @Select("SELECT * FROM slide WHERE author = #{author}")
    List<Slide> getSlideByAuthor(String author);

    @Select("SELECT * FROM slide WHERE submit_date = #{submitDate}")
    List<Slide> getSlideByDate(String submitDate);

    //Delete
    @Delete("DELETE FROM slide WHERE id = #{id}")
    void deleteSlide(int id);
}
