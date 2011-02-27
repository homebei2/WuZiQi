package com.wing.game.wzq.provider;

/**
 * ���η�������
 * 
 * @author yale
 * 
 */
public class ChessFormUtil {

	// ����������εķ���
	/** �������������б�ĳ��� */
	public static final int ANALYZE_LEN = 8;

	public static final int HALF_LEN = ANALYZE_LEN >> 1;
	/**
	 * ����:��ֻ����һ���Ϳ���ʤ����
	 */
	public static final int WU_LIAN = 85;
	/**
	 * ���ģ����߶��ɳ���ĵ�
	 */
	public static final int HUO_SI = 40;
	/**
	 * ����������һ�����Գɻ��ĵĵ�
	 */
	public static final int HUO_SAN = 15;
	/**
	 * ���ģ�ֻ��һ�˿ɳ���ĵ�
	 */
	public static final int CHONG_SI = 6;
	/**
	 * ���������һ���ɳɻ����ĵ�
	 */
	public static final int HUO_ER = 4;

	/**
	 * ����������һ���ɳɳ��ĵĵ�
	 */
	public static final int MIAN_SAN = 2;
	
	/**
	 * �߶�������һ���ɳ������ĵ�
	 */
	public static final int MIAN_ER = 1;
	
	public static ChessFormUtil util = new ChessFormUtil();
	private ChessFormUtil(){
		 //
	}
	public static ChessFormUtil getInstance(){
		return util;
	}

	// -------------------------------------------------------------
	/**
	 * ������������
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeWulian(int[] tmpChess, int isWho) {
		int count = 0;
		for (int i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		for (int i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (count == 4) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * �������� return �Ƿ���ڻ���
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeHuosi(int[] tmpChess, int isWho) {
		int count = 0;
		int i = 0;
		boolean isSpace = false;
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN - (i + 1)] == 0) {
			isSpace = true;
		}
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN + i] == 0) {
			isSpace = true;
		} else {
			isSpace = false;
		}

		if (count == 3 && isSpace) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * �������� return �Ƿ���ڻ���
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeHuosan(int[] tmpChess, int isWho) {
		int count = 0;
		int i = 0;
		boolean isSpace = false;
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN - (i + 1)] == 0) {
			isSpace = true;
		}
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN + i] == 0) {
			isSpace = true;
		} else {
			isSpace = false;
		}

		if (count == 2 && isSpace) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * �������� return �Ƿ���ڳ���
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeChongsi(int[] tmpChess, int isWho) {
		int count = 0;
		int i = 0;
		boolean isSpace = false;
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN - (i + 1)] == 0) {
			isSpace = true;
		}
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN + i] == 0) {
			isSpace = true;
		}

		if (count == 3 && isSpace) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * ������� return �Ƿ���ڻ��
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeHuoEr(int[] tmpChess, int isWho) {
		
		int count = 0;
		int i = 0;
		boolean isSpace = false;
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN - (i + 1)] == 0) {
			isSpace = true;
		}
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN + i] == 0) {
			isSpace = true;
		} else {
			isSpace = false;
		}

		if (count == 1 && isSpace) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * �������� return �Ƿ��������
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeMianSan(int[] tmpChess, int isWho) {
		int count = 0;
		int i = 0;
		boolean isSpace = false;
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN - (i + 1)] == 0) {
			isSpace = true;
		}
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN + i] == 0) {
			isSpace = true;
		}

		if (count == 2 && isSpace) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * �����߶� return �Ƿ�����߶�
	 * 
	 * @param tmpChess
	 */
	public boolean analyzeMianEr(int[] tmpChess, int isWho) {
		int count = 0;
		int i = 0;
		boolean isSpace = false;
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN - (i + 1)] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN - (i + 1)] == 0) {
			isSpace = true;
		}
		for (i = 0; i < HALF_LEN; i++) {
			if (tmpChess[HALF_LEN + i] == isWho) {
				count++;
			} else {
				break;
			}
		}
		if (tmpChess[HALF_LEN + i] == 0) {
			isSpace = true;
		}

		if (count == 1 && isSpace) {
			return true;
		}
		return false;
	}
	
}
