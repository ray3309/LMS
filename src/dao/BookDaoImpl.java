package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBUtil;
import util.StringUtil;
import vo.BookVO;

public class BookDaoImpl implements BookDao {
	static String syncSql = """
	    BEGIN
	        CTXSYS.CTX_DDL.SYNC_INDEX(
	            'DTEAM.IDX_BOOK_TITLE'
	        );
	    END;
	    """;
	
	@Override
	public int insertBook(BookVO vo) {

		int result = 0;

		String sql = """
				INSERT INTO BOOK(
				    BOOKID,
				    REGNO,
				    TITLE,
				    AUTHOR,
				    PUBLISHER,
				    PUBLISHDATE,
				    ISBN,
				    KDC,
				    DDC,
				    RENTSTATUS,
				    REGDATE
				)
				VALUES(
				    BOOK_SEQ.NEXTVAL,
				    ?,
				    ?,
				    ?,
				    ?,
				    ?,
				    ?,
				    ?,
				    ?,
				    'N',
				    SYSDATE
				)
				""";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				PreparedStatement syncPstmt = conn.prepareStatement(syncSql)) {

			pstmt.setString(1, vo.getRegNo());
			pstmt.setString(2, vo.getTitle());
			pstmt.setString(3, vo.getAuthor());
			pstmt.setString(4, vo.getPublisher());
			pstmt.setString(5, vo.getPublishDate());
			pstmt.setString(6, vo.getIsbn());
			pstmt.setString(7, vo.getKdc());
			pstmt.setString(8, vo.getDdc());

			result = pstmt.executeUpdate();

			/*
			 * Oracle Text Index 동기화
			 */
			syncPstmt.execute();

			conn.commit();
			
		} catch (Exception e) {

			throw new RuntimeException("도서 등록 실패", e);
		}

		return result;
	}

	@Override
	public int batchInsertBooks(List<BookVO> list) {
		int totalCount = 0;
		String sql = "INSERT INTO BOOK VALUES (BOOK_SEQ.NEXTVAL, TO_CHAR(BOOK_SEQ.CURRVAL) || '_' || ?, ?, NVL(?, 'None'), ?, TO_DATE(?, 'YYYY-MM-DD'), NVL(?, '00000000000'), ?, ?, 'N', SYSDATE)";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				PreparedStatement syncPstmt = conn.prepareStatement(syncSql)) {
			
			conn.setAutoCommit(false);
			int batchCount = 0;

			for (BookVO vo : list) {
				if (vo.getKdc().isEmpty() == true)
					vo.setRegNo(vo.getDdc());
				else
					vo.setRegNo(vo.getKdc());
				pstmt.setString(1, vo.getRegNo());
				pstmt.setString(2, vo.getTitle());
				if (vo.getAuthor().length() > 512) {
					vo.setAuthor(StringUtil.cutString(vo.getAuthor(), 512));
					String tempAuthor = vo.getAuthor();
					String tempAuthor2 = StringUtil.cutString(tempAuthor, 512);					
					vo.setAuthor(tempAuthor2);
					pstmt.setString(3, vo.getAuthor());
				} else {
					pstmt.setString(3, vo.getAuthor());
				}
				pstmt.setString(4, vo.getPublisher());
				String PubDate = vo.getPublishDate();
				if (PubDate != null && PubDate.length() == 8) {
					PubDate = PubDate.substring(0, 4) + "-" + PubDate.substring(4, 6) + "-" + PubDate.substring(6, 8);					
				}
				vo.setPublishDate(PubDate);
				pstmt.setString(5, vo.getPublishDate());
				pstmt.setString(6, vo.getIsbn());

				if (vo.getKdc() == null || vo.getKdc().isEmpty()) {
					pstmt.setString(7, "0");
				} else {
					pstmt.setString(7, StringUtil.cutString(vo.getKdc(), 20));
				}
				if (vo.getDdc() == null || vo.getDdc().isEmpty()) {
					pstmt.setString(8, "0");
				} else {
					pstmt.setString(8, StringUtil.cutString(vo.getDdc(), 20));
				}
				pstmt.addBatch();
				batchCount++;
				if (batchCount % 1000 == 0) {
					pstmt.executeBatch();
					conn.commit();
					totalCount += 1000;
					System.out.println(totalCount + "건 등록 완료");
				}
			}
			
			/*
			 * 1000건 단위로 나누어떨어지지 않은 나머지 처리
			 */
			if (batchCount % 1000 != 0) {

			    pstmt.executeBatch();

			    conn.commit();

			    totalCount += batchCount % 1000;

			    System.out.println(
			        totalCount + "건 등록 완료"
			    );
			}

			/*
			 * Oracle Text Index 동기화
			 */
			syncPstmt.execute();

			conn.commit();

			System.out.println("Oracle Text Index 동기화 완료");
		} catch (Exception e) {
			throw new RuntimeException("배치 등록 실패", e);
		}
		return totalCount;
	}

	@Override
	public List<BookVO> searchBook(String keyword, int page, int pageSize) {

		List<BookVO> list = new ArrayList<>();

		int offset = (page - 1) * pageSize;

		String sql = """
				SELECT
				    B.BOOKID,
				    B.REGNO,
				    B.TITLE,
				    B.AUTHOR,
				    B.PUBLISHER,
				    B.PUBLISHDATE,
				    B.ISBN,
				    B.KDC,
				    B.DDC,
				    B.RENTSTATUS,
				    R.RETURNDUEDATE
				FROM BOOK B
				LEFT JOIN RENTAL R
				ON B.BOOKID = R.BOOKID
				AND R.RENTALSTATUS = 'R'
				WHERE CONTAINS(
				    B.TITLE,
				    ?
				) > 0
				ORDER BY B.BOOKID
				OFFSET ? ROWS
				FETCH NEXT ? ROWS ONLY""";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, keyword);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, pageSize);

			pstmt.setFetchSize(1000);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				
				while (rs.next()) {

					BookVO vo = new BookVO();

					vo.setBookId(rs.getInt("BOOKID"));

					vo.setRegNo(rs.getString("REGNO"));

					vo.setTitle(rs.getString("TITLE"));

					vo.setAuthor(rs.getString("AUTHOR"));

					vo.setPublisher(rs.getString("PUBLISHER"));

					vo.setPublishDate(rs.getString("PUBLISHDATE"));

					vo.setIsbn(rs.getString("ISBN"));
					vo.setKdc(rs.getString("KDC"));
					vo.setDdc(rs.getString("DDC"));

					vo.setRentStatus(rs.getString("RENTSTATUS"));

					vo.setReturnDueDate(rs.getString("RETURNDUEDATE"));

					list.add(vo);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {

			throw new RuntimeException("도서 검색 실패", e);
		}

		return list;
	}

	@Override
	public BookVO findBookByRegNo(String regNo) {
		return null;
	}

	/*
	 * BOOKID 조회
	 */
	@Override
	public BookVO findBookById(int bookId) {

		BookVO vo = null;

		String sql = """
				SELECT *
				FROM BOOK
				WHERE BOOKID = ?
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, bookId);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {

					vo = new BookVO();

					vo.setBookId(rs.getInt("BOOKID"));

					vo.setTitle(rs.getString("TITLE"));

					vo.setRentStatus(rs.getString("RENTSTATUS"));
				}
			}

		} catch (Exception e) {

			throw new RuntimeException("BOOKID 조회 실패", e);
		}

		return vo;
	}

	/*
	 * ISBN 조회
	 */
	@Override
	public BookVO findBookByIsbn(String isbn) {

		return null;
	}

	/*
	 * 도서 수정
	 */
	@Override
	public int updateBook(BookVO vo) {

		return 0;
	}

	/*
	 * 도서 삭제
	 */
	@Override
	public int deleteBook(int bookId) {

		int result = 0;

		String sql = """
				DELETE FROM BOOK
				WHERE BOOKID = ?
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, bookId);

			result = pstmt.executeUpdate();

		} catch (Exception e) {

			throw new RuntimeException("도서 삭제 실패", e);
		}

		return result;
	}

	/*
	 * 대출 상태 변경
	 */
	@Override
	public int updateRentStatus(int bookId, String rentStatus) {

		int result = 0;

		String sql = """
				UPDATE BOOK
				SET RENTSTATUS = ?
				WHERE BOOKID = ?
				""";

		try (Connection conn = DBUtil.getConnection();

				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, rentStatus);
			pstmt.setInt(2, bookId);

			result = pstmt.executeUpdate();

		} catch (Exception e) {

			throw new RuntimeException("대출 상태 변경 실패", e);
		}

		return result;
	}

	@Override
	public int getTotalBookCount() {

		return 0;
	}

	@Override
	public int getAvailableBookCount() {

		return 0;
	}

	@Override
	public int getRentedBookCount() {

		return 0;
	}

	@Override
	public List<BookVO> getBooksByKdc(String kdc) {

		return new ArrayList<>();
	}

	@Override
	public List<BookVO> getBooksByDdc(String ddc) {

		return new ArrayList<>();
	}

	@Override
	public List<BookVO> getRecentBooks() {

		return new ArrayList<>();
	}

	@Override
	public List<BookVO> getAllBooks(int startRow, int endRow) {
		// TODO Auto-generated method stub
		return null;
	}
}