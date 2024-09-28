package com.sample.samplingserver.service.novelservice;

import com.sample.samplingserver.dto.ChapterDto;
import com.sample.samplingserver.dto.GetNovelsDto;
import com.sample.samplingserver.dto.NovelDto;
import com.sample.samplingserver.exceptions.SamplingErrors;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NovelBinServiceImpl implements NovelBinService {
    String url = "https://novelbin.me/";
    @Override
    public List<NovelDto> getHotNovels() {

        List<NovelDto> hotNovels = new ArrayList<>();
        log.info("Getting Hot Novels");
        try {
            Document doc = Jsoup.connect(url).get();
            Elements novels = doc.select(".index-novel .item");
            for (Element novel : novels) {
                String title = novel.select("h3").text();
                String link = novel.select("a").attr("href");
                String imageUrl = novel.select("img").attr("data-src");
                if (imageUrl.isEmpty()) {
                    imageUrl = novel.select("img").attr("src");
                }

                    NovelDto book = NovelDto
                            .builder()
                            .title(title)
                            .link(link)
                            .type("Hot Novels")
                            .imgUrl(imageUrl)
                            .build();

                hotNovels.add(book);

                System.out.println("Title: " + title);
                System.out.println("Link: " + link);
                System.out.println("Image URL: " + imageUrl);
                System.out.println("------------");
            }
        } catch (IOException e) {
            log.error("An error: has occurred {}", e.getMessage());
            throw new SamplingErrors.SamplingFailedException();
        }

        return hotNovels;
    }


    @Override
    public NovelDto getChapters(String url) {
        List<ChapterDto> allChapters = new ArrayList<>();

        WebDriver driver = null;

        try {

            // Set Chrome options to enable headless mode
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--enable-lazy-load-images");
            options.addArguments("--enable-lazy-load-scripts");

            // Initialize WebDriver with headless options
            driver = new ChromeDriver(options);

            url = url + "#tab-chapters-title";

            // Load the page
            driver.get(url);

            try {
                Thread.sleep(500); // 5000 milliseconds = 5 seconds
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }

            String pageSource = driver.getPageSource();

            Document doc = Jsoup.parse(pageSource);
            Element chapterList = doc.getElementById("list-chapter");
            Elements chapters = chapterList.select("ul.list-chapter li a");
            System.out.println("size: " + chapters.size());

            NovelDto book = new NovelDto();

            // Extract alternative names
            String alternativeNames = doc.select("li:contains(Alternative names:)").text().replace("Alternative names:", "").trim();
            System.out.println("Alternative Names: " + alternativeNames);
            book.setAlternativeNames(alternativeNames);

            // Extract author
            String author = doc.select("li:contains(Author:) a").text();
            System.out.println("Author: " + author);
            book.setAuthor(author);

            String description = doc.select("div.desc-text").text();
            book.setDescription(description);

            // Extract genres
            Elements genreElements = doc.select("li:contains(Genre:) a");
            String genres = genreElements.eachText().toString();
            System.out.println("Genres: " + genres);
            book.setGenres(genres);

            // Extract status
            String status = doc.select("li:contains(Status:) a").text();
            System.out.println("Status: " + status);
            book.setStatus(status);

            // Extract publisher
            String publisher = doc.select("li:contains(Publishers:)").text().replace("Publishers:", "").trim();
            System.out.println("Publisher: " + publisher);
            book.setPublisher(publisher);

            // Extract tags
            Elements tagElements = doc.select("div.tag-container a");
            String tags = tagElements.eachText().toString();
            System.out.println("Tags: " + tags);
            book.setTags(tags);

            // Extract year of publishing
            String yearOfPublishing = doc.select("li:contains(Year of publishing:) a").text();
            System.out.println("Year of Publishing: " + yearOfPublishing);
            book.setYearOfPublication(yearOfPublishing);

            for (Element chapter : chapters) {
                String chapterTitle = chapter.text();
                String chapterLink = chapter.attr("href");

                // Print or store the details as needed
                System.out.println("Title: " + chapterTitle);
                System.out.println("Link: " + chapterLink);
                System.out.println("------------");
                allChapters.add(
                        ChapterDto
                                .builder()
                                .title(chapterTitle)
                                .link(chapterLink)
                                .build());
            }
            book.setChapters(allChapters);
            return book;
        } catch (Exception e) {
            log.error("An error: has occurred {}", e.getMessage());
            throw new SamplingErrors.SamplingFailedException();
        } finally {
            if (driver != null) {
                driver.quit(); // Close the WebDriver
            }
        }
    }

    @Override
    public ChapterDto getChapter(String url) {

        try {
            Document doc = Jsoup.connect(url).get();
            Element contentElement = doc.getElementById("chr-content");

            // Extract the chapter title and content
            String chapterTitle = "";
            String chapterText = contentElement.text();

            Element anchorElement = doc.select("a.chr-title").first();

            // Extract the text inside the span tag within the anchor tag
            if (anchorElement != null) {
                chapterTitle = anchorElement.select("span.chr-text").text();
                System.out.println("Extracted Text: " + chapterTitle);
            } else {
                System.out.println("Element not found");
            }

            Element nextChapterElement = doc.getElementById("next_chap");
            String nextChapterLink = null;

            if (nextChapterElement != null) {
                nextChapterLink = nextChapterElement.attr("href");
            }

            Element prevChapterElement = doc.getElementById("prev_chap");
            String prevChapterLink = null;

            if (prevChapterElement != null) {
                prevChapterLink = nextChapterElement.attr("href");
            }

            return ChapterDto
                    .builder()
                    .title(chapterTitle)
                    .content(chapterText)
                    .nextChapterLink(nextChapterLink)
                    .prevChapterLink(prevChapterLink)
                    .build();
        } catch (IOException e) {
            log.error("An error: {}", e.getMessage());
            throw new SamplingErrors.SamplingFailedException();
        }

    }

    @Override
    public GetNovelsDto getPopularNovels() {
        try {

            Document doc = Jsoup.connect("https://novelbin.com/sort/top-view-novel").get();

            Elements novelRows = doc.select("div.row");

            List<NovelDto> novels = new ArrayList<>();

            System.out.print(doc);

            for (Element row : novelRows) {

                NovelDto novelDto = new NovelDto();

                // Get novel title and link
                Element titleElement = row.selectFirst(".novel-title a");
                String novelTitle = titleElement.text();
                String novelLink = titleElement.attr("href");

                novelDto.setTitle(novelTitle);
                novelDto.setLink(novelLink);

                // Get author
                String author = row.selectFirst(".author").text();
                novelDto.setAuthor(author);

                // Get image URL
                String imageUrl = row.selectFirst("img.cover").attr("src");
                novelDto.setImgUrl(imageUrl);

                // Get latest chapter and link
                Element chapterElement = row.selectFirst("div.text-info a");
                String latestChapter = chapterElement.text();
                String chapterLink = chapterElement.attr("href");
                novelDto.setLatestChapter(latestChapter);
                novelDto.setLatestChapterUrl(chapterLink);

                novelDto.setLink(novelLink);

                novels.add(novelDto);

                // Print novel details
                System.out.println("Title: " + novelTitle);
                System.out.println("Author: " + author);
                System.out.println("Image URL: " + imageUrl);
                System.out.println("Latest Chapter: " + latestChapter);
                System.out.println("Chapter Link: " + chapterLink);
                System.out.println("Novel Link: " + novelLink);
                System.out.println("--------------------------------");
            }

            return GetNovelsDto
                    .builder()
                    .novels(novels)
                    .build();

        } catch (RuntimeException | IOException e) {
            log.error("An error: has occurred {}", e.getMessage());
            throw new SamplingErrors.SamplingFailedException();
        }
    }
}