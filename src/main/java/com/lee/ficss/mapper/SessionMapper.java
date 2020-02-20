package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Paper;
import com.lee.ficss.pojo.Session;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SessionMapper {

    //Insert
    @Insert("INSERT INTO session (session_id, session_name, session_room, session_date)" +
            "VALUE (#{session_id}, #{session_name}, #{session_room}, #{session_date})")
    void createSession(Session session);

    //Update
    @Update("UPDATE session SET session_name = #{sessionName}, " +
            "session_room = #{sessionRoom}, session_date = #{sessionDate}")
    void editSessionInfo(Session session);

    //Select
    @Select("SELECT * FROM session WHERE session_id = #{sessionID}")
    Session getSessionByID(String sessionID);

    @Select("<script>" +
            "SELECT * FROM session WHERE" +
            "<if test='sessionName != null'>session_name = #{sessionName}</if>" +
            "<if test='sessionRoom != null'>session_room = #{sessionRoom}</if>" +
            "<if test='sessionDate != null'>session_date = #{sessionDate}</if>" +
            "ORDER BY session_date" +
            "</script>")
    List<Session> getSessions(String sessionName, String sessionRoom, String sessionDate);

    @Select("SELECT reviewer_name FROM session_reviewer WHERE session_id = #{sessionID}")
    List<String> getSessionReviewers(String sessionID);

    @Select("SELECT chair_name FROM session_chair WHERE session_id = #{sessionID}")
    List<String> getSessionChairs(String sessionID);

    @Select("SELECT * FROM paper WHERE paper_file_id IN " +
            "(SELECT paper_file_id FROM session_paper WHERE session_id = #{sessionID})")
    List<Paper> getSessionPapers(String sessionID);

    /*@Select("SELECT * FROM session WHERE session_room = #{sessionRoom}")
    List<Session> getSessionByRoom(String sessionRoom);

    @Select("SELECT * FROM session WHERE session_date = #{sessionDate}")
    List<Session> getSessionByDate(String sessionDate);*/


    //Delete
    @Delete("DELETE FROM session WHERE session_id = #{sessionID}")
    void deleteSession(String sessionID);
}
