package com.sample.samplingserver.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetNovelsDto {
    private List<NovelDto> novels;
    private String next;
    private String previous;
    private String lastQuery;
}
