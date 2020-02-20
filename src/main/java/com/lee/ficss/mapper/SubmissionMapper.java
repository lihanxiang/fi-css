package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Submission;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SubmissionMapper {

    //Insert
    @Insert("INSERT INTO submission (submission_id, submitter_id, title, abstract_text, keyword, " +
            "topic, email, paper_file_id, slide_file_id, commit_time, last_modified)" +
            "VALUE (#{submitterID}, #{submissionID}, #{title}, #{abstractText}, #{keyword}, #{topic}, " +
            "#{email}, #{paperFileID}, #{slideFileID}, #{commitTime}, #{lastModified})")
    void createSubmission(Submission submission);

    //Update
    @Update("UPDATE submission SET title = #{title}, abstract_text = #{abstractText}, " +
            "keyword = #{keyword}, topic = #{topic}, mail = #{email}, paper_file_id = #{paperFileID}, " +
            "slide_file_id = #{slideFileID}, last_modified = #{lastModified}" +
            "WHERE submission_id = #{submissionID}")
    void editSubmission(Submission submission);

    //Select
    @Select("SELECT * FROM submission WHERE submission_id = #{submissionID}")
    Submission getSubmissionByID(String submissionID);

    @Select("SELECT * FROM submission WHERE submitter_id = #{submitterID}")
    List<Submission> getSubmissionBySubmitterID(String submitterID);

    @Select("SELECT * FROM submission WHERE paper_file_id = #{paperFileID}")
    List<Submission> getSubmissionsByPaper(String paperFileID);

    @Select("<script>" +
            "SELECT * FROM session WHERE" +
            "<if test='keyword != null'>keyword = LIKE CONCAT('%', #{keyword}, '%')</if>" +
            "<if test='topic != null'>topic LIKE CONCAT('%', #{topic}, '%')</if>" +
            "<if test='commitTime != null'>commitTime LIKE CONCAT(#{commitTime}, '%')</if>" +
            "ORDER BY last_modified" +
            "</script>")
    List<Submission> getSubmissions(String keyword, String topic, String commitTime);

    /*@Select("SELECT * FROM submission WHERE keyword LIKE CONCAT('%', #{keyword}, '%')")
    List<Submission> getSubmissionsByKeyword(String keyword);

    @Select("SELECT * FROM submission WHERE topic LIKE CONCAT('%', #{topic}, '%')")
    List<Submission> getSubmissionsByTopic(String topic);
     */

    //Delete
    @Delete("DELETE FROM submission WHERE submission_id = #{submissionID}")
    void deleteSubmission(String submissionID);
}
