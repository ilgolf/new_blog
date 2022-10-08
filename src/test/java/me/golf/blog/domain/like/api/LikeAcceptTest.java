package me.golf.blog.domain.like.api;

import me.golf.blog.domain.like.application.LikeService;
import me.golf.blog.domain.member.WithAuthUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class LikeAcceptTest {
    @Autowired MockMvc mockMvc;
    @MockBean
    LikeService likeService;

    // 게시판에 좋아요를 누른다.
    @Test
    @WithAuthUser
    void 게시판_좋아요_누르기() throws Exception {
        // given
        when(likeService.likeBoard(anyLong(), anyLong())).thenReturn(1L);

        // when
        mockMvc.perform(post("/api/v1/likes/1"))
                .andDo(document("likes/create"))

        // then
                .andExpect(status().isCreated())
                .andDo(print());
    }

    // 게시판 좋아요 취소
    @Test
    @WithAuthUser
    void 게시판_좋아요_취소() throws Exception {
        // when
        mockMvc.perform(delete("/api/v1/likes/1"))
                .andDo(document("like/delete"))

        // then
                .andExpect(status().isNoContent())
                .andDo(print());

    }
}
