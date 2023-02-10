package nyongnyong.pangparty.controller.rollingpaper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nyongnyong.pangparty.dto.rollingpaper.RollingPaperPieceReq;
import nyongnyong.pangparty.dto.rollingpaper.RollingPaperPieceRes;
import nyongnyong.pangparty.dto.rollingpaper.RollingPaperStickerReq;
import nyongnyong.pangparty.service.rollingpaper.RollingPaperPieceService;
import nyongnyong.pangparty.service.rollingpaper.RollingPaperService;
import nyongnyong.pangparty.service.rollingpaper.RollingPaperStickerService;
import nyongnyong.pangparty.service.rollingpaper.StickerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/events/{eventUid}/rollingpaper")
@RequiredArgsConstructor
public class RollingPaperController {

    private final StickerService stickerService;
    private final RollingPaperService rollingPaperService;
    private final RollingPaperPieceService rollingPaperPieceService;
    private final RollingPaperStickerService rollingPaperStickerService;

    @GetMapping("/{rollingPaperUid}/pieces")
    public ResponseEntity<?> findRollingPaperPieces(@PathVariable("eventUid") Long eventUid, @PathVariable("rollingPaperUid") Long rollingPaperUid, Pageable pageable) {
        if (eventUid < 0 || rollingPaperUid < 0 || pageable.getPageNumber() < 0 || pageable.getPageSize() < 0) {
            return ResponseEntity.badRequest().build();
        }

        // Validate eventUid and rollingPaperUid
        // TODO validate eventUid
        if (!rollingPaperService.isExistRollingPaperByRollingPaperUid(rollingPaperUid)) {
            return ResponseEntity.badRequest().build();
        }

        Map<String, Object> response = new HashMap<>();

        Page<RollingPaperPieceRes> rollingPaperPiecePage = rollingPaperPieceService.findRollingPaperPieces(rollingPaperUid, pageable);
        response.put("size", pageable.getPageSize());
        response.put("page", pageable.getPageNumber());
        response.put("itemCnt", rollingPaperPiecePage.getTotalElements());
        response.put("totalPageCnt", rollingPaperPiecePage.getTotalPages());
        response.put("rollingPaperPieces", rollingPaperPiecePage.getContent());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{rollingPaperUid}/pieces")
    public ResponseEntity<?> addRollingPaperPiece(@PathVariable("eventUid") Long eventUid, @PathVariable("rollingPaperUid") Long rollingPaperUid, @RequestBody RollingPaperPieceReq rollingPaperPieceReq) {
        // TODO check login status
        // TODO if login, set as event participant

        // Validate Path Variable and Request Body
        if (eventUid < 0 || rollingPaperUid < 0) {
            return ResponseEntity.badRequest().build();
        }

        // Validate eventUid and rollingPaperUid
        // TODO validate eventUid
        if (!rollingPaperService.isExistRollingPaperByRollingPaperUid(rollingPaperUid)) {
            return ResponseEntity.badRequest().build();
        }

        rollingPaperPieceReq.setRollingPaperUid(rollingPaperUid);
        // TODO get member
        Long uid = rollingPaperPieceService.addRollingPaperPiece(rollingPaperPieceReq);

        return ResponseEntity.created(URI.create("/events/" + eventUid + "/rollingpaper/" + rollingPaperUid + "/pieces/" + uid)).build();
    }


    @GetMapping("/{rollingPaperUid}/stickers")
    public ResponseEntity<?> findRollingPaperStickersByTopLoc(@PathVariable("eventUid") Long eventUid, @PathVariable("rollingPaperUid") Long rollingPaperUid, @RequestParam int topStart, @RequestParam int topEnd) {
        // Validate Path Variable
        if (eventUid < 0 || rollingPaperUid < 0 || topStart < 0 || topEnd < 0 || topStart > topEnd) {
            return ResponseEntity.badRequest().build();
        }

        // Validate eventUid and rollingPaperUid
        // TODO validate eventUid
        if (!rollingPaperService.isExistRollingPaperByRollingPaperUid(rollingPaperUid)) {
            return ResponseEntity.badRequest().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("rollingPaperStickers", rollingPaperStickerService.findRollingPaperStickersByTopLoc(rollingPaperUid, topStart, topEnd));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{rollingPaperUid}/stickers")
    public ResponseEntity<?> addRollingPaperSticker(@PathVariable("eventUid") Long eventUid, @PathVariable("rollingPaperUid") Long rollingPaperUid, @RequestBody RollingPaperStickerReq rollingPaperStickerReq) {
        // TODO check login status
        // TODO if login, set as event participant

        // Validate Path Variable and Request Body
        if (eventUid < 0 || rollingPaperUid < 0 || rollingPaperStickerReq.getLeftLoc() < 0 || rollingPaperStickerReq.getTopLoc() < 0) {
            return ResponseEntity.badRequest().build();
        }

        // Validate eventUid and rollingPaperUid, stickerUid
        // TODO validate eventUid
        if (!rollingPaperService.isExistRollingPaperByRollingPaperUid(rollingPaperUid) || !stickerService.isExistStickerByStickerUid(rollingPaperStickerReq.getStickerUid())) {
            return ResponseEntity.badRequest().build();
        }

        rollingPaperStickerReq.setRollingPaperUid(rollingPaperUid);
        // TODO set member
        Long uid = rollingPaperStickerService.addRollingPaperSticker(rollingPaperStickerReq);

        return ResponseEntity.created(URI.create("/events/" + eventUid + "/rollingpaper/" + rollingPaperUid + "/stickers/" + uid)).build();
    }
}
