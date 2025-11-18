// Jason Loa
// 11/18/2025
// Card class for playing card attributes

//package cardGame;

public class Card {

    // Instance variables
    private String cardSuit;
    private String cardName;
    private int cardValue;
    private String cardPic;

    // Default constructor
    public Card() {
        cardName = "King";
        cardValue = 10; // Corrected this from a string to an int
    }

    // Non-default constructor
    public Card(String name, int value) {
        cardName = name;
        cardValue = value;
    }

    // Constructor with all attributes
    public Card(String suit, String name, int value, String pic) {
        cardSuit = suit;
        cardName = name;
        cardValue = value;
        cardPic = pic;
    }

    // Getter methods
    public String getCardName() {
        return cardName;
    }

    public int getCardValue() {
        return cardValue;
    }

    public String getCardSuit() {
        return cardSuit;
    }

    public String getCardPic() {
        return cardPic;
    }

    // Setter methods
    public void setCardName(String newCardName) {
        cardName = newCardName;
    }

    public void setCardValue(int newCardValue) {
        cardValue = newCardValue;
    }

    public void setCardSuit(String newCardSuit) {
        cardSuit = newCardSuit;
    }

    public void setCardPic(String newCardPic) {
        cardPic = newCardPic;
    }

    // Method to get combined card values
    public String getCardValues() {
        return cardName + " " + cardValue;
    }

    // Equals method to compare two cards
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Same object reference
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Null or different class
        }
        Card otherCard = (Card) obj;
        return cardValue == otherCard.cardValue && 
               cardName.equals(otherCard.cardName) && 
               cardSuit.equals(otherCard.cardSuit) && 
               cardPic.equals(otherCard.cardPic);
    }

    // toString method for printing the card
    @Override
    public String toString() {
        return cardName + " of " + cardSuit + " (Value: " + cardValue + ", Picture: " + cardPic + ")";
    }
}
