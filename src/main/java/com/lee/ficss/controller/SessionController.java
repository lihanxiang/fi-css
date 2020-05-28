package com.lee.ficss.controller;


import com.lee.ficss.mapper.*;
import com.lee.ficss.pojo.Session;
import com.lee.ficss.service.ConferenceService;
import com.lee.ficss.service.SessionService;
import com.lee.ficss.service.TopicService;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.JsonResult;
import com.lee.ficss.util.RandomIDBuilder;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequiresRoles("admin")
@RequestMapping("session")
public class SessionController {

    private final SessionService sessionService;
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private SessionMapper sessionMapper;
    @Autowired
    private SubmissionMapper submissionMapper;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @ResponseBody
    @RequestMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String create(@RequestParam("eventID") String eventID, @RequestParam("sessionName") String sessionName,
                         @RequestParam("sessionRoom") String sessionRoom, @RequestParam("sessionReviewer") String sessionReviewer,
                         @RequestParam("sessionChair") String sessionChair){
        String sessionID = sessionService.createSession(eventID, sessionName, sessionRoom, sessionReviewer, sessionChair);
        return JsonResult.build(sessionService.getSessionByID(sessionID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public String edit(@RequestParam("sessionID") String sessionID, @RequestParam("sessionName") String sessionName,
                         @RequestParam("sessionRoom") String sessionRoom, @RequestParam("sessionReviewer") String sessionReviewer,
                         @RequestParam("sessionChair") String sessionChair){
        return JsonResult.build(sessionService.editSessionInfo(sessionID, sessionName,
                sessionRoom, sessionReviewer, sessionChair)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "first-session", produces = MediaType.APPLICATION_JSON_VALUE)
    public String sessionsInEvent(@RequestParam("eventID") String eventID){
        return JsonResult.build(sessionService.getFirstSession(eventID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "get-session-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSessionByID(@RequestParam("sessionID") String sessionID){
        return JsonResult.build(sessionService.getSessionByID(sessionID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "paper-in-session", produces = MediaType.APPLICATION_JSON_VALUE)
    public String papersInSession(@RequestParam("sessionID") String sessionID){
        return JsonResult.build(sessionService.getPapersInSession(sessionID)).toJSONString();
    }

    @RequestMapping(value = "download/{sessionID}")
    public void download(@PathVariable("sessionID") String sessionID, HttpServletResponse response){
        Session session = sessionMapper.getSessionByID(sessionID);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 1, 0, 8);
        sheet.addMergedRegion(cellRangeAddress);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(session.getSessionName());
        CellStyle titleCellStyle = workbook.createCellStyle();
        Font titleFontStyle = workbook.createFont();
        titleFontStyle.setBold(true);
        titleFontStyle.setFontName("Times New Roman");
        titleFontStyle.setFontHeightInPoints((short) 16);
        titleCellStyle.setFont(titleFontStyle);
        titleCell.setCellStyle(titleCellStyle);

        cellRangeAddress = new CellRangeAddress(2, 2, 0, 8);
        sheet.addMergedRegion(cellRangeAddress);

        Row reviewerRow = sheet.createRow(2);
        Cell reviewerCell = reviewerRow.createCell(0);
        reviewerCell.setCellValue("Reviewed by: " + session.getSessionReviewer());
        CellStyle reviewerCellStyle = workbook.createCellStyle();
        Font reviewerFontStyle = workbook.createFont();
        reviewerFontStyle.setBold(true);
        reviewerFontStyle.setFontName("Times New Roman");
        reviewerFontStyle.setFontHeightInPoints((short) 12);
        reviewerCellStyle.setFont(reviewerFontStyle);
        reviewerCell.setCellStyle(reviewerCellStyle);

        cellRangeAddress = new CellRangeAddress(3, 3, 0, 8);
        sheet.addMergedRegion(cellRangeAddress);
        Row blankRow = sheet.createRow(3);
        Cell blankCell = blankRow.createCell(0);
        CellStyle blankCellStyle = workbook.createCellStyle();
        blankCell.setCellStyle(blankCellStyle);

        List<String> paperIDs = paperMapper.getPaperIDsInSession(session.getSessionID());
        int paperBeginAddress = 4;
        for (String paperID : paperIDs){
            cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress, 0, 8);
            sheet.addMergedRegion(cellRangeAddress);

            Row authorRow = sheet.createRow(paperBeginAddress++);
            Cell authorCell = authorRow.createCell(0);
            authorCell.setCellValue("Author: " + submissionMapper.getSubmissionByPaperID(paperID).getAuthor());
            CellStyle authorCellStyle = workbook.createCellStyle();
            Font authorFontStyle = workbook.createFont();
            authorFontStyle.setBold(true);
            authorFontStyle.setFontName("Times New Roman");
            authorFontStyle.setFontHeightInPoints((short) 12);
            authorCellStyle.setFont(authorFontStyle);
            authorCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            authorCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            authorCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            authorCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            authorCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            authorCell.setCellStyle(authorCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress, 0, 8);
            sheet.addMergedRegion(cellRangeAddress);

            Row paperTitleRow = sheet.createRow(paperBeginAddress++);
            Cell paperTitleCell = paperTitleRow.createCell(0);
            paperTitleCell.setCellValue("Paper Title: " + paperMapper.getPaperByFileID(paperID).getPaperTitle());
            CellStyle paperTitleCellStyle = workbook.createCellStyle();
            Font paperTitleFontStyle = workbook.createFont();
            paperTitleFontStyle.setBold(true);
            paperTitleFontStyle.setFontName("Times New Roman");
            paperTitleFontStyle.setFontHeightInPoints((short) 12);
            paperTitleCellStyle.setFont(paperTitleFontStyle);
            paperTitleCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            paperTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            paperTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            paperTitleCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            paperTitleCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            paperTitleCell.setCellStyle(paperTitleCellStyle);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress, 0, 1);
            sheet.addMergedRegion(cellRangeAddress);

            Row categoryRow = sheet.createRow(paperBeginAddress);
            Cell categoryCell = categoryRow.createCell(0);
            categoryCell.setCellValue("Category");
            CellStyle categoryCellStyle = workbook.createCellStyle();
            Font categoryFontStyle = workbook.createFont();
            categoryFontStyle.setBold(true);
            categoryFontStyle.setFontName("Times New Roman");
            categoryFontStyle.setFontHeightInPoints((short) 12);
            categoryCellStyle.setFont(categoryFontStyle);
            categoryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            categoryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            categoryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            categoryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            categoryCell.setCellStyle(categoryCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            Row contentRow = sheet.getRow(paperBeginAddress);
            Cell contentCell = contentRow.createCell(2);
            contentCell.setCellValue("Content");
            CellStyle contentCellStyle = workbook.createCellStyle();
            Font contentFontStyle = workbook.createFont();
            contentFontStyle.setBold(true);
            contentFontStyle.setFontName("Times New Roman");
            contentFontStyle.setFontHeightInPoints((short) 12);
            contentCellStyle.setFont(contentFontStyle);
            contentCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            contentCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            contentCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            contentCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            contentCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            contentCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            contentCell.setCellStyle(contentCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            Row innovativeRow = sheet.getRow(paperBeginAddress);
            Cell innovativeCell = innovativeRow.createCell(3);
            innovativeCell.setCellValue("Innovative");
            CellStyle innovativeCellStyle = workbook.createCellStyle();
            Font innovativeFontStyle = workbook.createFont();
            innovativeFontStyle.setBold(true);
            innovativeFontStyle.setFontName("Times New Roman");
            innovativeFontStyle.setFontHeightInPoints((short) 12);
            innovativeCellStyle.setFont(innovativeFontStyle);
            innovativeCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            innovativeCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            innovativeCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            innovativeCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            innovativeCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            innovativeCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            innovativeCell.setCellStyle(innovativeCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            Row logicRow = sheet.getRow(paperBeginAddress);
            Cell logicCell = logicRow.createCell(4);
            logicCell.setCellValue("Logic");
            CellStyle logicCellStyle = workbook.createCellStyle();
            Font logicFontStyle = workbook.createFont();
            logicFontStyle.setBold(true);
            logicFontStyle.setFontName("Times New Roman");
            logicFontStyle.setFontHeightInPoints((short) 12);
            logicCellStyle.setFont(logicFontStyle);
            logicCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            logicCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            logicCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            logicCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            logicCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            logicCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            logicCell.setCellStyle(logicCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            Row expressionRow = sheet.getRow(paperBeginAddress);
            Cell expressionCell = expressionRow.createCell(5);
            expressionCell.setCellValue("Expression");
            CellStyle expressionCellStyle = workbook.createCellStyle();
            Font expressionFontStyle = workbook.createFont();
            expressionFontStyle.setBold(true);
            expressionFontStyle.setFontName("Times New Roman");
            expressionFontStyle.setFontHeightInPoints((short) 12);
            expressionCellStyle.setFont(expressionFontStyle);
            expressionCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            expressionCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            expressionCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            expressionCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            expressionCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            expressionCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            expressionCell.setCellStyle(expressionCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            Row qaRow = sheet.getRow(paperBeginAddress);
            Cell qaCell = qaRow.createCell(6);
            qaCell.setCellValue("Q&A");
            CellStyle qaCellStyle = workbook.createCellStyle();
            Font qaFontStyle = workbook.createFont();
            qaFontStyle.setBold(true);
            qaFontStyle.setFontName("Times New Roman");
            qaFontStyle.setFontHeightInPoints((short) 12);
            qaCellStyle.setFont(qaFontStyle);
            qaCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            qaCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            qaCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            qaCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            qaCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            qaCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            qaCell.setCellStyle(qaCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress, 7, 8);
            sheet.addMergedRegion(cellRangeAddress);
            Row totalMarksRow = sheet.getRow(paperBeginAddress++);
            Cell totalMarksCell = totalMarksRow.createCell(7);
            totalMarksCell.setCellValue("Total marks");
            CellStyle totalMarksCellStyle = workbook.createCellStyle();
            Font totalMarksFontStyle = workbook.createFont();
            totalMarksFontStyle.setBold(true);
            totalMarksFontStyle.setFontName("Times New Roman");
            totalMarksFontStyle.setFontHeightInPoints((short) 12);
            totalMarksCellStyle.setFont(totalMarksFontStyle);
            totalMarksCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            totalMarksCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            totalMarksCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            totalMarksCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            totalMarksCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            totalMarksCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            totalMarksCell.setCellStyle(totalMarksCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress, 0, 1);
            sheet.addMergedRegion(cellRangeAddress);
            Row scoreRow = sheet.createRow(paperBeginAddress);
            Cell scoreCell = scoreRow.createCell(0);
            scoreCell.setCellValue("Score");
            CellStyle scoreCellStyle = workbook.createCellStyle();
            Font scoreFontStyle = workbook.createFont();
            scoreFontStyle.setBold(true);
            scoreFontStyle.setFontName("Times New Roman");
            scoreFontStyle.setFontHeightInPoints((short) 12);
            scoreCellStyle.setFont(scoreFontStyle);
            scoreCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            scoreCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            scoreCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            scoreCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            scoreCell.setCellStyle(scoreCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            Row contentScoreRow = sheet.getRow(paperBeginAddress);
            Cell contentScoreCell = contentScoreRow.createCell(2);
            contentScoreCell.setCellValue("   / 20");
            CellStyle contentScoreCellStyle = workbook.createCellStyle();
            Font contentScoreFontStyle = workbook.createFont();
            contentScoreFontStyle.setBold(true);
            contentScoreFontStyle.setFontName("Times New Roman");
            contentScoreFontStyle.setFontHeightInPoints((short) 12);
            contentScoreCellStyle.setFont(contentScoreFontStyle);
            contentScoreCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            contentScoreCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            contentScoreCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            contentScoreCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            contentScoreCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            contentScoreCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            contentScoreCell.setCellStyle(contentScoreCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            Row innovativeScoreRow = sheet.getRow(paperBeginAddress);
            Cell innovativeScoreCell = innovativeScoreRow.createCell(3);
            innovativeScoreCell.setCellValue("   / 20");
            CellStyle innovativeScoreCellStyle = workbook.createCellStyle();
            Font innovativeScoreFontStyle = workbook.createFont();
            innovativeScoreFontStyle.setBold(true);
            innovativeScoreFontStyle.setFontName("Times New Roman");
            innovativeScoreFontStyle.setFontHeightInPoints((short) 12);
            innovativeScoreCellStyle.setFont(innovativeScoreFontStyle);
            innovativeScoreCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            innovativeScoreCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            innovativeScoreCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            innovativeScoreCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            innovativeScoreCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            innovativeScoreCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            innovativeScoreCell.setCellStyle(innovativeScoreCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            Row logicScoreRow = sheet.getRow(paperBeginAddress);
            Cell logicScoreCell = logicScoreRow.createCell(4);
            logicScoreCell.setCellValue("   / 20");
            CellStyle logicScoreCellStyle = workbook.createCellStyle();
            Font logicScoreFontStyle = workbook.createFont();
            logicScoreFontStyle.setBold(true);
            logicScoreFontStyle.setFontName("Times New Roman");
            logicScoreFontStyle.setFontHeightInPoints((short) 12);
            logicScoreCellStyle.setFont(logicScoreFontStyle);
            logicScoreCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            logicScoreCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            logicScoreCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            logicScoreCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            logicScoreCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            logicScoreCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            logicScoreCell.setCellStyle(logicScoreCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            Row expressionScoreRow = sheet.getRow(paperBeginAddress);
            Cell expressionScoreCell = expressionScoreRow.createCell(5);
            expressionScoreCell.setCellValue("   / 20");
            CellStyle expressionScoreCellStyle = workbook.createCellStyle();
            Font expressionScoreFontStyle = workbook.createFont();
            expressionScoreFontStyle.setBold(true);
            expressionScoreFontStyle.setFontName("Times New Roman");
            expressionScoreFontStyle.setFontHeightInPoints((short) 12);
            expressionScoreCellStyle.setFont(expressionScoreFontStyle);
            expressionScoreCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            expressionScoreCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            expressionScoreCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            expressionScoreCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            expressionScoreCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            expressionScoreCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            expressionScoreCell.setCellStyle(expressionScoreCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            Row qaScoreRow = sheet.getRow(paperBeginAddress);
            Cell qaScoreCell = qaScoreRow.createCell(6);
            qaScoreCell.setCellValue("   / 20");
            CellStyle qaScoreCellStyle = workbook.createCellStyle();
            Font qaScoreFontStyle = workbook.createFont();
            qaScoreFontStyle.setBold(true);
            qaScoreFontStyle.setFontName("Times New Roman");
            qaScoreFontStyle.setFontHeightInPoints((short) 12);
            qaScoreCellStyle.setFont(qaScoreFontStyle);
            qaScoreCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            qaScoreCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            qaScoreCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            qaScoreCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            qaScoreCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            qaScoreCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            qaScoreCell.setCellStyle(qaScoreCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress, 7, 8);
            sheet.addMergedRegion(cellRangeAddress);
            Row totalMarksScoreRow = sheet.getRow(paperBeginAddress++);
            Cell totalMarksScoreCell = totalMarksScoreRow.createCell(7);
            totalMarksScoreCell.setCellValue("   / 100");
            CellStyle totalMarksScoreCellStyle = workbook.createCellStyle();
            Font totalMarksScoreFontStyle = workbook.createFont();
            totalMarksScoreFontStyle.setBold(true);
            totalMarksScoreFontStyle.setFontName("Times New Roman");
            totalMarksScoreFontStyle.setFontHeightInPoints((short) 12);
            totalMarksScoreCellStyle.setFont(totalMarksScoreFontStyle);
            totalMarksScoreCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            totalMarksScoreCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            totalMarksScoreCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            totalMarksScoreCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            totalMarksScoreCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            totalMarksScoreCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            totalMarksScoreCell.setCellStyle(totalMarksScoreCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress, 0, 1);
            sheet.addMergedRegion(cellRangeAddress);
            Row commentRow = sheet.createRow(paperBeginAddress);
            Cell commentCell = commentRow.createCell(0);
            commentCell.setCellValue("Comment");
            CellStyle commentCellStyle = workbook.createCellStyle();
            Font commentFontStyle = workbook.createFont();
            commentFontStyle.setBold(true);
            commentFontStyle.setFontName("Times New Roman");
            commentFontStyle.setFontHeightInPoints((short) 12);
            commentCellStyle.setFont(commentFontStyle);
            commentCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            commentCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            commentCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            commentCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            commentCell.setCellStyle(commentCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress, 2, 8);
            sheet.addMergedRegion(cellRangeAddress);
            Row commentContentRow = sheet.getRow(paperBeginAddress++);
            Cell commentContentCell = commentContentRow.createCell(2);
            CellStyle commentContentCellStyle = workbook.createCellStyle();
            Font commentContentFontStyle = workbook.createFont();
            commentContentFontStyle.setBold(true);
            commentContentFontStyle.setFontName("Times New Roman");
            commentContentFontStyle.setFontHeightInPoints((short) 12);
            commentContentCellStyle.setFont(commentContentFontStyle);
            commentContentCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            commentContentCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            commentContentCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            commentContentCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            commentContentCell.setCellStyle(commentContentCellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

            cellRangeAddress = new CellRangeAddress(paperBeginAddress, paperBeginAddress, 0, 8);
            sheet.addMergedRegion(cellRangeAddress);
            blankRow = sheet.createRow(paperBeginAddress++);
            blankCell = blankRow.createCell(0);
            blankCellStyle = workbook.createCellStyle();
            blankCell.setCellStyle(blankCellStyle);
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
