package com.insurance.monocept.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
	
	@Column(name = "date_created", nullable = false, updatable = false)
	protected Date dateCreated;
	
	@Column(name = "date_updated", nullable = false)
	protected Date dateUpdated;
	
	public BaseEntity() {
		super();
	    this.dateCreated = new Date();
	    this.dateUpdated = new Date();
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}	
}
