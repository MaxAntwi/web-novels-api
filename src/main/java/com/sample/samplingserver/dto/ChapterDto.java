package com.sample.samplingserver.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDto {
    private Long id;

    private int chapterNumber;
    private String title;
    private String content;
    private String date;
    private String complete;
    private String link;
    private String nextChapterLink;
    private String prevChapterLink;

    private NovelDto book;
}
