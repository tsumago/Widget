package com.boulifa.widget.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Positive;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Widget {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false, unique=false)
	private int centerX;

	@Column(nullable=false, unique=false)
	private int centerY;
	
	@Column(nullable=false, unique=false)
	@Positive
	private int width;
	
	@Column(nullable=false, unique=false)
	@Positive
	private int height;
	
	@Column(nullable=false, unique=false)
	@LastModifiedDate
	private LocalDateTime lasModified;
	
	@Column(nullable=false, unique=true)
	private Integer depthZ;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public int getCenterX() {
		return centerX;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Integer getDepthZ() {
		return depthZ;
	}

	public void setDepthZ(Integer depth) {
		this.depthZ = depth;
	}
	
	public LocalDateTime getLasModified() {
		return lasModified;
	}

	public void setLasModified(LocalDateTime lasModified) {
		this.lasModified = lasModified;
	}

	@Override
	public String toString() {
		return "Center (" + centerX + ", " + centerY + ") Dimensions (W: " + width + ", H: " + height + ") Depth (" + depthZ + ")";   
	}
	
	@Override
	public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
  
        if (!(obj instanceof Widget)) {
            return false;
        }
          
        Widget widget = (Widget) obj;
          
        return centerX == widget.getCenterX() && centerY == widget.getCenterY() && height == widget.getHeight() && depthZ == widget.getDepthZ();
	}
	
	public void update(Widget w) {
		this.centerX = w.getCenterX();
		this.centerY = w.getCenterY();
		this.height = w.getHeight();
		this.width = w.getWidth();
		this.depthZ = w.getDepthZ();
	}
}
