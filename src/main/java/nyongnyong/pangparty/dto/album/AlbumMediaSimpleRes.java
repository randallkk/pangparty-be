package nyongnyong.pangparty.dto.album;

import lombok.*;
import nyongnyong.pangparty.entity.album.AlbumMedia;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
public class AlbumMediaSimpleRes {

    private Long uid;
    private Long memberUid;
    @URL(message = "URL 형식이 아닙니다.")
    private String thumbnailUrl;
    private String extension;

    // Entity -> Dto
    public AlbumMediaSimpleRes(AlbumMedia albumMedia) {
        this.uid = albumMedia.getUid();
        this.memberUid = albumMedia.getMember().getUid();
        this.thumbnailUrl = albumMedia.getThumbnailUrl();
        this.extension = albumMedia.getExtension();
    }

}
