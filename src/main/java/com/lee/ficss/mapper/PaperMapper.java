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

    @Insert("INSERT INTO session_paper (session_id, paper_file_id)" +
            "VALUE (#{sessionID}, #{paperFileId})")
    void addPaperToSession(@Param("sessionID") String sessionID, @Param("paperFileId") String paperFileId);

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

    @Select("SELECT paper_file_id FROM session_paper WHERE session_id = #{sessionID}" )
    List<String> getPaperIDsInSession(String sessionID);

    @Select("SELECT paper_file_id FROM submission WHERE conference_id = #{conferenceID} AND paper_file_id NOT IN" +
            "(SELECT paper_file_id FROM session_paper)")
    List<String> getAvailablePaperIDs(String conferenceID);

    @Select("SELECT * FROM paper WHERE submitter_id = #{submitterID}")
    List<Paper> getPaperBySubmitterID(String submitterID);

    @Select("<script>" +
            "SELECT * FROM submission WHERE 1 = 1" +
            "<if test='title != \"ignore\"'>AND title LIKE CONCAT('%', #{title}, '%')</if>" +
            "<if test='author != \"ignore\"'>AND author LIKE CONCAT('%', #{author}, '%')</if>" +
            "<if test='keyword != \"ignore\"'>AND keyword LIKE CONCAT('%', #{keyword}, '%')</if>" +
            "<if test='topic != \"ignore\"'>AND topic LIKE CONCAT(#{topic}, '%')</if>" +
            "ORDER BY commit_time" +
            "</script>")
    @ResultMap(value = "resultMap")
    List<Paper> searchPapers(@Param("title") String title, @Param("author") String author,
                             @Param("keyword") String keyword, @Param("topic") String topic);

    //Delete
    @Delete("DELETE FROM paper WHERE paper_file_id = #{paperFileID}")
    void deletePaper(String paperFileID);

    @Delete("DELETE FROM session_paper WHERE paper_file_id = #{paperFileID}")
    void deletePaperInSession(String paperFileID);

    @Delete("DELETE FROM submission WHERE paper_file_id = #{paperFileID}")
    void deletePaperInSubmission(String paperFileID);
}
