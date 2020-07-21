package example.agora.recording.usecase;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcquireRequest {
    private String cname;
    private String uid;
    private ClientRequest clientRequest = new ClientRequest();

    public AcquireRequest(String cname,String uid){
        this.cname = cname;
        this.uid = uid;
    }

    @Getter
    @Setter
    public static class ClientRequest {
        private Integer resourceExpiredHour = 1;
    }
}
