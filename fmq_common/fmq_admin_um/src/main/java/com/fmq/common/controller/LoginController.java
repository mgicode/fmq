package com.fmq.common.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.fmq.common.base.BaseController;
import com.fmq.common.base.RspCodeConstants;
import com.fmq.common.config.WebSecurityConfig;
import com.fmq.common.controller.vo.CommonVO;
import com.fmq.common.controller.vo.LoginVO;
import com.fmq.common.dto.UserInfoDTO;
import com.fmq.common.service.LoginService;
import com.fmq.common.service.UserInfoService;
/**
 * 
 * @author ljg
 *
 */
@RestController
public class LoginController extends BaseController {
	@Autowired
	LoginService loginService;
	@Autowired
	UserInfoService userInfoService;

	@GetMapping("/index")
	public String index(@SessionAttribute(WebSecurityConfig.SESSION_KEY) String account, Model model, Object webSecurityConfig) {
		model.addAttribute("name", account);
		return "index";
	}


	@RequestMapping("/login")
	@ResponseBody
	public CommonVO loginPost(@RequestParam (value="account" )String account ,
			@RequestParam (value="password" )String password ,
			HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		LoginVO vo = new LoginVO();

		if ("".equals(account) || account == null) {
			vo.setResponseCode(RspCodeConstants.RSP_CODE_FAI);
			vo.setResponseMsg("账户不能空");
			return vo;
		}
		if ("".equals(password) || password == null) {
			vo.setResponseCode(RspCodeConstants.RSP_CODE_FAI);
			vo.setResponseMsg("密码不能空");
			return vo;
		}
		if (!"".equals(password)) {
			UserInfoDTO userAccount = userInfoService.fundUserInfoByUserName(account);
			UserInfoDTO userPassword = userInfoService.fundUserInfoByPassword(password);
			if (userAccount == null) {
				vo.setResponseCode(RspCodeConstants.RSP_CODE_FAI);
				vo.setResponseMsg(RspCodeConstants.RSP_CODE_ACCOUNT_NULL);
				return vo;
			}
			if (userPassword == null) {
				vo.setResponseCode(RspCodeConstants.RSP_CODE_FAI);
				vo.setResponseMsg("密码不正确");
				return vo;
			}
			if (!account.equals(userAccount.getUserName())) {
				vo.setResponseCode(RspCodeConstants.RSP_CODE_FAI);
				vo.setResponseMsg("用户名不正确");
				return vo;
			}
			if (password.equals(userPassword.getPassWord())) {
				vo.setResponseCode(RspCodeConstants.RSP_CODE_SUCCESS);
				vo.setResponseMsg("登陆成功");
				// 设置session
				session.setAttribute(WebSecurityConfig.SESSION_KEY, account);
				return vo;
			}
		}
		return vo;
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// 移除session
		session.removeAttribute(WebSecurityConfig.SESSION_KEY);
		return "redirect:/login";
	}

}
