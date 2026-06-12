package controller;

import java.util.Scanner;

import service.MemberService;

import vo.MemberVO;

public class MemberController {

	private Scanner sc;
	private MemberService memberService;

	public MemberController() {

		sc = new Scanner(System.in);

		memberService = new MemberService();
	}
	
	/*
	 * 회원가입
	 */
	public void join() {

		try {

			MemberVO vo = new MemberVO();

			System.out.print("아이디 : ");
			
			String userId = sc.nextLine();
			
			while (!memberService.existUserId(userId)) {
				System.out.println("이미 존재하는 아이디입니다.");
				System.out.print("아이디 : ");
				userId = sc.nextLine();
			}
			
			vo.setUserId(userId);
			
			System.out.print("비밀번호 : ");

			vo.setPassword(sc.nextLine());

			System.out.print("이름 : ");

			vo.setName(sc.nextLine());

			System.out.print("전화번호 : ");

			vo.setPhone(sc.nextLine());

			int nResult = memberService.join(vo);

			if (nResult > 0) {
				System.out.println("회원가입 성공");
				System.out.println("회원번호 : " + vo.getMemberId());				

			} else {

				System.out.println("회원가입 실패");
			}

		} catch (Exception e) {

			System.out.println("회원가입 오류 : " + e.getMessage());
		}
	}

	/*
	 * 로그인 실패 3회 종료 처리
	 */
	public MemberVO login() {

		int failCount = 0;

		while (failCount < 3) {

			try {

				System.out.print("아이디 : ");

				String userId = sc.nextLine();

				System.out.print("비밀번호 : ");

				String userPass = sc.nextLine();

				MemberVO loginMember = memberService.login(userId, userPass);

				if (loginMember != null) {

					System.out.println("로그인 성공");

					return loginMember;

				} else {

					failCount++;

					System.out.println("로그인 실패 (" + failCount + "/3)");
				}

			} catch (Exception e) {

				System.out.println("로그인 오류 : " + e.getMessage());
			}
		}

		System.out.println("로그인 3회 실패");

		return null;
	}
}