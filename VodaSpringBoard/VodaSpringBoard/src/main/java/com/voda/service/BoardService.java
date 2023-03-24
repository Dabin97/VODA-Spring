package com.voda.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.voda.dto.BoardDTO;
import com.voda.dto.FileDTO;
import com.voda.mapper.BoardMapper;

@Service
public class BoardService {
	private BoardMapper mapper;
	
	public BoardService(BoardMapper mapper) {
		this.mapper = mapper;
	}

	public int insertReviewLike(int rno, String id) {
		int r = 0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("rno", rno);
		map.put("id", id);
		try {
			r = mapper.insertReviewLike(map);
		}catch (Exception e) {
			mapper.deleteReviewLike(map);
		}
		return r;
	}

	public Object selectReviewLike(int rno) {
		return null;
	}

	public int uploadImage(String absolutePath) {
		//이미지 파일 번호 시퀸스로 생성한 결과를 받아옴
				int fno = mapper.selectImageFileNo();
				//받아온 파일 경로를 board_image 폴더 저장
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("fno", fno);
				map.put("path", absolutePath);
				mapper.insertBoardImage(map);
				return fno;
	}

	public FileDTO selectImageFile(int fno) {
		return mapper.selectImageFile(fno);
	}

	public FileDTO selectFile(int bno, int fno) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bno",bno);
		map.put("fno", fno);
		return mapper.selectFile(map);
	}

	public int insertBoard(BoardDTO dto) {
		int bno = mapper.selectBoardBno();
		dto.setBno(bno);
		mapper.insertBoard(dto);
		return bno; 
	}

	public int insertFile(FileDTO fileDTO) {
		return mapper.insertFile(fileDTO);
	}

	public BoardDTO selectBoard(int bno) {
		return mapper.selectBoard(bno);
	}

	public List<FileDTO> selectFileList(int bno) {
		return mapper.selectFileList(bno);
	}

	public int updateBoard(BoardDTO dto) {
		return mapper.updateBoard(dto);
	}

	public List<String> deleteFileList(int bno, String[] del_file) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bno",bno);
		map.put("fno",del_file);
		return mapper.deleteFileList(map);
	}

	public int deleteFile(int bno, String[] del_file) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bno",bno);
		map.put("fno",del_file);
		return mapper.deleteFile(map);
	}

	public void deleteBoard(int bno) {
		mapper.deleteBoard(bno);
	}

	public List<BoardDTO> selectBoardList(int pageNo, int i) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pageNo",pageNo);
		map.put("contentCount", i);
		return mapper.selectBoardList(map);
	}

	public int selectBoardCount() {
		return mapper.selectBoardCount();
	}

}