package me.golf.blog.domain.like.application;

import me.golf.blog.domain.board.domain.persist.BoardRepository;
import me.golf.blog.domain.like.domain.persist.LikeRepository;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LikeServiceTest {
    LikeRepository likeRepository;
    MemberRepository memberRepository;
    BoardRepository boardRepository;
    LikeService likeService;

    @BeforeEach
    void setUp() {
        likeRepository = mock(LikeRepository.class);
        memberRepository = mock(MemberRepository.class);
     트   likeService = new LikeService(likeRepository, boardRepository, memberRepository);
    }

    // todo : 좋아요 및 좋아요 취소 기능 mock 테스
}