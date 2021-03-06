package com.lee.ficss.service;

import com.lee.ficss.pojo.Slide;

import java.util.List;

public interface SlideService {

    //Insert
    void createSlide(Slide slide);

    //Update
    void editSlideInfo(Slide slide);

    //Select
    Slide getSlideByFileID(String slideFileID);

    List<Slide> getSlideBySubmitter(String submitterID);

    List<Slide> getSlides(String slideTitle, String commitTime);

    //Delete
    void deleteSlide(String slideFileID);
}
