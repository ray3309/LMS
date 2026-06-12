package service;

import dao.MemberDao;
import dao.MemberDaoImpl;
import vo.MemberVO;

public class MemberService {
	private MemberDao memberDao;
	private MemberVO myVO;
	//private RentalDao rentalDao;
	public MemberService() {
		memberDao = new MemberDaoImpl();
	}
	public int join(MemberVO vo) {
		memberDao.insertMember(vo);
		return memberDao.getMemberId(vo.getUserId());
	}
	public MemberVO login(String userId, String password) {
		myVO = memberDao.findById(memberDao.login(userId, password));
		return myVO;
	}
	public MemberVO viewMyInfo(int memberId) {
		return memberDao.findById(myVO.getMemberId());
	}
	public boolean updateMyInfo(MemberVO memberVO) {
		return memberDao.updateMember(memberVO);
	}
	public boolean existUserId(String userId) {
		return memberDao.existUserId(userId);
	}
	public boolean isValidPassword(String pwd) {
		return memberDao.isValidPassword(pwd);
	}
	public int getMemberId(String userId) {
		return getMemberId(userId);
	}
}
