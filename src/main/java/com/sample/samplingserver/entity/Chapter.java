//package com.sample.samplingserver.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "chapters")
//@Builder
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Chapter {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private int chapterNumber;
//    @Column(length = 1000)
//    private String title;
//    @Column(length = 1000000)
//    private String content;
//    private String date;
//    @Column(length = 1000)
//    private String complete;
//    @Column(length = 1000)
//    private String link;
//
//    private LocalDateTime addedAt;
//
//    @Column(length = 1000)
//    private String nextLink;
//
//    @ManyToOne
//    @JoinColumn(name = "book_id", nullable = false)
//    private Book book;
//}
