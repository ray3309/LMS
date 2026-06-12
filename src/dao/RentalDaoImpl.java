package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import util.DBUtil;
import vo.RentalVO;

public class RentalDaoImpl implements RentalDao {

	/*
	 * 도서 대출
	 */
	@Override
	public int rentBook(RentalVO vo) {

		int result = 0;

		String sql = """
				INSERT INTO RENTAL(
				    RENTALID,
				    MEMBERID,
				    BOOKID,
				    RENTDATE,
				    RETURNDUEDATE,
				    RETURNDATE,
				    RENTALSTATUS
				)
				VALUES(
				    RENTAL_SEQ.NEXTVAL,
				    ?,
				    ?,
				    SYSDATE,
				    SYSDATE + 14,
				    SYSDATE + 14,
				    'R'
				)
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, vo.getMemberId());

			pstmt.setInt(2, vo.getBookId());

			result = pstmt.executeUpdate();

		} catch (Exception e) {

			throw new RuntimeException("도서 대출 실패", e);
		}

		return result;
	}

	/*
	 * 도서 반납
	 */
	@Override
	public int returnBook(int rentalId) {

		int result = 0;

		String sql = """
				UPDATE RENTAL
				SET
				    RETURNDATE = SYSDATE,
				    RENTALSTATUS = 'C'
				WHERE RENTALID = ?
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, rentalId);

			result = pstmt.executeUpdate();

		} catch (Exception e) {

			throw new RuntimeException("도서 반납 실패", e);
		}

		return result;
	}

	/*
	 * 회원 대출 목록 조회
	 */
	@Override
	public List<RentalVO> getRentalList(int memberId) {

		List<RentalVO> list = new ArrayList<>();

		String sql = """
				SELECT
				    R.RENTALID,
				    R.BOOKID,
				    B.TITLE,
				    R.RENTDATE,
				    R.RETURNDUEDATE,
				    R.RETURNDATE,
				    R.RENTALSTATUS
				FROM RENTAL R
				INNER JOIN BOOK B
				ON R.BOOKID = B.BOOKID
				WHERE R.MEMBERID = ?
				ORDER BY R.RENTDATE DESC
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, memberId);

			try (ResultSet rs = pstmt.executeQuery()) {

				while (rs.next()) {

					RentalVO vo = new RentalVO();

					vo.setRentalId(rs.getInt("RENTALID"));

					vo.setBookId(rs.getInt("BOOKID"));

					vo.setBookTitle(rs.getString("TITLE"));

					vo.setRentDate(rs.getString("RENTDATE"));

					vo.setReturnDueDate(rs.getString("RETURNDUEDATE"));

					vo.setReturnDate(rs.getString("RETURNDATE"));

					vo.setRentalStatus(rs.getString("RENTALSTATUS"));

					list.add(vo);
				}
			}

		} catch (Exception e) {

			throw new RuntimeException("대출 목록 조회 실패", e);
		}

		return list;
	}

	/*
	 * 전체 대출 목록
	 */
	@Override
	public List<RentalVO> getAllRentalList() {

		List<RentalVO> list = new ArrayList<>();

		String sql = """
				SELECT
				    R.RENTALID,
				    M.USERNAME,
				    B.TITLE,
				    R.RENTDATE,
				    R.RETURNDUEDATE,
				    R.RETURNDATE,
				    R.RENTALSTATUS
				FROM RENTAL R
				INNER JOIN MEMBER M
				ON R.MEMBERID = M.MEMBERID
				INNER JOIN BOOK B
				ON R.BOOKID = B.BOOKID
				ORDER BY R.RENTDATE DESC
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql);

				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {

				RentalVO vo = new RentalVO();

				vo.setRentalId(rs.getInt("RENTALID"));

				vo.setMemberName(rs.getString("USERNAME"));

				vo.setBookTitle(rs.getString("TITLE"));

				vo.setRentDate(rs.getString("RENTDATE"));

				vo.setReturnDueDate(rs.getString("RETURNDUEDATE"));

				vo.setReturnDate(rs.getString("RETURNDATE"));

				vo.setRentalStatus(rs.getString("RENTALSTATUS"));

				list.add(vo);
			}

		} catch (Exception e) {

			throw new RuntimeException("전체 대출 목록 조회 실패", e);
		}

		return list;
	}

	/*
	 * 현재 대출중 조회
	 */
	@Override
	public RentalVO findActiveRental(int memberId, int bookId) {

		RentalVO vo = null;

		String sql = """
				SELECT *
				FROM RENTAL
				WHERE MEMBERID = ?
				AND BOOKID = ?
				AND RENTALSTATUS = 'R'
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, memberId);
			pstmt.setInt(2, bookId);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {

					vo = new RentalVO();

					vo.setRentalId(rs.getInt("RENTALID"));

					vo.setMemberId(rs.getInt("MEMBERID"));

					vo.setBookId(rs.getInt("BOOKID"));
				}
			}

		} catch (Exception e) {

			throw new RuntimeException("대출 조회 실패", e);
		}

		return vo;
	}

	/*
	 * 대출 상태 변경
	 */
	@Override
	public int updateRentalStatus(int rentalId, String rentalStatus) {

		int result = 0;

		String sql = """
				UPDATE RENTAL
				SET RENTALSTATUS = ?
				WHERE RENTALID = ?
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, rentalStatus);

			pstmt.setInt(2, rentalId);

			result = pstmt.executeUpdate();

		} catch (Exception e) {

			throw new RuntimeException("대출 상태 변경 실패", e);
		}

		return result;
	}

	/*
	 * 연체 대상 조회
	 */
	@Override
	public List<RentalVO> findOverdueRentals() {

		List<RentalVO> list = new ArrayList<>();

		String sql = """
				SELECT *
				FROM RENTAL
				WHERE RENTALSTATUS = 'R'
				AND RETURNDUEDATE < SYSDATE
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql);

				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {

				RentalVO vo = new RentalVO();

				vo.setRentalId(rs.getInt("RENTALID"));

				vo.setMemberId(rs.getInt("MEMBERID"));

				vo.setBookId(rs.getInt("BOOKID"));

				list.add(vo);
			}

		} catch (Exception e) {

			throw new RuntimeException("연체 대상 조회 실패", e);
		}

		return list;
	}

	/*
	 * 현재 대출 권수
	 */
	@Override
	public int getCurrentRentalCount(int memberId) {

		String sql = """
				SELECT COUNT(*)
				FROM RENTAL
				WHERE MEMBERID = ?
				AND RENTALSTATUS = 'R'
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, memberId);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {

					return rs.getInt(1);
				}
			}

		} catch (Exception e) {

			throw new RuntimeException("대출 권수 조회 실패", e);
		}

		return 0;
	}

	/*
	 * 대출중 도서 목록
	 */
	@Override
	public List<RentalVO> getCurrentRentalBooks(int memberId) {

		List<RentalVO> list = new ArrayList<>();

		String sql = """
				SELECT
				    R.RENTALID,
				    R.MEMBERID,
				    R.BOOKID,
				    B.TITLE,
				    R.RENTDATE,
				    R.RETURNDUEDATE,
				    R.RENTALSTATUS
				FROM RENTAL R
				INNER JOIN BOOK B
				ON R.BOOKID = B.BOOKID
				WHERE R.MEMBERID = ?
				AND R.RENTALSTATUS = 'R'
				ORDER BY R.RENTDATE DESC
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, memberId);

			try (ResultSet rs = pstmt.executeQuery()) {

				while (rs.next()) {

					RentalVO vo = new RentalVO();

					vo.setRentalId(rs.getInt("RENTALID"));

					vo.setMemberId(rs.getInt("MEMBERID"));

					vo.setBookId(rs.getInt("BOOKID"));

					vo.setBookTitle(rs.getString("TITLE"));

					vo.setRentDate(rs.getString("RENTDATE"));

					vo.setReturnDueDate(rs.getString("RETURNDUEDATE"));

					vo.setRentalStatus(rs.getString("RENTALSTATUS"));

					list.add(vo);
				}
			}

		} catch (Exception e) {

			throw new RuntimeException("현재 대출중 도서 조회 실패", e);
		}

		return list;
	}

	/*
	 * 도서 대출 여부
	 */
	@Override
	public boolean isBookRented(int bookId) {

		String sql = """
				SELECT COUNT(*)
				FROM RENTAL
				WHERE BOOKID = ?
				AND RENTALSTATUS = 'R'
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, bookId);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {

					return rs.getInt(1) > 0;
				}
			}

		} catch (Exception e) {

			throw new RuntimeException("도서 대출 여부 조회 실패", e);
		}

		return false;
	}

	/*
	 * 반납 예정일 조회
	 */
	@Override
	public String getReturnDueDate(int bookId) {

		String sql = """
				SELECT TO_CHAR(
				    RETURNDUEDATE,
				    'YYYY-MM-DD'
				)
				FROM RENTAL
				WHERE BOOKID = ?
				AND RENTALSTATUS = 'R'
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, bookId);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {

					return rs.getString(1);
				}
			}

		} catch (Exception e) {

			throw new RuntimeException("반납 예정일 조회 실패", e);
		}

		return null;
	}

	/*
	 * 대출 ID 조회
	 */
	@Override
	public RentalVO findRentalById(int rentalId) {

		RentalVO vo = null;

		String sql = """
				SELECT *
				FROM RENTAL
				WHERE RENTALID = ?
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, rentalId);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {

					vo = new RentalVO();

					vo.setRentalId(rs.getInt("RENTALID"));

					vo.setMemberId(rs.getInt("MEMBERID"));

					vo.setBookId(rs.getInt("BOOKID"));

					vo.setRentalStatus(rs.getString("RENTALSTATUS"));
				}
			}

		} catch (Exception e) {

			throw new RuntimeException("대출 조회 실패", e);
		}

		return vo;
	}

	/*
	 * 전체 대출 이력
	 */
	@Override
	public List<RentalVO> getRentalHistory(int memberId) {

		return getRentalList(memberId);
	}

	/*
	 * 대출 삭제
	 */
	@Override
	public int deleteRental(int rentalId) {

		int result = 0;

		String sql = """
				DELETE FROM RENTAL
				WHERE RENTALID = ?
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, rentalId);

			result = pstmt.executeUpdate();

		} catch (Exception e) {

			throw new RuntimeException("대출 삭제 실패", e);
		}

		return result;
	}


}