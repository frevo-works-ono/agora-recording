package example.agora.recording.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
public class Meeting {
    private String channelName;
    private List<String> joiningUids = new ArrayList<>();
    private String sid;
    private String resourceId;
    private String recordingUid;

    public Meeting(String channelName){
        this.channelName = channelName;
    }

    public void join(String uid){
        if(this.joiningUids.size() == 2){
            throw new RuntimeException("参加者は２人までです");
        }
        this.joiningUids.add(uid);
    }

    public void leave(String uid){
        this.joiningUids.remove(uid);
        this.recordingUid = null;
        this.sid = null;
        this.resourceId = null;
    }

    public boolean canRecording(){
        if(this.joiningUids.size() == 2){
            Random rand = new Random();
            this.recordingUid = String.valueOf(rand.nextInt(1000000));
            return true;
        }
        return false;
    }

    public boolean isRecording(){
        return Objects.nonNull(recordingUid);
    }
}
