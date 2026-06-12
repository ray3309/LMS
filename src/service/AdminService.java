package service;

import java.util.List;

import dao.MemberDao;
import dao.MemberDaoImpl;

import dao.OverdueDao;
import dao.OverdueDaoImpl;

import dao.RentalDao;
import dao.RentalDaoImpl;

import vo.OverdueVO;
import vo.RentalVO;

public class AdminService {

	private MemberDao memberDao;
	private OverdueDao overdueDao;
	private RentalDao rentalDao;

	public AdminService() {

		memberDao = new MemberDaoImpl();

		overdueDao = new OverdueDaoImpl();

		rentalDao = new RentalDaoImpl();
	}

	/*
	 * 전체 연체자 조회
	 */
	public List<OverdueVO> getOverdueList() {

		return overdueDao.getOverdueList();
	}

	/*
	 * 연체 등록 처리
	 */
	public boolean registerOverdue(int memberId) {

		/*
		 * 이미 연체중인지 확인
		 */
		boolean exists = overdueDao.isOverdueMember(memberId);

		if (exists) {

			return false;
		}

		OverdueVO vo = new OverdueVO();

		vo.setMemberId(memberId);

		int result = overdueDao.insertOverdue(vo);

		/*
		 * MEMBER 테이블 연체 상태 변경
		 */
		if (result > 0) {

			memberDao.updateOverdueState(memberId, "Y");
		}

		return result > 0;
	}

	/*
	 * 관리자 직접 연체 해제
	 */
	public boolean releaseOverdue(int overdueId, int memberId) {

		int result = overdueDao.releaseOverdue(overdueId);

		/*
		 * MEMBER 연체 상태 해제
		 */
		if (result > 0) {

			memberDao.updateOverdueState(memberId, "N");
		}

		return result > 0;
	}

	/*
	 * 자동 연체 해제 반납 후 14일 경과
	 */
	public void autoReleaseOverdue() {

		List<OverdueVO> list = overdueDao.findAutoReleaseTargets();

		for (OverdueVO vo : list) {

			overdueDao.autoReleaseOverdue(vo.getOverdueId());

			memberDao.updateOverdueState(vo.getMemberId(), "N");
		}
	}

	/*
	 * 연체 대상 검사
	 */
	public void checkOverdueMembers() {

		List<RentalVO> list = rentalDao.findOverdueRentals();

		for (RentalVO vo : list) {

			/*
			 * RENTAL 상태 변경
			 */
			rentalDao.updateRentalStatus(vo.getRentalId(), "O");

			/*
			 * OVERDUE 등록
			 */
			registerOverdue(vo.getMemberId());
		}
	}

	/*
	 * 현재 연체자 수
	 */
	public int getActiveOverdueCount() {

		return overdueDao.getActiveOverdueCount();
	}

	/*
	 * 회원 연체 여부 확인
	 */
	public boolean isOverdueMember(int memberId) {

		return overdueDao.isOverdueMember(memberId);
	}

	/*
	 * 회원 연체 기록 조회
	 */
	public List<OverdueVO> getMemberOverdueHistory(int memberId) {

		return overdueDao.getMemberOverdueHistory(memberId);
	}

	/*
	 * 전체 대출 목록 조회
	 */
	public List<RentalVO> getAllRentalList() {

		return rentalDao.getAllRentalList();
	}

	/*
	 * 연체 삭제
	 */
	public boolean deleteOverdue(int overdueId) {

		return overdueDao.deleteOverdue(overdueId) > 0;
	}
}