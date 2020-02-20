package com.lee.ficss.service.impl;

import com.lee.ficss.mapper.SlideMapper;
import com.lee.ficss.pojo.Slide;
import com.lee.ficss.service.SlideService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlideServiceImpl implements SlideService {

    private final SlideMapper slideMapper;

    public SlideServiceImpl(SlideMapper slideMapper) {
        this.slideMapper = slideMapper;
    }

    @Override
    public void createSlide(Slide slide) {
        slideMapper.createSlide(slide);
    }

    @Override
    public void editSlideInfo(Slide slide) {
        slideMapper.editSlideInfo(slide);
    }

    @Override
    public Slide getSlideByFileID(String slideFileID) {
        return slideMapper.getSlideByFileID(slideFileID);
    }

    @Override
    public List<Slide> getSlideBySubmitter(String submitterID) {
        return slideMapper.getSlideBySubmitter(submitterID);
    }

    @Override
    public List<Slide> getSlides(String author, String slideTitle, String commitTime) {
        return slideMapper.getSlides(author, slideTitle, commitTime);
    }

    @Override
    public void deleteSlide(String slideFileID) {
        slideMapper.deleteSlide(slideFileID);
    }
}
