package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Submission;
import com.lee.ficss.util.DataMap;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SubmissionMapper {

    //Insert
    @Insert("INSERT INTO submission (conference_id, submission_id, submitter_id, title, author, abstract_text, " +
            "keyword, topic, email, paper_file_id, slide_file_id, commit_time, last_modified)" +
            "VALUE (#{conferenceID}, #{submissionID}, #{submitterID}, #{title}, #{author}, #{abstractText}, #{keyword}, " +
            "#{topic}, #{email}, #{paperFileID}, #{slideFileID}, #{commitTime}, #{lastModified})")
    void createSubmission(Submission submission);

    //Update
    @Update("UPDATE submission SET title = #{title}, author = #{author} abstract_text = #{abstractText}, " +
            "keyword = #{keyword}, topic = #{topic}, mail = #{email}, paper_file_id = #{paperFileID}, " +
            "slide_file_id = #{slideFileID}, last_modified = #{lastModified}" +
            "WHERE submission_id = #{submissionID}")
    void editSubmission(Submission submission);

    //Select
    @Select("SELECT * FROM submission WHERE submission_id = #{submissionID}")
    @Results(id = "resultMap", value = {
            @Result(column = "conference_id", property = "conferenceID"),
            @Result(column = "submission_id", property = "submissionID"),
            @Result(column = "submitter_id", property = "submitterID"),
            @Result(column = "abstract_text", property = "abstractText"),
            @Result(column = "paper_file_id", property = "paperFileID"),
            @Result(column = "slide_file_id", property = "slideFileID"),
            @Result(column = "commit_time", property = "commitTime"),
            @Result(column = "last_modified", property = "lastModified")
    })
    Submission getSubmissionByID(String submissionID);

    @Select("SELECT * FROM submission WHERE conference_id = #{conferenceID} AND submitter_id = #{submitterID}")
    @ResultMap(value = "resultMap")
    Submission getSubmissionInConferenceBySubmitterID(@Param("conferenceID") String conferenceID,
                                                      @Param("submitterID") String submitterID);

    @Select("SELECT COUNT(*) FROM submission WHERE conference_id = #{conferenceID} AND submitter_id = #{submitterID}")
    int checkIfSubmissionExist(@Param("conferenceID") String conferenceID, @Param("submitterID") String submitterID);

    @Select("SELECT * FROM submission WHERE submitter_id = #{submitterID}")
    @ResultMap(value = "resultMap")
    List<Submission> getSubmissionsBySubmitterID(String submitterID);

    @Select("SELECT * FROM submission WHERE submitter_id = #{submitterID} AND conference_id = #{conferenceID}")
    @ResultMap(value = "resultMap")
    Submission getSubmissionsBySubmitterIDAndConferenceID(@Param("submitterID") String submitterID,
                                                          @Param("conferenceID") String conferenceID);

    @Select("SELECT * FROM submission WHERE conference_id = #{conferenceID} ORDER BY #{condition}")
    @ResultMap(value = "resultMap")
    List<Submission> getSubmissionsInConferenceOrderByCondition(@Param("conferenceID") String conferenceID,
                                                                @Param("condition") String condition);

    @Select("SELECT COUNT(*) FROM submission WHERE submitter_id = #{submitterID}")
    int getSubmissionCountBySubmitterID(String submitterID);

    @Select("SELECT COUNT(*) FROM submission WHERE conference_id = #{conferenceID}")
    int getSubmissionCountInConference(String conferenceID);

    @Select("SELECT * FROM submission WHERE paper_file_id = #{paperFileID}")
    @ResultMap(value = "resultMap")
    Submission getSubmissionByPaperID(String paperFileID);

    @Select("<script>" +
            "SELECT * FROM session WHERE 1 = 1" +
            "<if test='keyword != \"ignore\"'>AND keyword = LIKE CONCAT('%', #{keyword}, '%')</if>" +
            "<if test='topic != \"ignore\"'>AND topic LIKE CONCAT('%', #{topic}, '%')</if>" +
            "<if test='commitTime != \"ignore\"'>AND commitTime LIKE CONCAT(#{commitTime}, '%')</if>" +
            "ORDER BY last_modified" +
            "</script>")
    @ResultMap(value = "resultMap")
    List<Submission> getSubmissions(@Param("keyword") String keyword, @Param("topic") String topic,
                                    @Param("commitTime") String commitTime);

    @Select("SELECT COUNT(*) FROM submission")
    int getSubmissionCount();

    //Delete
    @Delete("DELETE FROM submission WHERE submission_id = #{submissionID}")
    void deleteSubmission(String submissionID);
}
