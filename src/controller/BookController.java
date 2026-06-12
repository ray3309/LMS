package controller;

import java.util.List;
import java.util.Scanner;

import service.BookService;

import vo.BookVO;
import vo.RentalVO;

public class BookController {

	private Scanner sc;

	private BookService bookService;

	public BookController() {

		sc = new Scanner(System.in);

		bookService = new BookService();
	}

	/*
	 * 도서 검색
	 */
	public void searchBook() {

		try {

			System.out.print("검색어 입력 : ");

			String keyword = sc.nextLine();

			List<BookVO> list = bookService.searchBook(keyword, 1, 20);

			if (list.isEmpty()) {

				System.out.println("검색 결과 없음");

				return;
			}

			for (BookVO vo : list) {

				System.out.println("=================================");

				System.out.println("BOOKID : " + vo.getBookId());

				System.out.println("등록번호 : " + vo.getRegNo());

				System.out.println("제목 : " + vo.getTitle());

				System.out.println("저자 : " + vo.getAuthor());

				System.out.println("출판사 : " + vo.getPublisher());

				System.out.println("ISBN : " + vo.getIsbn());

				System.out.println("KDC : " + vo.getKdc());

				System.out.println("DDC : " + vo.getDdc());

				System.out.println("대출상태 : " + vo.getRentStatus());

				/*
				 * 대출중 반납 예정일 출력
				 */
				if ("Y".equals(vo.getRentStatus())) {

					System.out.println("반납예정일 : " + vo.getReturnDueDate());
				}
			}

		} catch (Exception e) {

			System.out.println("도서 검색 오류 : " + e.getMessage());
		}
	}

	/*
	 * 도서 대출
	 */
	public void rentBook(int memberId) {

		try {

			System.out.print("대출할 BOOKID 입력 : ");

			int bookId = Integer.parseInt(sc.nextLine());

			boolean result = bookService.rentBook(memberId, bookId);

			if (result) {

				System.out.println("도서 대출 성공");

			} else {

				System.out.println("도서 대출 실패");
			}

		} catch (Exception e) {

			System.out.println("도서 대출 오류 : " + e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * 도서 반납
	 */
	public void returnBook(int memberId) {

		try {

			List<RentalVO> list = bookService.getCurrentRentalBooks(memberId);

			if (list == null || list.isEmpty()) {

				System.out.println("현재 대출중인 도서가 없습니다.");

				return;
			}

			System.out.println();
			System.out.println("===== 현재 대출중인 도서 =====");

			for (RentalVO vo : list) {

				System.out.println("RENTALID : " + vo.getRentalId());

				System.out.println("BOOKID   : " + vo.getBookId());

				System.out.println("제목     : " + vo.getBookTitle());

				System.out.println("대출일   : " + vo.getRentDate());

				System.out.println("반납예정 : " + vo.getReturnDueDate());

				System.out.println("-----------------------------");
			}

			System.out.print("반납할 RENTALID 입력 : ");

			int rentalId = Integer.parseInt(sc.nextLine());

			boolean result = bookService.returnBook(rentalId);

			if (result) {

				System.out.println("도서 반납 성공");

			} else {

				System.out.println("도서 반납 실패");
			}

		} catch (Exception e) {

			System.out.println("도서 반납 오류 : " + e.getMessage());

			e.printStackTrace();
		}
	}

	/*
	 * 도서 등록
	 */
	public void insertBook() {

		try {

			BookVO vo = new BookVO();

			System.out.print("등록번호 : ");

			vo.setRegNo(sc.nextLine());

			System.out.print("제목 : ");

			vo.setTitle(sc.nextLine());

			System.out.print("저자 : ");

			vo.setAuthor(sc.nextLine());

			System.out.print("출판사 : ");

			vo.setPublisher(sc.nextLine());

			System.out.print("ISBN : ");

			vo.setIsbn(sc.nextLine());

			System.out.print("KDC : ");

			vo.setKdc(sc.nextLine());

			System.out.print("DDC : ");

			vo.setDdc(sc.nextLine());

			int result = bookService.insertBook(vo);

			if (result > 0) {

				System.out.println("도서 등록 성공");

			} else {

				System.out.println("도서 등록 실패");
			}

		} catch (Exception e) {

			System.out.println("도서 등록 오류 : " + e.getMessage());
		}
	}

	/*
	 * CSV 다운로드 후 대량 등록
	 */
	public void importCsvBooks() {

		try {

			System.out.println("CSV 다운로드 및 대량 등록 시작...");

			boolean result = bookService.importCsvBooks();

			if (result) {

				System.out.println("CSV 대량 등록 성공");

			} else {

				System.out.println("CSV 대량 등록 실패");
			}

		} catch (Exception e) {

			System.out.println("CSV 등록 오류 : " + e.getMessage());

			e.printStackTrace();
		}
	}
}