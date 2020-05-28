package com.lee.ficss.controller;

import com.lee.ficss.mapper.UserMapper;
import com.lee.ficss.pojo.User;
import com.lee.ficss.service.AgendaService;
import com.lee.ficss.service.ConferenceService;
import com.lee.ficss.service.TopicService;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.JsonResult;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller
@RequiresRoles("admin")
@RequestMapping("admin")
public class AdministratorController {

    private final UserService userService;
    private final AgendaService agendaService;
    private final DateFormatter dateFormatter;
    private final UserMapper userMapper;
    private final ConferenceService conferenceService;

    public AdministratorController(UserService userService, AgendaService agendaService, DateFormatter dateFormatter, UserMapper userMapper, ConferenceService conferenceService) {
        this.userService = userService;
        this.agendaService = agendaService;
        this.dateFormatter = dateFormatter;
        this.userMapper = userMapper;
        this.conferenceService = conferenceService;
    }

    @RequestMapping("/index")
    public String index(){
        return "admin/index";
    }

    @ResponseBody
    @RequestMapping("/agenda/show")
    public String showAgenda(){
        DataMap dataMap = agendaService.getValidAgendaList(dateFormatter.formatDateToSimpleString(new Date()));
        return JsonResult.build(dataMap).toJSONString();
    }


    @ResponseBody
    @PostMapping(value = "/search-candidate", produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchCandidate(@RequestParam("cnName") String cnName, @RequestParam("enName") String enName,
                                  @RequestParam("email") String email, @RequestParam("phone") String phone){
        DataMap dataMap = userService.getCandidate(cnName, enName, email, phone);
        return JsonResult.build(dataMap).toJSONString();
    }

    @ResponseBody
    @PostMapping(value = "/agenda/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createAgenda(@RequestParam("conferenceID")String conferenceID,
                               @RequestParam("agendaName")String agendaName,
                               @RequestParam("agendaDate") String agendaDate){
        Date now = new Date();
        agendaService.createAgenda(conferenceID, agendaName, agendaDate);
        DataMap dataMap = agendaService.getValidAgendaList(dateFormatter.formatDateToSimpleString(now));
        return JsonResult.build(dataMap).toJSONString();
    }

    @ResponseBody
    @PostMapping(value = "/event/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createEvent(@RequestParam("cnName") String cnName, @RequestParam("enName") String enName,
                                  @RequestParam("email") String email, @RequestParam("phone") String phone){
        DataMap dataMap = userService.getCandidate(cnName, enName, email, phone);
        return JsonResult.build(dataMap).toJSONString();
    }

   @RequestMapping(value = "download-candidate-form/{conferenceID}")
    public void downloadCandidateForm(@PathVariable("conferenceID") String conferenceID, HttpServletResponse response) {
       List<User> candidates = userMapper.getCandidatesInConference(conferenceID);

       Workbook workbook = new XSSFWorkbook();
       Sheet sheet = workbook.createSheet();

       CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 1, 0, 8);
       sheet.addMergedRegion(cellRangeAddress);

       Row titleRow = sheet.createRow(0);
       Cell titleCell = titleRow.createCell(0);
       titleCell.setCellValue(conferenceService.getConferenceByID(conferenceID).getConferenceName());
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

       cellRangeAddress = new CellRangeAddress(2, 2, 0, 8);
       sheet.addMergedRegion(cellRangeAddress);

       Row subTitleRow = sheet.createRow(2);
       Cell subTitleCell = subTitleRow.createCell(0);
       subTitleCell.setCellValue("Registration for Authors");
       CellStyle subTitleCellStyle = workbook.createCellStyle();
       Font subTitleFontStyle = workbook.createFont();
       subTitleFontStyle.setBold(true);
       subTitleFontStyle.setFontName("Times New Roman");
       subTitleFontStyle.setFontHeightInPoints((short) 16);
       subTitleCellStyle.setFont(subTitleFontStyle);
       subTitleCellStyle.setBorderTop(CellStyle.BORDER_THIN);
       subTitleCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
       subTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
       subTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
       subTitleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
       subTitleCell.setCellStyle(subTitleCellStyle);

       RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

       cellRangeAddress = new CellRangeAddress(3, 4, 0, 0);
       sheet.addMergedRegion(cellRangeAddress);

       Row orderRow = sheet.createRow(3);
       Cell orderCell = orderRow.createCell(0);
       orderCell.setCellValue("No.");
       CellStyle orderCellStyle = workbook.createCellStyle();
       Font orderFontStyle = workbook.createFont();
       orderFontStyle.setBold(true);
       orderFontStyle.setFontName("Times New Roman");
       orderFontStyle.setFontHeightInPoints((short) 16);
       orderCellStyle.setFont(orderFontStyle);
       orderCellStyle.setBorderTop(CellStyle.BORDER_THIN);
       orderCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
       orderCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
       orderCellStyle.setBorderRight(CellStyle.BORDER_THIN);
       orderCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
       orderCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
       orderCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       orderCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
       orderCell.setCellStyle(orderCellStyle);

       RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

       cellRangeAddress = new CellRangeAddress(3, 4, 1, 2);
       sheet.addMergedRegion(cellRangeAddress);

       Row cnNameRow = sheet.getRow(3);
       Cell cnNameCell = cnNameRow.createCell(1);
       cnNameCell.setCellValue("Chinese Name");
       CellStyle cnNameCellStyle = workbook.createCellStyle();
       Font cnNameFontStyle = workbook.createFont();
       cnNameFontStyle.setBold(true);
       cnNameFontStyle.setFontName("Times New Roman");
       cnNameFontStyle.setFontHeightInPoints((short) 12);
       cnNameCellStyle.setFont(cnNameFontStyle);
       cnNameCellStyle.setBorderTop(CellStyle.BORDER_THIN);
       cnNameCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
       cnNameCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
       cnNameCellStyle.setBorderRight(CellStyle.BORDER_THIN);
       cnNameCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
       cnNameCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
       cnNameCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       cnNameCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
       cnNameCell.setCellStyle(cnNameCellStyle);

       RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

       cellRangeAddress = new CellRangeAddress(3, 4, 3, 4);
       sheet.addMergedRegion(cellRangeAddress);

       Row enNameRow = sheet.getRow(3);
       Cell enNameCell = enNameRow.createCell(3);
       enNameCell.setCellValue("English Name");
       CellStyle enNameCellStyle = workbook.createCellStyle();
       Font enNameFontStyle = workbook.createFont();
       enNameFontStyle.setBold(true);
       enNameFontStyle.setFontName("Times New Roman");
       enNameFontStyle.setFontHeightInPoints((short) 12);
       enNameCellStyle.setFont(enNameFontStyle);
       enNameCellStyle.setBorderTop(CellStyle.BORDER_THIN);
       enNameCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
       enNameCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
       enNameCellStyle.setBorderRight(CellStyle.BORDER_THIN);
       enNameCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
       enNameCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
       enNameCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       enNameCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
       enNameCell.setCellStyle(enNameCellStyle);

       RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

       cellRangeAddress = new CellRangeAddress(3, 4, 5, 6);
       sheet.addMergedRegion(cellRangeAddress);

       Row signatureRow = sheet.getRow(3);
       Cell signatureCell = signatureRow.createCell(5);
       signatureCell.setCellValue("Signature");
       CellStyle signatureCellStyle = workbook.createCellStyle();
       Font signatureFontStyle = workbook.createFont();
       signatureFontStyle.setBold(true);
       signatureFontStyle.setFontName("Times New Roman");
       signatureFontStyle.setFontHeightInPoints((short) 12);
       signatureCellStyle.setFont(signatureFontStyle);
       signatureCellStyle.setBorderTop(CellStyle.BORDER_THIN);
       signatureCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
       signatureCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
       signatureCellStyle.setBorderRight(CellStyle.BORDER_THIN);
       signatureCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
       signatureCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
       signatureCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       signatureCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
       signatureCell.setCellStyle(signatureCellStyle);

       RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

       Row lunchRow = sheet.getRow(3);
       Cell lunchCell = lunchRow.createCell(7);
       lunchCell.setCellValue("Lunch");
       CellStyle lunchCellStyle = workbook.createCellStyle();
       Font lunchFontStyle = workbook.createFont();
       lunchFontStyle.setBold(true);
       lunchFontStyle.setFontName("Times New Roman");
       lunchFontStyle.setFontHeightInPoints((short) 12);
       lunchCellStyle.setFont(lunchFontStyle);
       lunchCellStyle.setBorderTop(CellStyle.BORDER_THIN);
       lunchCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
       lunchCellStyle.setBorderRight(CellStyle.BORDER_THIN);
       lunchCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
       lunchCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       lunchCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
       lunchCell.setCellStyle(lunchCellStyle);

       RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

       lunchRow = sheet.getRow(4);
       lunchCell = lunchRow.createCell(7);
       lunchCell.setCellValue("Buffet");
       lunchCellStyle = workbook.createCellStyle();
       lunchFontStyle = workbook.createFont();
       lunchFontStyle.setBold(true);
       lunchFontStyle.setFontName("Times New Roman");
       lunchFontStyle.setFontHeightInPoints((short) 12);
       lunchCellStyle.setFont(lunchFontStyle);
       lunchCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
       lunchCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
       lunchCellStyle.setBorderRight(CellStyle.BORDER_THIN);
       lunchCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
       lunchCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       lunchCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
       lunchCell.setCellStyle(lunchCellStyle);

       RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

       Row retailRow = sheet.getRow(3);
       Cell retailCell = retailRow.createCell(8);
       retailCell.setCellValue("Retail");
       CellStyle retailCellStyle = workbook.createCellStyle();
       Font retailFontStyle = workbook.createFont();
       retailFontStyle.setBold(true);
       retailFontStyle.setFontName("Times New Roman");
       retailFontStyle.setFontHeightInPoints((short) 12);
       retailCellStyle.setFont(retailFontStyle);
       retailCellStyle.setBorderTop(CellStyle.BORDER_THIN);
       retailCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
       retailCellStyle.setBorderRight(CellStyle.BORDER_THIN);
       retailCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
       retailCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       retailCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
       retailCell.setCellStyle(retailCellStyle);

       RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

       retailRow = sheet.getRow(4);
       retailCell = retailRow.createCell(8);
       retailCell.setCellValue("Vouchers");
       retailCellStyle = workbook.createCellStyle();
       retailFontStyle = workbook.createFont();
       retailFontStyle.setBold(true);
       retailFontStyle.setFontName("Times New Roman");
       retailFontStyle.setFontHeightInPoints((short) 12);
       retailCellStyle.setFont(retailFontStyle);
       retailCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
       retailCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
       retailCellStyle.setBorderRight(CellStyle.BORDER_THIN);
       retailCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
       retailCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       retailCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
       retailCell.setCellStyle(retailCellStyle);

       RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
       RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

       int rowBeginAddress = 5;
       int order = 1;
       for (User candidate : candidates){
           orderRow = sheet.createRow(rowBeginAddress);
           orderCell = orderRow.createCell(0);
           orderCell.setCellValue(order++);
           orderCellStyle = workbook.createCellStyle();
           orderFontStyle = workbook.createFont();
           orderFontStyle.setBold(true);
           orderFontStyle.setFontName("Times New Roman");
           orderFontStyle.setFontHeightInPoints((short) 16);
           orderCellStyle.setFont(orderFontStyle);
           orderCellStyle.setBorderTop(CellStyle.BORDER_THIN);
           orderCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
           orderCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
           orderCellStyle.setBorderRight(CellStyle.BORDER_THIN);
           orderCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
           orderCell.setCellStyle(orderCellStyle);

           RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

           cellRangeAddress = new CellRangeAddress(rowBeginAddress, rowBeginAddress, 1, 2);
           sheet.addMergedRegion(cellRangeAddress);

           cnNameRow = sheet.getRow(rowBeginAddress);
           cnNameCell = cnNameRow.createCell(1);
           cnNameCell.setCellValue(candidate.getCnName());
           cnNameCellStyle = workbook.createCellStyle();
           cnNameFontStyle = workbook.createFont();
           cnNameFontStyle.setBold(true);
           cnNameFontStyle.setFontName("Times New Roman");
           cnNameFontStyle.setFontHeightInPoints((short) 16);
           cnNameCellStyle.setFont(cnNameFontStyle);
           cnNameCellStyle.setBorderTop(CellStyle.BORDER_THIN);
           cnNameCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
           cnNameCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
           cnNameCellStyle.setBorderRight(CellStyle.BORDER_THIN);
           cnNameCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
           cnNameCell.setCellStyle(cnNameCellStyle);

           RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

           cellRangeAddress = new CellRangeAddress(rowBeginAddress, rowBeginAddress, 3, 4);
           sheet.addMergedRegion(cellRangeAddress);

           enNameRow = sheet.getRow(rowBeginAddress);
           enNameCell = enNameRow.createCell(3);
           enNameCell.setCellValue(candidate.getEnName());
           enNameCellStyle = workbook.createCellStyle();
           enNameFontStyle = workbook.createFont();
           enNameFontStyle.setBold(true);
           enNameFontStyle.setFontName("Times New Roman");
           enNameFontStyle.setFontHeightInPoints((short) 16);
           enNameCellStyle.setFont(enNameFontStyle);
           enNameCellStyle.setBorderTop(CellStyle.BORDER_THIN);
           enNameCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
           enNameCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
           enNameCellStyle.setBorderRight(CellStyle.BORDER_THIN);
           enNameCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
           enNameCell.setCellStyle(enNameCellStyle);

           RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

           cellRangeAddress = new CellRangeAddress(rowBeginAddress, rowBeginAddress, 5, 6);
           sheet.addMergedRegion(cellRangeAddress);

           signatureRow = sheet.getRow(rowBeginAddress);
           signatureCell = signatureRow.createCell(5);
           signatureCellStyle = workbook.createCellStyle();
           signatureFontStyle = workbook.createFont();
           signatureFontStyle.setBold(true);
           signatureFontStyle.setFontName("Times New Roman");
           signatureFontStyle.setFontHeightInPoints((short) 16);
           signatureCellStyle.setFont(signatureFontStyle);
           signatureCellStyle.setBorderTop(CellStyle.BORDER_THIN);
           signatureCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
           signatureCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
           signatureCellStyle.setBorderRight(CellStyle.BORDER_THIN);
           signatureCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
           signatureCell.setCellStyle(signatureCellStyle);

           RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

           lunchRow = sheet.getRow(rowBeginAddress);
           lunchCell = lunchRow.createCell(7);
           lunchCellStyle = workbook.createCellStyle();
           lunchFontStyle = workbook.createFont();
           lunchFontStyle.setBold(true);
           lunchFontStyle.setFontName("Times New Roman");
           lunchFontStyle.setFontHeightInPoints((short) 16);
           lunchCellStyle.setFont(lunchFontStyle);
           lunchCellStyle.setBorderTop(CellStyle.BORDER_THIN);
           lunchCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
           lunchCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
           lunchCellStyle.setBorderRight(CellStyle.BORDER_THIN);
           lunchCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
           lunchCell.setCellStyle(lunchCellStyle);

           RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

           retailRow = sheet.getRow(rowBeginAddress++);
           retailCell = retailRow.createCell(8);
           retailCellStyle = workbook.createCellStyle();
           retailFontStyle = workbook.createFont();
           retailFontStyle.setBold(true);
           retailFontStyle.setFontName("Times New Roman");
           retailFontStyle.setFontHeightInPoints((short) 16);
           retailCellStyle.setFont(retailFontStyle);
           retailCellStyle.setBorderTop(CellStyle.BORDER_THIN);
           retailCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
           retailCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
           retailCellStyle.setBorderRight(CellStyle.BORDER_THIN);
           retailCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
           retailCell.setCellStyle(retailCellStyle);

           RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
           RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
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
