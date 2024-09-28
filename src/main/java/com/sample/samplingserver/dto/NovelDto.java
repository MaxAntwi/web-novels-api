package com.sample.samplingserver.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NovelDto {
    private String title;
    private String imgUrl;
    private String author;
    private String alternativeNames;
    private String description;
    private String genres;
    private String status;
    private String publisher;
    private String tags;
    private String yearOfPublication;
    private String link;
    private String LastUpdated;
    private String complete;
    private String type;
    private String latestChapter;
    private String latestChapterUrl;
    private List<ChapterDto> chapters;
}
