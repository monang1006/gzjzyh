package com.strongit.xxbs.common;

import java.util.Comparator;

import com.strongit.xxbs.dto.StatisticsDto;

public class StatisticsComparator implements Comparator<StatisticsDto>
{

	public int compare(StatisticsDto arg0, StatisticsDto arg1)
	{
		if(arg0.getTotal() == arg1.getTotal())
			return 0;
		if(arg0.getTotal() > arg1.getTotal())
			return -1;
		else
			return 1;
	}

}
