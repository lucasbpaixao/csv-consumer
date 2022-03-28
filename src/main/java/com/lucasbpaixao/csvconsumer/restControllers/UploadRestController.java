package com.lucasbpaixao.csvconsumer.restControllers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.lucasbpaixao.csvconsumer.dao.TablesDao;
import com.lucasbpaixao.csvconsumer.models.Column;
import com.lucasbpaixao.csvconsumer.services.UploadService;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadRestController {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private TablesDao tablesDao;

    @PostMapping
    public ResponseEntity<List<Column>> upload(@RequestParam(name="csvFile") MultipartFile csvFile, @RequestParam(name="tableName") String tableName, @RequestParam(name="primaryKeyField") String primaryKeyField) throws IOException, SQLException {

        CSVParser csvParser = CSVFormat.EXCEL.withHeader().parse(new InputStreamReader(csvFile.getInputStream()));

        List<String> header = csvParser.getHeaderNames();
        List<String> newColumnNames = uploadService.standardizeColumnNames(header);

        List<Column> columns = uploadService.creatColumns(newColumnNames, csvParser.getRecords());

        tablesDao.startConnection();
        tablesDao.createTable(tableName, columns);
        // /tablesDao.closeConnection();

        return ResponseEntity.ok().body(columns);        
    }
}
