package nyongnyong.pangparty.repository.feed;

import nyongnyong.pangparty.dto.feed.FeedDto;
import nyongnyong.pangparty.dto.feed.FeedRes;
import nyongnyong.pangparty.dto.feed.PostRes;
import nyongnyong.pangparty.entity.event.Event;
import nyongnyong.pangparty.entity.feed.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByUid(Long uid);

    Optional<Post> findByUid(Long uid);

    @Query("select new nyongnyong.pangparty.dto.feed.PostRes(p.uid, p.event.uid, p.member.memberProfile.id, p.title, p.content, " +
            "p.imgUrl, p.createTime, p.modifyTime) from Post p where p.uid = :postUid")
    PostRes findPostResByUid(Long postUid);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post p set p.event = :event, p.title = :title, p.content = :content, p.imgUrl = :imgUrl where p.uid = :postUid")
    int updatePost(Long postUid, Event event, String title, String content, String imgUrl);

    @Query("select new nyongnyong.pangparty.dto.feed.FeedDto(p.uid, p.event.uid, p.member.memberProfile.id," +
            " case pl.member.uid when :memberUid then true else false end, p.title, p.content," +
            " p.imgUrl, p.createTime, p.modifyTime) from Post p" +
            " left join Event e on e.uid = p.event.uid" +
            " left join Member m on p.member.uid = m.uid" +
            " left join MemberProfile mp on m.uid = mp.member.uid" +
            " left join Friendship f on m.uid = f.followee.uid" +
            " left join PostLike pl on pl.post.uid = p.uid" +
            " where f.follower.uid = :memberUid" +
            " order by p.createTime desc")
    Page<FeedDto> findPostsByMemberUid(@Param("memberUid") Long memberUid, Pageable pageable);
}
