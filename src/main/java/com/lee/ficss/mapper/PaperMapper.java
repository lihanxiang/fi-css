package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Paper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PaperMapper {

    //Insert
    @Insert("INSERT INTO paper (paper_file_id, submitter_id, paper_title, paper_file_path, commit_time, last_modified)" +
            "VALUE (#{paperFileID}, #{submitterID}, #{paperTitle}, #{paperFilePath}, #{commitTime}, #{lastModified})")
    void createPaper(Paper paper);

    //Update
    @Insert("UPDATE paper SET paper_title = #{paperTitle}, paper_file_path = #{paperFilePath}, " +
            "last_modified = #{lastModified} WHERE paper_file_id = #{paperFileID}")
    void editPaperInfo(Paper paper);

    //Select
    @Select("SELECT * FROM paper WHERE paper_file_id = #{paperFileID}")
    @Results(id = "resultMap", value = {
            @Result(column = "paper_file_id", property = "paperFileID"),
            @Result(column = "submitter_id", property = "submitterID"),
            @Result(column = "paper_title", property = "paperTitle"),
            @Result(column = "paper_file_path", property = "paperFilePath"),
            @Result(column = "commit_time", property = "commitTime"),
            @Result(column = "last_modified", property = "lastModified")
    })
    Paper getPaperByFileID(String paperFileID);

    @Select("SELECT * FROM paper WHERE submitter_id = #{submitterID}")
    @ResultMap(value = "resultMap")
    List<Paper> getPaperBySubmitterID(String submitterID);

    @Select("<script>" +
            "SELECT * FROM paper WHERE 1 = 1" +
            "<if test='paperTitle != \"ignore\"'>AND paper_title LIKE CONCAT('%', #{paperTitle}, '%')</if>" +
            "<if test='commitTime != \"ignore\"'>AND commitTime LIKE CONCAT(#{commitTime}, '%')</if>" +
            "ORDER BY last_modified" +
            "</script>")
    @ResultMap(value = "resultMap")
    List<Paper> getPapers(@Param("paperTitle")String paperTitle, @Param("commitTime")String commitTime);

    //Delete
    @Delete("DELETE FROM paper WHERE paper_file_id = #{paperFileID}")
    void deletePaper(String paperFileID);
}
