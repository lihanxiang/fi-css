package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Session;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SessionMapper {

    //Insert
    @Insert("INSERT INTO session (event_id, session_id, session_name, session_room, " +
            "session_reviewer, session_chair, session_index, creation_time)" +
            "VALUE (#{eventID}, #{sessionID}, #{sessionName}, #{sessionRoom}," +
            " #{sessionReviewer}, #{sessionChair}, #{sessionIndex}, #{creationTime})")
    void createSession(Session session);

    @Insert("INSERT INTO session_paper (session_id, paper_file_id) " +
            "VALUE (#{sessionID}, #{paperFileID})")
    void addPaperToSession(@Param("sessionID") String sessionID, @Param("paperFileID") String paperFileID);

    //Update
    @Update("UPDATE session SET session_name = #{sessionName}, session_room = #{sessionRoom}, " +
            "session_reviewer = #{sessionReviewer}, session_chair = #{sessionChair} WHERE session_id = #{sessionID}")
    void editSessionInfo(@Param("sessionID") String sessionID, @Param("sessionName") String sessionName,
                         @Param("sessionRoom") String sessionRoom, @Param("sessionReviewer") String sessionReviewer,
                         @Param("sessionChair") String sessionChair);

    @Update("UPDATE session SET session_reviewer_id = #{sessionReviewerID} WHERE session_id = #{sessionID}")
    void addReviewerToSession(@Param("sessionID") String sessionID, @Param("sessionReviewerID") String sessionReviewerID);

    @Update("UPDATE session SET session_chair_id = #{sessionChairID} WHERE session_id = #{sessionID}")
    void addChairToSession(@Param("sessionID") String sessionID, @Param("sessionChairID") String sessionChairID);

    //Select
    @Select("SELECT * FROM session WHERE session_id = #{sessionID}")
    @Results(id = "resultMap", value = {
            @Result(column = "event_id", property = "eventID"),
            @Result(column = "session_id", property = "sessionID"),
            @Result(column = "session_name", property = "sessionName"),
            @Result(column = "session_room", property = "sessionRoom"),
            @Result(column = "session_reviewer_id", property = "sessionReviewerID"),
            @Result(column = "session_chair_id", property = "sessionChairID"),
            @Result(column = "session_index", property = "sessionIndex"),
            @Result(column = "creation_time", property = "creationTime"),
    })
    Session getSessionByID(String sessionID);

    @Select("SELECT COUNT(*) FROM session WHERE event_id = #{eventID}")
    int getSessionsCountInEvent(String eventID);

    @Select("SELECT * FROM session WHERE event_id = #{eventID}")
    List<Session> getSessionsInEvent(String eventID);

    @Select("SELECT * FROM session WHERE event_id = #{eventID} AND session_index = #{sessionIndex}")
    @ResultMap(value = "resultMap")
    Session getSessionInEventByIndex(@Param("eventID") String eventID, @Param("sessionIndex") int sessionIndex);

    @Select("SELECT * FROM session WHERE event_id = #{eventID} ORDER BY session_index LIMIT 1")
    @ResultMap(value = "resultMap")
    Session getFirstSessionInEvent(String eventID);

    @Select("SELECT * FROM session WHERE event_id = #{eventID} ORDER BY session_index DESC LIMIT 1")
    @ResultMap(value = "resultMap")
    Session getLastSessionInEvent(String eventID);

    @Select("SELECT session_reviewer_id FROM session WHERE session_id = #{sessionID}")
    List<String> getSessionReviewerID(String sessionID);

    @Select("SELECT session_chair_id FROM session WHERE session_id = #{sessionID}")
    List<String> getSessionChairID(String sessionID);


    @Select("SELECT COUNT(*) FROM session_paper WHERE session_id = #{sessionID}")
    int getSessionPapersCount(String sessionID);

    //Delete
    @Delete("DELETE FROM session WHERE session_id = #{sessionID}")
    void deleteSession(String sessionID);

    @Delete("DELETE FROM session WHERE event_id = #{eventID}")
    void deleteAllSessionsInEvent(String eventID);
}
