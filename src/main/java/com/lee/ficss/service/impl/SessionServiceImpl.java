package com.lee.ficss.service.impl;

import com.lee.ficss.mapper.SessionMapper;
import com.lee.ficss.pojo.Paper;
import com.lee.ficss.pojo.Session;
import com.lee.ficss.service.SessionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionMapper sessionMapper;

    public SessionServiceImpl(SessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }

    @Override
    public void createSession(Session session) {
        sessionMapper.createSession(session);
    }

    @Override
    public void editSessionInfo(Session session) {
        sessionMapper.editSessionInfo(session);
    }

    @Override
    public Session getSessionByID(String sessionID) {
        return sessionMapper.getSessionByID(sessionID);
    }

    @Override
    public List<Session> getSessions(String sessionName, String sessionRoom, String sessionDate) {
        return sessionMapper.getSessions(sessionName, sessionRoom, sessionDate);
    }

    @Override
    public List<String> getSessionReviewers(String sessionID) {
        return sessionMapper.getSessionReviewers(sessionID);
    }

    @Override
    public List<String> getSessionChairs(String sessionID) {
        return sessionMapper.getSessionChairs(sessionID);
    }

    @Override
    public List<Paper> getSessionPapers(String sessionID) {
        return sessionMapper.getSessionPapers(sessionID);
    }

    @Override
    public void deleteSession(String sessionID) {
        sessionMapper.deleteSession(sessionID);
    }
}
