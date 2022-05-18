package me.golf.blog.domain.board.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.golf.blog.domain.board.domain.persist.Board;
import me.golf.blog.domain.board.domain.vo.Content;
import me.golf.blog.domain.board.domain.vo.Title;
import me.golf.blog.domain.member.domain.vo.Nickname;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardAllResponse {
    private Title title;
    private Content content;
    private Nickname nickname;
    private LocalDateTime createdAt;

    public static BoardAllResponse of(final Board board) {
        return new BoardAllResponse(board.getTitle(), board.getContent(), board.getMember().getNickname(), board.getCreateTime());
    }
}
