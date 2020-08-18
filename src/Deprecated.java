import java.util.List;

public class Deprecated {
	// Cross squares that cannot be reached
	public static boolean crossUnreachDep(List<Pattern> patterns, Square[][] arr, int pattern, int leftSquare, int rightSquare, int leftPattern, int rightPattern) {
		boolean progress = false;
		Pattern pat = patterns.get(pattern);
		List<Integer> seq = pat.seq;
		int p = leftPattern;
		int i = leftSquare;
		int count = 0;
		while (i < rightSquare && p < rightPattern) {
			if (arr[pattern][i].state == Option.FILL) {
				count++;
			} else if (arr[pattern][i].state == Option.CROSS) {
				if (count > 0) {
					if (p < leftPattern || p >= rightPattern) {
						throw new IllegalStateException("Additional Pattern in Pattern " + pattern + " between index " + (i - count) + " and " + (i - 1));
					}
					if (i - count - 1 == leftSquare - 1 || arr[pattern][i - 1 - count].state == Option.CROSS) {
						p = Nonogram.findEqualLeft(seq, p, count) + 1;
					} else {
						p = Nonogram.findGreaterLeft(seq, p, count) + 1;
					}
					count = 0;
				}
			} else {
				if (count > 0) {
					p = Nonogram.findGreaterLeft(seq, p, count);
					int diff = seq.get(p) - count;
					int empty = 0;
					int j = i;
					while (j < rightSquare && empty < diff && arr[pattern][j].state == Option.EMPTY) {
						j++;
						empty++;
					}
					if (j == rightSquare || arr[pattern][j].state == Option.CROSS || empty == diff) {
						p++;
					}
					i = j - 1;
					count = 0;
				}
			}
			i++;
		}
		while (i < rightSquare) {
			progress = Nonogram.assignPattern(patterns, arr, pattern, i++, Option.CROSS) || progress;
		}
		p = rightPattern - 1;
		i = rightSquare - 1;
		count = 0;
		while (i >= leftSquare && p >= leftPattern && p < rightPattern) {
			if (arr[pattern][i].state == Option.FILL) {
				count++;
			} else if (arr[pattern][i].state == Option.CROSS) {
				if (count > 0) {
					if (i + 1 + count == rightSquare || arr[i][i + 1 + count].state == Option.CROSS) {
						p = Nonogram.findEqualRight(seq, p, count) - 1;
					} else {
						p = Nonogram.findGreaterRight(seq, p, count) - 1;
					}
					count = 0;
				}
			} else {
				if (count > 0) {
					p = Nonogram.findGreaterRight(seq, p, count);
					int diff = seq.get(p) - count;
					int empty = 0;
					int j = i;
					while (j > leftSquare - 1 && empty < diff && arr[pattern][j].state == Option.EMPTY) {
						j--;
						empty++;
					}
					if (j == leftSquare - 1 || empty == diff || arr[pattern][j].state == Option.CROSS) {
						p--;
					}
					i = j + 1;
					count = 0;
				}
			}
			i--;
		}
		while (i >= leftSquare) {
			progress = Nonogram.assignPattern(patterns, arr, pattern, i--, Option.CROSS) || progress;
		}
		return progress;
	}
}
