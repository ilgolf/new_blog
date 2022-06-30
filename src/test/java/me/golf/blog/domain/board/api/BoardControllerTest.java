package me.golf.blog.domain.board.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.golf.blog.domain.board.application.BoardReadService;
import me.golf.blog.domain.board.application.BoardService;
import me.golf.blog.domain.board.dto.*;
import me.golf.blog.domain.board.util.GivenBoardCount;
import me.golf.blog.domain.boardCount.domain.persist.BoardCount;
import me.golf.blog.domain.member.WithAuthUser;
import me.golf.blog.domain.member.util.GivenMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static me.golf.blog.domain.board.util.GivenBoard.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class BoardControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean BoardService boardService;
    @MockBean BoardReadService boardReadService;
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("요청 받은 게시판 정보를 저장한다.")
    @WithAuthUser
    void create() throws Exception {
        // given
        BoardCreateRequest createRequest = BoardCreateRequest.of(toEntity());
        String body = objectMapper.writeValueAsString(createRequest);

        // when, then
        when(boardService.create(any(), any())).thenReturn(1L);

        mockMvc.perform(post("/api/v1/boards").content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("board/create",
                        requestFields(
                                fieldWithPath("title").description("게시물 제목"),
                                fieldWithPath("content").description("게시물 내용"),
                                fieldWithPath("boardImage").description("게시물 이미지")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("id 값을 이용해 게시판을 상세히 조회해온다.")
    @WithAuthUser
    void findById() throws Exception {
        BoardCount boardCount = GivenBoardCount.toEntityWithId();
        BoardResponse boardResponse = BoardResponse.of(BoardDTO.of(toEntityWithBoardCount(boardCount)), 0);

        when(boardReadService.findById(any())).thenReturn(boardResponse);

        mockMvc.perform(get("/api/v1/public/boards/id/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/findById",
                        responseFields(
                                fieldWithPath("title").description("게시물 제목"),
                                fieldWithPath("content").description("게시물 내용"),
                                fieldWithPath("boardImage").description("게시물 이미지"),
                                fieldWithPath("lastModifiedAt").description("게시물 수정 이력"),
                                fieldWithPath("createdBy").description("게시물 생성시간"),
                                fieldWithPath("view").description("게시물 조회 수")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("전체 조회 테스트")
    void findAll() throws Exception {
        BoardCount boardCount = GivenBoardCount.toEntityWithId();
        List<BoardAllResponse> boards = List.of(BoardAllResponse.of(toEntityWithBoardCount(boardCount)));

        when(boardReadService.findAll(any(), any())).thenReturn(boards);

        mockMvc.perform(get("/api/v1/public/boards")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/findAll",
                        responseFields(
                                fieldWithPath("[].title").description("게시물 제목"),
                                fieldWithPath("[].content").description("게시물 내용"),
                                fieldWithPath("[].createdBy").description("게시물 작성자"),
                                fieldWithPath("[].createdAt").description("게시물 작성 시간")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("검색 조회 테스트")
    void findSearch() throws Exception {
        BoardCount boardCount = GivenBoardCount.toEntityWithId();
        List<BoardAllResponse> boards = List.of(BoardAllResponse.of(toEntityWithBoardCount(boardCount)));

        when(boardReadService.findAll(any(), any())).thenReturn(boards);

        mockMvc.perform(get("/api/v1/public/boards")
                        .param("title", GIVEN_TITLE.title())
                        .param("content", GIVEN_CONTENT.content())
                        .param("email", GivenMember.GIVEN_EMAIL.email())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/search",
                        requestParameters(
                                parameterWithName("title").description("검색 제목 키워드"),
                                parameterWithName("content").description("검색 내용 키워드"),
                                parameterWithName("email").description("검색 이메일 키워드")),
                        responseFields(
                                fieldWithPath("[].title").description("게시물 제목"),
                                fieldWithPath("[].content").description("게시물 내용"),
                                fieldWithPath("[].createdBy").description("게시물 작성자"),
                                fieldWithPath("[].createdAt").description("게시물 작성 시간")
                        )))
                .andDo(print());
    }

    @Test
    void findByEmail() throws Exception {
        List<BoardAllResponse> boards = List.of(BoardAllResponse.of(toEntityWithBoardCount(new BoardCount())));

        when(boardReadService.findByEmail(any(), any())).thenReturn(boards);

        mockMvc.perform(get("/api/v1/public/boards/email/ilgolc@naver.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/findByEmail",
                        responseFields(
                                fieldWithPath("[].title").description("게시물 제목"),
                                fieldWithPath("[].content").description("게시물 내용"),
                                fieldWithPath("[].createdBy").description("게시물 작성자"),
                                fieldWithPath("[].createdAt").description("게시물 작성 시간")
                        )))
                .andDo(print());
    }

    @Test
    @WithAuthUser
    void updateTest() throws Exception {
        BoardUpdateRequest request = BoardUpdateRequest.of(GIVEN_TITLE, GIVEN_CONTENT);
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/v1/boards/1")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/update",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )))
                .andDo(print());
    }

    @Test
    @WithAuthUser
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/api/v1/boards/1"))
                .andExpect(status().isNoContent())
                .andDo(document("board/delete"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시판을 임시로 저장한다.")
    @WithAuthUser
    void createTempBoard() throws Exception {
        // given
        TempBoardCreateRequest request = new TempBoardCreateRequest(
                GIVEN_TITLE, GIVEN_CONTENT, BOARD_IMAGE);

        String body = objectMapper.writeValueAsString(request);

        Long boardId = 1L;

        when(boardService.createTemp(any(), any())).thenReturn(boardId);

        // when
        mockMvc.perform(post("/api/v1/boards/temp-board")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/tempboard",
                        requestFields(
                                fieldWithPath("title").description("임시 저장될 제목"),
                                fieldWithPath("content").description("임시 저장될 내용"),
                                fieldWithPath("boardImage").description("임시 저장될 게시판 이미지")
                        )))

        // then
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("임시 게시판 목록을 불러온다.")
    @WithAuthUser
    void getTempBoardListTest() throws Exception {
        // given
        TempBoardListResponse response = new TempBoardListResponse(
                GIVEN_TITLE, GIVEN_CONTENT, BOARD_IMAGE);

        when(boardReadService.getTempBoardList(any(), any())).thenReturn(response);

        // when
        mockMvc.perform(get("/api/v1/boards/temp-board")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/tempboardList",
                        responseFields(
                                fieldWithPath("title").description("임시 저장된 제목"),
                                fieldWithPath("content").description("임시 저장돤 내용"),
                                fieldWithPath("boardImage").description("임시 저장된 게시판")
                        )))

        // then
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("임시 저장된 게시글을 상세 페이지로 불러온다.")
    @WithAuthUser
    void getDetailTemp() throws Exception {
        // given
        TempDetailResponse response = new TempDetailResponse(
                GIVEN_TITLE, GIVEN_CONTENT, BOARD_IMAGE);

        when(boardReadService.getTempBoard(any(), any())).thenReturn(response);

        // when
        mockMvc.perform(get("/api/v1/boards/temp-board/1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())

        // then
                .andExpect(status().isOk())
                .andDo(document("board/tempBoardDetail",
                        responseFields(
                                fieldWithPath("title").description("임시 상세 조회 제목"),
                                fieldWithPath("content").description("임시 상세 조회 내용"),
                                fieldWithPath("boardImage").description("임시 상세 조회 게시판 이미지")
                        )));
    }

    @Test
    @DisplayName("임시 저장된 게시글을 삭제한다.")
    @WithAuthUser
    void deleteTempBoardTest() throws Exception {
        // given

        // when
        mockMvc.perform(delete("/api/v1/board/temp-board/1"))
                .andDo(print())
                .andDo(document("board/temp-board"))

        // then
                .andExpect(status().isOk());
    }
}
