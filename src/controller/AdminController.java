package controller;

import java.util.List;
import java.util.Scanner;

import service.AdminService;

import vo.OverdueVO;

public class AdminController {

	private Scanner sc;

	private AdminService adminService;

	public AdminController() {

		sc = new Scanner(System.in);

		adminService = new AdminService();
	}

	/*
	 * 연체자 목록 조회
	 */
	public void showOverdueList() {

		try {

			List<OverdueVO> list = adminService.getOverdueList();

			if (list.isEmpty()) {

				System.out.println("연체자 없음");

				return;
			}

			for (OverdueVO vo : list) {

				System.out.println("----------------------------");

				System.out.println("연체번호 : " + vo.getOverdueId());

				System.out.println("회원번호 : " + vo.getMemberId());

				System.out.println("연체시작일 : " + vo.getOverdueStartDate());

				System.out.println("해제예정일 : " + vo.getOverdueReleaseDate());

				System.out.println("상태 : " + vo.getOverdueStatus());
			}

		} catch (Exception e) {

			System.out.println("연체 목록 조회 오류 : " + e.getMessage());
		}
	}

	/*
	 * 관리자 직접 연체 해제
	 */
	public void releaseOverdue() {

		try {

			System.out.print("연체번호 : ");

			int overdueId = Integer.parseInt(sc.nextLine());

			System.out.print("회원번호 : ");

			int memberId = Integer.parseInt(sc.nextLine());

			boolean result = adminService.releaseOverdue(overdueId, memberId);

			if (result) {

				System.out.println("연체 해제 완료");

			} else {

				System.out.println("연체 해제 실패");
			}

		} catch (Exception e) {

			System.out.println("연체 해제 오류 : " + e.getMessage());
		}
	}

	/*
	 * 자동 연체 해제 실행
	 */
	public void autoRelease() {

		try {

			adminService.autoReleaseOverdue();

			System.out.println("자동 연체 해제 완료");

		} catch (Exception e) {

			System.out.println("자동 해제 오류 : " + e.getMessage());
		}
	}

	/*
	 * 연체 검사
	 */
	public void checkOverdue() {

		try {

			adminService.checkOverdueMembers();

			System.out.println("연체 검사 완료");

		} catch (Exception e) {

			System.out.println("연체 검사 오류 : " + e.getMessage());
		}
	}
}