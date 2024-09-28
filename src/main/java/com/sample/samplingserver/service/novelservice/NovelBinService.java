package com.sample.samplingserver.service.novelservice;

import com.sample.samplingserver.dto.GetNovelsDto;
import com.sample.samplingserver.dto.NovelDto;
import com.sample.samplingserver.dto.ChapterDto;

import java.util.List;

public interface NovelBinService {

    List<NovelDto> getHotNovels();

    NovelDto getChapters(String url);

    ChapterDto getChapter(String url);

    GetNovelsDto getPopularNovels();
}
