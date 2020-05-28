package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Conference;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface ConferenceMapper {

    //Insert
    @Insert("INSERT INTO conference (conference_id, conference_name, creation_time)" +
            "VALUE (#{conferenceID}, #{conferenceName}, #{creationTime})")
    void createConference(Conference conference);

    //Update
    @Update("UPDATE conference SET conference_name = #{conferenceName}" +
            "WHERE conference_id = #{conferenceID}")
    void editConferenceInfo(@Param("conferenceID") String conferenceID, @Param("conferenceName") String conferenceName);

    //Select
    @Select("SELECT COUNT(*) FROM conference WHERE conference_name = #{conferenceName}")
    int checkIfConferenceExist(String conferenceName);

    @Select("SELECT * FROM conference WHERE conference_id = #{conferenceID}")
    @Results(id = "resultMap", value = {
            @Result(column = "conference_id", property = "conferenceID"),
            @Result(column = "conference_name", property = "conferenceName"),
            @Result(column = "creation_time", property = "creationTime")
    })
    Conference getConferenceByID(String conferenceID);

    @Select("SELECT * FROM conference WHERE conference_id IN " +
            "(SELECT conference_id FROM agenda WHERE agenda_date >= #{date} ORDER BY agenda_date)")
    @ResultMap(value = "resultMap")
    List<Conference> getValidConferences(String date);

    @Select("SELECT * FROM conference WHERE conference_id IN " +
            "(SELECT conference_id FROM agenda WHERE agenda_date < #{date} ORDER BY agenda_date)")
    @ResultMap(value = "resultMap")
    List<Conference> getPreviousConferences(String date);

    @Select("SELECT COUNT(*) FROM conference")
    int getConferenceCount();

    //Delete
    @Delete("DELETE FROM conference WHERE conference_id = #{conferenceID}")
    void deleteConferenceByID(String conferenceID);
}
