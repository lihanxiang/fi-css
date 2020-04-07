package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Agenda;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AgendaMapper {

    //Insert
    @Insert("INSERT INTO agenda (conference_id, agenda_id, agenda_name, agenda_date, creation_time) " +
            "VALUE (#{conferenceID}, #{agendaID}, #{agendaName}, #{agendaDate}, #{creationTime})")
    void createAgenda(Agenda agenda);

    //Update
    @Update("UPDATE agenda SET agenda_name = #{agendaName}, agenda_date = #{agendaDate} " +
            "WHERE agenda_id = #{agendaID}")
    void editAgendaInfo(Agenda agenda);

    //SELECT
    @Select("SELECT COUNT(*) FROM agenda WHERE conference_id = #{conferenceID} AND agenda_date = #{agendaDate}")
    int checkIfAgendaExist(@Param("conferenceID") String conferenceID, @Param("agendaDate") String agendaDate);

    @Select("SELECT * FROM agenda WHERE agenda_id = #{agendaID}")
    @Results(id = "resultMap", value = {
            @Result(column = "conference_id", property = "conferenceID"),
            @Result(column = "agenda_id", property = "agendaID"),
            @Result(column = "agenda_name", property = "agendaName"),
            @Result(column = "agenda_date", property = "agendaDate"),
            @Result(column = "creation_time", property = "creationTime")
    })
    Agenda getAgendaByID(String agendaID);

    @Select("SELECT * FROM agenda WHERE conference_id = #{conferenceID} ORDER BY agenda_date")
    @ResultMap(value = "resultMap")
    List<Agenda> getAgendasInConference(String conferenceID);

    @Select("SELECT COUNT(*) FROM agenda WHERE conference_id = #{conferenceID}")
    int getAgendaCountInConference(String conferenceID);

    @Select("SELECT * FROM agenda WHERE conference_id = #{conferenceID} ORDER BY agenda_date LIMIT 1")
    @ResultMap(value = "resultMap")
    Agenda getFirstDayInConference(String conferenceID);

    @Select("SELECT agenda_date FROM agenda WHERE conference_id = #{conferenceID} ORDER BY agenda_date DESC LIMIT 1")
    String getLastDayInConference(String conferenceID);

    @Select("SELECT * FROM agenda WHERE conference_id = #{conferenceID} AND agenda_date = #{date} ORDER BY agenda_date")
    @ResultMap(value = "resultMap")
    Agenda getAgendaByDate(@Param("conferenceID") String conferenceID, @Param("date") String date);

    @Select("SELECT * FROM agenda WHERE agenda_date >= #{date} ORDER BY agenda_date")
    @ResultMap(value = "resultMap")
    List<Agenda> getValidAgenda(@Param("date") String date);

    @Select("SELECT * FROM agenda WHERE agenda_date < #{date} ORDER BY agenda_date")
    List<Agenda> getPreviousAgenda(@Param("date") String date);

    //Delete
    @Delete("DELETE FROM agenda WHERE agenda_id = #{agendaID}")
    void deleteAgenda(String agendaID);
}
