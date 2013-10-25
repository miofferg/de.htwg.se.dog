package de.htwg.se.dog.models;

import java.util.Random;
import java.util.Stack;

public class CardStack {
	
	
	private Stack<Card> cardstack = null;
	private final int CARDS = 4;
	private final int JOKER = 14;
	private final int ZERO = 0;
	
	private Random gen;
	
	public CardStack(int size) {
		cardstack = new Stack<Card>();
		generateStack(size);
	}

	public void generateStack(int size) {
		for (int i = ZERO; i <= (CARDS-1); i++) {
			for (int j = ZERO; j <= size; j++) {
				cardstack.push(new Card(j + 1));
			}
		}
		cardstack.push(new Card(JOKER));
		cardstack.push(new Card(JOKER));
		cardstack.push(new Card(JOKER));
	}

	public Stack<Card> getStack() {
		return this.cardstack;
	}

	public Card dealCard(int start, int range) {
		gen = new Random();
		int index = gen.nextInt(range - start) + start;
		System.out.println("RANDOMINDEX: "+index);
		return cardstack.remove(index);
	}
}