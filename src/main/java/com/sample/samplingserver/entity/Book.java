//package com.sample.samplingserver.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.List;
//
//@Entity
//@Table(name = "books")
//@Builder
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Book {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String title;
//    private String imgUrl;
//    private String author;
//    private String alternativeNames;
//    private String description;
//    private String genres;
//    private String status;
//    private String publisher;
//    @Column(length = 1000)
//    private String tags;
//    private String yearOfPublication;
//    @Column(length = 1000)
//    private String link;
//    @Column(length = 1000)
//    private String LastUpdated;
//    private String complete;
//    private String type;
//
//    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
//    private List<Chapter> chapters;
//}
