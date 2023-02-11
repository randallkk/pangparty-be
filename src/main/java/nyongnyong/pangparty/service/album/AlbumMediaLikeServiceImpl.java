package nyongnyong.pangparty.service.album;

import lombok.extern.slf4j.Slf4j;
import nyongnyong.pangparty.entity.album.AlbumMediaLike;
import nyongnyong.pangparty.exception.MemberNotFoundException;
import nyongnyong.pangparty.repository.album.AlbumMediaLikeRepository;
import nyongnyong.pangparty.repository.album.AlbumMediaRepository;
import nyongnyong.pangparty.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AlbumMediaLikeServiceImpl implements AlbumMediaLikeService {

    private final AlbumMediaLikeRepository albumMediaLikeRepository;
    private final AlbumMediaRepository albumMediaRepository;
    private final MemberRepository memberRepository;

    public AlbumMediaLikeServiceImpl(AlbumMediaLikeRepository albumMediaLikeRepository, AlbumMediaRepository albumMediaRepository, MemberRepository memberRepository) {
        this.albumMediaLikeRepository = albumMediaLikeRepository;
        this.albumMediaRepository = albumMediaRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void likeAlbumMedia(Long memberUid, Long albumMediaUid) {
        albumMediaLikeRepository.save(
                AlbumMediaLike.builder()
                        .member(memberRepository.findById(memberUid).orElseThrow(MemberNotFoundException::new))
                        .albumMedia(albumMediaRepository.findById(albumMediaUid).orElseThrow())
                        .likeTime(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    @Transactional
    public void unlikeAlbumMedia(Long memberUid, Long albumMediaUid) {
        albumMediaLikeRepository.deleteByAlbumMediaUidAndMemberUid(albumMediaUid, memberUid);
    }
}
