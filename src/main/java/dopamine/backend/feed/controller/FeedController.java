package dopamine.backend.feed.controller;

import dopamine.backend.feed.request.FeedEditDTO;
import dopamine.backend.feed.request.FeedRequestDTO;
import dopamine.backend.feed.response.FeedResponseDTO;
import dopamine.backend.feed.service.FeedService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "인증글 API")
public class FeedController {

    private final FeedService feedService;

    /**
     * 피드 조회
     * @param feedId
     */
    @GetMapping("/feeds/{feedId}")
    public FeedResponseDTO getFeed(@PathVariable Long feedId){
        return feedService.getFeed(feedId);
    }

    @GetMapping("/feeds/order-by-date")
    public List<FeedResponseDTO> getFeedsByDate(@RequestParam Integer page){
        return feedService.feedListOrderByDate(page);
    }

    @GetMapping("/feeds/order-by-likecount")
    public void getFeedsByLikeCount(@RequestParam Integer page){
        // todo

        feedService.feedListOrderByLikeCount(page);
    }

    /**
     * 피드 생성
     * @param feedRequestDTO
     */
    @PostMapping("/feeds")
    public void postFeed(@RequestBody FeedRequestDTO feedRequestDTO){
        feedService.postFeed(feedRequestDTO);
    }

    /**
     * 피드 수정
     * @param feedId
     * @param feedEditDTO
     */
    @PutMapping("/feeds/{feedId}")
    public void editFeed(@PathVariable Long feedId, @RequestBody FeedEditDTO feedEditDTO) {
        feedService.editFeed(feedId, feedEditDTO);
    }

    /**
     * 피드 기준 미달 여부 수정
     * @param feedId
     * @param value
     */
    @PatchMapping("/feeds/{feedId}/fulfill")
    public void patchFeedFulfill(@PathVariable Long feedId, @RequestParam Boolean value){}

    /**
     * 피드 삭제
     * @param feedId
     */
    @DeleteMapping("/feeds/{feedId}")
    public void deleteFeed(@PathVariable Long feedId){
        feedService.deleteFeed(feedId);
    }

    /**
     * 피드 삭제 (DB에서도)
     * @param feedId
     */
    @DeleteMapping("/feeds/{feedId}/hard")
    public void deleteFeedHard(@PathVariable Long feedId){
        feedService.deleteFeedHard(feedId);
    }
}
