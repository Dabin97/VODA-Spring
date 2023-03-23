package com.voda.dto;

import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("board")
public class BoardDTO {
	private int bno; 
	private String title;
	private String pd;
	private String casting; 
	private String content;
	private String newDate;
	private String expireDate;
	private int genre;
	private String posterVideo; 
	private int ottNo;
	
	public BoardDTO() {	}

	public int getBno() {
		return bno;
	}

	public void setBno(int bno) {
		this.bno = bno;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPd() {
		return pd;
	}

	public void setPd(String pd) {
		this.pd = pd;
	}

	public String getCasting() {
		return casting;
	}

	public void setCasting(String casting) {
		this.casting = casting;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNewDate() {
		return newDate;
	}

	public void setNewDate(String newDate) {
		this.newDate = newDate;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public int getGenre() {
		return genre;
	}

	public void setGenre(int genre) {
		this.genre = genre;
	}

	public String getPostVideo() {
		return posterVideo;
	}

	public void setPostVideo(String posterVideo) {
		this.posterVideo = posterVideo;
	}

	public int getOttNo() {
		return ottNo;
	}

	public void setOttNo(int ottNo) {
		this.ottNo = ottNo;
	}

	@Override
	public String toString() {
		return "BoardDTO [bno=" + bno + ", title=" + title + ", pd=" + pd + ", casting=" + casting + ", content="
				+ content + ", newDate=" + newDate + ", expireDate=" + expireDate + ", genre=" + genre + ", posterVideo="
				+ posterVideo + ", ottNo=" + ottNo + "]";
	}

	
	
	
	
	
	
	
}
