package example.agora.recording.usecase;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QueryResult {
    private String resourceId;
    private String sid;

    @Getter
    @Setter
    public static class ServerResponse {
        private String fileListMode;
        private List<FileItem> fileList;
        private Integer status;
        private Integer sliceStartTime;
    }

    @Getter
    @Setter
    public static class FileItem {
        private String fileName;
        private String trackType;
        private String uid;
        private boolean mixedAllUser;
        private boolean isPlayable;
        private Integer sliceStartTime;
    }
}
