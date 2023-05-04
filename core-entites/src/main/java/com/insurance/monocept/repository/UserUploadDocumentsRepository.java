package com.insurance.monocept.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.monocept.entity.UserUploadDocuments;

public interface UserUploadDocumentsRepository extends JpaRepository<UserUploadDocuments, Long>{

	List<UserUploadDocuments> findByStatus(String string);

}
