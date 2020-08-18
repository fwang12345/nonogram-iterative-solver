import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Pattern {
	public List<Integer> seq = new ArrayList<Integer>();
	public List<Integer> sort = new LinkedList<Integer>();
	public List<Integer> next = new ArrayList<Integer>();
	public List<Integer> prev = new ArrayList<Integer>();
	public int sum;
	public int fill;
	public int cross;
	public int max = Integer.MIN_VALUE;
	public int min = Integer.MAX_VALUE;
	public int max(int leftPattern, int rightPattern) {
		int max = -1;
		for (int i = leftPattern; i < rightPattern; i++) {
			int num = seq.get(i);
			if (num > max) {
				max = num;
			}
		}
		return max;
	}
	public int min(int leftPattern, int rightPattern) {
		int min = Integer.MAX_VALUE;
		for (int i = leftPattern; i < rightPattern; i++) {
			int num = seq.get(i);
			if (num < min) {
				min = num;
			}
		}
		return min;
	}
	public void set(List<Integer> seq) {
		for (Integer i : seq) {
			this.seq.add(i);
		}
		int max = -1;
		for (int i = seq.size() - 1; i >= 0; i--) {
			next.add(0, max);
			if (seq.get(i) > max) {
				max = seq.get(i);
			}
		}
		max = -1;
		for (int i = 0; i < seq.size(); i++) {
			prev.add(i, max);
			if (seq.get(i) > max) {
				max = seq.get(i);
			}
		}
		Collections.sort(seq, Collections.reverseOrder());
		for (Integer i : seq) {
			this.sort.add(i);
		}
	}
}

