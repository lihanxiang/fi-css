package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Paper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PaperMapper {

    //Insert
    @Insert("INSERT INTO paper (paper_file_id, author, paper_name, paper_file_path, submit_date)" +
            "VALUE (#{paperFileID}, #{author}, #{paperName}, #{paperFilePath}, #{submitDate})")
    void createPaper(Paper paper);

    //Update
    @Insert("UPDATE paper SET author = #{author}, paper_name = #{paperName}, " +
            "paper_file_path = #{paperFilePath}, submit_date = #{submitDate}" +
            "WHERE paper_file_id = #{paperFileID}")
    void editPaperInfo(Paper paper);

    //Select
    @Select("SELECT * FROM paper WHERE paper_file_id = #{paperFileID}")
    Paper getPaperByFileID(String paperFileID);

    @Select("SELECT * FROM paper WHERE author = #{author}")
    List<Paper> getPaperByAuthor(String author);

    @Select("SELECT * FROM paper WHERE submit_date = #{submitDate}")
    List<Paper> getPaperByDate(String submitDate);

    //Delete
    @Delete("DELETE FROM paper WHERE paper_file_id = #{paperFileID}")
    void deletePaper(String paperFileID);
}
