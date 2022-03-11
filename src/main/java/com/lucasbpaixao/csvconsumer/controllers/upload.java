package com.lucasbpaixao.csvconsumer.controllers;

import com.lucasbpaixao.csvconsumer.models.TablesCreateds;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/upload")
public class upload {
    
    
    public ResponseEntity<TablesCreateds> upload() {
        return null;
    }
}
