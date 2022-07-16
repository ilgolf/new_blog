package me.golf.blog.global.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SliceCustomResponse<T> {
    private List<T> data = new ArrayList<>();
    private int pageSize;
    private int number;

    public static <T> SliceCustomResponse<T> of(Slice<T> response) {
        return new SliceCustomResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber());
    }
}
