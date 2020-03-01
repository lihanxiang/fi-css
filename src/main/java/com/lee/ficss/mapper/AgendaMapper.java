package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Agenda;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AgendaMapper {

    //Insert
    @Insert("INSERT INTO agenda (agenda_id, , agenda_date)" +
            "VALUE (#{agendaID}, #{agendaDate})")
    void createAgenda(Agenda agenda);

    //Update
    @Update("UPDATE agenda SET agenda_date = #{agendaDate} WHERE agenda_id = #{agendaID}")
    void editAgendaInfo(Agenda agenda);

    //SELECT
    @Select("SELECT * FROM agenda WHERE agenda_id = #{agendaID}")
    @Results(id = "resultMap", value = {
            @Result(column = "agenda_id", property = "agendaID"),
            @Result(column = "agenda_date", property = "agendaDate")
    })
    Agenda getAgendaByID(String agendaID);

    @Select("<script>" +
            "SELECT * FROM agenda WHERE 1 = 1" +
            "<if test='startDate != \"ignore\"'>AND agenda_date &gt;= #{startDate}</if>" +
            "<if test='endDate != \"ignore\"'>AND agenda_date  &lt;= #{endDate}</if>" +
            "ORDER BY agenda_date" +
            "</script>")
    @ResultMap(value = "resultMap")
    List<Agenda> getAgenda(@Param("startDate") String startDate, @Param("endDate") String endDate);

    //Delete
    @Delete("DELETE FROM agenda WHERE agenda_id = #{agendaID}")
    void deleteAgenda(String agendaID);

}
