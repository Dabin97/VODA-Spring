package com.voda;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.voda.dto.BoardDTO;
import com.voda.dto.FileDTO;
import com.voda.dto.ManagerDTO;
import com.voda.dto.MemberDTO;
import com.voda.dto.ReviewDTO;
import com.voda.dto.SecessionDTO;
import com.voda.mapper.BoardMapper;
import com.voda.service.BoardService;
import com.voda.service.MemberService;
import com.voda.service.ReviewService;
import com.voda.service.SecessionService;
import com.voda.vo.PaggingVO;

@Controller
public class MainController {
	private MemberService memberService;
	private BoardService boardService;
	private ReviewService reviewService;
	private SecessionService secessionService;
	

	public MainController(MemberService memberService, BoardService boardService, ReviewService reviewService, SecessionService secessionService) {
		super();
		this.memberService = memberService; 
		this.boardService = boardService;
		this.reviewService = reviewService;
		this.secessionService = secessionService;
	} 
	
	 
	@RequestMapping("/index") 
	public String index() {
		return "index"; 
	} 
	
	@RequestMapping("/before_login_main")
	public ModelAndView before_login_main() {
		ModelAndView view = new ModelAndView();
	    view.setViewName("before_login_main");

	    List<BoardDTO> list = boardService.selectMainContentList();
	    view.addObject("list", list);

	    return view;
	}
	
	

	@RequestMapping("/main")//메인 베스트 컨텐츠 -test중
	public ModelAndView MainContentList() {
	    ModelAndView view = new ModelAndView();
	    view.setViewName("main");

	    List<BoardDTO> list = boardService.selectMainContentList();
	    view.addObject("list", list);

	    return view;
	}

	
	
	
	@RequestMapping("/my_page")
	public String my_page() {
		return "my_page"; 
	}
	
	@RequestMapping("/search")
	public ModelAndView SearchContentList() {
	    ModelAndView view = new ModelAndView();
	    view.setViewName("search");
	    
	    List<BoardDTO> list = boardService.selectMainContentList();
	    view.addObject("list", list);

	    return view; 
	}
	
	@RequestMapping("/edit")
	public String edit() {
		return "profile_edit";  
	}
	
	
	@RequestMapping("/content_page")
	public String contentview(HttpSession session) {
		return "content_page";
	}
	
	@RequestMapping("/new_expire")
	public String new_expire(HttpSession session) {
		return "new_expire";
	}
	
		////////////////////관리자 페이지////////////////////////////////
	@RequestMapping("/admin/index")
	public String adminIndex() {
		return "admin_before_login";
		} 
	
	@PostMapping("/admin/login")
	public String adminLogin(String mid, String mpasswd, HttpSession session) {
		ManagerDTO dto = memberService.loginAdmin(mid, mpasswd);
		session.setAttribute("dto", dto); 
		if(dto == null) { return "admin_before_login";}
		return "redirect:/admin_main";
		}

	
	
	@RequestMapping("/admin_main") 
	public String adminMain() {
		return "admin_main";
		}
		
		@GetMapping("/admin/logout")
		public String logoutAdmin(HttpSession session){
			session.invalidate();
			return "redirect:/admin/index"; //index로 보내서 오류뜸
			}

		@RequestMapping("/admin/member/list")
		public ModelAndView memberList(@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
			ModelAndView view = new ModelAndView();
			view.setViewName("admin_list_member");
			// 게시판 글목록
			List<MemberDTO> list = memberService.selectMemberList(pageNo, 7);
			// 페이징 정보
			int count = memberService.selectMemberCount();
			PaggingVO pagging = new PaggingVO(count, pageNo, 7);
			
			view.addObject("list", list);
			view.addObject("pagging", pagging);
			
			return view;
		}
		
		@RequestMapping("/member/edit/view/{id}")
		public ModelAndView memberEditView(@PathVariable String id,ModelAndView view) {
			MemberDTO dto = memberService.selectMember(id);
			view.addObject("dto", dto);
			view.setViewName("admin_member_edit");
			return view;
		}
	
		
		@RequestMapping("/member/edit")
		public String memberEdit(MemberDTO dto) {
			int result = memberService.editMember(dto);   
			return "redirect:/admin/member/list";
		}
		
		@RequestMapping("/admin/secession")
		public ModelAndView secessionList(@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
			ModelAndView view = new ModelAndView();
			view.setViewName("admin_withdrawal_member");
			// 게시판 글목록
			List<SecessionDTO> list = secessionService.selectMemberList(pageNo, 7);
			// 페이징 정보
			int count = secessionService.selectMemberCount();
			PaggingVO pagging = new PaggingVO(count, pageNo, 7);
			view.addObject("list", list);
			view.addObject("pagging", pagging);
			return view;
		}
		

		@RequestMapping("/member/delete/{id}")
		public ResponseEntity<String> deleteMember(@PathVariable String id) {
			int result = memberService.deleteMember(id);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("count", String.valueOf(result));
			if(result != 0) {
				map.put("message", "데이터 삭제 성공");
			}else {
				map.put("message", "데이터 삭제 실패");
				System.out.println(result);
			}
			return new ResponseEntity(map,HttpStatus.OK);
		}
		

		@RequestMapping("/login/member") // /login과 같은 메소드인지 확인할것
		public String memberLogin(String id, String passwd, HttpSession session) {
			MemberDTO dto = memberService.login(id, passwd);
			session.setAttribute("dto", dto);
			
			return "redirect:/main";
		}
		

		
		
	@RequestMapping("/admin/content/list") //컨텐츠 등록 게시판 리스트 - Main의 역할
	public ModelAndView adminContentList(@RequestParam(name = "pageNo", defaultValue = "1")int pageNo) {
		ModelAndView view = new ModelAndView();
		view.setViewName("admin_content_list");
		//게시판 글목록
		List<BoardDTO> list = boardService.selectBoardList(pageNo, 7);
		 
		//페이징 정보
		int count = boardService.selectBoardCount();
		PaggingVO pagging = new PaggingVO(count, pageNo, 7);
		
		view.addObject("list",list); 
		view.addObject("pagging",pagging); 
		
		return view;
	}
	
	@RequestMapping("/register/view")
	public String registerView() {
		return "register";
	}
	
	@RequestMapping("/admin/content/register/view")
	public String adminContentRegisterView() {
		return "admin_content_register";
	}
	
	
	@RequestMapping("/admin/content/register") //등록
	public String adminContentRegister(BoardDTO dto, @RequestParam("file") MultipartFile[] file) {
			int bno = boardService.insertBoard(dto);
			
			//파일 업로드할 경로 설정
			String root = "c:\\fileupload\\";
			//현재 날짜 시간
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
			String date = sdf.format(Calendar.getInstance().getTime());
			
			for(int i=0;i<file.length;i++) {
				if(file[i].getSize() == 0) continue; 
				//서버에 파일을 저장할때 파일명을 날짜시간으로 변경
				//DB에 저장할 때는 원본파일명과 변경된 파일명 모두 저장
				//원본 파일명 뽑음
				String originFileName = file[i].getOriginalFilename();
				//저장할 파일명
				String fileName = date + "_" + i + originFileName +originFileName.substring(originFileName.lastIndexOf('.'));
				System.out.println("저장할 파일명 : " + fileName);
				
				//실제 파일이 업로드 되는 부분
				try {
					File saveFile = new File(root + fileName); 
					file[i].transferTo(saveFile);
					boardService.insertFile(new FileDTO(saveFile, bno, i));
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
				
		return "redirect:/admin/content/list";
	}
	
	
	@RequestMapping("/fileUpload") //UploadAdapter.js에 해당 경로가 연결되어있다. 그리고 해당 js는 board_write_view.html에 제이쿼리로 적용되어있다.
	public ResponseEntity<String> fileUpload(@RequestParam(value="upload") MultipartFile file, 
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		//원본 파일명
		String originFileName = file.getOriginalFilename();
		//upload 경로 설정
		String root = "c:\\fileupload\\";
		 
		//저장할 파일명
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		String date = sdf.format(Calendar.getInstance().getTime());		
		
		MemberDTO dto = (MemberDTO)session.getAttribute("dto");
		String fileName = date + "_" + dto.getId() +originFileName+ originFileName.substring(originFileName.lastIndexOf('.'));
		System.out.println("저장할 파일명 : " + fileName);
		
		File savefile =  new File(root + fileName); 
		//저장한 파일의 경로를 테이블에 저장
		int fno = boardService.uploadImage(savefile.getAbsolutePath());
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			file.transferTo(savefile); //파일 업로드
			map.put("uploaded", true);
			map.put("url", "/image/"+fno);
			map.put("bi_no", fno); //만든 파일 경로를 bi_no로 보냄. uploadAdater.js에서 bi_no는 파일번호이고 json객체로 받아야하기때문에 hashmap으로 보냄
			
		} catch (IOException e) {
			map.put("uploaded", false);
			map.put("message", "파일 업로드 중 에러 발생");
		}
		return new ResponseEntity(map,HttpStatus.OK); 
	}
	
	@RequestMapping("/image/{bno}")
	public void imageDown(@PathVariable("bno") int bno, HttpServletResponse response) {
		FileDTO dto = boardService.selectImageFile(bno);
		
		String path = dto.getPath();
		File file = new File(path);
		String fileName = dto.getFileName();
		
		try {
			fileName = URLEncoder.encode(fileName,"utf-8");
		} catch (UnsupportedEncodingException e1) { 
			e1.printStackTrace();
		}
		
		response.setHeader("Content-Disposition", "attachement;fileName="+fileName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setContentLength((int)file.length());
		try(FileInputStream fis = new FileInputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());) {
			
			byte[] buffer = new byte[1024*1024];
			
			while(true) {
				int size = fis.read(buffer);
				if(size == -1) break;
				bos.write(buffer,0,size);
				bos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@RequestMapping("/filedown") //borad_view 첨부파일 목록 출력 
	public void fileDown(int bno, int fno, HttpServletResponse response) { //되돌려줄것없이 write로 뿌릴것만 있으므로 void
		FileDTO dto = boardService.selectFile(bno, fno);	//fileUpload와 중간은 비슷함, bno와 fno를 둘다 보냄줌
		
		try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream()); 
			 FileInputStream fis = new FileInputStream(dto.getPath());) {

			byte[] buffer = new byte[1024 * 1024];

			while (true) {
				int count = fis.read(buffer);
				if (count == -1) break;
				bos.write(buffer, 0, count);
				bos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	
	
	@RequestMapping("/admin/content/write/{bno}") //수정창 이동
	public ModelAndView adminContentEditview(@PathVariable(name ="bno")int bno) {
		ModelAndView view = new ModelAndView();
		view.setViewName("admin_content_edit");
		
		//게시글 조회
		BoardDTO board = boardService.selectBoard(bno);
		//첨부파일 목록 조회
		List<FileDTO> fList = boardService.selectFileList(bno);
		
		view.addObject("board", board);
		view.addObject("fList", fList);
		
		return view;
	}
	
	
	@RequestMapping("/admin/content/edit") //수정메소드
	public String adminContentUpdate(BoardDTO dto, String[] del_file, @RequestParam("file") MultipartFile[] file) { //del_file : 삭제할 파일번호, 여러개받을거라 배열로
		boardService.updateBoard(dto);
		//파일 삭제 - 물리적
		//삭제할 파일 목록 받기 -- del_file에 아무것도 없을때 터지므로 if문으로 걸러준다.
		if(del_file != null && del_file.length != 0) { 
			List<String> filePath = boardService.deleteFileList(dto.getBno(),del_file);
			for(String f : filePath) {
				File dFile = new File(f); 
				dFile.delete();
				}
			//파일 삭제 - DB
			boardService.deleteFile(dto.getBno(),del_file);
		}
		
		//새 첨부파일 업로드처리(board-mapper insertFile의 메소드에 서브쿼리 추가함) -boardWrite를 복사해온다.
		// 파일 업로드할 경로 설정
		String root = "c:\\fileupload\\";
		// 현재 날짜 시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		String date = sdf.format(Calendar.getInstance().getTime());
		// 저장한 파일경로

		for (int i = 0; i < file.length; i++) {
			if (file[i].getSize() == 0)
				continue;
			// 서버에 파일을 저장할때 파일명을 날짜시간으로 변경
			// DB에 저장할 때는 원본파일명과 변경된 파일명 모두 저장
			// 원본 파일명 뽑음
			String originFileName = file[i].getOriginalFilename();
			// 저장할 파일명
			String fileName = date + "_" + i + originFileName + originFileName.substring(originFileName.lastIndexOf('.'));
			System.out.println("저장할 파일명 : " + fileName);

			try {
				// 실제 파일이 업로드 되는 부분
				File saveFile = new File(root + fileName);
				file[i].transferTo(saveFile);
				boardService.insertFile(new FileDTO(saveFile, dto.getBno(), 0));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "redirect:/admin/content/list";
	}
	
	
	@RequestMapping("/admin/content/detail/{bno}")
	public ModelAndView adminContentDetail(@PathVariable("bno") int bno, HttpSession session) {
		ModelAndView view = new ModelAndView();
		view.setViewName("admin_content_detail");
		
		//게시글 조회
		BoardDTO board = boardService.selectBoard(bno);
		//첨부파일 목록 조회
		List<FileDTO> fList = boardService.selectFileList(bno);
		
		view.addObject("board", board);
		view.addObject("fList", fList);
		
		return view;
		}

	
	@RequestMapping("/admin/content/delete/{bno}") //게시글 첨부파일 댓글삭제 모두 
	public String deleteBoard(@PathVariable(name ="bno")int bno) {
		//첨부파일 목록 조회
		List<FileDTO> fList = boardService.selectFileList(bno);
		//첨부파일 삭제 - bno가 외래키로 설정이 되어있다면 bno만 지워도 다 같이 지워질것이다.
		for(FileDTO f : fList) {
			File d = new File(f.getPath());
			d.delete();
		}
		boardService.deleteFile(bno, null); //d를 null로 넣고 deleteFile sql문을 고친다.
		
		//게시글 삭제
		boardService.deleteBoard(bno);
		return "redirect:/admin/content/list";
		//bno삭제시 comment도 삭제되도록 외래키로 묶어줌. 그러나 파일삭제는 외래키지정으로는 안되고 따로 메인컨트롤러 메소드에서 설정해주어야한다.
	}
	
	
	@RequestMapping("/content/search") // 검색 부분
	public ResponseEntity<String> selectSearchContentList(String kind, String search){
		List<MemberDTO> list = boardService.selectSearchContent(kind,search);
		
		return new ResponseEntity(list,HttpStatus.OK);
	}

	
	@RequestMapping("/admin/content/new") //신작 컨텐츠 리스트
	public ModelAndView adminNewContentList(@RequestParam(name = "pageNo", defaultValue = "1")int pageNo) {
		ModelAndView view = new ModelAndView();
		view.setViewName("admin_content_new");
		//게시판 글목록
		List<BoardDTO> list = boardService.selectNewList(pageNo, 7);
		 
		//페이징 정보
		int count = boardService.selectBoardCount(); //그대로 둬도 되는지 확인
		PaggingVO pagging = new PaggingVO(count, pageNo, 7);
		
		view.addObject("list",list); 
		view.addObject("pagging",pagging); 
		
		return view;
	}
	

	
	@RequestMapping("/admin/content/expire") //만료 컨텐츠 리스트
	public ModelAndView adminExpireContentList(@RequestParam(name = "pageNo", defaultValue = "1")int pageNo) {
		ModelAndView view = new ModelAndView();
		view.setViewName("admin_content_expire");
		//게시판 글목록
		List<BoardDTO> list = boardService.selectExpireList(pageNo, 7);
		 
		//페이징 정보
		int count = boardService.selectBoardCount(); 
		PaggingVO pagging = new PaggingVO(count, pageNo, 7);
		
		view.addObject("list",list); 
		view.addObject("pagging",pagging); 
		
		return view;
	}
	
	@RequestMapping("/review/register")
	public String registerReview(ReviewDTO dto) {
		reviewService.insertReview(dto);
		return "redirect:/content_page";
	}
	
	@PostMapping("/member_login") //login/member와 같은 기능?
	public String login(String id, String passwd, HttpSession session) {
		MemberDTO dto = memberService.login(id, passwd);
		session.setAttribute("member", dto);
		if(dto == null) {
				return "redirect:/index";
		}
		return "redirect:/main";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	


	@RequestMapping("/admin/review/list") //컨텐츠 등록 게시판 리스트 - Main의 역할
	public ModelAndView adminReviewList(@RequestParam(name = "pageNo", defaultValue = "1")int pageNo) {
	ModelAndView view = new ModelAndView();
	view.setViewName("admin_review_list");
	//게시판 글목록
	List<ReviewDTO> list = reviewService.selectReviewList(pageNo, 7);
	 
	//페이징 정보
	int count = reviewService.selectReviewCount();
	PaggingVO pagging = new PaggingVO(count, pageNo, 7);
	
	view.addObject("list",list); 
	view.addObject("pagging",pagging); 
	
	return view;
}
//	@RequestMapping("/content/detail/{bno}")
//	public ModelAndView updateView(@PathVariable int bno, ModelAndView mv, HttpSession session) {
//		BoardDTO dto = boardService.selectBoard(bno,session);
//		mv.addObject("board", dto);
//		mv.setViewName("content_page");
//		return mv;
//	}
	
	@RequestMapping("/content/detail/{bno}")
	public ModelAndView updateView(@PathVariable int bno, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		BoardDTO board = boardService.selectBoard(bno, session);
		List<ReviewDTO> rList = reviewService.selectReview(bno);
		
		//리뷰 목록 조회
		
		mv.addObject("board", board);
		mv.addObject("rList", rList);
		mv.setViewName("content_page");
		
		return mv;
	}

	

	@RequestMapping("/review/search") // 검색 부분
	public ResponseEntity<String> selectSearchReviewtList(String kind, String search){
		List<ReviewDTO> list = reviewService.selectSearchReview(kind,search);
			
		return new ResponseEntity(list,HttpStatus.OK);
	}
	
	@RequestMapping("/admin/review/detail/{rno}")
	public ModelAndView adminReviewDetail(@PathVariable("rno") int rno, HttpSession session) {
		ModelAndView view = new ModelAndView();
		view.setViewName("admin_review_detail");
		
		//게시글 조회
		ReviewDTO review = reviewService.selectAllReview(rno);
		
		view.addObject("review", review);
		
		return view;
		}
	
	@RequestMapping("/admin/review/delete/{rno}") //게시글 첨부파일 댓글삭제 모두 
	public String deleteReview(@PathVariable(name ="rno")int rno) {

		reviewService.deleteReview(rno);
		return "redirect:/admin/review/list";
	}
	
	@RequestMapping("/review/write")
	public String insertReview(ReviewDTO review, HttpSession session) {
		//댓글 작성자 정보 추가
		MemberDTO dto = (MemberDTO) session.getAttribute("dto");
		review.setId(dto.getId());
		
		reviewService.insertReview(review);
		
		return "redirect:/content/detail/{bno}"+review.getBno();
	}
		


		 
//		@RequestMapping("/member/delete/view")
//			public String memberDeleteView() {
//			return "/admin_list_member";
//		}	
		//
		//@RequestMapping("/list/paging")
		//public ModelAndView memberListPaiging(
		//@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
		//ModelAndView view = new ModelAndView();
		//List<MemberDTO> list = memberService.selectAllMember(pageNo);
		//view.addObject("list", list);
		//PaggingVO pagging = new  PaggingVO(memberService.selectMemberCount(),	pageNo, 5);
		//view.addObject("pagging", pagging);
		//view.setViewName("admin_list_member");
		//return view;
		//}
		
	
	
}