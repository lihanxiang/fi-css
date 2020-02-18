package com.lee.ficss.mapper;

import com.lee.ficss.pojo.Topic;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface TopicMapper {

    //Insert
    @Insert("INSERT INTO topic (topic_id, content) VALUE (#{topicID}, #{content})")
    void createTopic(Topic topic);

    //Update
    @Update("UPDATE topic SET content = #{content} WHERE topic_id = #{topicID}")
    void editTopicInfo(Topic topic);

    //Select
    @Select("SELECT * FROM topic")
    List<Topic> getAllTopics();

    //Delete
    @Delete("DELETE FROM topic WHERE topic_id = #{topicID}")
    void deleteTopic(String topicID);
}
