package me.golf.blog.domain.board.domain.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Getter
@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BoardCount {
    private int viewCount = 0;
    private int likeCount = 0;

    public void plusLike() {
        this.likeCount ++;
    }
}