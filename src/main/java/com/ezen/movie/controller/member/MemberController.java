package com.ezen.movie.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.movie.comm.AbstractController;
import com.ezen.movie.comm.AjaxResVO;
import com.ezen.movie.comm.ValueException;
import com.ezen.movie.config.EmailService;
import com.ezen.movie.service.member.MemberDTO;
import com.ezen.movie.service.member.MemberService;
import com.ezen.movie.service.store.StoreService;

@Controller
@RequestMapping("/member")
public class MemberController extends AbstractController{
	

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/loginForm")
	public ModelAndView loginForm() {
		
		ModelAndView mav = new ModelAndView("/member/login");
		return mav;
		
	}
	
	@GetMapping("/joinForm")
	public ModelAndView joinForm() {
		
		ModelAndView mav = new ModelAndView("/member/join");
		return mav;
		
	}
	
	/**
	 * 아이디 중복 체크
	 * @param dto
	 * @return
	 * @throws ValueException
	 */
	@ResponseBody
	@PostMapping("/doubleChk")
	public AjaxResVO<?> doubleChk(MemberDTO dto) throws ValueException{
		
		AjaxResVO<?> data = new AjaxResVO<>();
		
		try {
			
			if(isNull(dto.getMemberId())) {
				throw new ValueException("잘못된 접근 경로입니다.");
			}
			
			boolean flag = memberService.getDoubleChk(dto);
			String msg = "";
			
			if(flag) {
				msg ="이미 있는 계정입니다.";
			}
			msg ="없는 계정입니다.";
			data = new AjaxResVO<>(AJAXPASS, msg);
			
		} catch (ValueException e) {
			e.getMessage();
			data = new AjaxResVO<>(AJAXFAIL, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			data = new AjaxResVO<>(AJAXFAIL, "오류로 인하여 실패하였습니다.");
		} 
		
		return data;
		
	}

	//인증 메일 발송
	@PostMapping("/mailAuthSend")
	@ResponseBody
    public AjaxResVO<?> mailConfirm(@RequestParam String email) throws Exception {
		
		AjaxResVO<?> data = new AjaxResVO<>();
		
		try {
			
			if(isNull(email)) {
				throw new ValueException("잘못된 접근 경로입니다.");
			}

	        String code = emailService.sendSimpleMessage(email);
	        
			emailService.sendMail(code);
			
			data = new AjaxResVO<>(AJAXPASS,"메일을 확인해주세요.");
			
		} catch (ValueException e) {
			e.getMessage();
			data = new AjaxResVO<>(AJAXFAIL, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			data = new AjaxResVO<>(AJAXFAIL, "오류로 인하여 실패하였습니다.");
		} 
		
		return data;
		
    }
	
}