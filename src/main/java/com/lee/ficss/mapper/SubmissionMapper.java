package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Submission;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SubmissionMapper {

    //Insert
    @Insert("INSERT INTO submission (title, abstract_text, keyword, topic, email, paper_file_id, slide_file_id)" +
            "VALUE (#{title}, #{abstractText}, #{keyword}, #{topic}, #{email}, #{paperFileID}, #{slideFileID})")
    void createSubmission(Submission submission);

    //Update
    @Update("UPDATE submission SET title = #{title}, abstract_text = #{abstractText}, keyword = #{keyword}," +
            "topic = #{topic}, email = #{email}, paper_file_id = #{paperFileID}, slide_file_id = #{slideFileID}" +
            "WHERE id = #{id}")
    void editSubmission(Submission submission);

    //Select
    @Select("SELECT * FROM submission WHERE id = #{id}")
    Submission getSubmissionByID(int id);

    @Select("SELECT * FROM submission WHERE paper_file_id = #{paperFileID}")
    List<Submission> getSubmissionsByPaper(String paperFileID);

    @Select("SELECT * FROM submission WHERE keyword LIKE CONCAT('%', #{keyword}, '%')")
    List<Submission> getSubmissionsByKeyword(String keyword);

    @Select("SELECT * FROM submission WHERE topic LIKE CONCAT('%', #{topic}, '%')")
    List<Submission> getSubmissionsByTopic(String topic);

    //Delete
    @Delete("DELETE FROM submission WHERE id = #{id}")
    void deleteSubmission(int id);
}
