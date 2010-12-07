/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 
 * @author Vadim Kisen
 *
 * copyright 2010 by uTest 
 */
package com.utest.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class NumberUtil
{
	/**
	 * Rounds a given number with the amount of decimal given by decimalPlaces.
	 * The rounding mode is specified by the second argument in setScale()
	 * method.
	 * 
	 * @param target
	 *            , the number to round
	 * @param decimalPlace
	 *            , the amount of decimals
	 * @return the given number rounded with the amount of decimals places
	 */
	public static Double round(final Double target, final int decimalPlace)
	{
		BigDecimal bd = new BigDecimal(target);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	/**
	 * Rounds a given number with the amount of decimal given by decimalPlaces.
	 * The rounding mode is specified by the second argument in setScale()
	 * method.
	 * 
	 * @param target
	 *            , the number to round
	 * @param decimalPlace
	 *            , the amount of decimals
	 * @return the given number rounded with the amount of decimals places
	 */
	public static Float round(final Float target, final int decimalPlace)
	{
		final Double intermediate = Math.ceil(target * java.lang.Math.pow(10, decimalPlace)) / java.lang.Math.pow(10, decimalPlace);
		return (intermediate).floatValue();
	}

	public static int roundToInt(final double target)
	{
		return target - ((int) target) >= 0.5 ? ((int) target) + 1 : ((int) target);
	}

	public static int randomInt(final int min, final int max)
	{
		return (int) (Math.random() * (max - min) + min);
	}

	/**
	 * Takes in several groups of numbers and return distinct combinations of
	 * all numbers between different groups.
	 * 
	 * @param outputCombinations_
	 * @param inputGroups_
	 */
	public static List<List<Integer>> buildCombinations(final List<List<Integer>> inputGroups_)
	{
		final List<List<Integer>> outputCombinations = new ArrayList<List<Integer>>();
		buildCombinations(0, outputCombinations, "", inputGroups_);
		return outputCombinations;
	}

	private static void buildCombinations(final int d, final List<List<Integer>> outputCombinations_, final String str, final List<List<Integer>> inputGroups_)
	{
		if (d == inputGroups_.size())
		{
			final List<Integer> newGroup = new ArrayList<Integer>();
			final StringTokenizer stk = new StringTokenizer(str.substring(0, str.length() - 1), ",");
			while (stk.hasMoreTokens())
			{
				newGroup.add(new Integer(stk.nextToken()));
			}
			outputCombinations_.add(newGroup);
			return;
		}
		for (int k = 0; k < inputGroups_.get(d).size(); k++)
		{
			buildCombinations(d + 1, outputCombinations_, str + inputGroups_.get(d).get(k) + ",", inputGroups_);
		}
		return;
	}

	// VMKTEST
	public static void main(final String[] args)
	{
		final List<List<Integer>> vecList = new ArrayList<List<Integer>>();
		final List<Integer> list1 = new ArrayList<Integer>();
		list1.add(1);
		list1.add(2);
		final List<Integer> list2 = new ArrayList<Integer>();
		list2.add(3);
		list2.add(4);
		// list2.add(5);
		final List<Integer> list3 = new ArrayList<Integer>();
		list3.add(6);
		list3.add(7);
		// list3.add(8);
		// list3.add(9);
		final List<Integer> list4 = new ArrayList<Integer>();
		list4.add(10);
		list4.add(11);
		list4.add(12);
		list4.add(13);
		final List<Integer> list5 = new ArrayList<Integer>();
		list5.add(14);
		list5.add(15);
		vecList.add(list1);
		vecList.add(list2);
		vecList.add(list3);
		// vecList.add(list4);
		// vecList.add(list5);
		final List<List<Integer>> outputCombinations = buildCombinations(vecList);

		System.out.println("Combinations: " + outputCombinations.size());
		for (final List<Integer> singleGroup : outputCombinations)
		{
			System.out.println(singleGroup.toString());
		}

	}
}
