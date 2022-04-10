package com.lucasbpaixao.csvconsumer.repository;

import com.lucasbpaixao.csvconsumer.models.TablesCreateds;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<TablesCreateds, Long>{
    
}
