package com.voda.controller.usercontroller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voda.dto.MemberDTO;
import com.voda.dto.ReviewDTO;
import com.voda.service.ReviewService;

@Controller
@RequestMapping("/member/review")
public class UserReviewController {

	private ReviewService reviewService;
	
	public UserReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
//	@GetMapping("/review/register")
//	public String registerReview(ReviewDTO dto) {
//		reviewService.insertReview(dto);
//		return "redirect:/content_page";
//	}
	 
	@PostMapping("/write/{bno}")
    public String insertReview(ReviewDTO review, HttpSession session) {
        //댓글 작성자 정보 추가
         MemberDTO member = (MemberDTO) session.getAttribute("member");
         String id = member.getId();
         review.setId(id);
         //System.out.println(id); //아이디는 잘 읽어옴, sql문도 문제 없음 근데 부적합 열유형 1111, null에러

        reviewService.insertReview(review);
        return "redirect:/board/content/detail/"+review.getBno();
    }
	
	@PostMapping("/like/{rno}")
	public ResponseEntity<String> reviewLike(@PathVariable(name = "rno") int rno, HttpSession session){
		HashMap<String, Object> map = new HashMap<String, Object>();
		MemberDTO dto = (MemberDTO) session.getAttribute("member");
		
		int result = reviewService.insertBoardLike(rno,dto.getId());
		
		if(result == 0)
			map.put("msg", "좋아요를 취소하셨습니다.");
		else
			map.put("msg", "좋아요를 하셨습니다.");
		
		map.put("rlike",reviewService.selectBoardLike(rno));
		 
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
	@PostMapping("/hate/{rno}")
	public ResponseEntity<String> reviewHate(@PathVariable(name = "rno") int rno, HttpSession session){
		HashMap<String, Object> map = new HashMap<String, Object>();
		MemberDTO dto = (MemberDTO) session.getAttribute("member");
		
		int result = reviewService.insertBoardHate(rno,dto.getId());
		
		if(result == 0)
			map.put("msg", "싫어요를 취소하셨습니다.");
		else
			map.put("msg", "싫어요를 하셨습니다.");
		
		map.put("rhate",reviewService.selectBoardHate(rno));
		
		return new ResponseEntity(map,HttpStatus.OK);
	}

	
	
	
}
