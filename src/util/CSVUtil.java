package util;

import java.io.FileReader;
import java.io.InputStream;

import java.net.URI;

import java.nio.charset.Charset;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import vo.BookVO;

public class CSVUtil {

	private static final String FILE_URL = "https://github.com/ray3309/semiproject/raw/refs/heads/main/Book.csv";
	private static final String SAVE_PATH = "src/Book.csv";

	/*
	 * CSV 읽기
	 */
	public static List<BookVO> readCSV() {
		/*
		 * 다운로드
		 */
		download();
		List<BookVO> list = new ArrayList<>();
		CSVParser parser = new CSVParserBuilder().withSeparator(',').withQuoteChar('"').build();
		CSVReader reader = null;
		try {
			reader = new CSVReaderBuilder(new FileReader(SAVE_PATH, Charset.forName("EUC-KR"))).withCSVParser(parser).build();
			String[] line;
			/*
			 * 헤더 제거
			 */
			reader.readNext();
			while ((line = reader.readNext()) != null) {
				if (line == null || line.length < 30) {
					continue;
				}
				BookVO vo = new BookVO();
				/*
				 * TITLE
				 */
				vo.setTitle(cutString(line[4], 1024));
				/*
				 * AUTHOR
				 */
				vo.setAuthor(cutString(line[8], 512));
				/*
				 * PUBLISHER
				 */
				vo.setPublisher(cutString(line[10], 100));
				/*
				 * 출판일
				 */
				vo.setPublishDate(formatDate(line[13]));
				/*
				 * ISBN
				 */
				vo.setIsbn(line[15].replaceAll("['\"’‘\\s]", ""));
				/*
				 * KDC
				 */
				if (line[28] != null && !line[28].trim().isEmpty()) {
					vo.setKdc(line[28]);
				} else {
					vo.setKdc("000");
				}
				/*
				 * DDC
				 */
				if (line[29] != null && !line[29].trim().isEmpty()) {
					vo.setDdc(line[29]);
				} else {
					vo.setDdc("000");
				}
				/*
				 * 대출 상태
				 */
				vo.setRentStatus("N");
				list.add(vo);
			}
			System.out.println("CSV 파싱 완료 : " + list.size());
		} catch (Exception e) {
			System.out.println("CSV 파싱 실패");
			e.printStackTrace();
		} finally {
			/*
			 * CSVReader 강제 close
			 */
			try {
				if (reader != null) {
					reader.close();
					System.out.println("CSVReader close 완료");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			 /*
			 * 다운로드 파일 삭제
			 */
			try {
				Files.deleteIfExists(Paths.get(SAVE_PATH));
				System.out.println("CSV 파일 삭제 완료");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/*
	 * CSV 다운로드
	 */
	public static void download() {
		try (InputStream in = new URI(FILE_URL).toURL().openStream()) {
			Files.copy(in, Paths.get(SAVE_PATH), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("CSV 다운로드 완료 : " + SAVE_PATH);
		} catch (Exception e) {
			System.out.println("CSV 다운로드 실패");
			e.printStackTrace();
		}
	}
	/*
	 * 문자열 길이 제한
	 */
	private static String cutString(String str, int maxLength) {
		if (str == null) {
			return "";
		}
		str = str.trim();
		if (str.length() > maxLength) {
			return str.substring(0, maxLength);
		}
		return str;
	}
	/*
	 * 날짜 포맷 YYYY-MM-DD
	 */
	private static String formatDate(String value) {
		if (value == null) {
			return "1900-01-01";
		}
		String date = value.replaceAll("[^0-9]", "");
		/*
		 * YYYY
		 */
		if (date.length() == 4) {
			date += "0101";
		}
		/*
		 * YYYYMM
		 */
		else if (date.length() == 6) {
			date += "01";
		}
		/*
		 * 잘못된 날짜 방어
		 */
		if (date.length() != 8) {
			date = "19000101";
		}
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
	}
}