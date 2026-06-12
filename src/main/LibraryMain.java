package main;

import java.util.Scanner;

import controller.BookController;
import controller.MemberController;

import util.DBUtil;

import vo.MemberVO;

public class LibraryMain {

	private Scanner sc;

	private MemberController memberController;

	private BookController bookController;

	/*
	 * 로그인 회원
	 */
	private MemberVO loginMember;

	public LibraryMain() {

		sc = new Scanner(System.in);

		memberController = new MemberController();

		bookController = new BookController();
	}

	public static void main(String[] args) {

		LibraryMain main = new LibraryMain();
		
		/*
		 * 종료 Hook 프로그램 종료시 리소스 정리
		 */
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			DBUtil.closeDataSource();

			System.out.println("리소스 정리 완료");
		}));

		main.start();
	}

	/*
	 * 시작 메뉴
	 */
	public void start() {

		boolean run = true;

		while (run) {

			try {

				System.out.println();
				System.out.println("==================================");

				System.out.println("      도서관 대출 시스템");

				System.out.println("==================================");

				System.out.println("1. 회원가입");

				System.out.println("2. 로그인");

				System.out.println("0. 종료");

				System.out.print("메뉴 선택 : ");

				int menu = Integer.parseInt(sc.nextLine());

				switch (menu) {

				/*
				 * 회원가입
				 */
				case 1:

					memberController.join();

					break;

				/*
				 * 로그인
				 */
				case 2:

					loginMember = memberController.login();

					/*
					 * 로그인 성공
					 */
					if (loginMember != null) {

						loginMenu();
					}

					break;

				/*
				 * 종료
				 */
				case 0:

					run = false;

					break;

				default:

					System.out.println("잘못된 메뉴입니다.");
				}

			} catch (Exception e) {

				System.out.println("메뉴 오류 : " + e.getMessage());
			}
		}

		shutdown();
	}

	/*
	 * 로그인 이후 메뉴
	 */
	private void loginMenu() {

		boolean loginRun = true;

		while (loginRun) {

			try {

				System.out.println();
				System.out.println("==================================");

				System.out.println(" 로그인 사용자 : " + loginMember.getName() + "(" + loginMember.getMemberId() + ")");

				System.out.println("==================================");

				System.out.println("1. 도서 검색");

				System.out.println("2. 도서 대출");

				System.out.println("3. 도서 반납");

				System.out.println("4. CSV 도서 등록");

				System.out.println("5. 단일 도서 등록");

				System.out.println("0. 로그아웃");

				System.out.print("메뉴 선택 : ");

				int menu = Integer.parseInt(sc.nextLine());

				switch (menu) {

				/*
				 * 도서 검색
				 */
				case 1:

					bookController.searchBook();

					break;

				/*
				 * 도서 대출
				 */
				case 2:

					bookController.rentBook(loginMember.getMemberId());

					break;

				/*
				 * 도서 반납
				 */
				case 3:					
					bookController.returnBook(loginMember.getMemberId());

					break;

				/*
				 * CSV 대량 등록
				 */
				case 4:

					bookController.importCsvBooks();

					break;

				/*
				 * 단일 도서 등록
				 */
				case 5:

					bookController.insertBook();

					break;

				/*
				 * 로그아웃
				 */
				case 0:

					loginMember = null;

					loginRun = false;

					System.out.println("로그아웃 완료");

					break;

				default:

					System.out.println("잘못된 메뉴입니다.");
				}

			} catch (Exception e) {

				System.out.println("로그인 메뉴 오류 : " + e.getMessage());
			}
		}
	}

	/*
	 * 종료 처리
	 */
	private void shutdown() {

		try {

			if (sc != null) {

				sc.close();
			}

			DBUtil.closeDataSource();

		} catch (Exception e) {

			System.out.println("종료 처리 오류 : " + e.getMessage());
		}
	}
}