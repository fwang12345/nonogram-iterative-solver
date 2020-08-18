import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class NonogramTest {
	public static List<Pattern> patterns;
	public static Square[][] arr;
	
	public static void constructGrid(int pattern, int[] seq, String block) {
		patterns = new ArrayList<Pattern>();
		arr = new Square[block.length() * 2][block.length()];
		for (int i = 0; i < 2 * block.length(); i++) {
			patterns.add(new Pattern());
		}
		for (int i = 0; i < 2 * block.length(); i++) {
			for (int j = 0; j < block.length(); j++) {
				arr[i][j] = new Square();
			}
		}
		Pattern p = patterns.get(pattern);
		List<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < seq.length; i++) {
			l.add(seq[i]);
			p.sum += seq[i];
		}
		p.set(l);
		for (int i = 0; i < block.length(); i++) {
			if (block.charAt(i) == '-') {
				arr[pattern][i].state = Option.EMPTY;
			} else if (block.charAt(i) == 'X'){
				arr[pattern][i].state = Option.CROSS;
			} else {
				arr[pattern][i].state = Option.FILL;
			}
		}
		
	}
	public static String toString(Square[] arr) {
		String block = "";
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].state == Option.EMPTY) {
				block += '-';
			} else if (arr[i].state == Option.CROSS){
				block += 'X';
			} else {
				block += 'O';
			}
		}
		return block;
	}
	@Test
	public void testCrossFinish0() {
		int[] seq = {2, 1, 2, 1, 2};
		String block = "--XOX-OO-XO----";
		constructGrid(0, seq, block);
		boolean finish = Nonogram.crossFinish(patterns, arr, 0, 2, 11, 1, 4);
		String expected = "--XOXXOOXXO----";
		String actual = toString(arr[0]);
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossFinish1() {
		int[] seq = {2, 1, 2, 1, 2};
		String block = "--O--XOX--O---";
		constructGrid(0, seq, block);
		boolean finish = Nonogram.crossFinish(patterns, arr, 0, 2, 12, 1, 4);
		String actual = toString(arr[0]);
		assertEquals(block, actual);
		assertFalse(finish);
	}
	@Test
	public void testCrossFinish2() {
		int[] seq = {2, 1, 2, 1, 2};
		String block = "--O--XOOX------";
		constructGrid(0, seq, block);
		boolean finish = Nonogram.crossFinish(patterns, arr, 0, 2, 12, 1, 4);
		String actual = toString(arr[0]);
		assertEquals(block, actual);
		assertFalse(finish);
	}
	@Test(expected = IllegalStateException.class)
	public void testCrossFinish3() {
		int[] seq = {2, 1, 2, 1, 2};
		String block = "--O--XOOXOX-OO-";
		constructGrid(0, seq, block);
		Nonogram.crossFinish(patterns, arr, 0, 2, 15, 1, 4);
	}
	@Test
	public void testCrossMax0() {
		int[] seq = {3, 1, 2, 1, 3};
		String block = "--O--OO--------";
		constructGrid(0, seq, block);
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 1, 12, 1, 4);
		String actual = toString(arr[0]);
		String expected = "-XOXXOOX-------";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	
	@Test
	public void testCrossMax1() {
		int[] seq = {3, 1, 2, 1, 3};
		String block = "OOO---OO-------";
		constructGrid(0, seq, block);
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 12, 0, 3);
		String actual = toString(arr[0]);
		String expected = "OOOX-XOOX------";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMax2() {
		int[] seq = {3, 1, 2, 1, 3};
		String block = "--O---OO-------";
		constructGrid(0, seq, block);
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 12, 0, 3);
		String actual = toString(arr[0]);
		String expected = "--O--XOOX------";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	
	@Test
	public void testCrossMax3() {
		int[] seq = {3, 1, 2, 1, 3};
		String block = "OOO---OO----OOO";
		constructGrid(0, seq, block);
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 5);
		String actual = toString(arr[0]);
		String expected = "OOOX--OO---XOOO";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testCrossMax4() {
		int[] seq = {3, 1, 2, 1, 3};
		String block = "OOO---OOO---OOO";
		constructGrid(0, seq, block);
		Nonogram.crossMax(patterns, arr, 0, 3, 12, 1, 4);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testCrossMax5() {
		int[] seq = {3, 1, 2, 1, 3};
		String block = "OOO---OOO---OOO";
		constructGrid(0, seq, block);
		Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 5);
	}
	@Test
	public void testCrossMax6() {
		int[] seq = {3, 1, 3, 1, 2};
		String block = "OOO-O-OOO-O-OO-";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 5);
		String actual = toString(arr[0]);
		String expected = "OOOXOXOOOXOXOOX";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMax7() {
		int[] seq = {3, 1, 3, 1, 2};
		String block = "-OO-O-OOO--O-OO";
		constructGrid(0, seq, block);
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 5);
		String actual = toString(arr[0]);
		String expected = "-OOXOXOOOX-OXOO";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMax8() {
		int[] seq = {2, 1};
		String block = "OOX----O-------";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 2);
		String actual = toString(arr[0]);
		String expected = "OOX---XOX------";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMax9() {
		int[] seq = {2, 1};
		String block = "OOX-----------O";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 2);
		String actual = toString(arr[0]);
		String expected = "OOX----------XO";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMax10() {
		int[] seq = {3, 2, 1};
		String block = "-----OOX--OO---";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "-----OOX-XOOX--";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test(expected = IllegalStateException.class)
	public void testCrossMax11() {
		int[] seq = {3, 2, 1};
		String block = "----XOOX--OO---";
		constructGrid(0, seq, block);	
		Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 3);
	}
	@Test
	public void testCrossMax12() {
		int[] seq = {3, 2, 1};
		String block = "----XOOX---O---";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "----XOOX--XOX--";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMax13() {
		int[] seq = {5, 2, 1};
		String block = "-----OO--X-OO--";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "-----OO--XXOOX-";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMax14() {
		int[] seq = {5, 2, 1};
		String block = "-----OO---OO---";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "-----OO--XOOX--";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMax15() {
		int[] seq = {5, 2, 1};
		String block = "-----OO--OO----";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "-----OO--OO----";
		assertEquals(expected, actual);
		assertFalse(finish);
	}
	@Test
	public void testCrossMax16() {
		int[] seq = {15};
		String block = "OOOOOOOOOOOOOO-";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 14, 0, 1);
		String actual = toString(arr[0]);
		String expected = "OOOOOOOOOOOOOO-";
		assertEquals(expected, actual);
		assertFalse(finish);
	}
	@Test
	public void testCrossMax17() {
		int[] seq = {1, 2, 5};
		String block = "----OO---XOOO--";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "---XOOX--XOOO--";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMax18() {
		int[] seq = {1, 2, 5};
		String block = "--OO---OO-OO---";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "-XOOX--OO-OO---";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMax19() {
		int[] seq = {4, 2, 1, 3};
		String block = "O-OO--OO-O-----";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 4);
		String actual = toString(arr[0]);
		String expected = "O-OO--OOXO-----";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMax20() {
		int[] seq = {3, 1, 2, 4};
		String block = "-----O-OO--OO-O";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMax(patterns, arr, 0, 0, 15, 0, 4);
		String actual = toString(arr[0]);
		String expected = "-----OXOO--OO-O";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMin0() {
		int[] seq = {3, 4};
		String block = "OOO-X---X----X-";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMin(patterns, arr, 0, 0, 15, 0, 2);
		String actual = toString(arr[0]);
		String expected = "OOO-XXXXX----XX";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	
	@Test
	public void testCrossMin1() {
		int[] seq = {4, 3};
		String block = "-X----X---X-OOO";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMin(patterns, arr, 0, 0, 15, 0, 2);
		String actual = toString(arr[0]);
		String expected = "XX----XXXXX-OOO";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMin2() {
		int[] seq = {4, 3, 2};
		String block = "OOOO-OO-X-X----";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMin(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "OOOO-OO-XXX----";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	
	@Test
	public void testCrossMin3() {
		int[] seq = {4, 2, 3};
		String block = "OOOO-O-X--X---X";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMin(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "OOOO-O-XXXX---X";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	
	@Test
	public void testCrossMin4() {
		int[] seq = {2, 3, 4};
		String block = "----X-X-OO-OOOO";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMin(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "----XXX-OO-OOOO";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMin5() {
		int[] seq = {3, 2, 4};
		String block = "X---X--X-O-OOOO";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMin(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "X---XXXX-O-OOOO";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMin6() {
		int[] seq = {4, 3};
		String block = "-OO--OO-X------";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMin(patterns, arr, 0, 0, 14, 0, 2);
		String actual = toString(arr[0]);
		String expected = "-OO--OO-XXXXXX-";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMin7() {
		int[] seq = {3, 4};
		String block = "------X-OO--OO-";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMin(patterns, arr, 0, 1, 15, 0, 2);
		String actual = toString(arr[0]);
		String expected = "-XXXXXX-OO--OO-";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMin8() {
		int[] seq = {2, 4};
		String block = "--OOX----XX----";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMin(patterns, arr, 0, 2, 14, 0, 2);
		String actual = toString(arr[0]);
		String expected = "--OOX----XXXXX-";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMin9() {
		int[] seq = {4, 2};
		String block = "----XX----XOO--";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMin(patterns, arr, 0, 1, 13, 0, 2);
		String actual = toString(arr[0]);
		String expected = "-XXXXX----XOO--";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMin10() {
		int[] seq = {3, 2};
		String block = "-O--O-X---X--X-";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMin(patterns, arr, 0, 0, 15, 0, 2);
		String actual = toString(arr[0]);
		String expected = "-O--O-XXXXXXXXX";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossMin11() {
		int[] seq = {2, 3};
		String block = "-X--X---X-O--O-";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossMin(patterns, arr, 0, 0, 15, 0, 2);
		String actual = toString(arr[0]);
		String expected = "XXXXXXXXX-O--O-";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test(expected = IllegalStateException.class)
	public void testCrossMin12() {
		int[] seq = {3, 2};
		String block = "-O--O-X-O-X-OX-";
		constructGrid(0, seq, block);	
		Nonogram.crossMin(patterns, arr, 0, 0, 15, 0, 2);
	}
	@Test
	public void testCrossUnreach0() {
		int[] seq = {1, 2, 3};
		String block = "O-OOX--OX------";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossUnreach(patterns, arr, 0, 2, 15, 1, 3);
		String actual = toString(arr[0]);
		String expected = "O-OOX--OXXXXXXX";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossUnreach1() {
		int[] seq = {3, 2, 1};
		String block = "------XO--XOO-O";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossUnreach(patterns, arr, 0, 0, 13, 0, 2);
		String actual = toString(arr[0]);
		String expected = "XXXXXXXO--XOO-O";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossUnreach2() {
		int[] seq = {1, 3, 3};
		String block = "--OOO---O------";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossUnreach(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "--OOO---O--XXXX";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossUnreach3() {
		int[] seq = {3, 3, 1};
		String block = "------O---OOO--";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossUnreach(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "XXXX--O---OOO--";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	
	@Test
	public void testCrossUnreach4() {
		int[] seq = {3, 3, 1};
		String block = "-------XOX-----";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossUnreach(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "-------XOXXXXXX";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossUnreach5() {
		int[] seq = {1, 3, 3};
		String block = "-----XOX-------";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossUnreach(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "XXXXXXOX-------";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossUnreach6() {
		int[] seq = {2, 3, 3};
		String block = "-OX-O---O------";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossUnreach(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "-OX-O---O--XXXX";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossUnreach7() {
		int[] seq = {3, 3, 2};
		String block = "------O---O-XO-";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossUnreach(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "XXXX--O---O-XO-";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	@Test
	public void testCrossUnreach8() {
		int[] seq = {2, 3, 3};
		String block = "-OX-O-------OO-";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossUnreach(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "-OX-O-------OO-";
		assertEquals(expected, actual);
		assertFalse(finish);
	}
	
	@Test
	public void testCrossUnreach9() {
		int[] seq = {3, 3, 2};
		String block = "-OO-------O-XO-";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossUnreach(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "-OO-------O-XO-";
		assertEquals(expected, actual);
		assertFalse(finish);
	}
	
	@Test
	public void testCrossUnreach10() {
		int[] seq = {3, 2, 3};
		String block = "----O-OO-O-----";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.crossUnreach(patterns, arr, 0, 0, 15, 0, 3);
		String actual = toString(arr[0]);
		String expected = "----O-OO-O-----";
		assertEquals(expected, actual);
		assertFalse(finish);
	}
	@Test
	public void testFillDep() {
		int[] seq = {5, 5};
		String block = "---------------";
		constructGrid(0, seq, block);	
		boolean finish = Nonogram.fillDef(patterns, arr, 0, 10, 0, 15, 0, 2);
		String actual = toString(arr[0]);
		String expected = "----O-----O----";
		assertEquals(expected, actual);
		assertTrue(finish);
	}
	
}

