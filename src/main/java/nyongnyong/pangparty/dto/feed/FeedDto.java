package nyongnyong.pangparty.dto.feed;

import lombok.Builder;

import java.time.LocalDateTime;

public class FeedDto {
    private Long uid;
    private Long postuid;
    private Long memberUid;
    private LocalDateTime likeTime;

    @Builder
    public FeedDto(Long uid, Long postuid, Long memberUid, LocalDateTime likeTime) {
        this.uid = uid;
        this.postuid = postuid;
        this.memberUid = memberUid;
        this.likeTime = likeTime;
    }
}
