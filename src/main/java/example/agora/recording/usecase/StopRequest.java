package example.agora.recording.usecase;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class StopRequest {
    private String cname;
    private String uid;
    private Map<String,Object> clientRequest = new HashMap<>();

    public StopRequest(String cname,String uid){
        this.cname = cname;
        this.uid = uid;
    }
}
