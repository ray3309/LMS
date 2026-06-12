package dao;

import java.util.List;

import vo.BookVO;

public interface BookDao {

    /*
     * 단일 도서 등록
     */
    int insertBook(
        BookVO vo
    );

    /*
     * 도서 대량 등록
     * Batch Insert
     */
    int batchInsertBooks(
        List<BookVO> bookList
    );
    
    /*
     * Oracle Text 기반 도서 검색
     * Pagination 적용
     */
    List<BookVO> searchBook(
        String keyword,
        int startRow,
        int endRow
    );

    /*
     * 전체 도서 조회
     */
    List<BookVO> getAllBooks(
        int startRow,
        int endRow
    );

    /*
     * BOOKID 조회
     */
    BookVO findBookById(
        int bookId
    );

    /*
     * REGNO 조회
     */
    BookVO findBookByRegNo(
        String regNo
    );

    /*
     * ISBN 조회
     */
    BookVO findBookByIsbn(
        String isbn
    );

    /*
     * 도서 정보 수정
     */
    int updateBook(
        BookVO vo
    );

    /*
     * 도서 삭제
     */
    int deleteBook(
        int bookId
    );

    /*
     * 도서 대출 상태 변경
     * Y : 대출중
     * N : 대출가능
     */
    int updateRentStatus(
        int bookId,
        String rentStatus
    );

    /*
     * 전체 도서 수
     */
    int getTotalBookCount();

    /*
     * 대출 가능 도서 수
     */
    int getAvailableBookCount();

    /*
     * 대출중 도서 수
     */
    int getRentedBookCount();

    /*
     * KDC 분류 조회
     */
    List<BookVO> getBooksByKdc(
        String kdc
    );

    /*
     * DDC 분류 조회
     */
    List<BookVO> getBooksByDdc(
        String ddc
    );

    /*
     * 최신 등록 도서 조회
     */
    List<BookVO> getRecentBooks();
}