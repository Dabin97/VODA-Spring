package com.voda.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.voda.dto.SecessionDTO;
import com.voda.mapper.MemberMapper;
import com.voda.mapper.SecessionMapper;

@Service
public class SecessionService {
	private SecessionMapper mapper;

	public SecessionService(SecessionMapper mapper) {
		
		this.mapper = mapper;
	}

	public List<SecessionDTO> selectMemberList(int pageNo, int i) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pageNo", pageNo);
		map.put("contentCount", i);
		System.out.println("selectMemberList: " + mapper.selectMemberList(map));
		return mapper.selectMemberList(map);
	}

	public int selectMemberCount() {
		return mapper.selectMemberCount();
	}
}
