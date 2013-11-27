/**
 * 
 */
package test;

import java.util.Date;

import org.nex.util.DateUtil;

/**
 * @author park
 *
 */
public class SimpleDateTest {

	/**
	 * 
	 */
	public SimpleDateTest() {
		Date d = new Date();
		String dfts = DateUtil.defaultTimestamp(d);
		System.out.println(dfts);
		d = DateUtil.fromDefaultTimestamp(dfts);
		dfts = DateUtil.defaultTimestamp(d);
		System.out.println(dfts);
	}

}
