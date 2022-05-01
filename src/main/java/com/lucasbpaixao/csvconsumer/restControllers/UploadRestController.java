package com.lucasbpaixao.csvconsumer.restControllers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.lucasbpaixao.csvconsumer.dao.TablesDao;
import com.lucasbpaixao.csvconsumer.models.ColumnsCreateds;
import com.lucasbpaixao.csvconsumer.models.TablesCreateds;
import com.lucasbpaixao.csvconsumer.repository.TableRepository;
import com.lucasbpaixao.csvconsumer.services.UploadService;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class UploadRestController {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private TablesDao tablesDao;

    @Autowired
    private TableRepository tableRepository;

    @PostMapping("/upload")
    @Transactional
    public ResponseEntity<List<ColumnsCreateds>> upload(@RequestParam(name="csvFile") MultipartFile csvFile, @RequestParam(name="tableName") String tableName, @RequestParam(name="primaryKeyField") String primaryKeyField) throws IOException, SQLException {
        CSVParser csvParser = CSVFormat.EXCEL.withHeader().parse(new InputStreamReader(csvFile.getInputStream()));

        List<CSVRecord> csvRecords = csvParser.getRecords();

        List<String> header = csvParser.getHeaderNames();
        List<String> newColumnNames = uploadService.standardizeColumnNames(header);

        List<ColumnsCreateds> columns = uploadService.createColumns(newColumnNames, csvParser.getRecords(), primaryKeyField);

        TablesCreateds tablesCreateds = new TablesCreateds(tableName, columns.get(0).getColumnName(), columns);

        tablesDao.startConnection();
        tablesDao.createTable(tableName, columns);
        tablesDao.saveData(tablesCreateds, csvRecords);
        tablesDao.closeConnection();

        tableRepository.saveAndFlush(tablesCreateds);

        return ResponseEntity.ok().body(columns);        
    }

    @GetMapping("/tables")
    public ResponseEntity<List<TablesCreateds>> getTables(){

        List<TablesCreateds> tablesCreateds = tableRepository.findAll();
        return ResponseEntity.ok().body(tablesCreateds);
    }
}
