package org;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class TestWeb {
	private WebDriver driver;
	private String baseUrl;
	public String stu_id;
	public String stu_pwd;
	public String gitaddress="";
	public String filepath = "E:\\软件测试名单.xlsx";

	@Before
	public void setUp() throws Exception {
		String driverPath = System.getProperty("user.dir") + "/src/resources/driver/geckodriver.exe";
		System.setProperty("webdriver.gecko.driver", driverPath);
		driver = new FirefoxDriver();
		baseUrl = "http://121.193.130.195:8800";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(baseUrl + "/");
	}

	@Test
	public void testWeb() throws Exception {

		InputStream is = new FileInputStream(filepath);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		XSSFSheet sheet = xssfWorkbook.getSheet("Sheet1");

		for (int rowNum = 2; rowNum < sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
			org.apache.poi.ss.usermodel.Cell cellforid =row.getCell(1);
			stu_id = cellforid.toString();
            stu_id = stu_id.replace(".", "").replace("E9", "");
            while(stu_id.length()<10) {
            	stu_id = stu_id+"0";
            }
			stu_pwd = stu_id.substring(4);	
			org.apache.poi.ss.usermodel.Cell cellforgit = row.getCell(3);
			gitaddress = cellforgit.toString();
			
			// 使用driver的操作
			// 输入账号密码
			driver.findElement(By.name("id")).click();
			driver.findElement(By.name("id")).clear();
			driver.findElement(By.name("id")).sendKeys(stu_id);
			driver.findElement(By.name("password")).click();
			driver.findElement(By.name("password")).clear();
			driver.findElement(By.name("password")).sendKeys(stu_pwd);
			driver.findElement(By.id("btn_login")).click();

			// 进行测试
			Assert.assertEquals(gitaddress, driver.findElement(By.id("student-git")).getText());

			// 返回首页
			driver.findElement(By.id("btn_logout")).click();
			driver.findElement(By.id("btn_return")).click();
		}
	}
}
