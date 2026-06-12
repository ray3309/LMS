package vo;

public class OverdueVO {

    /*
     * OVERDUE.OVERDUEID
     */
    private int overdueId;

    /*
     * OVERDUE.MEMBERID
     */
    private int memberId;

    /*
     * OVERDUE.OVERDUESTARTDATE
     */
    private String overdueStartDate;

    /*
     * OVERDUE.OVERDUERELEASEDATE
     * 반납 후 14일
     */
    private String overdueReleaseDate;

    /*
     * OVERDUE.OVERDUESTATUS
     * O : 연체중
     * R : 해제됨
     * C : 관리자해제
     */
    private String overdueStatus;

    /*
     * JOIN 조회용
     */
    private String memberName;

    /*
     * JOIN 조회용
     */
    private String userId;

    public OverdueVO() {

    }

    public int getOverdueId() {
        return overdueId;
    }

    public void setOverdueId(int overdueId) {
        this.overdueId = overdueId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getOverdueStartDate() {
        return overdueStartDate;
    }

    public void setOverdueStartDate(String overdueStartDate) {
        this.overdueStartDate = overdueStartDate;
    }

    public String getOverdueReleaseDate() {
        return overdueReleaseDate;
    }

    public void setOverdueReleaseDate(String overdueReleaseDate) {
        this.overdueReleaseDate = overdueReleaseDate;
    }

    public String getOverdueStatus() {
        return overdueStatus;
    }

    public void setOverdueStatus(String overdueStatus) {
        this.overdueStatus = overdueStatus;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {

        return "OverdueVO{" +
                "overdueId=" + overdueId +
                ", memberId=" + memberId +
                ", overdueStartDate=" + overdueStartDate +
                ", overdueReleaseDate=" + overdueReleaseDate +
                ", overdueStatus='" + overdueStatus + '\'' +
                ", memberName='" + memberName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}