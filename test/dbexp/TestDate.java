package dbexp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TestDate {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestDate td = new TestDate();
		td.showTime("2006-01-01", "2006-03-01");
	}

	/**
	 * 计算工作日
	 * 
	 * @param date1
	 * @param date2
	 */
	public void showTime(String date1, String date2) {
		// 这里要判断第二个参数日期要比第一个参数日期大先继续运行
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat holidaysdf = new SimpleDateFormat("MM-dd");
		// 工作日
		int workDay = 0;
		try {
			Date d1 = sdf.parse(date1);
			Date d2 = sdf.parse(date2);
			gc.setTime(d1);
			long time = d2.getTime() - d1.getTime();
			long day = time / 3600000 / 24 + 1;
			for (int i = 0; i < day; i++) {
				if (gc.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SATURDAY			//排除周六
						&& gc.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SUNDAY) {		//排除周日
					if (!holidayList(holidaysdf.format(gc.getTime()))
							&& !holidayOfCN(sdf.format(gc.getTime()))){
						workDay++;
					}
				}else if(unholidayList(holidaysdf.format(gc.getTime()))){	//根据法定假日调整，需要上班的周六周日
					workDay++;
				}
				// 天数加1
				gc.add(gc.DATE, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(workDay);
	}
	

	// 春节放假三天，定义到2020年
	public boolean holidayOfCN(String year) {
		List ls = new ArrayList();
		ls.add("2005-02-09");
		ls.add("2005-02-10");
		ls.add("2005-02-11");
		ls.add("2006-01-29");
		ls.add("2006-01-30");
		ls.add("2006-01-31");
		ls.add("2007-02-18");
		ls.add("2007-02-19");
		ls.add("2007-02-21");
		ls.add("2008-02-07");
		ls.add("2008-02-08");
		ls.add("2008-02-09");
		ls.add("2009-01-26");
		ls.add("2009-01-27");
		ls.add("2009-01-28");
		ls.add("2010-02-14");
		ls.add("2010-02-15");
		ls.add("2010-02-16");
		ls.add("2011-02-03");
		ls.add("2011-02-04");
		ls.add("2011-02-05");
		ls.add("2012-01-23");
		ls.add("2012-01-24");
		ls.add("2012-01-25");
		ls.add("2013-02-10");
		ls.add("2013-02-11");
		ls.add("2013-02-12");
		ls.add("2014-01-31");
		ls.add("2014-02-01");
		ls.add("2014-02-02");
		ls.add("2015-02-19");
		ls.add("2015-02-20");
		ls.add("2015-02-21");
		ls.add("2006-02-08");
		ls.add("2006-02-09");
		ls.add("2006-02-10");
		ls.add("2017-01-28");
		ls.add("2017-01-29");
		ls.add("2017-01-30");
		ls.add("2018-02-16");
		ls.add("2018-02-17");
		ls.add("2018-02-18");
		ls.add("2019-02-05");
		ls.add("2019-02-06");
		ls.add("2019-02-07");
		ls.add("2020-01-25");
		ls.add("2020-01-26");
		ls.add("2020-01-27");
		if (ls.contains(year))
			return true;
		return false;
	}

	// 法定假日，五一和国庆
	public boolean holidayList(String findDate) {
		List ls = new ArrayList();
		ls.add("05-01");
		ls.add("05-02");
		ls.add("05-03");
		ls.add("10-01");
		ls.add("10-02");
		ls.add("10-03");
		if (ls.contains(findDate))
			return true;
		return false;
	}


	//法定假日 调整的周六周日
	public boolean unholidayList(String findDate) {
		List ls = new ArrayList();
		ls.add("2013-04-27");
		ls.add("2013-04-28");
		if (ls.contains(findDate))
			return true;
		return false;
	}
}
