//package com.sample.samplingserver.service;
//
//import com.sample.samplingserver.entity.Book;
//import com.sample.samplingserver.entity.Chapter;
//import com.sample.samplingserver.repository.BookRepository;
//import com.sample.samplingserver.repository.ChapterRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class SampleServiceImpl implements SampleService {
//
//    private final BookRepository bookRepository;
//
//    private final ChapterRepository chapterRepository;
//
//    String url = "https://novelbin.me/";
//
//    @Override
//    public void sample() {
//        getHotNovels();
//    }
//
//    public void getHotNovels() {
//        List<Book> hotNovels = new ArrayList<>();
//        log.info("Getting Hot Novels");
//        try {
//            Document doc = Jsoup.connect(url).get();
//            Elements novels = doc.select(".index-novel .item");
//            for (Element novel : novels) {
//                String title = novel.select("h3").text();
//                String link = novel.select("a").attr("href");
//                String imageUrl = novel.select("img").attr("data-src");
//                if (imageUrl.isEmpty()) {
//                    imageUrl = novel.select("img").attr("src");
//                }
//
//                Optional<Book> bookOptional = bookRepository.findByTitle(title);
//
//                if (bookOptional.isEmpty()) {
//                    Book book = Book
//                            .builder()
//                            .title(title)
//                            .link(link)
//                            .chapters(new ArrayList<>())
//                            .type("Hot Novels")
//                            .imgUrl(imageUrl)
//                            .build();
//
//                    Book savedBook = bookRepository.save(getAllInfo(book));
//                    getAllChapters(getFirstChapter(savedBook), savedBook);
//                } else {
//                    Book book = bookOptional.get();
//                    getAllChapters(getLastUpdatedChapter(book), book);
//                }
//
//                // Print or store the details as needed
//                System.out.println("Title: " + title);
//                System.out.println("Link: " + link);
//                System.out.println("Image URL: " + imageUrl);
//                System.out.println("------------");
//            }
//            bookRepository.saveAll(hotNovels);
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//
//    }
//
//    private Chapter getLastUpdatedChapter(Book book) {
//        try {
//            // Parse the HTML
//            Document doc = Jsoup.connect(book.getLastUpdated()).get();
//
//            // Select the div with id 'chr-content'
//            Element contentElement = doc.getElementById("chr-content");
//
//            // Extract the chapter title and content
//            String chapterTitle = "";
//            String chapterText = contentElement.text();
//
//            Element anchorElement = doc.select("a.chr-title").first();
//
//            // Extract the text inside the span tag within the anchor tag
//            if (anchorElement != null) {
//                chapterTitle = anchorElement.select("span.chr-text").text();
//                System.out.println("Extracted Text: " + chapterTitle);
//            } else {
//                System.out.println("Element not found");
//            }
//
//            // Extract the next chapter link, if it exists
//            Element nextChapterElement = doc.getElementById("next_chap");
//            String nextChapterLink = null;
//
//            if (nextChapterElement != null) {
//                nextChapterLink = nextChapterElement.attr("href");
//            }
//
//            // Build the current chapter
//            return Chapter
//                    .builder()
//                    .title(chapterTitle)
//                    .chapterNumber(extractChapterNumber(chapterTitle))
//                    .content(chapterText)
//                    .book(book)
//                    .link(book.getLastUpdated())
//                    .nextLink(nextChapterLink)
//                    .build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void getAllChapters(Chapter chapter, Book book) {
//        List<Chapter> chapters = new ArrayList<>();
//        fetchChaptersRecursively(chapter, chapters, book);
//    }
//
//    private void fetchChaptersRecursively(Chapter chapter, List<Chapter> chapters, Book book) {
//        try {
//            // Parse the HTML
//            Document doc = Jsoup.connect(chapter.getLink()).get();
//
//            // Select the div with id 'chr-content'
//            Element contentElement = doc.getElementById("chr-content");
//
//            // Extract the chapter title and content
//            String chapterTitle = "";
//            String chapterText = contentElement.text();
//
//            Element anchorElement = doc.select("a.chr-title").first();
//
//            // Extract the text inside the span tag within the anchor tag
//            if (anchorElement != null) {
//                chapterTitle = anchorElement.select("span.chr-text").text();
//                System.out.println("Extracted Text: " + chapterTitle);
//            } else {
//                System.out.println("Element not found");
//            }
//
//            // Extract the next chapter link, if it exists
//            Element nextChapterElement = doc.getElementById("next_chap");
//            String nextChapterLink = null;
//
//            if (nextChapterElement != null) {
//                nextChapterLink = nextChapterElement.attr("href");
//            }
//
//            // Build the current chapter
//            Chapter currentChapter = Chapter
//                    .builder()
//                    .title(chapterTitle)
//                    .chapterNumber(extractChapterNumber(chapterTitle))
//                    .content(chapterText)
//                    .book(book)
//                    .link(chapter.getLink())
//                    .nextLink(nextChapterLink)
//                    .build();
//
//            System.out.println("Chapter Title: " + chapterTitle);
//
//            chapterRepository.save(currentChapter);
//
//            book.setLastUpdated(currentChapter.getLink());
//            bookRepository.save(book);
//
//            // Add the current chapter to the list
//            chapters.add(currentChapter);
//
//            // Recursively fetch the next chapter if there's a next link
//            if (nextChapterLink != null && !nextChapterLink.isEmpty()) {
//                Chapter nextChapter = Chapter.builder().link(nextChapterLink).build();
//                fetchChaptersRecursively(nextChapter, chapters, book);
//            }
//
//        } catch (Exception e) {
//            // Log the error and rethrow the exception if needed
//            System.err.println("Error fetching chapter: " + e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//    private Book getAllInfo (Book book) {
//        try {
//            Document doc = Jsoup.connect(book.getLink()).get();
//            // Extract alternative names
//            String alternativeNames = doc.select("li:contains(Alternative names:)").text().replace("Alternative names:", "").trim();
//            System.out.println("Alternative Names: " + alternativeNames);
//            book.setAlternativeNames(alternativeNames);
//
//            // Extract author
//            String author = doc.select("li:contains(Author:) a").text();
//            System.out.println("Author: " + author);
//            book.setAuthor(author);
//
//            // Extract genres
//            Elements genreElements = doc.select("li:contains(Genre:) a");
//            String genres = genreElements.eachText().toString();
//            System.out.println("Genres: " + genres);
//            book.setGenres(genres);
//
//            // Extract status
//            String status = doc.select("li:contains(Status:) a").text();
//            System.out.println("Status: " + status);
//            book.setStatus(status);
//
//            // Extract publisher
//            String publisher = doc.select("li:contains(Publishers:)").text().replace("Publishers:", "").trim();
//            System.out.println("Publisher: " + publisher);
//            book.setPublisher(publisher);
//
//            // Extract tags
//            Elements tagElements = doc.select("div.tag-container a");
//            String tags = tagElements.eachText().toString();
//            System.out.println("Tags: " + tags);
//            book.setTags(tags);
//
//            // Extract year of publishing
//            String yearOfPublishing = doc.select("li:contains(Year of publishing:) a").text();
//            System.out.println("Year of Publishing: " + yearOfPublishing);
//            book.setYearOfPublication(yearOfPublishing);
//            return  book;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    private Chapter getChapter(Chapter chapter) {
//
//        Boolean nextChapter = true;
//
//        try {
//            // Parse the HTML
//            Document doc = Jsoup.connect(chapter.getLink()).get();
//
//            // Select the div with id 'chr-content'
//            Element contentElement = doc.getElementById("chr-content");
//
//            String chapterTitle = contentElement.select("h4").text();
//
//            String chapterText= "";
//
//            // Extract the text, excluding the script tags and other unnecessary elements
//            chapterText = contentElement.text();
//
//            Element nextChapterElement = doc.getElementById("next_chap");
//
//            String nextChapterLink = nextChapterElement.select("a").attr("href");
//
//            // Print the extracted text
//            System.out.println("Extracted Chapter Text: ");
//            System.out.println(chapterText);
//
//            return Chapter
//                    .builder()
//                    .title(chapterTitle)
//                    .content(chapterText)
//                    .link(chapter.getLink())
//                    .addedAt(LocalDateTime.now())
//                    .nextLink(nextChapterLink)
//                    .build();
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//    private Chapter getLastChapter(Book book) {
//        String bookUrl = book.getLink();
//        try {
//            Document doc = Jsoup.connect(bookUrl).get();
//
//            Element lastChapter = doc.select("a.chapter-title").first();
//
//            String chapterTitle = "";
//
//            String chapterLink = "";
//
//            //THE LAST CHAPTER
//            if (lastChapter != null) {
//                // Extract the chapter title
//                chapterTitle = lastChapter.text();
//                chapterLink = lastChapter.attr("href");
//
//                // Print the title and link
//                System.out.println("Chapter Title: " + chapterTitle);
//                System.out.println("Chapter Link: " + chapterLink);
//            } else {
//                System.out.println("No chapter title found.");
//            }
//            System.out.println("Title: " + chapterTitle);
//            System.out.println("Link: " + chapterLink);
//
//
//            return Chapter.builder()
//                    .link(chapterLink)
//                    .chapterNumber(extractChapterNumber(chapterTitle))
//                    .title(chapterTitle)
//                    .build();
//
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//    private void continueSample(Book book) {
//        Chapter first = getFirstChapter(book);
//        Chapter last = getLastChapter(book);
//    }
//
//    private Chapter getFirstChapter(Book book) {
//        String bookUrl = book.getLink();
//        try {
//            Document doc = Jsoup.connect(bookUrl).get();
//            Element chapterList = doc.getElementById("list-chapter");
//            Elements chapters = chapterList.select("ul.list-chapter li a");
//
//            Element lastChapter = doc.select("a.chapter-title").first();
//
//            //THE LAST CHAPTER
//            if (lastChapter != null) {
//                // Extract the chapter title
//                String chapterTitle = lastChapter.text();
//                String chapterLink = lastChapter.attr("href");
//
//                // Print the title and link
//                System.out.println("Chapter Title: " + chapterTitle);
//                System.out.println("Chapter Link: " + chapterLink);
//            } else {
//                System.out.println("No chapter title found.");
//            }
//
//            String chapterTitle = chapters.get(0).text();
//            String chapterLink = chapters.get(0).attr("href");
//            System.out.println("Title: " + chapterTitle);
//            System.out.println("Link: " + chapterLink);
//
//
//            return Chapter.builder()
//                    .link(chapterLink)
//                    .chapterNumber(extractChapterNumber(chapterTitle))
//                    .title(chapterTitle)
//                    .build();
//
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//
//
//
//    private void getGenres() {
//        ArrayList<String> genres = new ArrayList<>();
//        try {
//            Document document = Jsoup.connect("https://novelbin.me/novel-book/gunsoul-a-xianxia-apocalypse").get();
//            Elements genreLinks = document.select("div.dropdown-menu a");
//
//            for (Element genreLink : genreLinks) {
//                String genre = genreLink.text();
//                genres.add(genre);
//            }
//
//            System.out.println("Genres:");
//            for (String genre : genres) {
//                System.out.println(genre);
//            }
//
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    public static int extractChapterNumber(String chapterTitle) {
//        // Match the chapter number after the word "Chapter"
//        String[] words = chapterTitle.split(" ");
//        for (int i = 0; i < words.length; i++) {
//            if (words[i].equalsIgnoreCase("Chapter") && (i + 1) < words.length) {
//                // Return the number right after "Chapter" (ignore any other numbers later)
//                try {
//                    return Integer.parseInt(words[i + 1]);
//                } catch (NumberFormatException e) {
//                    // If it fails to parse, continue searching
//                    continue;
//                }
//            }
//        }
//        return -1; // Return -1 if no chapter number is found
//    }
//
//    //    private void getChapters(Book book) {
////        String bookUrl = book.getLink();
////
////        // Set up Selenium WebDriver
////        System.setProperty("webdriver.chrome.driver", "C:\\chrome-win64\\chrome.exe"); // Update this to your actual path
////        ChromeOptions options = new ChromeOptions();
////        options.addArguments("--headless"); // Optional: run in headless mode
////        WebDriver driver = new ChromeDriver(options);
////
////        try {
////            // Navigate to the book URL
////            driver.get(bookUrl);
////
////            // Wait for the chapters to load (consider using WebDriverWait for better handling)
////            Thread.sleep(5000); // Simple sleep (consider using WebDriverWait in production)
////
////            // Get the page source after JavaScript execution
////            String pageSource = driver.getPageSource();
////            Document doc = Jsoup.parse(pageSource);
////            Element chapterList = doc.getElementById("list-chapter");
////            Elements chapters = chapterList.select("ul.list-chapter li a");
////            System.out.println("size: " + chapters.size());
////
////            for (Element chapter : chapters) {
////                String chapterTitle = chapter.text();
////                String chapterLink = chapter.attr("href");
////
////                // Print or store the details as needed
////                System.out.println("Title: " + chapterTitle);
////                System.out.println("Link: " + chapterLink);
////                System.out.println("------------");
////            }
////        } catch (Exception e) {
////            throw new RuntimeException(e);
////        } finally {
////            driver.quit(); // Ensure the driver is closed to avoid memory leaks
////        }
////
////        log.info("Getting Chapters");
////    }
//}
