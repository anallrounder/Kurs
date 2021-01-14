package edu.bit.ex.dto;

import java.sql.Timestamp;

/*
bId number(4) primary key,
bName varchar2(20),
bTitle varchar2(100),
bContent varchar2(300),
bDate date default sysdate,
bHit number(4) default 0,
bGroup number(4),
bStep number(4),
bIndent number(4)
*/
//실무에서는 데이터 직접 안쓰고 툴이 있다.

public class BDto {
	private int bId; 
	private String bName;
	private String bTitle; 
	private String bContent;
	private Timestamp bDate;
	private int bHit;
	private int bGroup; 
	private int bStep;
	private int bIndent; 
	
	 public BDto() { 
		  // 디폴트 생성자 반드시 만들어야한다. 
		  // 생성자 만들면 디폴트 생성자가 자동생성되지않기 때문에 만드는 습관필요!
	      // TODO Auto-generated constructor stub
	   }
	   
	   public BDto(int bId, String bName, String bTitle, String bContent, Timestamp bDate, int bHit, int bGroup, int bStep, int bIndent) {
	      // TODO Auto-generated constructor stub
	      this.bId = bId;
	      this.bName = bName;
	      this.bTitle = bTitle;
	      this.bContent = bContent;
	      this.bDate = bDate;
	      this.bHit = bHit;
	      this.bGroup = bGroup;
	      this.bStep = bStep;
	      this.bIndent = bIndent;
	   }

	   public int getbId() {
	      return bId;
	   }

	   public void setbId(int bId) {
	      this.bId = bId;
	   }

	   public String getbName() {
	      return bName;
	   }

	   public void setbName(String bName) {
	      this.bName = bName;
	   }

	   public String getbTitle() {
	      return bTitle;
	   }

	   public void setbTitle(String bTitle) {
	      this.bTitle = bTitle;
	   }

	   public String getbContent() {
	      return bContent;
	   }

	   public void setbContent(String bContent) {
	      this.bContent = bContent;
	   }

	   public Timestamp getbDate() {
	      return bDate;
	   }

	   public void setbDate(Timestamp bDate) {
	      this.bDate = bDate;
	   }

	   public int getbHit() {
	      return bHit;
	   }

	   public void setbHit(int bHit) {
	      this.bHit = bHit;
	   }

	   public int getbGroup() {
	      return bGroup;
	   }

	   public void setbGroup(int bGroup) {
	      this.bGroup = bGroup;
	   }

	   public int getbStep() {
	      return bStep;
	   }

	   public void setbStep(int bStep) {
	      this.bStep = bStep;
	   }

	   public int getbIndent() {
	      return bIndent;
	   }

	   public void setbIndent(int bIndent) {
	      this.bIndent = bIndent;
	   }

}
