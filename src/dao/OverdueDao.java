package dao;

import java.util.List;

import vo.OverdueVO;

public interface OverdueDao {

    /*
     * 연체 등록
     */
    int insertOverdue(
        OverdueVO vo
    );

    /*
     * 관리자 직접 연체 해제
     * 상태값 : C
     */
    int releaseOverdue(
        int overdueId
    );

    /*
     * 자동 연체 해제
     * 상태값 : R
     */
    int autoReleaseOverdue(
        int overdueId
    );

    /*
     * 전체 연체자 조회
     */
    List<OverdueVO> getOverdueList();

    /*
     * 특정 회원 연체 조회
     */
    OverdueVO findOverdueMember(
        int memberId
    );

    /*
     * 연체 상태 변경
     */
    int updateOverdueStatus(
        int overdueId,
        String status
    );

    /*
     * 자동 해제 대상 조회
     * 반납 후 14일 경과 회원
     */
    List<OverdueVO> findAutoReleaseTargets();

    /*
     * 연체 여부 확인
     */
    boolean isOverdueMember(
        int memberId
    );

    /*
     * 회원 연체 기록 전체 조회
     */
    List<OverdueVO> getMemberOverdueHistory(
        int memberId
    );

    /*
     * 현재 활성 연체 수 조회
     */
    int getActiveOverdueCount();

    /*
     * 연체 삭제
     */
    int deleteOverdue(
        int overdueId
    );
}