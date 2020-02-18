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
    @Insert("INSERT INTO paper (paper_file_id, author, paper_title, paper_file_path, commit_time, last_modified)" +
            "VALUE (#{paperFileID}, #{author}, #{paperTitle}, #{paperFilePath}, #{commitTime}, #{lastModified})")
    void createPaper(Paper paper);

    //Update
    @Insert("UPDATE paper SET author = #{author}, paper_title = #{paperTitle}, " +
            "paper_file_path = #{paperFilePath}, last_modified = #{lastModified}" +
            "WHERE paper_file_id = #{paperFileID}")
    void editPaperInfo(Paper paper);

    //Select
    @Select("SELECT * FROM paper WHERE paper_file_id = #{paperFileID}")
    Paper getPaperByFileID(String paperFileID);

    @Select("<script>" +
            "SELECT * FROM paper WHERE" +
            "<if test='author != null>author = LIKE CONCAT('%', #{author}, '%')</if>'" +
            "<if test='paperTitle != null>paper_title LIKE CONCAT('%', #{paperTitle}, '%')</if>'" +
            "<if test='commitTime != null>commitTime LIKE CONCAT(#{commitTime}, '%')</if>'" +
            "ORDER BY last_modified" +
            "</script>")
    List<Paper> getPapers(String author, String paperTitle, String commitTime);

    //Delete
    @Delete("DELETE FROM paper WHERE paper_file_id = #{paperFileID}")
    void deletePaper(String paperFileID);
}
