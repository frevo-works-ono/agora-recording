package example.agora.recording.domain;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MeetingRepository {
    private final Map<String, Meeting> data = new HashMap<>();

    public void save(Meeting entity){
        data.put(entity.getChannelName(),entity);
    }

    public Optional<Meeting> findByChannelName(String channelName){
        return Optional.ofNullable(data.get(channelName));
    }
}
