package dao;

import vo.MemberVO;

public interface MemberDao {
	public boolean insertMember(MemberVO memberVO);
	public int login(String userId, String password);
	public MemberVO findById(int memberId);
	public boolean updateMember(MemberVO memberVO);
	
	public boolean existUserId(String userId);
	public boolean isValidPassword(String pwd);
	public int getMemberId(String userId);
	
	public int updateOverdueState(int memberId, String overdueState);
	public int updateRentCount(int memberId, int rentCount);
	public int getCurrentRentCount(int memberId);
	
}
