package me.golf.blog.domain.like.application;

import me.golf.blog.domain.board.application.BoardReadService;
import me.golf.blog.domain.like.domain.persist.LikeRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

class LikeServiceTest {
    LikeRepository likeRepository;
    LikeCountService likeCountService;
    LikeService likeService;

    @BeforeEach
    void setUp() {
        likeRepository = mock(LikeRepository.class);
        likeCountService = mock(LikeCountService.class);
        likeService = new LikeService(likeRepository, likeCountService);
    }

    // todo : 좋아요 및 좋아요 취소 기능 mock 테스
}