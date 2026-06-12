package service;

import java.util.List;

import util.CSVUtil;

import dao.BookDao;
import dao.BookDaoImpl;

import dao.RentalDao;
import dao.RentalDaoImpl;

import dao.OverdueDao;
import dao.OverdueDaoImpl;

import vo.BookVO;
import vo.RentalVO;

public class BookService {

	private BookDao bookDao;

	private RentalDao rentalDao;

	private OverdueDao overdueDao;

	public BookService() {

		bookDao = new BookDaoImpl();

		rentalDao = new RentalDaoImpl();

		overdueDao = new OverdueDaoImpl();
	}

	/*
	 * 단일 도서 등록
	 */
	public int insertBook(BookVO vo) {

		return bookDao.insertBook(vo);
	}

	/*
	 * 대량 도서 등록
	 */
	public int batchInsertBooks(List<BookVO> bookList) {

		return bookDao.batchInsertBooks(bookList);
	}

	/*
	 * Oracle Text 검색
	 */
	public List<BookVO> searchBook(String keyword, int startRow, int endRow) {

		return bookDao.searchBook(keyword, startRow, endRow);
	}

	/*
	 * 전체 도서 조회
	 */
	public List<BookVO> getAllBooks(int startRow, int endRow) {

		return bookDao.getAllBooks(startRow, endRow);
	}

	/*
	 * BOOKID 조회
	 */
	public BookVO findBookById(int bookId) {

		return bookDao.findBookById(bookId);
	}

	/*
	 * REGNO 조회
	 */
	public BookVO findBookByRegNo(String regNo) {

		return bookDao.findBookByRegNo(regNo);
	}

	/*
	 * ISBN 조회
	 */
	public BookVO findBookByIsbn(String isbn) {

		return bookDao.findBookByIsbn(isbn);
	}

	/*
	 * 도서 수정
	 */
	public boolean updateBook(BookVO vo) {

		return bookDao.updateBook(vo) > 0;
	}

	/*
	 * 도서 삭제
	 */
	public boolean deleteBook(int bookId) {

		return bookDao.deleteBook(bookId) > 0;
	}

	/*
	 * 도서 대출
	 */
	public boolean rentBook(int memberId, int bookId) {

		/*
		 * 연체 회원 검사
		 */
		boolean overdue = overdueDao.isOverdueMember(memberId);

		if (overdue) {

			System.out.println("연체 회원은 대출 불가");

			return false;
		}

		/*
		 * 최대 3권 제한
		 */
		int rentCount = rentalDao.getCurrentRentalCount(memberId);

		if (rentCount >= 3) {

			System.out.println("최대 대출 권수 초과");

			return false;
		}

		/*
		 * 도서 존재 확인
		 */
		BookVO book = bookDao.findBookById(bookId);

		if (book == null) {

			System.out.println("존재하지 않는 도서");

			return false;
		}

		/*
		 * 대출 가능 여부 확인
		 */
		if ("Y".equals(book.getRentStatus())) {

			System.out.println("이미 대출중인 도서");

			return false;
		}

		/*
		 * RENTAL 등록
		 */
		RentalVO rentalVO = new RentalVO();

		rentalVO.setMemberId(memberId);

		rentalVO.setBookId(bookId);

		int rentalResult = rentalDao.rentBook(rentalVO);

		if (rentalResult <= 0) {

			return false;
		}

		/*
		 * BOOK 대출상태 변경
		 */
		int bookResult = bookDao.updateRentStatus(bookId, "Y");

		return bookResult > 0;
	}

	public List<RentalVO> getCurrentRentalBooks(
		    int memberId
		) {

		    return rentalDao
		        .getCurrentRentalBooks(
		            memberId
		        );
		}
	
	/*
	 * 도서 반납
	 */
	public boolean returnBook(int rentalId) {

		/*
		 * RENTAL 조회
		 */
		RentalVO rentalVO = rentalDao.findRentalById(rentalId);

		if (rentalVO == null) {

			System.out.println("존재하지 않는 대출정보");

			return false;
		}

		/*
		 * 반납 처리
		 */
		int rentalResult = rentalDao.returnBook(rentalId);

		if (rentalResult <= 0) {

			return false;
		}

		/*
		 * BOOK 상태 변경
		 */
		int bookResult = bookDao.updateRentStatus(rentalVO.getBookId(), "N");

		return bookResult > 0;
	}

	/*
	 * 도서 대출 여부 확인
	 */
	public boolean isRentAvailable(int bookId) {

		BookVO vo = bookDao.findBookById(bookId);

		if (vo == null) {

			return false;
		}

		return "N".equals(vo.getRentStatus());
	}

	/*
	 * 전체 도서 수
	 */
	public int getTotalBookCount() {

		return bookDao.getTotalBookCount();
	}

	/*
	 * 대출 가능 도서 수
	 */
	public int getAvailableBookCount() {

		return bookDao.getAvailableBookCount();
	}

	/*
	 * 대출중 도서 수
	 */
	public int getRentedBookCount() {

		return bookDao.getRentedBookCount();
	}

	/*
	 * KDC별 조회
	 */
	public List<BookVO> getBooksByKdc(String kdc) {

		return bookDao.getBooksByKdc(kdc);
	}

	/*
	 * DDC별 조회
	 */
	public List<BookVO> getBooksByDdc(String ddc) {

		return bookDao.getBooksByDdc(ddc);
	}

	/*
	 * 최신 등록 도서
	 */
	public List<BookVO> getRecentBooks() {

		return bookDao.getRecentBooks();
	}

	/*
	 * 반납 예정일 조회
	 */
	public String getReturnDueDate(int bookId) {

		return rentalDao.getReturnDueDate(bookId);
	}

	/*
	 * CSV 다운로드 후 대량 등록
	 */
	public boolean importCsvBooks() {
		/*
		 * CSV 읽기
		 */
		List<BookVO> list = CSVUtil.readCSV();

		if (list == null || list.isEmpty()) {

			System.out.println("CSV 데이터 없음");

			return false;
		}

		/*
		 * Batch Insert
		 */
		int result = bookDao.batchInsertBooks(list);

		System.out.println("등록 건수 : " + result);

		return result > 0;
	}
}