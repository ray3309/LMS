package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBUtil;
import vo.MemberVO;

public class MemberDaoImpl implements MemberDao {
	private int currSessionMemberId;
	
	@Override
	public boolean insertMember(MemberVO memberVO) {
		// memberId, userId, password, name, phone, currentRentCount, isOverdue, createAt
		String createId = "INSERT INTO MEMBER VALUES (member_seq.nextVal, ?, ?, ?, ?, 'N', sysdate)";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(createId);) {
			pstmt.setString(1, memberVO.getUserId());
			pstmt.setString(2, memberVO.getPassword());
			pstmt.setString(3, memberVO.getName());
			pstmt.setString(4, memberVO.getPhone());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("회원가입 완료");
		return true;
	}
	
	public boolean existUserId(String userId) {
		String findId = "SELECT userid FROM MEMBER WHERE userid=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(findId);) {
			pstmt.setString(1, userId);
			ResultSet res = pstmt.executeQuery();
			if (res.next()) {
				System.out.println("아이디가 이미 존재합니다.");
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false;
	} 
	@Override
	public int login(String userId, String password) {
		String loginReq = "SELECT MemberID FROM MEMBER WHERE UserID = ? AND UserPass = ?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(loginReq);) {
			pstmt.setString(1, userId);
			pstmt.setString(2, password);
			
			ResultSet rs = pstmt.executeQuery();	
			
			if (rs.next()) {
				System.out.println("로그인 성공");
				return currSessionMemberId = rs.getInt("memberid");
			}	
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("아이디가 올바르지 않거나, 패스워드가 맞지 않습니다." + e);
		}
		System.out.println("로그인 실패");
		return 0;
	}

	@Override
	public MemberVO findById(int memberId) {
		String searchId = "SELECT * FROM MEMBER WHERE memberid=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(searchId);) {
			pstmt.setInt(1, memberId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				MemberVO v = new MemberVO();
				v.setUserId(rs.getString("userid"));
				v.setPassword(rs.getString("userpass"));
				v.setName(rs.getString("username"));
				v.setPhone(rs.getString("userphone"));
				v.setMemberId(memberId);
				return v;
			}
			//System.out.println("조회 성공");
		}  catch (Exception e) {
			System.out.println("에러");
			//e.printStackTrace();
		}
		return null;
	}
	@Override
	public boolean updateMember(MemberVO memberVO) {
		// 회원정보 변경하려면 이미 로그인 상태여야 하니까 pwd 입력 값 체크 뒤 닉네임, 비밀번호, 전화번호 변경 기능 
		// -> 비밀번호만 확인하는 메서드 추가해야함
		String searchId = "UPDATE MEMBER SET username = ?, userpass = ?, userphone = ? where memberid = ?";
		try (Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(searchId);) {
			pstmt.setString(1, memberVO.getName());
			pstmt.setString(2, memberVO.getPassword());
			pstmt.setString(3, memberVO.getPhone());
			pstmt.setInt(4, currSessionMemberId);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	public boolean isValidPassword(String pwd) { // main에 먼저 실행
		String searchId = "SELECT userpass FROM MEMBER WHERE memberid=? and userpass=?";
		try (Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(searchId);) {
			pstmt.setInt(1, currSessionMemberId);
			pstmt.setString(2, pwd);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public int getMemberId(String userId) {
		String searchId = "SELECT memberid FROM MEMBER WHERE userid=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(searchId);) {
			pstmt.setString(1, userId);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			return rs.getInt("memberid");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int updateOverdueState(int memberId, String overdueState) {
		return 0;
	}
	@Override
	public int updateRentCount(int memberId, int rentCount) {
		return 0;
	}
	@Override
	public int getCurrentRentCount(int memberId) {
		return 0;
	}
}
