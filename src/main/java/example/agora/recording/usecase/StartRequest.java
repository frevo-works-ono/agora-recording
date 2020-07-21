package example.agora.recording.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class StartRequest {
    private String cname;
    private String uid;
    private ClientRequest clientRequest = new ClientRequest();

    public StartRequest(String cname,String uid){
        this.cname = cname;
        this.uid = uid;
    }

    @Getter
    @Setter
    @ToString
    public static class ClientRequest {
        private String token;
        private RecordingConfig recordingConfig = new RecordingConfig();
        private StorageConfig storageConfig = new StorageConfig();
        private RecordingFileConfig recordingFileConfig = new RecordingFileConfig();
    }

    @Getter
    @ToString
    public static class RecordingFileConfig{
        /**
         * The format of the recorded files. avFileType can only take ["hls"], setting the recorded files to M3U8 and TS formats.
         */
        private List<String> avFileType = Arrays.asList("hls");
    }

    @Getter
    @Setter
    @ToString
    public static class RecordingConfig {
        /**
         * The channel profile. Agora Cloud Recording must use the same channel profile as the Agora RTC Native/Web SDK.
         * Otherwise, issues may occur.
         *
         * 0: Communication profile.
         * 1: Live broadcast profile.
         */
        private Integer channelType = 0;
        /**
         * The type of the media stream to subscribe to.
         *
         * 0: Subscribes to audio streams only.
         * 1: Subscribes to video streams only.
         * 2: Subscribes to both audio and video streams.
         */
        private Integer streamTypes = 2;
        /**
         * The decryption mode that is the same as the encryption mode set by the Agora Native/Web SDK.
         * When the channel is encrypted, Agora Cloud Recording uses decryptionMode to enable the built-in decryption function.
         *
         * 0: None.
         * 1: AES-128, XTS mode.
         * 2: AES-128, ECB mode.
         * 3: AES-256, XTS mode.
         */
//        private Integer decryptionMode = 0;
//        private String secret;
        /**
         * The profile of the output audio stream, including the sample rate, bitrate, encoding mode,
         * and the number of channels. You cannot set this parameter in individual recording mode.
         *
         * 0: Sample rate of 48 kHz, music encoding, mono, and a bitrate of up to 48 Kbps.
         * 1: Sample rate of 48 kHz, music encoding, mono, and a bitrate of up to 128 Kbps.
         * 2: Sample rate of 48 kHz, music encoding, stereo, and a bitrate of up to 192 Kbps.
         */
//        private Integer audioProfile = 0;
        /**
         * The type of the video stream to subscribe to.
         *
         * 0: Subscribes to the high-quality stream.
         * 1: Subscribes to the low-quality stream.
         */
//        private Integer videoStreamType = 0;
        /**
         * Agora Cloud Recording automatically stops recording and leaves the channel
         * when there is no user in the recording channel after a time period (30 seconds by default)
         * set by this parameter. The value range is from 5 to 232-1. Once the recording stops,
         * the recording service generates new recorded files if you call start for the second time.
         */
        private Integer maxIdleTime = 30;
        private TranscodingConfig transcodingConfig = new TranscodingConfig();
        private List<String> subscribeVideoUids = new ArrayList<>();
        private List<String> subscribeAudioUids = new ArrayList<>();
        private Integer subscribeUidGroup = 0;
    }

    @Getter
    @Setter
    @ToString
    public static class TranscodingConfig {
        /**
         * The width of the mixed video (pixels). width should not exceed 1920,
         * and width*height should not exceed 1920 * 1080; otherwise, an error occurs.
         */
        private Integer width = 360;
        /**
         * The height of the mixed video (pixels). height should not exceed 1920,
         * and width*height should not exceed 1920 * 1080; otherwise, an error occurs.
         */
        private Integer height = 640;
        /**
         * The video bitrate (Kbps).
         */
        private Integer bitrate = 500;
        /**
         * The video frame rate (fps).
         */
        private Integer fps = 15;
        /**
         * When mixedVideoLayout is set as 2 (vertical layout),
         * you can specify the UID of the large video window by this parameter.
         */
//        private String maxResolutionUid;
        /**
         * The video mixing layout. 0, 1, and 2 are the predefined layouts. If you set this parameter as 3,
         * you need to set the layout by the layoutConfig parameter.
         *
         * 0: Floating layout. The first user in the channel occupies the full canvas.
         * The other users occupy the small regions on top of the canvas, starting from the bottom left corner.
         * The small regions are arranged in the order of the users joining the channel.
         * This layout supports one full-size region and up to four rows of small regions on top with four regions per row,
         * comprising 17 users.
         * 1: Best fit layout. This is a grid layout. The number of columns and rows and the grid size vary depending
         * on the number of users in the channel. This layout supports up to 17 users.
         * 2: Vertical layout. One large region is displayed on the left edge of the canvas, and several smaller
         * regions are displayed along the right edge of the canvas. The space on the right
         * supports up to 2 columns of small regions with 8 regions per column. This layout supports up to 17 users.
         * 3: Customized layout. Set the layoutConfig parameter to customize the layout.
         */
        private Integer mixedVideoLayout = 1;
        /**
         * The background color of the canvas (the display window or screen) in RGB hex value.
         * The string starts with a "#". The default value is "#000000", the black color.
         */
        private String backgroundColor = "#000000";
//        private List<String> layoutConfig = Arrays.asList();
    }

    @Getter
    @Setter
    @ToString
    public static class StorageConfig {
        private Integer vendor = 1;
        private Integer region = 10;
        private String bucket = "";
        private String accessKey = "";
        private String secretKey = "";
//        private List<String> fileNamePrefix = new ArrayList<>();
    }

    @Override
    public String toString(){
        try{
            return new ObjectMapper().writeValueAsString(this);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
