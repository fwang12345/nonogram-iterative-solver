import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Nonogram {
	public static int SIZE = 15;
	public static int MAX = Integer.MIN_VALUE;
	
	public static void printPattern(List<Pattern> patterns) {
		for (Pattern p : patterns) {
			for (Integer i : p.seq) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
	}
	
	public static void printList(List<Integer> list) {
		for (Integer i : list) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
	
	public static int findEqualLeft(List<Integer> seq, int p, int num) {
		for (int i = p; i < seq.size(); i++) {
			if (seq.get(i) == num) {
				return i;
			}
		}
		return -1;
	}

	public static int findGreaterLeft(List<Integer> seq, int p, int num) {
		for (int i = p; i < seq.size(); i++) {
			if (seq.get(i) >= num) {
				return i;
			}
		}
		return -1;
	}
	
	public static int findEqualRight(List<Integer> seq, int p, int num) {
		for (int i = p; i >= 0; i--) {
			if (seq.get(i) == num) {
				return i;
			}
		}
		return -1;
	}

	public static int findGreaterRight(List<Integer> seq, int p, int num) {
		for (int i = p; i >= 0; i--) {
			if (seq.get(i) >= num) {
				return i;
			}
		}
		return -1;
	}
	
	public static boolean assignPattern(List<Pattern> patterns, Square[][] arr, int pattern, int i, Option state) {
		if (arr[pattern][i].state == Option.EMPTY) {
			int row;
			int col;
			if (pattern < SIZE) {
				row = pattern;
				col = SIZE + i;
			} else {
				row = i;
				col = pattern;
			}
			if (state == Option.FILL) {
				patterns.get(row).fill++;
				patterns.get(col).fill++;
			} else if (state == Option.CROSS) {
				patterns.get(row).cross++;
				patterns.get(col).cross++;
			} else {
				return false;
			}
			arr[pattern][i].state = state;
			return true;
		} else if (arr[pattern][i].state == Option.CROSS) {
			if (state == Option.FILL) {
				throw new IllegalStateException("Attempting to fill already crossed square at Pattern " + pattern + " and Index " + i);
			}
		} else {
			if (state == Option.CROSS) {
				throw new IllegalStateException("Attempting to cross already filled square at Pattern " + pattern + " and Index " + i);
			}
		}
		return false;
	}
	
	public static List<Pattern> readPuzzle(String filename) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				if (tokens.length > MAX) {
					MAX = tokens.length;
				}
				List<Integer> pattern = new ArrayList<Integer>();
				Pattern p = new Pattern();
				for (int i = 0; i < tokens.length; i++) {
					int num = Integer.parseInt(tokens[i]);
					pattern.add(num);
					p.sum += num;
					if (num > p.max) {
						p.max = num;
					}
					if (num < p.min) {
						p.min = num;
					}
				}
				p.set(pattern);
				patterns.add(p);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return patterns;
	}
	
	public static void drawGrid(List<Pattern> patterns, Square[][] grid) {
		// Draw light blue rectangles
		PennDraw.setPenRadius();
		PennDraw.setPenColor(234, 238, 253);
		for (int i = 0; i < SIZE; i++) {
			PennDraw.filledRectangle(MAX / 2.0, i + 0.5, MAX / 2.0, 0.5);
		}
		for (int i = 0; i < SIZE; i++) {
			PennDraw.filledRectangle(MAX + i + 0.5, SIZE + MAX / 2.0, 0.5, MAX / 2.0);
		}

		// Draw rectangle and square borders
		PennDraw.setPenColor(PennDraw.WHITE);
		PennDraw.setPenRadius(0.005);
		for (int i = 0; i < SIZE; i++) {
			PennDraw.rectangle(MAX / 2.0, i + 0.5, MAX / 2.0, 0.5);
		}
		for (int i = 0; i < SIZE; i++) {
			PennDraw.rectangle(MAX + i + 0.5, SIZE + MAX / 2.0, 0.5, MAX / 2.0);
		}
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				PennDraw.setPenColor(52, 72, 97);
				PennDraw.setPenRadius(0.005);
				if (grid[i][j].state == Option.FILL) {
					PennDraw.filledSquare(MAX + i + 0.5, j + 0.5, 0.5);
				} else if (grid[i][j].state == Option.CROSS) {
					PennDraw.line(MAX + i + 0.2, j + 0.2, MAX + i + 0.8, j + 0.8);
					PennDraw.line(MAX + i + 0.2, j + 0.8, MAX + i + 0.8, j + 0.2);
				}
				PennDraw.setPenColor();
				PennDraw.setPenRadius();
				PennDraw.square(MAX + i + 0.5, j + 0.5, 0.5);
			}
		}
		for (int i = 0; i < SIZE / 5; i++) {
			for (int j = 0; j < SIZE / 5; j++) {
				PennDraw.setPenRadius(0.003);
				PennDraw.square(MAX + i * 5 + 2.5, j * 5 + 2.5, 2.5);
			}
		}
		PennDraw.setPenRadius(0.005);
		PennDraw.square(MAX + SIZE / 2.0, SIZE / 2.0, SIZE / 2.0);
		
		// Draw pattern numbers
		PennDraw.setFontSize(18);
		PennDraw.setPenRadius();
		PennDraw.setPenColor(55, 50, 90);
		for (int i = 0; i < SIZE; i++) {
			List<Integer> pattern = patterns.get(i).seq;
			for (int j = 0; j < pattern.size(); j++) {
				PennDraw.text(MAX - pattern.size() + j + 0.5, SIZE - i - 0.60, "" + pattern.get(j));
			}
		}
		for (int i = 0; i < SIZE; i++) {
			List<Integer> pattern = patterns.get(SIZE + i).seq;
			for (int j = 0; j < pattern.size(); j++) {
				PennDraw.text(MAX + i + 0.5, SIZE + pattern.size() - j - 0.5 , "" + pattern.get(j));
			}
		}
	}

	/**
	 * Cross all squares if the entire pattern is satisfied
	 */
	public static boolean crossFinish(List<Pattern> patterns, Square[][] arr, int pattern, int leftSquare, int rightSquare, int leftPattern, int rightPattern) {
		boolean progress = false;
		List<Integer> seq = patterns.get(pattern).seq;
		int p = leftPattern;
		int count = 0;
		for (int i = leftSquare; i <= rightSquare; i++) {
			if (i < rightSquare && arr[pattern][i].state == Option.FILL) {
				count++;
			} else {
				if (count > 0) {
					if (p >= rightPattern) {
						throw new IllegalStateException("Additional Pattern in Pattern " + pattern + " between index " + (i - count) + " and " + (i - 1));
					}
					if (count == seq.get(p)) {
						p++;
						count = 0;
					} else {
						return false;
					}
				}
			}
		}
		if (p == rightPattern) {
			for (int i = leftSquare; i < rightSquare; i++) {
				if (arr[pattern][i].state == Option.EMPTY) {
					progress = assignPattern(patterns, arr, pattern, i, Option.CROSS) || progress;
				}
			}
		}
		return progress;
	}
	
	/**
	 *  Cross ends of pattern of max size
	 */
	public static boolean crossMax(List<Pattern> patterns, Square[][] arr, int pattern, int leftSquare, int rightSquare, int leftPattern, int rightPattern) {
		boolean progress = false;
		Pattern pat = patterns.get(pattern);
		int max = pat.max(leftPattern, rightPattern);
		int count = 0;
		int prev = 0;
		int p = leftPattern;
		for (int i = leftSquare; i <= rightSquare; i++) {
			if (i < rightSquare && arr[pattern][i].state == Option.FILL) {
				count++;
			} else {
				if (count > 0) {
					if (count == max) {
						if (i < rightSquare) {
							progress = assignPattern(patterns, arr, pattern, i, Option.CROSS) || progress;
						}
						if (i - count  - 1 >= leftSquare) {
							progress = assignPattern(patterns, arr, pattern, i - count - 1, Option.CROSS) || progress;
						}
						p = findEqualLeft(pat.seq, p, max) + 1;
					} else if (count > max) {
						throw new IllegalStateException("Pattern greater than max in Pattern " + pattern + 
								" between index " + (i - count) + " and " + (i - 1));
					} else {
						if (i == rightSquare || arr[pattern][i].state == Option.CROSS) {
							if (i - count - 1 == leftSquare - 1 || arr[pattern][i - count - 1].state == Option.CROSS) {
								p = findEqualLeft(pat.seq, p, count) + 1;
							} else {
								p = findGreaterLeft(pat.seq, p, count) + 1;
							}
						} else {
							p = findGreaterLeft(pat.seq, p, count);
							int num = pat.seq.get(p) - count;
							int empty = 0;
							int j = i;
							while (j < rightSquare && arr[pattern][j].state == Option.EMPTY) {
								j++;
								empty++;
							}
							if (j == rightSquare || arr[pattern][j].state == Option.CROSS) {
								p++;
							} else {
								if (num <= empty) {
									p++;
								}
							}
							if (prev + count >= max) {
								progress = assignPattern(patterns, arr, pattern, i - count - 1, Option.CROSS) || progress;
							}
							if (j - 1 > i) {
								count = 0;
							}
							i = j - 1;
						}
					}
					max = pat.max(p, rightPattern);
				}
				prev = count;
				count = 0;
			}
		}
		max = pat.max(leftPattern, rightPattern);
		prev = 0;
		count = 0;
		p = rightPattern - 1;
		for (int i = rightSquare - 1; i >= leftSquare - 1; i--) {
			if (i > leftSquare - 1 && arr[pattern][i].state == Option.FILL) {
				count++;
			} else {
				if (count > 0) {
					if (count == max) {
						if (i >= leftSquare) {
							progress = assignPattern(patterns, arr, pattern, i, Option.CROSS) || progress;
						}
						if (i + count + 1 < rightSquare) {
							progress = assignPattern(patterns, arr, pattern, i + count + 1, Option.CROSS) || progress;
						}
						p = findEqualRight(pat.seq, p, max) - 1;
					} else if (count > max) {
						throw new IllegalStateException("Pattern greater than max in Pattern " + pattern + 
								" between index " + (i + count) + " and " + (i + 1));
					} else {
						if (i == leftSquare - 1 || arr[pattern][i].state == Option.CROSS) {
							if (i + count + 1 == rightSquare || arr[pattern][i + count + 1].state == Option.CROSS) {
								p = findEqualRight(pat.seq, p, count) - 1;
							} else {
								p = findGreaterRight(pat.seq, p, count) - 1;
							}
						} else {
							p = findGreaterRight(pat.seq, p, count);
							int num = pat.seq.get(p) - count;
							int empty = 0;
							int j = i;
							while (j > leftSquare - 1 && arr[pattern][j].state == Option.EMPTY) {
								j--;
								empty++;
							}
							if (j == leftSquare - 1 || arr[pattern][j].state == Option.CROSS) {
								p--;
							} else {
								if (num <= empty) {
									p--;
								}
							}
							if (prev + count >= max) {
								progress = assignPattern(patterns, arr, pattern, i + count + 1, Option.CROSS) || progress;
							}
							if (j + 1 < i) {
								count = 0;
							}
							i = j + 1;
						}
					}
					max = pat.max(leftPattern, p + 1);
				}
				prev = count;
				count = 0;
			}
		}
		return progress;
	}

	/**
	 * Cross empty blocks of size less than min
	 */
	public static boolean crossMin(List<Pattern> patterns, Square[][] arr, int pattern, int leftSquare, int rightSquare, int leftPattern, int rightPattern) {
		boolean progress = false;
		Pattern pat = patterns.get(pattern);
		int max = pat.max(leftPattern, rightPattern);
		int min = pat.min(leftPattern, rightPattern);
		int count = 0;
		int p = leftPattern;
		for (int i = leftSquare; i <= rightSquare; i++) {
			if (i < rightSquare && 
					(count == 0 || arr[pattern][i - 1].state == arr[pattern][i].state)) {
				count++;
			} else {
				if (count > 0 && arr[pattern][i - 1].state == Option.FILL) {
					if (count == max) {
						p = findEqualLeft(pat.seq, p, max) + 1;
					} else if (count > max) {
						throw new IllegalStateException("Pattern greater than max in Pattern " + pattern + 
								" between index " + (i - count) + " and " + (i - 1));
					} else {
						if (i == rightSquare || arr[pattern][i].state == Option.CROSS) {
							if (i - count - 1 == leftSquare - 1 || arr[pattern][i - count - 1].state == Option.CROSS) {
								p = findEqualLeft(pat.seq, p, count) + 1;
							} else {
								p = findGreaterLeft(pat.seq, p, count) + 1;
							}
						} else {
							p = findGreaterLeft(pat.seq, p, count);
							int num = pat.seq.get(p) - count;
							int empty = 0;
							int j = i;
							while (j < rightSquare && arr[pattern][j].state == Option.EMPTY) {
								j++;
								empty++;
							}
							if (j == rightSquare || arr[pattern][j].state == Option.CROSS) {
								p++;
							} else {
								if (num <= empty) {
									p++;
								}
							}
							i = j - 1;
						}
					}
					max = pat.max(p, rightPattern);
					min = pat.min(p, rightPattern);
				} else if (count > 0 && arr[pattern][i - 1].state == Option.EMPTY) {
					if ((i == rightSquare || arr[pattern][i].state == Option.CROSS) && 
							(i - count - 1 == leftSquare - 1 || arr[pattern][i - count - 1].state == Option.CROSS)) {
						if (count < min) {
							for (int j = 0; j < count; j++) {
								progress = assignPattern(patterns, arr, pattern, i - j - 1, Option.CROSS) || progress;
							}
						}
					}
				}
				count = 1;
			}
		}
		max = pat.max(leftPattern, rightPattern);
		min = pat.min(leftPattern, rightPattern);
		count = 0;
		p = rightPattern - 1;
		for (int i = rightSquare - 1; i >= leftSquare - 1; i--) {
			if (i > leftSquare - 1 && ((count == 0 || arr[pattern][i + 1].state == arr[pattern][i].state))) {
				count++;
			} else {
				if (count > 0 && arr[pattern][i + 1].state == Option.FILL) {
					if (count == max) {
						p = findEqualRight(pat.seq, p, max) - 1;
					} else if (count > max) {
						throw new IllegalStateException("Pattern greater than max in Pattern " + pattern + 
								" between index " + (i + count) + " and " + (i + 1));
					} else {
						if (i == leftSquare - 1 || arr[pattern][i].state == Option.CROSS) {
							if (i + count + 1 == rightSquare || arr[pattern][i + count + 1].state == Option.CROSS) {
								p = findEqualRight(pat.seq, p, count) - 1;
							} else {
								p = findGreaterRight(pat.seq, p, count) - 1;
							}
						} else {
							p = findGreaterRight(pat.seq, p, count);
							int num = pat.seq.get(p) - count;
							int empty = 0;
							int j = i;
							while (j > leftSquare - 1 && arr[pattern][j].state == Option.EMPTY) {
								j--;
								empty++;
							}
							if (j == leftSquare - 1 || arr[pattern][j].state == Option.CROSS) {
								p--;
							} else {
								if (num <= empty) {
									p--;
								}
							}
							i = j + 1;
						}
					}
					max = pat.max(leftPattern, p + 1);
					min = pat.min(leftPattern, p + 1);
				} else if (count > 0 && arr[pattern][i + 1].state == Option.EMPTY) {
					if ((i == leftSquare - 1 || arr[pattern][i].state == Option.CROSS) && 
							(i + count + 1 == rightSquare || arr[pattern][i + count + 1].state == Option.CROSS)) {
						if (count < min) {
							for (int j = 0; j < count; j++) {
								progress = assignPattern(patterns, arr, pattern, i + j + 1, Option.CROSS) || progress;
							}
						}
					}
				}
				count = 1;
			}
		}
		return progress;
	}

	/**
	 * Cross squares that cannot be reached
	 */
	public static boolean crossUnreach(List<Pattern> patterns, Square[][] arr, int pattern, int leftSquare, int rightSquare, int leftPattern, int rightPattern) {
		boolean progress = false;
		Pattern pat = patterns.get(pattern);
		int max = pat.max(leftPattern, rightPattern);
		int p = leftPattern;
		int i = leftSquare;
		int count = 0;
		while (i < rightSquare && p < rightPattern) {
			if (arr[pattern][i].state == Option.FILL) {
				count++;
			} else {
				if (count > 0) {
					if (count == max) {
						p = findEqualLeft(pat.seq, p, max) + 1;
					} else if (count > max) {
						throw new IllegalStateException("Pattern greater than max in Pattern " + pattern + 
								" between index " + (i - count) + " and " + (i - 1));
					} else {
						if (arr[pattern][i].state == Option.CROSS) {
							if (i - count - 1 == leftSquare - 1 || arr[pattern][i - count - 1].state == Option.CROSS) {
								p = findEqualLeft(pat.seq, p, count) + 1;
							} else {
								p = findGreaterLeft(pat.seq, p, count) + 1;
							}
						} else {
							p = findGreaterLeft(pat.seq, p, count);
							int num = pat.seq.get(p) - count;
							int empty = 0;
							int j = i;
							while (j < rightSquare && empty < num && arr[pattern][j].state == Option.EMPTY) {
								j++;
								empty++;
							}
							if (j == rightSquare || empty == num || arr[pattern][j].state == Option.CROSS) {
								p++;
							}
							i = j - 1;
						}
					}
					max = pat.max(p, rightPattern);
				}
				count = 0;
			}
			i++;
		}
		while (i < rightSquare) {
			progress = assignPattern(patterns, arr, pattern, i++, Option.CROSS) || progress;
		}
		max = pat.max(leftPattern, rightPattern);
		p = rightPattern - 1;
		i = rightSquare - 1;
		count = 0;
		while (i >= leftSquare && p >= leftPattern) {
			if (arr[pattern][i].state == Option.FILL) {
				count++;
			} else {
				if (count > 0) {
					if (count == max) {
						p = findEqualRight(pat.seq, p, max) - 1;
					} else if (count > max) {
						throw new IllegalStateException("Pattern greater than max in Pattern " + pattern + 
								" between index " + (i + count) + " and " + (i + 1));
					} else {
						if (arr[pattern][i].state == Option.CROSS) {
							if (i + count + 1 == rightSquare || arr[pattern][i + count + 1].state == Option.CROSS) {
								p = findEqualRight(pat.seq, p, count) - 1;
							} else {
								p = findGreaterRight(pat.seq, p, count) - 1;
							}
						} else {
							p = findGreaterRight(pat.seq, p, count);
							int num = pat.seq.get(p) - count;
							int empty = 0;
							int j = i;
							while (j > leftSquare - 1 && empty < num && arr[pattern][j].state == Option.EMPTY) {
								j--;
								empty++;
							}
							if (j == leftSquare - 1 || empty == num || arr[pattern][j].state == Option.CROSS) {
								p--;
							}
							i = j + 1;
						}
					}
					max = pat.max(leftPattern, p + 1);
				}
				count = 0;
			}
			i--;
		}
		while (i >= leftSquare) {
			progress = assignPattern(patterns, arr, pattern, i--, Option.CROSS) || progress;
		}
		return progress;
	}

	public static boolean cross(List<Pattern> patterns, Square[][] arr, int pattern, int leftSquare, int rightSquare, int leftPattern, int rightPattern) {
		// Cross finished patterns
		Pattern p = patterns.get(pattern);
		if (p.cross == SIZE - p.sum) {
			return false;
		}
		boolean finish = crossFinish(patterns, arr, pattern, leftSquare, rightSquare, leftPattern, rightPattern);
		boolean max = crossMax(patterns, arr, pattern, leftSquare, rightSquare, leftPattern, rightPattern);
		boolean min = crossMin(patterns, arr, pattern, leftSquare, rightSquare, leftPattern, rightPattern);
		boolean unreach = crossUnreach(patterns, arr, pattern, leftSquare, rightSquare, leftPattern, rightPattern);
		return finish || max || min || unreach;
	}

	public static boolean fillDefDep(List<Pattern> patterns, Square[][] arr, int i, int numSquare, int numPattern, int sum, int index, int sIndex) {
		boolean progress = false;
		int min = sum + numPattern - 1;
		int diff = numSquare - min;
		Pattern p = patterns.get(i);
		List<Integer> seq = p.seq;
		for (int j = 0; j < numPattern; j++) {
			int num = seq.get(j + sIndex);
			if (num <= diff) {
				index += num + 1;
			} else {
				index += diff;
				for (int k = 0; k < num - diff; k++) {
					progress = assignPattern(patterns, arr, i, index++, Option.FILL) || progress;
				}
				index++;
			}
		}
		return progress;
	}
	public static boolean fillDef(List<Pattern> patterns, Square[][] arr, int pattern, int sum, int leftSquare, int rightSquare, int leftPattern, int rightPattern) {
		boolean progress = false;
		int min = sum + rightPattern - leftPattern - 1;
		int diff = rightSquare - leftSquare - min;
		if (diff < 0) {
			throw new IllegalStateException("Not enough squares to satisfy Pattern " + pattern + 
					" between square indices " + leftSquare + " and " + rightSquare + 
					" and between pattern indices " + leftPattern + " and " + rightPattern);
		}
		Pattern p = patterns.get(pattern);
		List<Integer> seq = p.seq;
		int index = leftSquare;
		for (int i = leftPattern; i < rightPattern; i++) {
			int num = seq.get(i);
			if (num <= diff) {
				 index += num + 1;
			} else {
				index += diff;
				for (int j = 0; j < num - diff; j++) {
					progress = assignPattern(patterns, arr, pattern, index++, Option.FILL) || progress;
				}
				index++;
			}
		}
		return progress;
	}
	// Fill squares that would be filled no matter how pattern is arranged
	public static boolean fillDef(List<Pattern> patterns, Square[][] arr, int i) {
		boolean progress = false;
		Pattern p = patterns.get(i);
		List<Integer> seq = p.seq;
		int sum = p.sum;
		int left = 0;
		int right = seq.size() - 1;
		boolean fill = false;
		boolean cont = true;
		int l = 0;
		int prev = -1;
		int count = 0;
		while (cont && l < SIZE && left < seq.size()) {
			if (arr[i][l].state == Option.FILL) {
				fill = true;
			} else if (arr[i][l].state == Option.CROSS) {
				count = l - 1 - prev;
				if (count > 0) {
					if (fill && (seq.size() - 1 == left || seq.get(left) + seq.get(left + 1) > count)) {
						sum -= seq.get(left);
						left++;
					} else {
						cont = false;
					}
				}
				prev = l;
				fill = false;
			}
			l++;
		}
		if (l == SIZE && prev < SIZE - 1) {
			count = SIZE - 1 - prev;
			prev = SIZE;
		}
		l = prev - count;
		fill = false;
		cont = true;
		int r = SIZE - 1;
		prev = SIZE;
		count = SIZE;
		while (cont && r >= 0 && right >= 0) {
			if (arr[i][r].state == Option.FILL) {
				fill = true;
			} else if (arr[i][r].state == Option.CROSS) {
				count = prev - 1 - r;
				if (count > 0) {
					if (fill && (right == 0 || seq.get(right) + seq.get(right - 1) > count)) {
						sum -= seq.get(right);
						right--;
					} else {
						cont = false;
					}
				}
				prev = r;
				fill = false;
			}
			r--;
		}
		if (r == -1 && prev > 0) {
			count = prev;
			prev = -1;
		}
		r = prev + count;
		progress = fillDefDep(patterns, arr, i, r + 1 - l, right + 1 - left, sum, l, left) || progress;

		return progress;
	}
	public static boolean fillDef(List<Pattern> patterns, Square[][] arr, int pattern, int leftSquare, int rightSquare, int leftPattern, int rightPattern) {
		boolean progress = false;
		return progress;
	}

	private static boolean fillSingle(List<Pattern> patterns, Square[][] arr, int i, int prev, int index, int sIndex) {
		boolean progress = false;
		Pattern p = patterns.get(i);
		List<Integer> seq = p.seq;
		int count = index - 1 - prev;
		if (arr[i][prev + 1].state == Option.FILL) {
			for (int j = 0; j < seq.get(sIndex); j++) {
				progress = assignPattern(patterns, arr, i, prev + 1 + j, Option.FILL) || progress;
			}
		} else if (arr[i][index - 1].state == Option.FILL) {
			for (int j = 0; j < seq.get(sIndex); j++) {
				progress = assignPattern(patterns, arr, i, index - 1 - j, Option.FILL) || progress;
			}
		} else {
			int diff = count - seq.get(sIndex);
			for (int j = 0; j < seq.get(sIndex) - diff; j++) {
				progress = assignPattern(patterns, arr, i, prev + 1 + diff + j, Option.FILL) || progress;
			}
		}
		return progress;
	}
	private static boolean fillBorderLeft(List<Pattern> patterns, Square[][] arr, int i, int prev, int index, int sIndex) {
		boolean progress = false;
		Pattern p = patterns.get(i);
		List<Integer> seq = p.seq;
		if (arr[i][prev + 1].state == Option.FILL) {
			for (int j = 0; j < seq.get(sIndex); j++) {
				progress = assignPattern(patterns, arr, i, prev + 1 + j, Option.FILL) || progress;
			}
		}
		return progress;
	}
	private static boolean fillBorderRight(List<Pattern> patterns, Square[][] arr, int i, int prev, int index, int sIndex) {
		boolean progress = false;
		Pattern p = patterns.get(i);
		List<Integer> seq = p.seq;
		if (arr[i][prev - 1].state == Option.FILL) {
			for (int j = 0; j < seq.get(sIndex); j++) {
				progress = assignPattern(patterns, arr, i, prev - 1 - j, Option.FILL) || progress;
			}
		}
		return progress;
	}
	// Fill block by block separated by crosses
	public static boolean fillBlock(List<Pattern> patterns, Square[][] arr, int i) {
		boolean progress = false;
		Pattern p = patterns.get(i);
		List<Integer> seq = p.seq;
		boolean fill = false;
		boolean cont = true;
		int index = 0;
		int sIndex = 0;
		int prev = -1;
		int count;
		while (cont && index < SIZE && sIndex < seq.size()) {
			if (arr[i][index].state == Option.FILL) {
				fill = true;
			} else if (arr[i][index].state == Option.CROSS) {
				count = index - 1 - prev;
				if (count > 0) {
					if (fill && (seq.size() - 1 == sIndex || seq.get(sIndex) + seq.get(sIndex + 1) > count)) {
						progress = fillSingle(patterns, arr, i, prev, index, sIndex) || progress;
						sIndex++;
					} else {
						progress = fillBorderLeft(patterns, arr, i, prev, index, sIndex) || progress;
						cont = false;
					}
				}
				prev = index;
				fill = false;
			}
			index++;
		}
		fill = false;
		cont = true;
		index = SIZE - 1;
		sIndex = seq.size() - 1;
		prev = SIZE;
		while (cont && index >= 0 && sIndex >= 0) {
			if (arr[i][index].state == Option.FILL) {
				fill = true;
			} else if (arr[i][index].state == Option.CROSS) {
				count = prev - 1 - index;
				if (count > 0) {
					if (fill && (sIndex == 0 || seq.get(sIndex) + seq.get(sIndex - 1) > count)) {
						progress = fillSingle(patterns, arr, i, index, prev, sIndex) || progress;
						sIndex--;
					} else {
						progress = fillBorderRight(patterns, arr, i, prev, index, sIndex) || progress;
						cont = false;
					}
				}
				prev = index;
				fill = false;
			}
			index--;

		}
		return progress;
	}
	// Fill patterns that are definitely in the leftmost and rightmost block
	public static boolean fillEnd(List<Pattern> patterns, Square[][] arr, int i) {
		boolean progress = false;
		Pattern p = patterns.get(i);
		List<Integer> seq = p.seq;
		int index = 0;
		int count = 0;
		while (index < SIZE) {
			if (arr[i][index].state == Option.CROSS) {
				if (count > 0) {
					break;
				}
			} else {
				count++;
			}
			index++;
		}
		int j = SIZE - 1;
		int space = 0;
		int sum = p.sum;
		int sIndex = seq.size() - 1;
		while (j >= index && sIndex >= 0) {
			if (arr[i][j].state == Option.CROSS) {
				int num;
				while (space > 0 && sIndex >= 0 && (num = seq.get(sIndex)) <= space) {
					space -= num + 1;
					sum -= num;
					sIndex--;
				}
				space = 0;
			} else {
				space++;
			}
			j--;
		}
		if (sIndex >= 0) {
			progress = fillDefDep(patterns, arr, i, count, sIndex + 1, sum, index - count, 0) || progress;
		}
		index = SIZE - 1;
		count = 0;
		while (index >= 0) {
			if (arr[i][index].state == Option.CROSS) {
				if (count > 0) {
					break;
				}
			} else {
				count++;
			}
			index--;
		}
		j = 0;
		space = 0;
		sum = p.sum;
		sIndex = 0;
		while (j <= index && sIndex < seq.size()) {
			if (arr[i][j].state == Option.CROSS) {
				int num;
				while (space > 0 && sIndex < seq.size() && (num = seq.get(sIndex)) <= space) {
					space -= num + 1;
					sum -= num;
					sIndex++;
				}
				space = 0;
			} else {
				space++;
			}
			j++;
		}
		if (sIndex < seq.size()) {
			progress = fillDefDep(patterns, arr, i, count, seq.size() - sIndex, sum, index + 1, sIndex) || progress;
		}
		return progress;
	}
	public static boolean fill(List<Pattern> patterns, Square[][] arr, int i, int leftSquare, int rightSquare, int leftPattern, int rightPattern) {
		// Fill squares that would be filled no matter how pattern is arranged
		Pattern p = patterns.get(i);
		if (p.fill == p.sum) {
			return false;
		}
		boolean def = fillDef(patterns, arr, i);
		boolean block = fillBlock(patterns, arr, i);
		boolean end = fillEnd(patterns, arr, i);
		return def || block || end;
	}
	// Try to extend filled blocks and cross out blocks that cannot be reached
	public static boolean extend(List<Pattern> patterns, Square[][] arr, int i, int leftSquare, int rightSquare, int leftPattern, int rightPattern) {
		boolean progress = false;
		Pattern p = patterns.get(i);
		if (p.fill == p.sum && p.cross == SIZE - p.sum) {
			return false;
		}
		List<Integer> seq = p.seq;
		int index = 0;
		int sIndex = 0;
		int count = 0;
		while (index < SIZE && sIndex < seq.size()) {
			if (arr[i][index].state == Option.CROSS) {
				int num = seq.get(sIndex);
				if (num > count) {
					for (int j = 0; j < count; j++) {
						progress = assignPattern(patterns, arr, i, index - 1 - j, Option.CROSS) || progress;
					}
					index++;
					count = 0;
				} else {
					break;
				}

			} else if (arr[i][index].state == Option.FILL) {
				int num = seq.get(sIndex);
				if (num >= count) {
					int fill = 0;
					while (index + fill < SIZE && arr[i][index + fill].state == Option.FILL) {
						fill++;
					}
					while (count + fill < num) {
						progress = assignPattern(patterns, arr, i, index + fill++, Option.FILL) || progress;
					}
					if (count + fill > num) {
						int diff = num - fill;
						for (int j = 0; j < count - diff; j++) {
							progress = assignPattern(patterns, arr, i, index - 1 - diff - j, Option.CROSS) || progress;
						}
					}
					if (fill == num && index + fill < SIZE) {
						progress = assignPattern(patterns, arr, i, index + fill, Option.CROSS) || progress;
						if (index - 1 >= 0) {
							progress = assignPattern(patterns, arr, i, index - 1, Option.CROSS) || progress;
						}
						sIndex++;
						count = 0;
						index += fill + 1;
					} else {
						break;
					}
				} else {
					break;
				}
			} else {
				index++;
				count++;
			}
		}
		index = SIZE - 1;
		sIndex = seq.size() - 1;
		count = 0;
		while (index >= 0 && sIndex >= 0) {
			if (arr[i][index].state == Option.CROSS) {
				int num = seq.get(sIndex);
				if (num > count) {
					for (int j = 0; j < count; j++) {
						progress = assignPattern(patterns, arr, i, index + 1 + j, Option.CROSS) || progress;
					}
					index--;
					count = 0;
				} else {
					break;
				}

			} else if (arr[i][index].state == Option.FILL) {
				int num = seq.get(sIndex);
				if (num >= count) {
					int fill = 0;
					while (index - fill >= 0 && arr[i][index - fill].state == Option.FILL) {
						fill++;
					}
					while (count + fill < num) {
						progress = assignPattern(patterns, arr, i, index - fill++, Option.FILL) || progress;
					}
					if (count + fill > num) {
						int diff = num - fill;
						for (int j = 0; j < count - diff; j++) {
							progress = assignPattern(patterns, arr, i, index + 1 + diff + j, Option.CROSS) || progress;
						}
					}
					if (fill == num && index - fill >= 0) {
						progress = assignPattern(patterns, arr, i, index - fill, Option.CROSS) || progress;
						if (index + 1 < SIZE) {
							progress = assignPattern(patterns, arr, i, index + 1, Option.CROSS) || progress;
						}
						sIndex--;
						count = 0;
						index -= fill + 1;
					} else {
						break;
					}
				} else {
					break;
				}
			} else {
				index--;
				count++;
			}
		}
		return progress;
	}
	public static boolean solve(List<Pattern> patterns, Square[][] arr) {
		boolean progress = false;
		for (int i = 0; i < SIZE * 2; i++) {
			boolean cross = cross(patterns, arr, i, 0, SIZE, 0, patterns.get(i).seq.size());
			boolean fill = fill(patterns, arr, i, 0, SIZE, 0, patterns.get(i).seq.size());
			boolean extend = extend(patterns, arr, i, 0, SIZE, 0, patterns.get(i).seq.size());
			progress = cross || fill || extend || progress;
		}
		return progress;
	}
	public static void main(String[] args) {
		// Parse puzzle
		List<Pattern> patterns = readPuzzle("puzzles/balloon.txt");
		//printPattern(patterns);
		SIZE = patterns.size() / 2;
		// Set up canvas
		PennDraw.setCanvasSize((SIZE + MAX + 4) * 30, (SIZE + MAX + 4) * 30);
		PennDraw.setXscale(-2, SIZE + MAX + 2);
		PennDraw.setYscale(-2, SIZE + MAX + 2);
		//        PennDraw.enableAnimation(100);
		Square[][] grid = new Square[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				grid[i][j] = new Square();
			}
		}
		Square[][] arr = new Square[SIZE * 2][SIZE];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				arr[SIZE - 1 - j][i] = grid[i][j];
				arr[SIZE + i][SIZE - 1 - j] = grid[i][j];
			}
		}
		drawGrid(patterns, grid);	
		//		PennDraw.advance();
		while (true) {
			if (PennDraw.hasNextKeyTyped()) {
				PennDraw.nextKeyTyped();
				solve(patterns, arr);
				drawGrid(patterns, grid);
			}
		}
	}
}
