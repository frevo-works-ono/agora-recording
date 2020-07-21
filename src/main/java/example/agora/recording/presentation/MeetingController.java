package example.agora.recording.presentation;

import example.agora.recording.usecase.MeetingUseCase;
import example.agora.recording.usecase.QueryResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("meeting")
public class MeetingController {

    private MeetingUseCase meetingUseCase;

    public MeetingController(MeetingUseCase meetingUseCase){
        this.meetingUseCase = meetingUseCase;
    }

    @PostMapping("join")
    public ResponseEntity<String> join(@RequestBody JoinRequest request){
        meetingUseCase.join(request.getCname(),request.getUid());
        return ResponseEntity.ok().build();
    }

    @PostMapping("leave")
    public ResponseEntity<String> leave(@RequestBody LeaveRequest request){
        meetingUseCase.leave(request.getCname(),request.getUid());
        return ResponseEntity.ok().build();
    }

    @GetMapping("query")
    public ResponseEntity<QueryResult> query(@RequestParam String cname){
        return ResponseEntity.ok(meetingUseCase.query(cname));
    }
}
