package me.golf.blog.domain.board.domain.persist;

import me.golf.blog.domain.board.domain.vo.BoardCount;
import me.golf.blog.domain.board.domain.vo.BoardStatus;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.board.dto.BoardAllResponse;
import me.golf.blog.domain.member.domain.persist.Member;
import me.golf.blog.domain.member.domain.persist.MemberRepository;
import me.golf.blog.domain.member.util.GivenMember;
import me.golf.blog.global.config.AbstractContainerBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class BoardQueryRepositoryTest extends AbstractContainerBaseTest {

    @Autowired BoardRepository boardRepository;
    @Autowired MemberRepository memberRepository;
    static Member member;

    @BeforeEach
    void init() {
        member = memberRepository.save(GivenMember.toEntityWithCount());

        for (int i = 0; i < 20; i++) {
            Board board = Board.builder()
                    .title(Title.from("게시판 제목 " + (i + 1)))
                    .content(Content.from("게시판 내용입니다. 안녕하세요 " + (i + 1)))
                    .memberId(member.getId())
                    .status(BoardStatus.SAVE)
                    .build();
            boardRepository.save(board);
        }
    }

    @Test
    @DisplayName("모든 조건을 다 받아오면 모든 조건문이 나간다.")
    @Transactional
    void findAllKeyword() {
        // given
        SearchKeywordRequest keyword = SearchKeywordRequest.builder()
                .title("게시판 제목 3")
                .content("게시판 내용입니다.")
                .nickname(member.getNickname().nickname())
                .build();

        List<BoardAllResponse> boards =
                boardRepository.findAllWithQuery(keyword, PageRequest.of(0, 10)).getData();

        assertThat(boards.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("일부의 키워드만 보내면 동적으로 null에 대해 동작하지 않는다.")
    @Transactional
    void findPartKeyword() {
        SearchKeywordRequest keyword = SearchKeywordRequest.builder()
                .title("게시판 제목 1")
                .content("게시판 내용입니다. 안녕하세요 1")
                .nickname(null)
                .build();

        List<BoardAllResponse> boards =
                boardRepository.findAllWithQuery(keyword, PageRequest.of(0, 10)).getData();

        assertThat(boards.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("아예 키워드가 없으면 where절은 제외 된다.")
    @Transactional
    void findWithout() {
        SearchKeywordRequest keyword = SearchKeywordRequest.builder()
                .title(null)
                .content(null)
                .nickname(null)
                .build();

        List<BoardAllResponse> boards =
                boardRepository.findAllWithQuery(keyword, PageRequest.of(0, 15)).getData();

        assertThat(boards.size()).isEqualTo(15);
    }
}