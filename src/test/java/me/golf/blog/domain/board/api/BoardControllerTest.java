package me.golf.blog.domain.board.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.golf.blog.domain.board.application.BoardReadService;
import me.golf.blog.domain.board.application.BoardService;
import me.golf.blog.domain.board.domain.redisForm.BoardRedisEntity;
import me.golf.blog.domain.board.dto.*;
import me.golf.blog.domain.board.util.GivenBoard;
import me.golf.blog.domain.board.util.GivenBoardCount;
import me.golf.blog.domain.boardCount.domain.persist.BoardCount;
import me.golf.blog.domain.member.WithAuthUser;
import me.golf.blog.domain.member.util.GivenMember;
import me.golf.blog.global.common.PageCustomResponse;
import me.golf.blog.global.common.SliceCustomResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
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
    @DisplayName("?????? ?????? ????????? ????????? ????????????.")
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
                                fieldWithPath("title").description("????????? ??????"),
                                fieldWithPath("content").description("????????? ??????")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("id ?????? ????????? ???????????? ????????? ???????????????.")
    @WithAuthUser
    void findById() throws Exception {
        BoardCount boardCount = GivenBoardCount.toEntityWithId();
        BoardResponse boardResponse = BoardResponse.of
                (new BoardRedisEntity(GivenBoard.toEntityWithBoardCount(boardCount)), 0);

        when(boardReadService.findById(any())).thenReturn(boardResponse);

        mockMvc.perform(get("/api/v1/public/boards/id/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/findById",
                        responseFields(
                                fieldWithPath("title").description("????????? ??????"),
                                fieldWithPath("content").description("????????? ??????"),
                                fieldWithPath("lastModifiedAt").description("????????? ?????? ??????"),
                                fieldWithPath("createdBy").description("????????? ????????????"),
                                fieldWithPath("view").description("????????? ?????? ???")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void findAll() throws Exception {
        BoardCount boardCount = GivenBoardCount.toEntityWithId();
        List<BoardAllResponse> boards = List.of(BoardAllResponse.of(toEntityWithBoardCount(boardCount)));
        Pageable pageable = PageRequest.of(0, 10);

        PageCustomResponse<BoardAllResponse> response = PageCustomResponse.of(new PageImpl<>(boards, pageable, 1));

        when(boardReadService.findAll(any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/public/boards")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/findAll",
                        responseFields(
                                fieldWithPath("data.[].title").description("????????? ??????"),
                                fieldWithPath("data.[].content").description("????????? ??????"),
                                fieldWithPath("data.[].createdBy").description("????????? ?????????"),
                                fieldWithPath("data.[].createdAt").description("????????? ?????? ??????"),
                                fieldWithPath("totalPage").description("?????? ??? ?????????"),
                                fieldWithPath("pageSize").description("????????? ?????????"),
                                fieldWithPath("totalElements").description("????????? ??? ??????"),
                                fieldWithPath("number").description("????????? ??????")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void findSearch() throws Exception {
        BoardCount boardCount = GivenBoardCount.toEntityWithId();
        List<BoardAllResponse> boards = List.of(BoardAllResponse.of(toEntityWithBoardCount(boardCount)));

        Pageable pageable = PageRequest.of(0, 10);

        PageCustomResponse<BoardAllResponse> response = PageCustomResponse.of(new PageImpl<>(boards, pageable, 1));

        when(boardReadService.findAll(any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/public/boards")
                        .param("title", GIVEN_TITLE.title())
                        .param("content", GIVEN_CONTENT.content())
                        .param("email", GivenMember.GIVEN_EMAIL.email())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/search",
                        requestParameters(
                                parameterWithName("title").description("?????? ?????? ?????????"),
                                parameterWithName("content").description("?????? ?????? ?????????"),
                                parameterWithName("email").description("?????? ????????? ?????????")),
                        responseFields(
                                fieldWithPath("data.[].title").description("????????? ??????"),
                                fieldWithPath("data.[].content").description("????????? ??????"),
                                fieldWithPath("data.[].createdBy").description("????????? ?????????"),
                                fieldWithPath("data.[].createdAt").description("????????? ?????? ??????"),
                                fieldWithPath("totalPage").description("?????? ??? ?????????"),
                                fieldWithPath("pageSize").description("????????? ?????????"),
                                fieldWithPath("totalElements").description("????????? ??? ??????"),
                                fieldWithPath("number").description("????????? ??????")
                        )))
                .andDo(print());
    }

    @Test
    void findByEmail() throws Exception {
        List<BoardAllResponse> boards = List.of(BoardAllResponse.of(toEntityWithBoardCount(new BoardCount())));
        Pageable pageable = PageRequest.of(0, 10);

        PageCustomResponse<BoardAllResponse> response = PageCustomResponse.of(new PageImpl<>(boards, pageable, 1));


        when(boardReadService.findByEmail(any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/public/boards/email/ilgolc@naver.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/findByEmail",
                        responseFields(
                                fieldWithPath("data.[].title").description("????????? ??????"),
                                fieldWithPath("data.[].content").description("????????? ??????"),
                                fieldWithPath("data.[].createdBy").description("????????? ?????????"),
                                fieldWithPath("data.[].createdAt").description("????????? ?????? ??????"),
                                fieldWithPath("totalPage").description("?????? ??? ?????????"),
                                fieldWithPath("pageSize").description("????????? ?????????"),
                                fieldWithPath("totalElements").description("????????? ??? ??????"),
                                fieldWithPath("number").description("????????? ??????")
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
                                fieldWithPath("title").description("??????"),
                                fieldWithPath("content").description("??????")
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
    @DisplayName("???????????? ????????? ????????????.")
    @WithAuthUser
    void createTempBoard() throws Exception {
        // given
        TempBoardCreateRequest request = new TempBoardCreateRequest(
                GIVEN_TITLE, GIVEN_CONTENT);

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
                                fieldWithPath("title").description("?????? ????????? ??????"),
                                fieldWithPath("content").description("?????? ????????? ??????")
                        )))

        // then
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("?????? ????????? ????????? ????????????.")
    @WithAuthUser
    void getTempBoardListTest() throws Exception {
        // given
        TempBoardListResponse response = new TempBoardListResponse(
                GIVEN_TITLE, GIVEN_CONTENT);

        List<TempBoardListResponse> tempBoardList = List.of(response);
        Pageable pageable = PageRequest.of(0, 10);

        PageCustomResponse<TempBoardListResponse> responses =
                PageCustomResponse.of(new PageImpl<>(tempBoardList, pageable, 1));

        when(boardReadService.getTempBoardList(any(), any())).thenReturn(responses);

        // when
        mockMvc.perform(get("/api/v1/boards/temp-board")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/tempboardList",
                        responseFields(
                                fieldWithPath("data.[].title").description("?????? ????????? ??????"),
                                fieldWithPath("data.[].content").description("?????? ????????? ??????"),
                                fieldWithPath("totalPage").description("?????? ??? ?????????"),
                                fieldWithPath("pageSize").description("????????? ?????????"),
                                fieldWithPath("totalElements").description("????????? ??? ??????"),
                                fieldWithPath("number").description("????????? ??????")
                        )))

        // then
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("?????? ????????? ???????????? ?????? ???????????? ????????????.")
    @WithAuthUser
    void getDetailTemp() throws Exception {
        // given
        TempDetailResponse response = new TempDetailResponse(
                GIVEN_TITLE, GIVEN_CONTENT);

        when(boardReadService.getTempBoard(any(), any())).thenReturn(response);

        // when
        mockMvc.perform(get("/api/v1/boards/temp-board/1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())

        // then
                .andExpect(status().isOk())
                .andDo(document("board/tempBoardDetail",
                        responseFields(
                                fieldWithPath("title").description("?????? ?????? ?????? ??????"),
                                fieldWithPath("content").description("?????? ?????? ?????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? ????????? ???????????? ????????????.")
    @WithAuthUser
    void deleteTempBoardTest() throws Exception {
        // given

        // when
        mockMvc.perform(delete("/api/v1/boards/temp-board/1"))
                .andDo(print())
                .andDo(document("board/temp-board"))

        // then
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("???????????? ???????????? ???????????? ????????? ????????????.")
    @WithAuthUser
    void getBoardLikeList() throws Exception {
        // given
        List<LikeAllResponse> responses = List.of(new LikeAllResponse(1L, GivenMember.GIVEN_NICKNAME));
        Slice<LikeAllResponse> likeAllResponses =
                new SliceImpl<>(responses, PageRequest.of(0, 10), true);

        when(boardReadService.getBoardLikeList(anyLong(), any()))
                .thenReturn(SliceCustomResponse.of(likeAllResponses));

        // when
        mockMvc.perform(get("/api/v1/boards/1/likes")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())

        // then
                .andExpect(status().isOk())
                .andDo(document("board/likeList",
                        responseFields(
                                fieldWithPath("data.[].memberId").description("?????? ?????? ?????????"),
                                fieldWithPath("data.[].nickname").description("?????? ?????????"),
                                fieldWithPath("pageSize").description("????????? ??????"),
                                fieldWithPath("number").description("????????? ??????")
                        )));

    }
}
