package dao;

import java.util.List;

import vo.RentalVO;

public interface RentalDao {

    /*
     * 도서 대출
     */
    int rentBook(
        RentalVO vo
    );

    /*
     * 도서 반납
     */
    int returnBook(
        int rentalId
    );

    /*
     * 회원 대출 목록 조회
     */
    List<RentalVO> getRentalList(
        int memberId
    );

    /*
     * 전체 대출 목록 조회
     */
    List<RentalVO> getAllRentalList();

    /*
     * 현재 대출중 조회
     */
    RentalVO findActiveRental(
        int memberId,
        int bookId
    );

    /*
     * 대출 상태 변경
     */
    int updateRentalStatus(
        int rentalId,
        String rentalStatus
    );

    /*
     * 연체 대상 조회
     */
    List<RentalVO> findOverdueRentals();

    /*
     * 회원 현재 대출 권수
     */
    int getCurrentRentalCount(
        int memberId
    );

    /*
     * 특정 회원의 대출중 목록
     */
    List<RentalVO> getCurrentRentalBooks(
    		int memberId
    	);

    /*
     * 특정 도서 대출 여부 확인
     */
    boolean isBookRented(
        int bookId
    );

    /*
     * 반납 예정일 조회
     */
    String getReturnDueDate(
        int bookId
    );

    /*
     * 대출 ID 조회
     */
    RentalVO findRentalById(
        int rentalId
    );

    /*
     * 회원 전체 대출 이력
     */
    List<RentalVO> getRentalHistory(
        int memberId
    );

    /*
     * 대출 삭제
     */
    int deleteRental(
        int rentalId
    );
}