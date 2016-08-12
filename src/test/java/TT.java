/**
 * <p>Title: TT </p>
 * <p>Description:  </p>
 * Date: 2016年8月11日 下午2:38:16
 * @author hangzongguo@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date         Author        Content
 * ===========================================
 * 2016年8月11日    hangzongguo   创建文件,实现基本功能
 * 
 * ============================================
 */
public class TT {
	public static void main(String[] args) {
		ff();
	}

	/** 
	 * <p>Title: ff </p>
	 * <p>Description:  </p>
	 */
	private static void ff() {
		TTT .tt();
	}
	
}

class TTT {
	public static void tt() {
		System.out.println(Thread.currentThread().getStackTrace()[2].getMethodName());
		System.out.println(Thread.currentThread().getStackTrace()[2].getClassName());
	}
}