package com.lee.ficss.service;

import com.lee.ficss.pojo.Slide;

import java.util.List;

public interface SlideService {

    //Insert
    void createSlide(Slide slide);

    //Update
    void editSlideInfo(Slide slide);

    //Select
    Slide getSlideByID(int id);

    Slide getSlideByFileID(String slideFileID);

    List<Slide> getSlideByAuthor(String author);

    List<Slide> getSlideByDate(String submitDate);

    //Delete
    void deleteSlide(int id);
}
