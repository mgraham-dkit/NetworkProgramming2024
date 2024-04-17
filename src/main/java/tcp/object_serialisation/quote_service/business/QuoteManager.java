package tcp.object_serialisation.quote_service.business;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author michelle
 */
public class QuoteManager {
    // Create a list to store all of the quotation information
    private final ArrayList<Quote> quotations = new ArrayList<Quote>();
    // Create a Random object to allow for selecting quotes at random
    private final Random randomNumGen = new Random();

    /**
     * Constructor to create a new QuoteManager object. 
     * This will create a new QuoteManager with a default set of quotes
     */
    public QuoteManager()
    {
        // Create a set of default quotes and add them to the quotations list.
        bootstrapQuoteList();
    }
    
    /**
     * Add a new quote to the list of quotations. If a quote already exists matching the 
     * quotation text and author, no quote will be added.
     * @param text The text of the quotation
     * @param author The author of the quote
     * @return True if the quote was successfully added, false otherwise.
     */
    public boolean addQuote(String text, String author)
    {
        Quote quote = new Quote(text, author);
        // If there is already a quote in the system matching the supplied
        // quote text and author, return false
        if(quotations.contains(quote)){
            return false;
        }
        // If there is not already a quote in the system, add it to the list
        // and return true
        else{
            quotations.add(quote);
            return true;
        }
    }
    
    /**
     * Remove a quote from the list of quotations. If there is a quote matching the 
     * quotation text and author, it will be removed from the list. 
     * @param text The text of the quotation
     * @param author The author of the quote
     * @return True if the quote was successfully found and removed, false otherwise.
     */
    public boolean removeQuote(String text, String author)
    {
        Quote quote = new Quote(text, author);
        // If there is already a quote in the system matching the supplied
        // quote text and author, remove it and return true
        if(quotations.contains(quote)){
            quotations.remove(quote);
            return true;
        }
        // If there is not already a quote in the system, return false
        else{
            return false;
        }
    }
    
    /**
     * Select a random quote from the list of quotations. A random number is generated
     * between 0 and the number of quotes in the list, and the quote at that position is returned.
     * @return The quote at the randomly selected position in the list.
     */
    public Quote getRandomQuote()
    {
        // Pick a random number between 0 and the size of the list
        int index = randomNumGen.nextInt(quotations.size());
        // Retrieve the quote at the randomly selected index
        return quotations.get(index);
    }
    
    /**
     * Retrieve a quote from the stored list at a specific position. The method 
     * confirms that the supplied position is valid, then returns that quote.
     * @param pos The position of the quote to be retrieved.
     * @return The Quote at the specified position, otherwise null if the position is not valid.
     */
    public Quote getQuote(int pos)
    {
        // Check if the position supplied is valid - i.e. it is within the bounds of the list
        // If it's not, then return null as we cannot supply a quote for that position
        if(pos < 0 || pos >= quotations.size())
        {
            return null;
        }
        // If the position is a valid one, return the quote at the supplied position
        else
        {
            return quotations.get(pos);
        }
    }
    
    /**
     * Search through all stored quotes for any matching a supplied author.
     * @param author The name of the author we want quotes from.
     * @return The ArrayList of all quotes by the specified author.
     */
    public ArrayList<Quote> searchByAuthor(String author)
    {
        // Create a list to store all matching Quotes
        ArrayList<Quote> matches = new ArrayList<Quote>();
        // Check each quote to see if it matches the author we're searching for
        for(Quote q : quotations)
        {
            // If the current quote is by the same author, add it to the matching list
            if(q.getAuthor().equalsIgnoreCase(author))
            {
                matches.add(q);
            }
        }
        // Return the list of all matches (note: this could be empty, but cannot be null)
        return matches;
    }

    public String encode(String quoteDelimiter, String quoteComponentDelimiter){
        if(quotations.isEmpty()){
            return "";
        }
        String encoded = quotations.get(0).encode(quoteComponentDelimiter);
        for(int i = 1; i < quotations.size(); i++){
            Quote q = quotations.get(i);
            String encodedQuote = q.encode(quoteComponentDelimiter);
            encoded += quoteDelimiter + encodedQuote;
        }
        return encoded;
    }
    
    // Display all quotes on the command line
    public void displayQuotes()
    {
        int count = 1;
        for(Quote q: quotations)
        {
            System.out.println(count++ + ") \"" + q.getQuotationText() + "\" - " + q.getAuthor());
        }
    }
    
    // Method to fill the list of quotations with a set of initial quotes
    private void bootstrapQuoteList()
    {
        Quote q1 = new Quote("The way get started is to quit talking & begin doing.", "Walt Disney");
        Quote q2 = new Quote("No one can make you feel inferior without your consent.", "Eleanor Roosevelt");
        Quote q3 = new Quote("Life is 10% what happens to us and 90% how we react to it.", "Dennis P. Kimbro");
        Quote q4 = new Quote("Even if you’re on the right track, you’ll get run over if you just sit there", "Will Rogers");
        quotations.add(q1);
        quotations.add(q2);
        quotations.add(q3);
        quotations.add(q4);
    }

    public static void main(String [] args)
    {
        // Method to test methods
        // Create a QuoteManager to test the methods with
        QuoteManager quoteManager = new QuoteManager();
//        // Display all quotes to the user
//        System.out.println("Display all quotes to the user: ");
//        quoteManager.displayQuotes();
//        // Add a new quote to the system
//        quoteManager.addQuote("We arrive into this world as innocents.", "Klaus");
//        // Retrieve a random quote from the list
//        System.out.println("Random quote: " + quoteManager.getRandomQuote());
//        // Try to retrieve a quote from a position that doesn't exist (this should display false)
//        System.out.println("Quote from position 12: " + quoteManager.getQuote(12));
//        // Remove an existing quote
//        System.out.println("Removed an existing quote: " + quoteManager.removeQuote("Even if you’re on the right track, you’ll get run over if you just sit there", "Will Rogers"));
//        // Remove a quote that doesn't exist.
//        System.out.println("Removed the same quote: " + quoteManager.removeQuote("Even if you’re on the right track, you’ll get run over if you just sit there", "Will Rogers"));
//
//        // Display all quotes to the user
//        System.out.println("Display all quotes to the user: ");
//        quoteManager.displayQuotes();
        String encoded = quoteManager.encode("~~", "%%");
        System.out.println(encoded);
    }
}
