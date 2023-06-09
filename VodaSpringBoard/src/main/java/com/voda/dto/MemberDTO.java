package com.voda.dto;

import org.apache.ibatis.type.Alias;

@Alias("member")
public class MemberDTO {
	private String id;
	private String passwd;
	private String name;
	private String nick;
	private String email;
	private int heart;

	
	public MemberDTO() {
		super();
	}


	public MemberDTO(String id, String passwd, String name, String nick, String email) {
		
		this.id = id;
		this.passwd = passwd;
		this.name = name;
		this.nick = nick;
		this.email = email;
	}
	
	


//	public MemberDTO(String id, String passwd, String nick) {
//		super();
//		this.id = id;
//		this.passwd = passwd;
//		this.nick = nick;
//	}

	
	
	public int getHeart() {
		return heart;
	}

	public void setHeart(int heart) {
		this.heart = heart;
	}
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPasswd() {
		return passwd;
	}


	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getNick() {
		return nick;
	}


	public void setNick(String nick) {
		this.nick = nick;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	@Override
	public String toString() {
		return "MemberDTO [id=" + id + ", passwd=" + passwd + ", name=" + name + ", nick=" + nick + ", email=" + email
				+ "]";
	}
	
	
	
	
}
