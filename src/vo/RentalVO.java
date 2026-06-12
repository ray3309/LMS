package vo;

public class RentalVO {

    /*
     * RENTAL.RENTALID
     */
    private int rentalId;

    /*
     * RENTAL.MEMBERID
     */
    private int memberId;

    /*
     * RENTAL.BOOKID
     */
    private int bookId;

    /*
     * RENTAL.RENTDATE
     */
    private String rentDate;

    /*
     * RENTAL.RETURNDUEDATE
     * 대출일 + 14일
     */
    private String returnDueDate;

    /*
     * RENTAL.RETURNDATE
     */
    private String returnDate;

    /*
     * RENTAL.RENTALSTATUS
     * R : 대출중
     * C : 반납완료
     * O : 연체중
     */
    private String rentalStatus;

    /*
     * JOIN 조회용
     */
    private String bookTitle;

    /*
     * JOIN 조회용
     */
    private String memberName;

    public RentalVO() {

    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getRentDate() {
        return rentDate;
    }

    public void setRentDate(String rentDate) {
        this.rentDate = rentDate;
    }

    public String getReturnDueDate() {
        return returnDueDate;
    }

    public void setReturnDueDate(String returnDueDate) {
        this.returnDueDate = returnDueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(String rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public String toString() {

        return "RentalVO{" +
                "rentalId=" + rentalId +
                ", memberId=" + memberId +
                ", bookId=" + bookId +
                ", rentDate=" + rentDate +
                ", returnDueDate=" + returnDueDate +
                ", returnDate=" + returnDate +
                ", rentalStatus='" + rentalStatus + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}