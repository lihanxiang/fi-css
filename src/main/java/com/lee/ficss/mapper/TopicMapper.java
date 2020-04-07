package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Topic;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface TopicMapper {

    //Insert
    @Insert("INSERT INTO topic (conference_id, topic_id, topic_name, topic_size, creation_time) " +
            "VALUE (#{conferenceID}, #{topicID}, #{topicName}, 0, #{creationTime})")
    void createTopic(Topic topic);

    //Update
    @Insert("UPDATE topic SET topic_size = topic_size + 1 WHERE topic_id = #{topicID}")
    void increaseTopicSize(@Param("topicID") String topicID);

    @Update("UPDATE topic SET topic_name = #{topicName} WHERE topic_id = #{topicID}")
    void editTopicInfo(Topic topic);

    //Select
    @Select("SELECT COUNT(*) FROM topic WHERE conference_id = #{conferenceID} AND topic_name = #{topicName}")
    int checkIfTopicExist(@Param("conferenceID") String conferenceID, @Param("topicName") String topicName);

    @Select("SELECT * FROM topic WHERE conference_id = #{conferenceID} ORDER BY topic_name")
    @Results(id = "resultMap", value = {
            @Result(column = "conference_id", property = "conferenceID"),
            @Result(column = "topic_id", property = "topicID"),
            @Result(column = "topic_name", property = "topicName"),
            @Result(column = "topic_size", property = "topicSize"),
            @Result(column = "creation_time", property = "creationTime")
    })
    List<Topic> getAllTopicsOrderByName(String conferenceID);

    @Select("SELECT * FROM topic WHERE conference_id = #{conferenceID} ORDER BY topic_size")
    List<Topic> getAllTopicsOrderBySize(String conferenceID);

    //Delete
    @Delete("DELETE FROM topic WHERE topic_id = #{topicID}")
    void deleteTopic(String topicID);
}
