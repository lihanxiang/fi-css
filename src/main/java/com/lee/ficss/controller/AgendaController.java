package com.lee.ficss.controller;

import com.lee.ficss.mapper.*;
import com.lee.ficss.pojo.Agenda;
import com.lee.ficss.pojo.Event;
import com.lee.ficss.pojo.Paper;
import com.lee.ficss.pojo.Session;
import com.lee.ficss.service.AgendaService;
import com.lee.ficss.service.ConferenceService;
import com.lee.ficss.service.TopicService;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.JsonResult;
import com.lee.ficss.util.RandomIDBuilder;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("agenda")
public class AgendaController {

    String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();

    private static final String AGENDA_LOCATION = "static/file/agenda/agenda.xlsx";

    @Autowired
    private AgendaMapper agendaMapper;
    private final AgendaService agendaService;
    private final PaperMapper paperMapper;
    private final EventMapper eventMapper;
    private final SessionMapper sessionMapper;
    private final SubmissionMapper submissionMapper;

    public AgendaController(AgendaService agendaService, PaperMapper paperMapper, EventMapper eventMapper,
                            SubmissionMapper submissionMapper, SessionMapper sessionMapper) {
        this.agendaService = agendaService;
        this.paperMapper = paperMapper;
        this.eventMapper = eventMapper;
        this.submissionMapper = submissionMapper;
        this.sessionMapper = sessionMapper;
    }

    @ResponseBody
    @RequestMapping(value = "agendas-in-conference", produces = MediaType.APPLICATION_JSON_VALUE)
    public String agendasInConference(@RequestParam("conferenceID") String conferenceID){
        return JsonResult.build(agendaService.getAgendasInConference(conferenceID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "first-day-in-conference", produces = MediaType.APPLICATION_JSON_VALUE)
    public String firstDayInConference(@RequestParam("conferenceID") String conferenceID){
        return JsonResult.build(agendaService.getFirstDayInConference(conferenceID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "get-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getByID(@RequestParam("agendaID") String agendaID){
        return JsonResult.build(agendaService.getAgendaByID(agendaID)).toJSONString();
    }

    @RequiresRoles("admin")
    @ResponseBody
    @RequestMapping(value = "edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public String edit(@RequestParam("agendaID") String agendaID, @RequestParam("agendaName") String agendaName){
        return JsonResult.build(agendaService.editAgendaInfo(agendaID, agendaName)).toJSONString();
    }

    @RequestMapping(value = "download/{agendaID}")
    public void download(@PathVariable("agendaID") String agendaID, HttpServletResponse response) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 1, 0, 13);
        sheet.addMergedRegion(cellRangeAddress);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(agendaMapper.getAgendaByID(agendaID).getAgendaName());
        CellStyle titleCellStyle = workbook.createCellStyle();
        Font titleFontStyle = workbook.createFont();
        titleFontStyle.setBold(true);
        titleFontStyle.setFontName("Times New Roman");
        titleFontStyle.setFontHeightInPoints((short) 16);
        titleCellStyle.setFont(titleFontStyle);
        titleCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        titleCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        titleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        titleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleCell.setCellStyle(titleCellStyle);

        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

        cellRangeAddress = new CellRangeAddress(2, 2, 0, 13);
        sheet.addMergedRegion(cellRangeAddress);
        Row blankRow = sheet.createRow(2);
        Cell blankCell = blankRow.createCell(0);
        CellStyle blankCellStyle = workbook.createCellStyle();
        blankCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        blankCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        blankCell.setCellStyle(blankCellStyle);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

        List<Event> events = eventMapper.getEventsInAgenda(agendaID);
        int eventRowAddress = 3;
        for (Event event : events) {
            cellRangeAddress = new CellRangeAddress(eventRowAddress, eventRowAddress, 0, 13);
            sheet.addMergedRegion(cellRangeAddress);

            Row eventRow = sheet.createRow(eventRowAddress++);
            Cell eventCell = eventRow.createCell(0);
            if (!event.getRoom().isEmpty()) {
                eventCell.setCellValue(event.getEventStartTime() + " - " + event.getEventEndTime() + " in " + event.getRoom());
            } else {
                eventCell.setCellValue(event.getEventStartTime() + " - " + event.getEventEndTime());
            }
            CellStyle eventCellStyle = workbook.createCellStyle();
            Font eventFontStyle = workbook.createFont();
            eventFontStyle.setBold(true);
            eventFontStyle.setFontName("Times New Roman");
            eventFontStyle.setFontHeightInPoints((short) 16);
            eventCellStyle.setFont(eventFontStyle);
            eventCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            eventCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            eventCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            eventCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            eventCellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
            eventCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            eventCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            eventCell.setCellStyle(eventCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            cellRangeAddress = new CellRangeAddress(eventRowAddress, eventRowAddress, 0, 13);
            sheet.addMergedRegion(cellRangeAddress);

            Row eventTimeRow = sheet.createRow(eventRowAddress++);
            Cell eventTimeCell = eventTimeRow.createCell(0);
            eventTimeCell.setCellValue(event.getEventName());
            CellStyle eventTimeCellStyle = workbook.createCellStyle();
            Font eventTimeFontStyle = workbook.createFont();
            eventTimeFontStyle.setBold(true);
            eventTimeFontStyle.setFontName("Times New Roman");
            eventTimeFontStyle.setFontHeightInPoints((short) 16);
            eventTimeCellStyle.setFont(eventTimeFontStyle);
            eventTimeCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            eventTimeCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            eventTimeCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            eventTimeCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            eventTimeCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            eventTimeCell.setCellStyle(eventTimeCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            if (event.getStatus() == 1) {
                List<Session> sessions = sessionMapper.getSessionsInEvent(event.getEventID());
                if (sessions.size() == 1) {
                    Session session = sessions.get(0);
                    int sessionBeginRowAddress = eventRowAddress;
                    int paperCount = 0;
                    int sessionSize = 4;

                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress, sessionBeginRowAddress,
                            0, 13);
                    sheet.addMergedRegion(cellRangeAddress);

                    Row sessionTitleRow = sheet.createRow(sessionBeginRowAddress);
                    Cell sessionTitleCell = sessionTitleRow.createCell(0);
                    sessionTitleCell.setCellValue(session.getSessionName());
                    CellStyle sessionTitleCellStyle = workbook.createCellStyle();
                    Font sessionTitleFontStyle = workbook.createFont();
                    sessionTitleFontStyle.setBold(true);
                    sessionTitleFontStyle.setFontName("Times New Roman");
                    sessionTitleFontStyle.setFontHeightInPoints((short) 16);
                    sessionTitleCellStyle.setFont(sessionTitleFontStyle);
                    sessionTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                    sessionTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                    sessionTitleCell.setCellStyle(sessionTitleCellStyle);

                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 1, sessionBeginRowAddress + 1,
                            0, 13);
                    sheet.addMergedRegion(cellRangeAddress);

                    Row sessionRoomRow = sheet.createRow(sessionBeginRowAddress + 1);
                    Cell sessionRoomCell = sessionRoomRow.createCell(0);
                    sessionRoomCell.setCellValue("Room " + session.getSessionRoom());
                    CellStyle sessionRoomCellStyle = workbook.createCellStyle();
                    Font sessionRoomFontStyle = workbook.createFont();
                    sessionRoomFontStyle.setBold(true);
                    sessionRoomFontStyle.setFontName("Times New Roman");
                    sessionRoomFontStyle.setFontHeightInPoints((short) 16);
                    sessionRoomCellStyle.setFont(sessionRoomFontStyle);
                    sessionRoomCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                    sessionRoomCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                    sessionRoomCell.setCellStyle(sessionRoomCellStyle);

                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 2, sessionBeginRowAddress + 2,
                            0, 13);
                    sheet.addMergedRegion(cellRangeAddress);

                    Row sessionReviewerRow = sheet.createRow(sessionBeginRowAddress + 2);
                    Cell sessionReviewerCell = sessionReviewerRow.createCell(0);
                    sessionReviewerCell.setCellValue("Reviewer: " + session.getSessionReviewer());
                    CellStyle sessionReviewerCellStyle = workbook.createCellStyle();
                    Font sessionReviewerFontStyle = workbook.createFont();
                    sessionReviewerFontStyle.setBold(true);
                    sessionReviewerFontStyle.setFontName("Times New Roman");
                    sessionReviewerFontStyle.setFontHeightInPoints((short) 16);
                    sessionReviewerCellStyle.setFont(sessionReviewerFontStyle);
                    sessionReviewerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                    sessionReviewerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                    sessionReviewerCell.setCellStyle(sessionReviewerCellStyle);

                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 3, sessionBeginRowAddress + 3,
                            0, 13);
                    sheet.addMergedRegion(cellRangeAddress);

                    Row sessionChairRow = sheet.createRow(sessionBeginRowAddress + 3);
                    Cell sessionChairCell = sessionChairRow.createCell(0);
                    sessionChairCell.setCellValue("Session Chair: " + session.getSessionChair());
                    CellStyle sessionChairCellStyle = workbook.createCellStyle();
                    Font sessionChairFontStyle = workbook.createFont();
                    sessionChairFontStyle.setBold(true);
                    sessionChairFontStyle.setFontName("Times New Roman");
                    sessionChairFontStyle.setFontHeightInPoints((short) 16);
                    sessionChairCellStyle.setFont(sessionChairFontStyle);
                    sessionChairCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                    sessionChairCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                    sessionChairCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                    RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                    sessionChairCell.setCellStyle(sessionChairCellStyle);

                    int paperBeginAddress = sessionBeginRowAddress + 4;

                    List<String> paperIDs = paperMapper.getPaperIDsInSession(session.getSessionID());
                    for (String paperID : paperIDs) {
                        Paper paper = paperMapper.getPaperByFileID(paperID);
                        Row paperAuthorRow = sheet.createRow(paperBeginAddress);
                        Row paperTitleRow = sheet.createRow(paperBeginAddress + 1);

                        cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress,
                                0, 13);
                        sheet.addMergedRegion(cellRangeAddress);

                        Cell paperAuthorCell = paperAuthorRow.createCell(0);
                        paperAuthorCell.setCellValue(submissionMapper.getSubmissionByPaperID(paperID).getAuthor());
                        CellStyle paperAuthorCellStyle = workbook.createCellStyle();
                        Font paperAuthorCellFontStyle = workbook.createFont();
                        paperAuthorCellFontStyle.setFontName("Times New Roman");
                        paperAuthorCellFontStyle.setFontHeightInPoints((short) 12);
                        paperAuthorCellStyle.setFont(paperAuthorCellFontStyle);
                        paperAuthorCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                        paperAuthorCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                        paperAuthorCell.setCellStyle(paperAuthorCellStyle);
                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                        cellRangeAddress = new CellRangeAddress(paperBeginAddress + 1, paperBeginAddress + 1,
                                0, 13);
                        sheet.addMergedRegion(cellRangeAddress);

                        Cell paperTitleCell = paperTitleRow.createCell(0);
                        paperTitleCell.setCellValue(paper.getPaperTitle());
                        CellStyle paperTitleCellStyle = workbook.createCellStyle();
                        Font paperTitleCellFontStyle = workbook.createFont();
                        paperTitleCellFontStyle.setFontName("Times New Roman");
                        paperTitleCellFontStyle.setFontHeightInPoints((short) 14);
                        paperTitleCellStyle.setFont(paperTitleCellFontStyle);
                        paperTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                        paperTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                        paperTitleCell.setCellStyle(paperTitleCellStyle);
                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                        paperBeginAddress += 2;
                        paperCount++;
                    }
                    sessionSize += paperCount * 2;
                    eventRowAddress += sessionSize;
                } else if (sessions.size() == 2) {
                    int sessionBeginRowAddress = eventRowAddress;
                    int sessionCount = 0;
                    int sessionSize = 4;
                    int maxPaperCount = 0;
                    Row sessionTitleRow = sheet.createRow(sessionBeginRowAddress);
                    Row sessionRoomRow = sheet.createRow(sessionBeginRowAddress + 1);
                    Row sessionReviewerRow = sheet.createRow(sessionBeginRowAddress + 2);
                    Row sessionChairRow = sheet.createRow(sessionBeginRowAddress + 3);
                    for (Session session : sessions) {
                        List<String> paperIDs = paperMapper.getPaperIDsInSession(session.getSessionID());
                        maxPaperCount = Math.max(maxPaperCount, paperIDs.size());
                    }
                    for (Session session : sessions) {
                        cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress, sessionBeginRowAddress,
                                sessionCount * 7, (sessionCount + 1) * 7 - 1);
                        sheet.addMergedRegion(cellRangeAddress);

                        Cell sessionTitleCell = sessionTitleRow.createCell(sessionCount * 7);
                        sessionTitleCell.setCellValue(session.getSessionName());
                        CellStyle sessionTitleCellStyle = workbook.createCellStyle();
                        Font sessionTitleFontStyle = workbook.createFont();
                        sessionTitleFontStyle.setBold(true);
                        sessionTitleFontStyle.setFontName("Times New Roman");
                        sessionTitleFontStyle.setFontHeightInPoints((short) 16);
                        sessionTitleCellStyle.setFont(sessionTitleFontStyle);
                        sessionTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                        sessionTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                        sessionTitleCell.setCellStyle(sessionTitleCellStyle);
                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                        cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 1, sessionBeginRowAddress + 1,
                                sessionCount * 7, (sessionCount + 1) * 7 - 1);
                        sheet.addMergedRegion(cellRangeAddress);

                        Cell sessionRoomCell = sessionRoomRow.createCell(sessionCount * 7);
                        sessionRoomCell.setCellValue("Room " + session.getSessionRoom());
                        CellStyle sessionRoomCellStyle = workbook.createCellStyle();
                        Font sessionRoomFontStyle = workbook.createFont();
                        sessionRoomFontStyle.setBold(true);
                        sessionRoomFontStyle.setFontName("Times New Roman");
                        sessionRoomFontStyle.setFontHeightInPoints((short) 16);
                        sessionRoomCellStyle.setFont(sessionRoomFontStyle);
                        sessionRoomCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                        sessionRoomCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                        sessionRoomCell.setCellStyle(sessionRoomCellStyle);

                        cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 2, sessionBeginRowAddress + 2,
                                sessionCount * 7, (sessionCount + 1) * 7 - 1);
                        sheet.addMergedRegion(cellRangeAddress);
                        Cell sessionReviewerCell = sessionReviewerRow.createCell(sessionCount * 7);
                        sessionReviewerCell.setCellValue(session.getSessionReviewer());
                        CellStyle sessionReviewerCellStyle = workbook.createCellStyle();
                        Font sessionReviewerFontStyle = workbook.createFont();
                        sessionReviewerFontStyle.setBold(true);
                        sessionReviewerFontStyle.setFontName("Times New Roman");
                        sessionReviewerFontStyle.setFontHeightInPoints((short) 16);
                        sessionReviewerCellStyle.setFont(sessionReviewerFontStyle);
                        sessionReviewerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                        sessionReviewerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                        sessionReviewerCell.setCellStyle(sessionReviewerCellStyle);
                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                        cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 3, sessionBeginRowAddress + 3,
                                sessionCount * 7, (sessionCount + 1) * 7 - 1);
                        sheet.addMergedRegion(cellRangeAddress);
                        Cell sessionChairCell = sessionChairRow.createCell(sessionCount * 7);
                        sessionChairCell.setCellValue(session.getSessionChair());
                        CellStyle sessionChairCellStyle = workbook.createCellStyle();
                        Font sessionChairFontStyle = workbook.createFont();
                        sessionChairFontStyle.setBold(true);
                        sessionChairFontStyle.setFontName("Times New Roman");
                        sessionChairFontStyle.setFontHeightInPoints((short) 16);
                        sessionChairCellStyle.setFont(sessionChairFontStyle);
                        sessionChairCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                        sessionChairCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                        sessionChairCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                        sessionChairCell.setCellStyle(sessionChairCellStyle);
                        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                        int paperBeginAddress = sessionBeginRowAddress + 4;
                        int paperCount = 0;

                        List<String> paperIDs = paperMapper.getPaperIDsInSession(session.getSessionID());
                        for (String paperID : paperIDs) {
                            Paper paper = paperMapper.getPaperByFileID(paperID);
                            Row paperAuthorRow = sheet.getRow(paperBeginAddress);
                            if (paperAuthorRow == null) {
                                paperAuthorRow = sheet.createRow(paperBeginAddress);
                            }
                            Row paperTitleRow = sheet.getRow(paperBeginAddress + 1);
                            if (paperTitleRow == null) {
                                paperTitleRow = sheet.createRow(paperBeginAddress + 1);
                            }

                            cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress,
                                    sessionCount * 7, (sessionCount + 1) * 7 - 1);
                            sheet.addMergedRegion(cellRangeAddress);

                            Cell paperAuthorCell = paperAuthorRow.createCell(sessionCount * 7);
                            paperAuthorCell.setCellValue(submissionMapper.getSubmissionByPaperID(paperID).getAuthor());
                            CellStyle paperAuthorCellStyle = workbook.createCellStyle();
                            Font paperAuthorCellFontStyle = workbook.createFont();
                            paperAuthorCellFontStyle.setFontName("Times New Roman");
                            paperAuthorCellFontStyle.setFontHeightInPoints((short) 12);
                            paperAuthorCellStyle.setFont(paperAuthorCellFontStyle);
                            paperAuthorCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                            paperAuthorCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                            paperAuthorCell.setCellStyle(paperAuthorCellStyle);
                            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                            cellRangeAddress = new CellRangeAddress(paperBeginAddress + 1, paperBeginAddress + 1,
                                    sessionCount * 7, (sessionCount + 1) * 7 - 1);
                            sheet.addMergedRegion(cellRangeAddress);

                            Cell paperTitleCell = paperTitleRow.createCell(sessionCount * 7);
                            paperTitleCell.setCellValue(paper.getPaperTitle());
                            CellStyle paperTitleCellStyle = workbook.createCellStyle();
                            Font paperTitleCellFontStyle = workbook.createFont();
                            paperTitleCellFontStyle.setFontName("Times New Roman");
                            paperTitleCellFontStyle.setFontHeightInPoints((short) 14);
                            paperTitleCellStyle.setFont(paperTitleCellFontStyle);
                            paperTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                            paperTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                            paperTitleCell.setCellStyle(paperTitleCellStyle);
                            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                            paperBeginAddress += 2;
                            paperCount++;
                        }
                        while (paperCount < maxPaperCount) {
                            Row paperAuthorRow = sheet.getRow(paperBeginAddress);
                            if (paperAuthorRow == null) {
                                paperAuthorRow = sheet.createRow(paperBeginAddress);
                            }
                            Row paperTitleRow = sheet.getRow(paperBeginAddress + 1);
                            if (paperTitleRow == null) {
                                paperTitleRow = sheet.createRow(paperBeginAddress + 1);
                            }

                            cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress,
                                    sessionCount * 7, (sessionCount + 1) * 7 - 1);
                            sheet.addMergedRegion(cellRangeAddress);

                            Cell paperAuthorCell = paperAuthorRow.createCell(sessionCount * 7);
                            paperAuthorCell.setCellValue("");
                            CellStyle paperAuthorCellStyle = workbook.createCellStyle();
                            Font paperAuthorCellFontStyle = workbook.createFont();
                            paperAuthorCellFontStyle.setFontName("Times New Roman");
                            paperAuthorCellFontStyle.setFontHeightInPoints((short) 12);
                            paperAuthorCellStyle.setFont(paperAuthorCellFontStyle);
                            paperAuthorCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                            paperAuthorCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                            paperAuthorCell.setCellStyle(paperAuthorCellStyle);
                            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                            cellRangeAddress = new CellRangeAddress(paperBeginAddress + 1, paperBeginAddress + 1,
                                    sessionCount * 7, (sessionCount + 1) * 7 - 1);
                            sheet.addMergedRegion(cellRangeAddress);

                            Cell paperTitleCell = paperTitleRow.createCell(sessionCount * 7);
                            paperTitleCell.setCellValue("");
                            CellStyle paperTitleCellStyle = workbook.createCellStyle();
                            Font paperTitleCellFontStyle = workbook.createFont();
                            paperTitleCellFontStyle.setFontName("Times New Roman");
                            paperTitleCellFontStyle.setFontHeightInPoints((short) 14);
                            paperTitleCellStyle.setFont(paperTitleCellFontStyle);
                            paperTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                            paperTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                            paperTitleCell.setCellStyle(paperTitleCellStyle);
                            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                            paperBeginAddress += 2;
                            paperCount++;
                        }
                        sessionCount++;
                    }
                    sessionSize += maxPaperCount * 2;
                    eventRowAddress += sessionSize;
                } else {
                    int part = (int) Math.ceil((double) sessions.size() / 3);
                    for (int i = 0; i < part; i++) {
                        int sessionSize = 4;
                        int maxPaperCount = 0;
                        int sessionBeginRowAddress = eventRowAddress;
                        Row sessionTitleRow = sheet.getRow(sessionBeginRowAddress);
                        if (sessionTitleRow == null) {
                            sessionTitleRow = sheet.createRow(sessionBeginRowAddress);
                        }
                        Row sessionRoomRow = sheet.getRow(sessionBeginRowAddress + 1);
                        if (sessionRoomRow == null) {
                            sessionRoomRow = sheet.createRow(sessionBeginRowAddress + 1);
                        }
                        Row sessionReviewerRow = sheet.getRow(sessionBeginRowAddress + 2);
                        if (sessionReviewerRow == null) {
                            sessionReviewerRow = sheet.createRow(sessionBeginRowAddress + 2);
                        }
                        Row sessionChairRow = sheet.getRow(sessionBeginRowAddress + 3);
                        if (sessionChairRow == null) {
                            sessionChairRow = sheet.createRow(sessionBeginRowAddress + 3);
                        }
                        for (int j = 0; j < 3 && (i * 3 + j) < sessions.size(); j++) {
                            Session session = sessions.get((i * 3 + j));
                            maxPaperCount = Math.max(maxPaperCount, paperMapper.getPaperIDsInSession(session.getSessionID()).size());
                        }
                        for (int j = 0; j < 3; j++) {
                            if ((i * 3 + j) < sessions.size()) {
                                Session session = sessions.get(i * 3 + j);
                                if (j < 2) {
                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress, sessionBeginRowAddress,
                                            j * 5, (j + 1) * 5 - 1);
                                    sheet.addMergedRegion(cellRangeAddress);

                                    Cell sessionTitleCell = sessionTitleRow.createCell(j * 5);
                                    sessionTitleCell.setCellValue(session.getSessionName());
                                    CellStyle sessionTitleCellStyle = workbook.createCellStyle();
                                    Font sessionTitleFontStyle = workbook.createFont();
                                    sessionTitleFontStyle.setBold(true);
                                    sessionTitleFontStyle.setFontName("Times New Roman");
                                    sessionTitleFontStyle.setFontHeightInPoints((short) 16);
                                    sessionTitleCellStyle.setFont(sessionTitleFontStyle);
                                    sessionTitleCellStyle.setBorderTop(CellStyle.BORDER_THIN);
                                    sessionTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    sessionTitleCell.setCellStyle(sessionTitleCellStyle);
                                    RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 1, sessionBeginRowAddress + 1,
                                            j * 5, (j + 1) * 5 - 1);
                                    sheet.addMergedRegion(cellRangeAddress);

                                    Cell sessionRoomCell = sessionRoomRow.createCell(j * 5);
                                    sessionRoomCell.setCellValue("Room " + session.getSessionRoom());
                                    CellStyle sessionRoomCellStyle = workbook.createCellStyle();
                                    Font sessionRoomFontStyle = workbook.createFont();
                                    sessionRoomFontStyle.setBold(true);
                                    sessionRoomFontStyle.setFontName("Times New Roman");
                                    sessionRoomFontStyle.setFontHeightInPoints((short) 16);
                                    sessionRoomCellStyle.setFont(sessionRoomFontStyle);
                                    sessionRoomCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionRoomCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    sessionRoomCell.setCellStyle(sessionRoomCellStyle);

                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 2, sessionBeginRowAddress + 2,
                                            j * 5, (j + 1) * 5 - 1);
                                    sheet.addMergedRegion(cellRangeAddress);

                                    Cell sessionReviewerCell = sessionReviewerRow.createCell(j * 5);
                                    sessionReviewerCell.setCellValue(session.getSessionReviewer());
                                    CellStyle sessionReviewerCellStyle = workbook.createCellStyle();
                                    Font sessionReviewerFontStyle = workbook.createFont();
                                    sessionReviewerFontStyle.setBold(true);
                                    sessionReviewerFontStyle.setFontName("Times New Roman");
                                    sessionReviewerFontStyle.setFontHeightInPoints((short) 16);
                                    sessionReviewerCellStyle.setFont(sessionReviewerFontStyle);
                                    sessionReviewerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionReviewerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    sessionReviewerCell.setCellStyle(sessionReviewerCellStyle);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 3, sessionBeginRowAddress + 3,
                                            j * 5, (j + 1) * 5 - 1);
                                    sheet.addMergedRegion(cellRangeAddress);
                                    Cell sessionChairCell = sessionChairRow.createCell(j * 5);
                                    sessionChairCell.setCellValue(session.getSessionChair());
                                    CellStyle sessionChairCellStyle = workbook.createCellStyle();
                                    Font sessionChairFontStyle = workbook.createFont();
                                    sessionChairFontStyle.setBold(true);
                                    sessionChairFontStyle.setFontName("Times New Roman");
                                    sessionChairFontStyle.setFontHeightInPoints((short) 16);
                                    sessionChairCellStyle.setFont(sessionChairFontStyle);
                                    sessionChairCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                                    sessionChairCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionChairCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    sessionChairCell.setCellStyle(sessionChairCellStyle);
                                    RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                    int paperBeginAddress = sessionBeginRowAddress + 4;
                                    int paperCount = 0;

                                    List<String> paperIDs = paperMapper.getPaperIDsInSession(session.getSessionID());
                                    for (String paperID : paperIDs) {
                                        Paper paper = paperMapper.getPaperByFileID(paperID);
                                        Row paperAuthorRow = sheet.getRow(paperBeginAddress);
                                        if (paperAuthorRow == null) {
                                            paperAuthorRow = sheet.createRow(paperBeginAddress);
                                        }
                                        Row paperTitleRow = sheet.getRow(paperBeginAddress + 1);
                                        if (paperTitleRow == null) {
                                            paperTitleRow = sheet.createRow(paperBeginAddress + 1);
                                        }

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress,
                                                j * 5, (j + 1) * 5 - 1);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperAuthorCell = paperAuthorRow.createCell(j * 5);
                                        paperAuthorCell.setCellValue(submissionMapper.getSubmissionByPaperID(paperID).getAuthor());
                                        CellStyle paperAuthorCellStyle = workbook.createCellStyle();
                                        Font paperAuthorCellFontStyle = workbook.createFont();
                                        paperAuthorCellFontStyle.setFontName("Times New Roman");
                                        paperAuthorCellFontStyle.setFontHeightInPoints((short) 12);
                                        paperAuthorCellStyle.setFont(paperAuthorCellFontStyle);
                                        paperAuthorCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setWrapText(true);
                                        paperAuthorCell.setCellStyle(paperAuthorCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress + 1, paperBeginAddress + 1,
                                                j * 5, (j + 1) * 5 - 1);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperTitleCell = paperTitleRow.createCell(j * 5);
                                        paperTitleCell.setCellValue(paper.getPaperTitle());
                                        CellStyle paperTitleCellStyle = workbook.createCellStyle();
                                        Font paperTitleCellFontStyle = workbook.createFont();
                                        paperTitleCellFontStyle.setFontName("Times New Roman");
                                        paperTitleCellFontStyle.setFontHeightInPoints((short) 14);
                                        paperTitleCellStyle.setFont(paperTitleCellFontStyle);
                                        paperTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setWrapText(true);
                                        paperTitleCell.setCellStyle(paperTitleCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        paperBeginAddress += 2;
                                        paperCount++;
                                    }
                                    while (paperCount < maxPaperCount) {
                                        Row paperAuthorRow = sheet.getRow(paperBeginAddress);
                                        if (paperAuthorRow == null) {
                                            paperAuthorRow = sheet.createRow(paperBeginAddress);
                                        }
                                        Row paperTitleRow = sheet.getRow(paperBeginAddress + 1);
                                        if (paperTitleRow == null) {
                                            paperTitleRow = sheet.createRow(paperBeginAddress + 1);
                                        }

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress,
                                                j * 5, (j + 1) * 5 - 1);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperAuthorCell = paperAuthorRow.createCell(j * 5);
                                        paperAuthorCell.setCellValue("");
                                        CellStyle paperAuthorCellStyle = workbook.createCellStyle();
                                        Font paperAuthorCellFontStyle = workbook.createFont();
                                        paperAuthorCellFontStyle.setFontName("Times New Roman");
                                        paperAuthorCellFontStyle.setFontHeightInPoints((short) 12);
                                        paperAuthorCellStyle.setFont(paperAuthorCellFontStyle);
                                        paperAuthorCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setWrapText(true);
                                        paperAuthorCell.setCellStyle(paperAuthorCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress + 1, paperBeginAddress + 1,
                                                j * 5, (j + 1) * 5 - 1);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperTitleCell = paperTitleRow.createCell(j * 5);
                                        paperTitleCell.setCellValue("");
                                        CellStyle paperTitleCellStyle = workbook.createCellStyle();
                                        Font paperTitleCellFontStyle = workbook.createFont();
                                        paperTitleCellFontStyle.setFontName("Times New Roman");
                                        paperTitleCellFontStyle.setFontHeightInPoints((short) 14);
                                        paperTitleCellStyle.setFont(paperTitleCellFontStyle);
                                        paperTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setWrapText(true);
                                        paperTitleCell.setCellStyle(paperTitleCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        paperBeginAddress += 2;
                                        paperCount++;
                                    }
                                } else {
                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress, sessionBeginRowAddress,
                                            j * 5, (j + 1) * 5 - 2);
                                    sheet.addMergedRegion(cellRangeAddress);

                                    Cell sessionTitleCell = sessionTitleRow.createCell(j * 5);
                                    sessionTitleCell.setCellValue(session.getSessionName());
                                    CellStyle sessionTitleCellStyle = workbook.createCellStyle();
                                    Font sessionTitleFontStyle = workbook.createFont();
                                    sessionTitleFontStyle.setBold(true);
                                    sessionTitleFontStyle.setFontName("Times New Roman");
                                    sessionTitleFontStyle.setFontHeightInPoints((short) 16);
                                    sessionTitleCellStyle.setFont(sessionTitleFontStyle);
                                    sessionTitleCellStyle.setBorderTop(CellStyle.BORDER_THIN);
                                    sessionTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    sessionTitleCell.setCellStyle(sessionTitleCellStyle);
                                    RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 1, sessionBeginRowAddress + 1,
                                            j * 5, (j + 1) * 5 - 2);
                                    sheet.addMergedRegion(cellRangeAddress);

                                    Cell sessionRoomCell = sessionRoomRow.createCell(j * 5);
                                    sessionRoomCell.setCellValue("Room " + session.getSessionRoom());
                                    CellStyle sessionRoomCellStyle = workbook.createCellStyle();
                                    Font sessionRoomFontStyle = workbook.createFont();
                                    sessionRoomFontStyle.setBold(true);
                                    sessionRoomFontStyle.setFontName("Times New Roman");
                                    sessionRoomFontStyle.setFontHeightInPoints((short) 16);
                                    sessionRoomCellStyle.setFont(sessionRoomFontStyle);
                                    sessionRoomCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionRoomCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    sessionRoomCell.setCellStyle(sessionRoomCellStyle);

                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 2, sessionBeginRowAddress + 2,
                                            j * 5, (j + 1) * 5 - 2);
                                    sheet.addMergedRegion(cellRangeAddress);

                                    Cell sessionReviewerCell = sessionReviewerRow.createCell(j * 5);
                                    sessionReviewerCell.setCellValue(session.getSessionReviewer());
                                    CellStyle sessionReviewerCellStyle = workbook.createCellStyle();
                                    Font sessionReviewerFontStyle = workbook.createFont();
                                    sessionReviewerFontStyle.setBold(true);
                                    sessionReviewerFontStyle.setFontName("Times New Roman");
                                    sessionReviewerFontStyle.setFontHeightInPoints((short) 16);
                                    sessionReviewerCellStyle.setFont(sessionReviewerFontStyle);
                                    sessionReviewerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionReviewerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    sessionReviewerCell.setCellStyle(sessionReviewerCellStyle);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 3, sessionBeginRowAddress + 3,
                                            j * 5, (j + 1) * 5 - 2);
                                    sheet.addMergedRegion(cellRangeAddress);
                                    Cell sessionChairCell = sessionChairRow.createCell(j * 5);
                                    sessionChairCell.setCellValue(session.getSessionChair());
                                    CellStyle sessionChairCellStyle = workbook.createCellStyle();
                                    Font sessionChairFontStyle = workbook.createFont();
                                    sessionChairFontStyle.setBold(true);
                                    sessionChairFontStyle.setFontName("Times New Roman");
                                    sessionChairFontStyle.setFontHeightInPoints((short) 16);
                                    sessionChairCellStyle.setFont(sessionChairFontStyle);
                                    sessionChairCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                                    sessionChairCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionChairCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    sessionChairCell.setCellStyle(sessionChairCellStyle);
                                    RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                    int paperBeginAddress = sessionBeginRowAddress + 4;
                                    int paperCount = 0;

                                    List<String> paperIDs = paperMapper.getPaperIDsInSession(session.getSessionID());
                                    for (String paperID : paperIDs) {
                                        Paper paper = paperMapper.getPaperByFileID(paperID);
                                        Row paperAuthorRow = sheet.getRow(paperBeginAddress);
                                        if (paperAuthorRow == null) {
                                            paperAuthorRow = sheet.createRow(paperBeginAddress);
                                        }
                                        Row paperTitleRow = sheet.getRow(paperBeginAddress + 1);
                                        if (paperTitleRow == null) {
                                            paperTitleRow = sheet.createRow(paperBeginAddress + 1);
                                        }

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress,
                                                j * 5, (j + 1) * 5 - 2);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperAuthorCell = paperAuthorRow.createCell(j * 5);
                                        paperAuthorCell.setCellValue(submissionMapper.getSubmissionByPaperID(paperID).getAuthor());
                                        CellStyle paperAuthorCellStyle = workbook.createCellStyle();
                                        Font paperAuthorCellFontStyle = workbook.createFont();
                                        paperAuthorCellFontStyle.setFontName("Times New Roman");
                                        paperAuthorCellFontStyle.setFontHeightInPoints((short) 12);
                                        paperAuthorCellStyle.setFont(paperAuthorCellFontStyle);
                                        paperAuthorCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setWrapText(true);
                                        paperAuthorCell.setCellStyle(paperAuthorCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress + 1, paperBeginAddress + 1,
                                                j * 5, (j + 1) * 5 - 2);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperTitleCell = paperTitleRow.createCell(j * 5);
                                        paperTitleCell.setCellValue(paper.getPaperTitle());
                                        CellStyle paperTitleCellStyle = workbook.createCellStyle();
                                        Font paperTitleCellFontStyle = workbook.createFont();
                                        paperTitleCellFontStyle.setFontName("Times New Roman");
                                        paperTitleCellFontStyle.setFontHeightInPoints((short) 14);
                                        paperTitleCellStyle.setFont(paperTitleCellFontStyle);
                                        paperTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setWrapText(true);
                                        paperTitleCell.setCellStyle(paperTitleCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        paperBeginAddress += 2;
                                        paperCount++;
                                    }
                                    while (paperCount < maxPaperCount) {
                                        Row paperAuthorRow = sheet.getRow(paperBeginAddress);
                                        if (paperAuthorRow == null) {
                                            paperAuthorRow = sheet.createRow(paperBeginAddress);
                                        }
                                        Row paperTitleRow = sheet.getRow(paperBeginAddress + 1);
                                        if (paperTitleRow == null) {
                                            paperTitleRow = sheet.createRow(paperBeginAddress + 1);
                                        }

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress,
                                                j * 5, (j + 1) * 5 - 2);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperAuthorCell = paperAuthorRow.createCell(j * 5);
                                        paperAuthorCell.setCellValue("");
                                        CellStyle paperAuthorCellStyle = workbook.createCellStyle();
                                        Font paperAuthorCellFontStyle = workbook.createFont();
                                        paperAuthorCellFontStyle.setFontName("Times New Roman");
                                        paperAuthorCellFontStyle.setFontHeightInPoints((short) 12);
                                        paperAuthorCellStyle.setFont(paperAuthorCellFontStyle);
                                        paperAuthorCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setWrapText(true);
                                        paperAuthorCell.setCellStyle(paperAuthorCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress + 1, paperBeginAddress + 1,
                                                j * 5, (j + 1) * 5 - 2);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperTitleCell = paperTitleRow.createCell(j * 5);
                                        paperTitleCell.setCellValue("");
                                        CellStyle paperTitleCellStyle = workbook.createCellStyle();
                                        Font paperTitleCellFontStyle = workbook.createFont();
                                        paperTitleCellFontStyle.setFontName("Times New Roman");
                                        paperTitleCellFontStyle.setFontHeightInPoints((short) 14);
                                        paperTitleCellStyle.setFont(paperTitleCellFontStyle);
                                        paperTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setWrapText(true);
                                        paperTitleCell.setCellStyle(paperTitleCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        paperBeginAddress += 2;
                                        paperCount++;
                                    }
                                }
                            } else {
                                if (j < 2) {
                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress, sessionBeginRowAddress,
                                            j * 5, (j + 1) * 5 - 1);
                                    sheet.addMergedRegion(cellRangeAddress);

                                    Cell sessionTitleCell = sessionTitleRow.createCell(j * 5);
                                    CellStyle sessionTitleCellStyle = workbook.createCellStyle();
                                    Font sessionTitleFontStyle = workbook.createFont();
                                    sessionTitleFontStyle.setBold(true);
                                    sessionTitleFontStyle.setFontName("Times New Roman");
                                    sessionTitleFontStyle.setFontHeightInPoints((short) 16);
                                    sessionTitleCellStyle.setFont(sessionTitleFontStyle);
                                    sessionTitleCellStyle.setBorderTop(CellStyle.BORDER_THIN);
                                    sessionTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    sessionTitleCell.setCellStyle(sessionTitleCellStyle);
                                    RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 1, sessionBeginRowAddress + 1,
                                            j * 5, (j + 1) * 5 - 1);
                                    sheet.addMergedRegion(cellRangeAddress);

                                    Cell sessionRoomCell = sessionRoomRow.createCell(j * 5);
                                    CellStyle sessionRoomCellStyle = workbook.createCellStyle();
                                    Font sessionRoomFontStyle = workbook.createFont();
                                    sessionRoomFontStyle.setBold(true);
                                    sessionRoomFontStyle.setFontName("Times New Roman");
                                    sessionRoomFontStyle.setFontHeightInPoints((short) 16);
                                    sessionRoomCellStyle.setFont(sessionRoomFontStyle);
                                    sessionRoomCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionRoomCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    sessionRoomCell.setCellStyle(sessionRoomCellStyle);

                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 2, sessionBeginRowAddress + 2,
                                            j * 5, (j + 1) * 5 - 1);
                                    sheet.addMergedRegion(cellRangeAddress);

                                    Cell sessionReviewerCell = sessionReviewerRow.createCell(j * 5);
                                    CellStyle sessionReviewerCellStyle = workbook.createCellStyle();
                                    Font sessionReviewerFontStyle = workbook.createFont();
                                    sessionReviewerFontStyle.setBold(true);
                                    sessionReviewerFontStyle.setFontName("Times New Roman");
                                    sessionReviewerFontStyle.setFontHeightInPoints((short) 16);
                                    sessionReviewerCellStyle.setFont(sessionReviewerFontStyle);
                                    sessionReviewerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionReviewerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    sessionReviewerCell.setCellStyle(sessionReviewerCellStyle);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 3, sessionBeginRowAddress + 3,
                                            j * 5, (j + 1) * 5 - 1);
                                    sheet.addMergedRegion(cellRangeAddress);
                                    Cell sessionChairCell = sessionChairRow.createCell(j * 5);
                                    CellStyle sessionChairCellStyle = workbook.createCellStyle();
                                    Font sessionChairFontStyle = workbook.createFont();
                                    sessionChairFontStyle.setBold(true);
                                    sessionChairFontStyle.setFontName("Times New Roman");
                                    sessionChairFontStyle.setFontHeightInPoints((short) 16);
                                    sessionChairCellStyle.setFont(sessionChairFontStyle);
                                    sessionChairCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                                    sessionChairCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionChairCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    sessionChairCell.setCellStyle(sessionChairCellStyle);
                                    RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                    int paperBeginAddress = sessionBeginRowAddress + 4;
                                    int paperCount = 0;

                                    for (int k = 0; k < maxPaperCount; k++) {
                                        Row paperAuthorRow = sheet.getRow(paperBeginAddress);
                                        if (paperAuthorRow == null) {
                                            paperAuthorRow = sheet.createRow(paperBeginAddress);
                                        }
                                        Row paperTitleRow = sheet.getRow(paperBeginAddress + 1);
                                        if (paperTitleRow == null) {
                                            paperTitleRow = sheet.createRow(paperBeginAddress + 1);
                                        }

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress,
                                                j * 5, (j + 1) * 5 - 1);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperAuthorCell = paperAuthorRow.createCell(j * 5);
                                        CellStyle paperAuthorCellStyle = workbook.createCellStyle();
                                        Font paperAuthorCellFontStyle = workbook.createFont();
                                        paperAuthorCellFontStyle.setFontName("Times New Roman");
                                        paperAuthorCellFontStyle.setFontHeightInPoints((short) 12);
                                        paperAuthorCellStyle.setFont(paperAuthorCellFontStyle);
                                        paperAuthorCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setWrapText(true);
                                        paperAuthorCell.setCellStyle(paperAuthorCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress + 1, paperBeginAddress + 1,
                                                j * 5, (j + 1) * 5 - 1);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperTitleCell = paperTitleRow.createCell(j * 5);
                                        CellStyle paperTitleCellStyle = workbook.createCellStyle();
                                        Font paperTitleCellFontStyle = workbook.createFont();
                                        paperTitleCellFontStyle.setFontName("Times New Roman");
                                        paperTitleCellFontStyle.setFontHeightInPoints((short) 14);
                                        paperTitleCellStyle.setFont(paperTitleCellFontStyle);
                                        paperTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setWrapText(true);
                                        paperTitleCell.setCellStyle(paperTitleCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        paperBeginAddress += 2;
                                        paperCount++;
                                    }
                                    while (paperCount < maxPaperCount) {
                                        Row paperAuthorRow = sheet.getRow(paperBeginAddress);
                                        if (paperAuthorRow == null) {
                                            paperAuthorRow = sheet.createRow(paperBeginAddress);
                                        }
                                        Row paperTitleRow = sheet.getRow(paperBeginAddress + 1);
                                        if (paperTitleRow == null) {
                                            paperTitleRow = sheet.createRow(paperBeginAddress + 1);
                                        }

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress,
                                                j * 5, (j + 1) * 5 - 1);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperAuthorCell = paperAuthorRow.createCell(j * 5);
                                        paperAuthorCell.setCellValue("");
                                        CellStyle paperAuthorCellStyle = workbook.createCellStyle();
                                        Font paperAuthorCellFontStyle = workbook.createFont();
                                        paperAuthorCellFontStyle.setFontName("Times New Roman");
                                        paperAuthorCellFontStyle.setFontHeightInPoints((short) 12);
                                        paperAuthorCellStyle.setFont(paperAuthorCellFontStyle);
                                        paperAuthorCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setWrapText(true);
                                        paperAuthorCell.setCellStyle(paperAuthorCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress + 1, paperBeginAddress + 1,
                                                j * 5, (j + 1) * 5 - 1);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperTitleCell = paperTitleRow.createCell(j * 5);
                                        paperTitleCell.setCellValue("");
                                        CellStyle paperTitleCellStyle = workbook.createCellStyle();
                                        Font paperTitleCellFontStyle = workbook.createFont();
                                        paperTitleCellFontStyle.setFontName("Times New Roman");
                                        paperTitleCellFontStyle.setFontHeightInPoints((short) 14);
                                        paperTitleCellStyle.setFont(paperTitleCellFontStyle);
                                        paperTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setWrapText(true);
                                        paperTitleCell.setCellStyle(paperTitleCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        paperBeginAddress += 2;
                                        paperCount++;
                                    }
                                } else {
                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress, sessionBeginRowAddress,
                                            j * 5, (j + 1) * 5 - 2);
                                    sheet.addMergedRegion(cellRangeAddress);

                                    Cell sessionTitleCell = sessionTitleRow.createCell(j * 5);
                                    CellStyle sessionTitleCellStyle = workbook.createCellStyle();
                                    Font sessionTitleFontStyle = workbook.createFont();
                                    sessionTitleFontStyle.setBold(true);
                                    sessionTitleFontStyle.setFontName("Times New Roman");
                                    sessionTitleFontStyle.setFontHeightInPoints((short) 16);
                                    sessionTitleCellStyle.setFont(sessionTitleFontStyle);
                                    sessionTitleCellStyle.setBorderTop(CellStyle.BORDER_THIN);
                                    sessionTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    sessionTitleCell.setCellStyle(sessionTitleCellStyle);
                                    RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 1, sessionBeginRowAddress + 1,
                                            j * 5, (j + 1) * 5 - 2);
                                    sheet.addMergedRegion(cellRangeAddress);

                                    Cell sessionRoomCell = sessionRoomRow.createCell(j * 5);
                                    CellStyle sessionRoomCellStyle = workbook.createCellStyle();
                                    Font sessionRoomFontStyle = workbook.createFont();
                                    sessionRoomFontStyle.setBold(true);
                                    sessionRoomFontStyle.setFontName("Times New Roman");
                                    sessionRoomFontStyle.setFontHeightInPoints((short) 16);
                                    sessionRoomCellStyle.setFont(sessionRoomFontStyle);
                                    sessionRoomCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionRoomCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    sessionRoomCell.setCellStyle(sessionRoomCellStyle);

                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 2, sessionBeginRowAddress + 2,
                                            j * 5, (j + 1) * 5 - 2);
                                    sheet.addMergedRegion(cellRangeAddress);

                                    Cell sessionReviewerCell = sessionReviewerRow.createCell(j * 5);
                                    CellStyle sessionReviewerCellStyle = workbook.createCellStyle();
                                    Font sessionReviewerFontStyle = workbook.createFont();
                                    sessionReviewerFontStyle.setBold(true);
                                    sessionReviewerFontStyle.setFontName("Times New Roman");
                                    sessionReviewerFontStyle.setFontHeightInPoints((short) 16);
                                    sessionReviewerCellStyle.setFont(sessionReviewerFontStyle);
                                    sessionReviewerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionReviewerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    sessionReviewerCell.setCellStyle(sessionReviewerCellStyle);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                    cellRangeAddress = new CellRangeAddress(sessionBeginRowAddress + 3, sessionBeginRowAddress + 3,
                                            j * 5, (j + 1) * 5 - 2);
                                    sheet.addMergedRegion(cellRangeAddress);
                                    Cell sessionChairCell = sessionChairRow.createCell(j * 5);
                                    CellStyle sessionChairCellStyle = workbook.createCellStyle();
                                    Font sessionChairFontStyle = workbook.createFont();
                                    sessionChairFontStyle.setBold(true);
                                    sessionChairFontStyle.setFontName("Times New Roman");
                                    sessionChairFontStyle.setFontHeightInPoints((short) 16);
                                    sessionChairCellStyle.setFont(sessionChairFontStyle);
                                    sessionChairCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                                    sessionChairCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                    sessionChairCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                    sessionChairCell.setCellStyle(sessionChairCellStyle);
                                    RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                    RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                    int paperBeginAddress = sessionBeginRowAddress + 4;
                                    int paperCount = 0;

                                    for (int k = 0; k < maxPaperCount; k++) {
                                        Row paperAuthorRow = sheet.getRow(paperBeginAddress);
                                        if (paperAuthorRow == null) {
                                            paperAuthorRow = sheet.createRow(paperBeginAddress);
                                        }
                                        Row paperTitleRow = sheet.getRow(paperBeginAddress + 1);
                                        if (paperTitleRow == null) {
                                            paperTitleRow = sheet.createRow(paperBeginAddress + 1);
                                        }

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress,
                                                j * 5, (j + 1) * 5 - 2);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperAuthorCell = paperAuthorRow.createCell(j * 5);
                                        CellStyle paperAuthorCellStyle = workbook.createCellStyle();
                                        Font paperAuthorCellFontStyle = workbook.createFont();
                                        paperAuthorCellFontStyle.setFontName("Times New Roman");
                                        paperAuthorCellFontStyle.setFontHeightInPoints((short) 12);
                                        paperAuthorCellStyle.setFont(paperAuthorCellFontStyle);
                                        paperAuthorCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setWrapText(true);
                                        paperAuthorCell.setCellStyle(paperAuthorCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress + 1, paperBeginAddress + 1,
                                                j * 5, (j + 1) * 5 - 2);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperTitleCell = paperTitleRow.createCell(j * 5);
                                        CellStyle paperTitleCellStyle = workbook.createCellStyle();
                                        Font paperTitleCellFontStyle = workbook.createFont();
                                        paperTitleCellFontStyle.setFontName("Times New Roman");
                                        paperTitleCellFontStyle.setFontHeightInPoints((short) 14);
                                        paperTitleCellStyle.setFont(paperTitleCellFontStyle);
                                        paperTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setWrapText(true);
                                        paperTitleCell.setCellStyle(paperTitleCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        paperBeginAddress += 2;
                                        paperCount++;
                                    }
                                    while (paperCount < maxPaperCount) {
                                        Row paperAuthorRow = sheet.getRow(paperBeginAddress);
                                        if (paperAuthorRow == null) {
                                            paperAuthorRow = sheet.createRow(paperBeginAddress);
                                        }
                                        Row paperTitleRow = sheet.getRow(paperBeginAddress + 1);
                                        if (paperTitleRow == null) {
                                            paperTitleRow = sheet.createRow(paperBeginAddress + 1);
                                        }

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress,
                                                j * 5, (j + 1) * 5 - 2);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperAuthorCell = paperAuthorRow.createCell(j * 5);
                                        paperAuthorCell.setCellValue("");
                                        CellStyle paperAuthorCellStyle = workbook.createCellStyle();
                                        Font paperAuthorCellFontStyle = workbook.createFont();
                                        paperAuthorCellFontStyle.setFontName("Times New Roman");
                                        paperAuthorCellFontStyle.setFontHeightInPoints((short) 12);
                                        paperAuthorCellStyle.setFont(paperAuthorCellFontStyle);
                                        paperAuthorCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperAuthorCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperAuthorCell.setCellStyle(paperAuthorCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        cellRangeAddress = new CellRangeAddress(paperBeginAddress + 1, paperBeginAddress + 1,
                                                j * 5, (j + 1) * 5 - 2);
                                        sheet.addMergedRegion(cellRangeAddress);

                                        Cell paperTitleCell = paperTitleRow.createCell(j * 5);
                                        paperTitleCell.setCellValue("");
                                        CellStyle paperTitleCellStyle = workbook.createCellStyle();
                                        Font paperTitleCellFontStyle = workbook.createFont();
                                        paperTitleCellFontStyle.setFontName("Times New Roman");
                                        paperTitleCellFontStyle.setFontHeightInPoints((short) 14);
                                        paperTitleCellStyle.setFont(paperTitleCellFontStyle);
                                        paperTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                        paperTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                        paperTitleCell.setCellStyle(paperTitleCellStyle);
                                        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
                                        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

                                        paperBeginAddress += 2;
                                        paperCount++;
                                    }
                                }
                            }
                        }
                        sessionSize += maxPaperCount * 2;
                        eventRowAddress += sessionSize;
                    }
                }
            }
        }

        String fileName = "";
        try {
            FileOutputStream outputStream = new FileOutputStream(
                    new File(fileName));
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(fileName);
        response.setHeader("content-type", "application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
