// Jason Loa and Ruben Pulido
// 11/18/2025
// CardGame class to read cards from a file and manage a linked list of Card objects
// Implements a Blackjack game

//package linkedLists;

import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
//import java.util.ArrayList;
//import java.util.List;



public class CardGame {
	
	private static LinkedList cardList = new LinkedList();  // Deck of cards
	private static Scanner scanner = new Scanner(System.in); // User input

	public static void main(String[] args) {

		// File name to read from
        String fileName = "cards.txt"; // Ensure the file is in the working directory or specify the full path

        // Read the file and create Card objects
        // Use try-with-resources to automatically close the BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Read each line from the file until EOF
            while ((line = br.readLine()) != null) {
                // Split each line by comma to extract card attributes
                String[] details = line.split(","); // Assuming comma-separated values
                // Validate that we have exactly 4 attributes per card
                if (details.length == 4) {
                    // Parse and trim each card detail to remove whitespace
                    String suit = details[0].trim();
                    String name = details[1].trim();
                    int value = Integer.parseInt(details[2].trim());
                    String pic = details[3].trim();

                    // Create a new Card object with parsed details
                    Card card = new Card(suit, name, value, pic);

                    // Add the Card object to the linked list
                    cardList.add(card);
                } else {
                    // Print error message if line doesn't have expected format
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            // Handle file reading errors
            System.err.println("Error reading file: " + e.getMessage());
        }

        // Display all cards that were loaded from the file
        System.out.println("Cards loaded: " + countCards() + " total cards");
        System.out.println();
		
		// Start the blackjack game
		playBlackjack();
		
		scanner.close();

	}//end main

	// Method to count total cards in the deck by traversing without removing
	// Traverses the linked list from first to last, counting each card
	// Does not modify the linked list structure
	private static int countCards() {
		int count = 0;
		Link current = cardList.getFirstLink();  // Get reference to first link
		// Traverse entire linked list
		while (current != null) {
			count++;                // Increment counter for each card found
			current = current.next; // Move to next link in the list
		}
		return count;               // Return total number of cards
	}

	// Main blackjack game method
	private static void playBlackjack() {
		System.out.println("========================================");
		System.out.println("        WELCOME TO BLACKJACK GAME       ");
		System.out.println("========================================");
		System.out.println();
		
		// Game loop - allow multiple rounds
		while (true) {
			// Initialize player and dealer hands for this round using LinkedLists
			LinkedList playerHand = new LinkedList();
			LinkedList dealerHand = new LinkedList();
			
			// Deal initial cards: Player and Dealer each get 2 cards
			System.out.println("Dealing initial cards...");
			playerHand.add(getRandomCard());
			playerHand.add(getRandomCard());
			dealerHand.add(getRandomCard());
			dealerHand.add(getRandomCard());
			
			System.out.println();
			
			// Display dealer's visible card (only first card)
			System.out.println("Dealer's hand: [Hidden] + ");
			Link dealerFirstLink = dealerHand.getFirstLink();
			System.out.println(dealerFirstLink.cardLink);
			
			System.out.println();
			
			// Display player's hand with total value
			System.out.println("Your hand:");
			displayHand(playerHand);
			int playerTotal = calculateHandValue(playerHand);
			System.out.println("Hand Value: " + playerTotal);
			System.out.println();
			
			// Check for blackjack (21 on first 2 cards)
			if (playerTotal == 21) {
				System.out.println("BLACKJACK! You win!");
				System.out.println();
				if (!askPlayAgain()) break;
				continue;
			}
			
			// Player's turn - hit or stand
			boolean playerStanding = false;
			while (!playerStanding) {
				System.out.print("Do you want to (H)it or (S)tand? ");
				String choice = scanner.nextLine().toUpperCase();
				
				if (choice.equals("H")) {
					// Player hits - deal another card
					Card newCard = getRandomCard();
					playerHand.add(newCard);
					System.out.println("You drew: " + newCard);
					
					playerTotal = calculateHandValue(playerHand);
					System.out.println("Your hand value: " + playerTotal);
					System.out.println();
					
					// Check if player busted (exceeded 21)
					if (playerTotal > 21) {
						System.out.println("BUST! You exceeded 21. You lose!");
						playerStanding = true;
						System.out.println();
						if (!askPlayAgain()) return;
						continue;
					}
				} else if (choice.equals("S")) {
					// Player stands
					playerStanding = true;
					System.out.println("You stand with: " + playerTotal);
					System.out.println();
				} else {
					System.out.println("Invalid input. Please enter H or S.");
				}
			}
			
			// Skip dealer turn if player already busted
			if (playerTotal > 21) {
				continue;
			}
			
			// Dealer's turn - dealer must hit on 16 or less, stand on 17+
			System.out.println("Dealer's turn...");
			int dealerTotal = calculateHandValue(dealerHand);
			System.out.println("Dealer's hand value: " + dealerTotal);
			System.out.println();
			
			// Dealer hits while hand value is 16 or less
			while (dealerTotal <= 16) {
				Card dealerCard = getRandomCard();
				dealerHand.add(dealerCard);
				System.out.println("Dealer drew: " + dealerCard);
				dealerTotal = calculateHandValue(dealerHand);
				System.out.println("Dealer's hand value: " + dealerTotal);
				System.out.println();
			}
			
			// Check if dealer busted
			if (dealerTotal > 21) {
				System.out.println("Dealer busted! You win!");
				System.out.println();
				if (!askPlayAgain()) break;
				continue;
			}
			
			// Compare final hands and determine winner
			System.out.println("========== FINAL RESULTS ==========");
			System.out.println("Your hand value: " + playerTotal);
			System.out.println("Dealer's hand value: " + dealerTotal);
			System.out.println();
			
			if (playerTotal > dealerTotal) {
				System.out.println("You win! Your hand is higher!");
			} else if (dealerTotal > playerTotal) {
				System.out.println("Dealer wins! Dealer's hand is higher!");
			} else {
				System.out.println("It's a tie! Push!");
			}
			System.out.println();
			
			// Ask if player wants to play another round
			if (!askPlayAgain()) break;
		}
		
		System.out.println("Thanks for playing!");
	}

	// Helper method to ask if player wants to play again - returns true if yes, false if no
	// Prompts user with (Y)es or (N)o option
	private static boolean askPlayAgain() {
		System.out.print("Do you want to play again? (Y)es or (N)o? ");
		String response = scanner.nextLine().toUpperCase(); // Read and convert to uppercase
		System.out.println();
		return response.equals("Y"); // Return true if user enters Y, false otherwise
	}

	// Helper method to get a random card from the deck without removing it
	// Uses Math.random() to generate random index, then traverses to that card
	// The deck is not modified - cards can be reused across multiple games
	private static Card getRandomCard() {
		// Get total number of cards available in the deck
		int totalCards = countCards();
		if (totalCards == 0) {
			System.out.println("No more cards in deck!");
			return null;
		}
		
		// Generate random index between 0 and totalCards-1
		int randomIndex = (int)(Math.random() * totalCards);
		
		// Traverse to the card at randomIndex
		Link current = cardList.getFirstLink(); // Start at first link
		for (int i = 0; i < randomIndex; i++) {
			current = current.next;            // Move to next link
		}
		
		return current.cardLink;               // Return the card at random index
	}

	// Helper method to calculate the value of a hand
	// Takes into account that Aces can be 1 or 11 (whichever is better)
	// Traverses through the hand's linked list to sum up card values
	private static int calculateHandValue(LinkedList hand) {
		int total = 0;
		int aces = 0;
		
		// Traverse hand and calculate initial value
		Link current = hand.getFirstLink(); // Start at first card in hand
		while (current != null) {
			Card card = current.cardLink;
			
			// Check if card is an Ace
			if (card.getCardName().equalsIgnoreCase("ace")) {
				aces++;               // Count number of aces
				total += 11;          // Initially count ace as 11 (higher value)
			} else {
				total += card.getCardValue(); // Add card's regular value
			}
			current = current.next; // Move to next card
		}
		
		// Adjust for aces if hand value exceeds 21
		// Convert aces from 11 to 1 as needed to avoid busting
		while (total > 21 && aces > 0) {
			total -= 10;            // Convert one ace from 11 to 1 (difference of 10)
			aces--;                 // Decrement ace counter
		}
		
		return total;               // Return final hand value
	}

	// Helper method to display all cards in a hand
	// Traverses the hand's linked list and prints each card
	private static void displayHand(LinkedList hand) {
		Link current = hand.getFirstLink(); // Start at first card in hand
		while (current != null) {
			System.out.println("  - " + current.cardLink); // Print card with formatting
			current = current.next;         // Move to next card
		}
	}

}//end class
