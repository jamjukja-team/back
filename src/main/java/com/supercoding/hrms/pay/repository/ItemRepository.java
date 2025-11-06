package com.supercoding.hrms.pay.repository;

import com.supercoding.hrms.pay.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, String> {
}
