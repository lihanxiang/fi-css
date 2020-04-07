package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Event;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EventMapper {

    //Insert
    @Insert("INSERT INTO event (agenda_id, event_id, event_name, event_start_time, event_end_time, room, description, status, creation_time)" +
            "VALUES (#{agendaID}, #{eventID}, #{eventName}, #{eventStartTime}, #{eventEndTime}, #{room}, #{description}, #{status}, #{creationTime})")
    void createEvent(Event event);

    //Update
    @Update("UPDATE event SET event_name = #{eventName}, event_start_time = #{eventStartTime}, " +
            "event_end_time = #{eventEndTime}, room = #{room}, description = #{description} WHERE event_id = #{eventID}")
    void editEventInfo(Event event);

    @Update("UPDATE event SET status = #{status} WHERE event_id = #{eventID}")
    void changeEventStatus(@Param("eventID") String eventID, @Param("status") int status);

    //Select
    @Select("SELECT COUNT(*) FROM event WHERE agenda_id = #{agendaID} AND " +
            "event_start_time = #{eventStartTime} AND event_end_time = #{eventEndTime}")
    int checkIfEventExist(@Param("agendaID") String agendaID, @Param("eventStartTime") String eventStartTime,
                          @Param("eventEndTime") String eventEndTime);

    @Select("SELECT * FROM event WHERE event_id = #{eventID}")
    @Results(id = "resultMap", value = {
            @Result(column = "agenda_id", property = "agendaID"),
            @Result(column = "event_id", property = "eventID"),
            @Result(column = "event_name", property = "eventName"),
            @Result(column = "event_start_time", property = "eventStartTime"),
            @Result(column = "event_end_time", property = "eventEndTime")
    })
    Event getEventByID(String eventID);

    @Select("SELECT * FROM event WHERE agenda_id = #{agendaID} ORDER BY event_start_time")
    @ResultMap(value = "resultMap")
    List<Event> getEventsInAgenda(String agendaID);

    @Select("SELECT COUNT(*) FROM event WHERE agenda_id = #{agendaID}")
    int getEventsCount(String agendaID);

    //Delete
    @Delete("DELETE FROM event WHERE event_id = #{eventID}")
    void deleteEvent(String eventID);
}
