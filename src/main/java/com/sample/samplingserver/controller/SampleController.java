package com.sample.samplingserver.controller;

import com.sample.samplingserver.dto.GetNovelsDto;
import com.sample.samplingserver.dto.NovelDto;
import com.sample.samplingserver.dto.ChapterDto;
import com.sample.samplingserver.service.SampleService;
import com.sample.samplingserver.service.novelservice.NovelBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1")
@RequiredArgsConstructor
public class SampleController {
    private final NovelBinService novelBinService;

    @GetMapping("home")
    public ResponseEntity<List<NovelDto>> hotNovels() {
        return new ResponseEntity<>(novelBinService.getHotNovels(), HttpStatus.OK);
    }

    @GetMapping("novel/details")
    public ResponseEntity<NovelDto> getChapters(@RequestParam String url) {
        return new ResponseEntity<>(novelBinService.getChapters(url), HttpStatus.OK);
    }


    @GetMapping("novel/popular")
    public ResponseEntity<GetNovelsDto> getChapters() {
        return new  ResponseEntity<>(novelBinService.getPopularNovels(), HttpStatus.OK);
    }


    @GetMapping("novel/read")
    public ResponseEntity<ChapterDto> getChapter(@RequestParam String url) {
        return new ResponseEntity<>(novelBinService.getChapter(url), HttpStatus.OK);
    }
}
