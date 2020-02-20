package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Agenda;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AgendaMapper {

    //Insert
    @Insert("INSERT INTO agenda (agenda_id, event, event_date, event_start_time, event_end_time, room, description)" +
            "VALUE (#{agendaID}, #{event}, #{event_date}, #{eventStartTime}, #{eventEndTime}, #{room}, #{description})")
    void createAgenda(Agenda agenda);

    //Update
    @Update("UPDATE agenda SET event = #{event}, event_date = #{eventDate}, event_start_time = #{eventStartTime}, " +
            "event_end_time = {eventEndTime}, room = #{room}, description = #{description} WHERE id = #{id}")
    void editAgendaInfo(Agenda agenda);

    //SELECT
    @Select("SELECT * FROM agenda WHERE agenda_id = #{agendaID}")
    Agenda getAgendaByID(String agendaID);

    @Select("<script>" +
            "SELECT * FROM agenda WHERE" +
            "<if test='eventDate != null'>event_date = #{eventDate}</if>" +
            "<if test='eventStartTime != null'>event_start_time &gt;= #{eventStartTime}</if>" +
            "<if test='eventEndTime != null'>event_end_time  &lt;= #{eventEndTime}</if>" +
            "<if test='room != null'>room = #{room}</if>" +
            "ORDER BY event_start_time, event_end_time" +
            "</script>")
    List<Agenda> getAgenda(@Param("eventDate") String eventDate,
                           @Param("eventStartTime") String eventStartTime,
                           @Param("eventEndTime") String eventEndTime,
                           @Param("room") String room);



    /*@Select("SELECT * FROM agenda WHERE event_date = #{eventDate}")
    List<Agenda> getAgendaByDate(String eventDate);

    @Select("SELECT * FROM agenda WHERE event_start_time >= #{eventStartTime} " +
            "AND event_end_time = #{eventEndTime} ORDER by event_end_time")
    List<Agenda> getAgendaByTime(@Param("eventStartTime") String eventStartTime,
                                 @Param("eventEndTime") String eventEndTime);

    @Select("SELECT * FROM agenda WHERE event_start_time >= #{eventStartTime} " +
            "ORDER BY event_start_time, event_end_time")
    List<Agenda> getAgendaByEventStartTime(String eventStartTime);

    @Select("SELECT * FROM agenda WHERE event_end_time >= #{eventEndTime} " +
            "ORDER BY event_start_time, event_end_time")
    List<Agenda> getAgendaByEventEndTime(String eventEndTime);
     */

    //Delete
    @Delete("DELETE FROM agenda WHERE agenda_id = #{agendaID}")
    void deleteAgenda(String agendaID);

}
