package entity.collection;

import java.util.ArrayList;
import java.util.List;

import utils.EnumUtils.Types;

public class Beatmap {
	private List<List<Pair<Integer, Types>>> data;
	
	public Beatmap() {
		this.data = new ArrayList<>();
	}
	
	public Beatmap(List<List<Pair<Integer, Types>>> data) {
		this.data = data;
	}

	public List<List<Pair<Integer, Types>>> getData() {
		return data;
	}
	
	public void add(List<Pair<Integer, Types>> note) {
		this.data.add(note);
	}
	
	public List<Pair<Integer, Types>> get(int index) {
		return this.data.get(index);
	}
	
	public int size() {
		return this.data.size();
	}
}
