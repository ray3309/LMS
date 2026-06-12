package vo;

public class BookVO {

    /*
     * BOOK.BOOKID
     */
    private int bookId;

    /*
     * BOOK.REGNO
     */
    private String regNo;

    /*
     * BOOK.TITLE
     */
    private String title;

    /*
     * BOOK.AUTHOR
     */
    private String author;

    /*
     * BOOK.PUBLISHER
     */
    private String publisher;

    /*
     * BOOK.PUBLISHDATE
     */
    private String publishDate;

    /*
     * BOOK.ISBN
     */
    private String isbn;

    /*
     * BOOK.KDC
     */
    private String kdc;

    /*
     * BOOK.DDC
     */
    private String ddc;

    /*
     * BOOK.RENTSTATUS
     * Y : 대출중
     * N : 대출가능
     */
    private String rentStatus;

    /*
     * BOOK.REGDATE
     */
    private String regDate;

    /*
     * 검색용
     * RENTAL.RETURNDUEDATE
     */
    private String returnDueDate;

    public BookVO() {

    }    

    public BookVO(int bookId, String regNo, String title, String author, String publisher, String publishDate,
			String isbn, String kdc, String ddc, String rentStatus, String regDate, String returnDueDate) {
		super();
		this.bookId = bookId;
		this.regNo = regNo;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.publishDate = publishDate;
		this.isbn = isbn;
		this.kdc = kdc;
		this.ddc = ddc;
		this.rentStatus = rentStatus;
		this.regDate = regDate;
		this.returnDueDate = returnDueDate;
	}

	public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getKdc() {
        return kdc;
    }

    public void setKdc(String kdc) {
        this.kdc = kdc;
    }

    public String getDdc() {
        return ddc;
    }

    public void setDdc(String ddc) {
        this.ddc = ddc;
    }

    public String getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(String rentStatus) {
        this.rentStatus = rentStatus;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getReturnDueDate() {
        return returnDueDate;
    }

    public void setReturnDueDate(String returnDueDate) {
        this.returnDueDate = returnDueDate;
    }

    @Override
    public String toString() {

        return "BookVO{" +
                "bookId=" + bookId +
                ", regNo='" + regNo + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishDate=" + publishDate +
                ", isbn='" + isbn + '\'' +
                ", kdc='" + kdc + '\'' +
                ", ddc='" + ddc + '\'' +
                ", rentStatus='" + rentStatus + '\'' +
                ", regDate=" + regDate +
                ", returnDueDate=" + returnDueDate +
                '}';
    }
}