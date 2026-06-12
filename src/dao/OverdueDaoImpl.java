package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import util.DBUtil;
import vo.OverdueVO;

public class OverdueDaoImpl
implements OverdueDao {

    /*
     * 연체 등록
     */
    @Override
    public int insertOverdue(
        OverdueVO vo
    ) {

        int result = 0;

        String sql = """
        INSERT INTO OVERDUE(
            OVERDUEID,
            MEMBERID,
            OVERDUESTARTDATE,
            OVERDUERELEASEDATE,
            OVERDUESTATUS
        )
        VALUES(
            SEQ_OVERDUE.NEXTVAL,
            ?,
            SYSDATE,
            SYSDATE + 14,
            'O'
        )
        """;

        try(
            Connection conn =
                DBUtil.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(sql)
        ){

            pstmt.setInt(
                1,
                vo.getMemberId()
            );

            result =
                pstmt.executeUpdate();

        } catch(Exception e){

            throw new RuntimeException(
                "연체 등록 실패",
                e
            );
        }

        return result;
    }

    /*
     * 관리자 직접 연체 해제
     * C : 관리자 해제
     */
    @Override
    public int releaseOverdue(
        int overdueId
    ) {

        int result = 0;

        String sql = """
        UPDATE OVERDUE
        SET OVERDUESTATUS = 'C'
        WHERE OVERDUEID = ?
        """;

        try(
            Connection conn =
                DBUtil.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(sql)
        ){

            pstmt.setInt(
                1,
                overdueId
            );

            result =
                pstmt.executeUpdate();

        } catch(Exception e){

            throw new RuntimeException(
                "관리자 연체 해제 실패",
                e
            );
        }

        return result;
    }

    /*
     * 자동 연체 해제
     * R : 자동 해제
     */
    @Override
    public int autoReleaseOverdue(
        int overdueId
    ) {

        int result = 0;

        String sql = """
        UPDATE OVERDUE
        SET OVERDUESTATUS = 'R'
        WHERE OVERDUEID = ?
        """;

        try(
            Connection conn =
                DBUtil.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(sql)
        ){

            pstmt.setInt(
                1,
                overdueId
            );

            result =
                pstmt.executeUpdate();

        } catch(Exception e){

            throw new RuntimeException(
                "자동 연체 해제 실패",
                e
            );
        }

        return result;
    }

    /*
     * 전체 연체자 목록
     */
    @Override
    public List<OverdueVO> getOverdueList() {

        List<OverdueVO> list =
            new ArrayList<>();

        String sql = """
        SELECT
            O.OVERDUEID,
            O.MEMBERID,
            M.USERNAME,
            M.USERID,
            O.OVERDUESTARTDATE,
            O.OVERDUERELEASEDATE,
            O.OVERDUESTATUS
        FROM OVERDUE O
        INNER JOIN MEMBER M
        ON O.MEMBERID = M.MEMBERID
        WHERE O.OVERDUESTATUS = 'O'
        ORDER BY O.OVERDUESTARTDATE DESC
        """;

        try(
            Connection conn =
                DBUtil.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(sql);

            ResultSet rs =
                pstmt.executeQuery()
        ){

            while(rs.next()){

                OverdueVO vo =
                    new OverdueVO();

                vo.setOverdueId(
                    rs.getInt(
                        "OVERDUEID"
                    )
                );

                vo.setMemberId(
                    rs.getInt(
                        "MEMBERID"
                    )
                );

                vo.setMemberName(
                    rs.getString(
                        "USERNAME"
                    )
                );

                vo.setUserId(
                    rs.getString(
                        "USERID"
                    )
                );

                vo.setOverdueStartDate(
                    rs.getString(
                        "OVERDUESTARTDATE"
                    )
                );

                vo.setOverdueReleaseDate(
                    rs.getString(
                        "OVERDUERELEASEDATE"
                    )
                );

                vo.setOverdueStatus(
                    rs.getString(
                        "OVERDUESTATUS"
                    )
                );

                list.add(vo);
            }

        } catch(Exception e){

            throw new RuntimeException(
                "연체 목록 조회 실패",
                e
            );
        }

        return list;
    }

    /*
     * 특정 회원 연체 조회
     */
    @Override
    public OverdueVO findOverdueMember(
        int memberId
    ) {

        OverdueVO vo = null;

        String sql = """
        SELECT *
        FROM OVERDUE
        WHERE MEMBERID = ?
        AND OVERDUESTATUS = 'O'
        """;

        try(
            Connection conn =
                DBUtil.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(sql)
        ){

            pstmt.setInt(
                1,
                memberId
            );

            try(
                ResultSet rs =
                    pstmt.executeQuery()
            ){

                if(rs.next()){

                    vo =
                        new OverdueVO();

                    vo.setOverdueId(
                        rs.getInt(
                            "OVERDUEID"
                        )
                    );

                    vo.setMemberId(
                        rs.getInt(
                            "MEMBERID"
                        )
                    );

                    vo.setOverdueStartDate(
                        rs.getString(
                            "OVERDUESTARTDATE"
                        )
                    );

                    vo.setOverdueReleaseDate(
                        rs.getString(
                            "OVERDUERELEASEDATE"
                        )
                    );

                    vo.setOverdueStatus(
                        rs.getString(
                            "OVERDUESTATUS"
                        )
                    );
                }
            }

        } catch(Exception e){

            throw new RuntimeException(
                "연체 회원 조회 실패",
                e
            );
        }

        return vo;
    }

    /*
     * 연체 상태 변경
     */
    @Override
    public int updateOverdueStatus(
        int overdueId,
        String status
    ) {

        int result = 0;

        String sql = """
        UPDATE OVERDUE
        SET OVERDUESTATUS = ?
        WHERE OVERDUEID = ?
        """;

        try(
            Connection conn =
                DBUtil.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(sql)
        ){

            pstmt.setString(
                1,
                status
            );

            pstmt.setInt(
                2,
                overdueId
            );

            result =
                pstmt.executeUpdate();

        } catch(Exception e){

            throw new RuntimeException(
                "연체 상태 변경 실패",
                e
            );
        }

        return result;
    }

    /*
     * 자동 해제 대상 조회
     * 반납 후 14일 지난 사용자
     */
    @Override
    public List<OverdueVO> findAutoReleaseTargets() {

        List<OverdueVO> list =
            new ArrayList<>();

        String sql = """
        SELECT *
        FROM OVERDUE
        WHERE OVERDUESTATUS = 'O'
        AND OVERDUERELEASEDATE <= SYSDATE
        """;

        try(
            Connection conn =
                DBUtil.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(sql);

            ResultSet rs =
                pstmt.executeQuery()
        ){

            while(rs.next()){

                OverdueVO vo =
                    new OverdueVO();

                vo.setOverdueId(
                    rs.getInt(
                        "OVERDUEID"
                    )
                );

                vo.setMemberId(
                    rs.getInt(
                        "MEMBERID"
                    )
                );

                vo.setOverdueReleaseDate(
                    rs.getString(
                        "OVERDUERELEASEDATE"
                    )
                );

                list.add(vo);
            }

        } catch(Exception e){

            throw new RuntimeException(
                "자동 해제 대상 조회 실패",
                e
            );
        }

        return list;
    }

    /*
     * 연체 회원 여부 확인
     */
    @Override
    public boolean isOverdueMember(
        int memberId
    ) {

        String sql = """
        SELECT COUNT(*)
        FROM OVERDUE
        WHERE MEMBERID = ?
        AND OVERDUESTATUS = 'O'
        """;

        try(
            Connection conn =
                DBUtil.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(sql)
        ){

            pstmt.setInt(
                1,
                memberId
            );

            try(
                ResultSet rs =
                    pstmt.executeQuery()
            ){

                if(rs.next()){

                    return rs.getInt(1) > 0;
                }
            }

        } catch(Exception e){

            throw new RuntimeException(
                "연체 여부 확인 실패",
                e
            );
        }

        return false;
    }

    /*
     * 회원 연체 기록 조회
     */
    @Override
    public List<OverdueVO> getMemberOverdueHistory(
        int memberId
    ) {

        List<OverdueVO> list =
            new ArrayList<>();

        String sql = """
        SELECT *
        FROM OVERDUE
        WHERE MEMBERID = ?
        ORDER BY OVERDUESTARTDATE DESC
        """;

        try(
            Connection conn =
                DBUtil.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(sql)
        ){

            pstmt.setInt(
                1,
                memberId
            );

            try(
                ResultSet rs =
                    pstmt.executeQuery()
            ){

                while(rs.next()){

                    OverdueVO vo =
                        new OverdueVO();

                    vo.setOverdueId(
                        rs.getInt(
                            "OVERDUEID"
                        )
                    );

                    vo.setMemberId(
                        rs.getInt(
                            "MEMBERID"
                        )
                    );

                    vo.setOverdueStartDate(
                        rs.getString(
                            "OVERDUESTARTDATE"
                        )
                    );

                    vo.setOverdueReleaseDate(
                        rs.getString(
                            "OVERDUERELEASEDATE"
                        )
                    );

                    vo.setOverdueStatus(
                        rs.getString(
                            "OVERDUESTATUS"
                        )
                    );

                    list.add(vo);
                }
            }

        } catch(Exception e){

            throw new RuntimeException(
                "연체 기록 조회 실패",
                e
            );
        }

        return list;
    }

    /*
     * 현재 활성 연체 수
     */
    @Override
    public int getActiveOverdueCount() {

        String sql = """
        SELECT COUNT(*)
        FROM OVERDUE
        WHERE OVERDUESTATUS = 'O'
        """;

        try(
            Connection conn =
                DBUtil.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(sql);

            ResultSet rs =
                pstmt.executeQuery()
        ){

            if(rs.next()){

                return rs.getInt(1);
            }

        } catch(Exception e){

            throw new RuntimeException(
                "활성 연체 수 조회 실패",
                e
            );
        }

        return 0;
    }

    /*
     * 연체 삭제
     */
    @Override
    public int deleteOverdue(
        int overdueId
    ) {

        int result = 0;

        String sql = """
        DELETE FROM OVERDUE
        WHERE OVERDUEID = ?
        """;

        try(
            Connection conn =
                DBUtil.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(sql)
        ){

            pstmt.setInt(
                1,
                overdueId
            );

            result =
                pstmt.executeUpdate();

        } catch(Exception e){

            throw new RuntimeException(
                "연체 삭제 실패",
                e
            );
        }

        return result;
    }
}