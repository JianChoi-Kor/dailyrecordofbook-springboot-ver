package com.community.dailyrecordofbook.main.entity;

import com.community.dailyrecordofbook.main.dto.AddBook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookSlide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String bookTitle;
    private String communityInfo;
    @Setter
    private String bookImg;

    public BookSlide(AddBook addBook) {
        this.bookTitle = addBook.getBookTitle();
        this.communityInfo = addBook.getCommunityInfo();
    }
}
