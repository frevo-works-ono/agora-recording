package example.agora.recording.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.agora.recording.domain.Meeting;
import example.agora.recording.domain.MeetingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.management.Query;

@Service
public class MeetingUseCase {

    RestTemplate restTemplate;

    private MeetingRepository meetingRepository;

    @Value("${agora.app.id}")
    private String appId;

    @Value("${agora.token}")
    private String token;

    @Value("${aws.access.key}")
    private String awsAccessKey;

    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @Value("${s3.bucket}")
    private String bucket;

    private final String BASE_URL = "https://api.agora.io/v1/apps/%s/cloud_recording";
    private final String ACQUIRE_URL = BASE_URL + "/acquire";
    private final String START_URL = BASE_URL + "/resourceid/%s/mode/mix/start";
    private final String STOP_URL = BASE_URL + "/resourceid/%s/sid/%s/mode/mix/stop";
    private final String QUERY_URL = BASE_URL + "/resourceid/%s/sid/%s/mode/mix/query";

    public MeetingUseCase(RestTemplate restTemplate,MeetingRepository meetingRepository){
        this.restTemplate = restTemplate;
        this.meetingRepository = meetingRepository;
    }

    public void join(String channelName,String uid){

        Meeting entity = meetingRepository.findByChannelName(channelName).orElse(new Meeting(channelName));
        entity.join(uid);

        if(entity.canRecording()){
            // 録画開始

            AcquireResult acquireResult = restTemplate.postForEntity(String.format(ACQUIRE_URL,appId),new AcquireRequest(channelName,entity.getRecordingUid()), AcquireResult.class).getBody();
            entity.setResourceId(acquireResult.getResourceId());

            StartRequest startRequest = new StartRequest(channelName,entity.getRecordingUid());
            startRequest.getClientRequest().setToken(token);
            StartRequest.RecordingConfig recordingConfig = startRequest.getClientRequest().getRecordingConfig();
            recordingConfig.getSubscribeAudioUids().addAll(entity.getJoiningUids());
            recordingConfig.getSubscribeVideoUids().addAll(entity.getJoiningUids());
            StartRequest.StorageConfig storageConfig = startRequest.getClientRequest().getStorageConfig();
            storageConfig.setBucket(bucket);
            storageConfig.setAccessKey(awsAccessKey);
            storageConfig.setSecretKey(awsSecretKey);
            StartResult startResult = restTemplate.postForEntity(String.format(START_URL,appId,acquireResult.getResourceId()),startRequest, StartResult.class).getBody();
            System.out.println(startRequest.toString());
            entity.setSid(startResult.getSid());

            System.out.println(entity.getSid());
        }

        meetingRepository.save(entity);
    }

    public void leave(String channelName,String uid){
        Meeting entity = meetingRepository.findByChannelName(channelName)
                .orElseThrow(() -> new RuntimeException("該当チャンネルが存在しません"));
        if(entity.isRecording()) {
            restTemplate.postForEntity(String.format(STOP_URL, appId,entity.getResourceId(), entity.getSid()), new StopRequest(channelName, entity.getRecordingUid()), StopResult.class);
        }
        entity.leave(uid);
        meetingRepository.save(entity);
    }

    public QueryResult query(String channelName){
        Meeting entity = meetingRepository.findByChannelName(channelName)
                .orElseThrow(() -> new RuntimeException("該当チャンネルが存在しません"));
        return restTemplate.getForEntity(String.format(QUERY_URL, appId,entity.getResourceId(), entity.getSid()), QueryResult.class).getBody();
    }
}
