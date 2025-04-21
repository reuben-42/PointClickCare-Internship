import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BlackjackGraphics extends JPanel implements ActionListener
{
	//Fields/Global Variables
	private static Random rnd;  //Random number generator
	private static DecimalFormat df;  //This is used to format numbers
	private static JFrame frame;  //This will be used to create the frame
	
	private static String[] cards = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};  //All card values (Suit doesn't matter)
	
	private static ArrayList<String> dealer, player, bot1, bot2;  //ArrayLists to store each person's cards
	
	private static Timer bgSwitch;	//Timer to switch from the Title Screen background to the Game background
	private static Timer timer1, timer2, timer3;  //These timers are for transitions between each part of the game
	
	private static boolean player_game_over, bot1_game_over, bot2_game_over;  //Boolean variables that indicate if that person/player is done playing for that round
	private static boolean showTitle;  //Boolean variable to indicate when to switch backgrounds
	
	private static double player_bet, bot1_bet, bot2_bet, dealer_bet;  //Each person's/player's bets
	
	private static int player_value, bot1_value, bot2_value, dealer_value;  //Each person's/player's total value of all their cards in their hand
	private static int dealer_cards;  //This keeps track of the number of cards the dealer has
	private static int player_round_won, bot1_round_won, bot2_round_won;  //This keeps track of how many rounds each person/player has won
	
	private static ImageIcon bg;  //Title Screen background
	private static ImageIcon table;  //Game background
	
	public static void main(String[] args) 
	{
		new BlackjackGraphics();  //Call constructor
	}
	
	//Constructor
	public BlackjackGraphics()
	{
		//Initialize all Fields/Global Variables
		rnd = new Random();
		df = new DecimalFormat("$##,##0.00");
		
		dealer = new ArrayList<String>();
		player = new ArrayList<String>();
		bot1 = new ArrayList<String>();
		bot2 = new ArrayList<String>();
		
		//Add 2 cards, at random, to each person's/player's hands
		for (int i = 1; i <= 2; i++)
		{
			player.add(cards[rnd.nextInt(0, cards.length)]);
			bot1.add(cards[rnd.nextInt(0, cards.length)]);
			bot2.add(cards[rnd.nextInt(0, cards.length)]);
			dealer.add(cards[rnd.nextInt(0, cards.length)]);
		}
		
		player_game_over = false;	//Set them to False because in the beginning, all the people/players didn't win or lose yet, so it's not Game Over for them
		bot1_game_over = false;
		bot2_game_over = false;
		
		dealer_bet = 0;  //The dealer starts with $0.00
		dealer_cards = 0;
		
		player_round_won = 0;
		bot1_round_won = 0;
		bot2_round_won = 0;
		
		bgSwitch = new Timer(3000, this);  //Run for 3000 milliseconds (3 seconds)
		bg = new ImageIcon("Images/background.png");
		showTitle = true;
		
		table = new ImageIcon("Images/table.png");
		
		timer1 = new Timer(1000, this);  //Run for 1000 milliseconds (1 second)
		timer2 = new Timer(1000, this);  //Run for 1000 milliseconds (1 second)
		timer3 = new Timer(1000, this);  //Run for 1000 milliseconds (1 second)
		
		
		//Create the JFrame
		frame = new JFrame();

		frame.setContentPane(this);  //Add the JPanel to the frame

		frame.setTitle("BlackJack");  //Title of the frame/window
		frame.setSize(bg.getIconWidth(), bg.getIconHeight());  //Size of the frame/window (same size as the Title Screen background image)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //If the person clicks the "X" button in the top right corner, immediately close the frame/window
		frame.setResizable(false);  //Do NOT allow the user to resize the frame/window
		frame.setLocationRelativeTo(null);  //Centered on screen

		frame.setVisible(true);   //Show the frame
			//When "setVisible" runs, it automatically calls on the "paint" method
		
		
		//Start timer
		bgSwitch.start();  
	}
	
	//Paint method
	public void paint(Graphics g)
	{
		//Repaint the frame and its components
		super.paint(g);

		//Declare and Initialize a Graphics2D Object
		Graphics2D g2 = (Graphics2D) g;
		
		if (showTitle)
		{
			g2.drawImage(bg.getImage(), 0, 0, this);  //Show the Title Screen background image
		}
		else
		{
			g2.drawImage(table.getImage(), 0, 0, this);  //Show the Game background image
			
			if (dealer_cards == 1)
			{
				//Show the dealer's first card
				g2.drawImage(new ImageIcon("Images/Cards/" + dealer.get(0) + ".png").getImage(), 200, 10, this);
			}
			else if (dealer_cards == 2)
			{
				//Show both of the dealer's cards
				g2.drawImage(new ImageIcon("Images/Cards/" + dealer.get(0) + ".png").getImage(), 200, 10, this);
				g2.drawImage(new ImageIcon("Images/Cards/" + dealer.get(1) + ".png").getImage(), 350, 10, this);
			}
			else if (dealer_cards == 3)
			{
				//Show the dealer's 3 cards if the dealer picked up another one
				g2.drawImage(new ImageIcon("Images/Cards/" + dealer.get(0) + ".png").getImage(), 150, 10, this);
				g2.drawImage(new ImageIcon("Images/Cards/" + dealer.get(1) + ".png").getImage(), 300, 10, this);
				g2.drawImage(new ImageIcon("Images/Cards/" + dealer.get(2) + ".png").getImage(), 450, 10, this);
			}
			
		}
	}

	//The Timers method
	public void actionPerformed(ActionEvent e)   //Will run every time each timer reaches the end of its interval
	{
		if (e.getSource() == bgSwitch)
		{
			showTitle = false;  //Stop showing the Title Screen background
			bgSwitch.stop();
			
			frame.setSize(table.getIconWidth(), table.getIconHeight());  //Set the size to the new background image
			frame.setLocationRelativeTo(null);  //Centered on screen
			
			dealer_cards++;  //Increase to 1
			timer1.start();	
		}
		
		if (e.getSource() == timer1)
		{
			timer1.stop();
			
			//While Loop to continually ask the player how much they want to bet until a correct input is entered
			while (true)
			{
				try
				{
					player_bet = Double.parseDouble(JOptionPane.showInputDialog(null, "How much do you want to bet?", "Input Dialog", JOptionPane.QUESTION_MESSAGE));
					
					if (player_bet > 0)
						break;
					
					else  //If the value is a negative number or 0, ask the user again
						JOptionPane.showMessageDialog(null, "Please enter a number greater than 0", "Message Dialog", JOptionPane.ERROR_MESSAGE);
				}
				catch (Exception error)  //If there's any error, ask the user again
				{
					JOptionPane.showMessageDialog(null, "Please enter ONLY a number", "Message Dialog", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			bot1_bet = rnd.nextDouble(1, 10001);  // Random number from 1 - 10,000
			bot2_bet = rnd.nextDouble(1, 10001);
			JOptionPane.showMessageDialog(null, "You bet " + df.format(player_bet) + "\nBot 1 bet " + df.format(bot1_bet) + "\nBot 2 bet " + df.format(bot2_bet) + "\n\nThe dealer's first card is shown on screen", "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
			
			//Show player's cards
			JOptionPane.showMessageDialog(null, "Your first card is a " + player.get(0), "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/Cards/" + player.get(0) + ".png"));
			JOptionPane.showMessageDialog(null, "Your second card is a " + player.get(1), "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/Cards/" + player.get(1) + ".png"));

			//Calculate the total value of each person's/player's cards
			player_value = value(player);
			bot1_value = value(bot1);
			bot2_value = value(bot2);
						
			//If any person/player's total value is 21, they win this round
			if (player_value == 21) 
			{
				player_bet += player_bet * 1.5;	  //The player earns 1.5 times its original bet
				player_game_over = true;   
				player_round_won++;
				
				JOptionPane.showMessageDialog(null, "Both your cards total to 21! You won!\nYour bet value is now worth " + df.format(player_bet), "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/won.png"));
			}
			else if (bot1_value == 21)
			{
				bot1_bet += bot1_bet * 1.5;
				bot1_game_over = true; 
				bot1_round_won++;
				
				JOptionPane.showMessageDialog(null, "Bot 1's cards total to 21! Its bet is now worth " + df.format(bot1_bet), "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/won.png"));
			}
			else if (bot2_value == 21)
			{
				bot2_bet += bot2_bet * 1.5;
				bot2_game_over = true;   
				bot2_round_won++;
				
				JOptionPane.showMessageDialog(null, "Bot 2's cards total to 21! Its bet is now worth " + df.format(bot2_bet), "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/won.png"));
			}
			
			
			//Player's Turn
			if(!player_game_over)   //Ensure the user is still participating in this round
			{
				String[] buttons = {"HIT", "STAY"};  //Buttons array for the JOptionPane below

				//While Loop to continually ask the user if they want to pick up more cards
				while(true)
				{
					//Ask the user if they want to HIT or STAY (each button pressed returns an INDEX VALUE)
					int index = JOptionPane.showOptionDialog(null, "HIT (pick up another card)\nOR\nSTAY (go to the next person)", "Option Dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, null);
					
					if (index == 0)  //If the index value is 0, the HIT button was pressed
					{
						player.add(cards[rnd.nextInt(0, cards.length)]);
						JOptionPane.showMessageDialog(null, "Your card is a " + player.get(player.size() - 1), "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/Cards/" + player.get(player.size() - 1) + ".png"));
					}
					else if (index == 1)  //If the index value is 1, the STAY button was pressed
					{
						break;
					}
					else  //Wrong input, ask again
					{
						JOptionPane.showMessageDialog(null, "Please click only HIT or STAY", "Message Dialog", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				player_value = value(player);  	//Calculate the NEW total value of the user's cards
				
				if (player_value > 21)  //The user BUSTS if the total value is greater than 21
				{
					dealer_bet += player_bet;	//Add the player's bet to the dealer's bet
					player_bet = 0;
					player_game_over = true;
					
					JOptionPane.showMessageDialog(null, "Your cards total " + player_value + "\nYou BUSTED\nYour entire bet has been given to the Dealer", "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/lost.png"));
				}
				else if (player_value == 21)  //The user WINS if their total is equal to 21
				{
					player_bet += player_bet * 2.5;		//The player earns 2.5 times its original bet
					player_game_over = true;
					player_round_won++;
					
					JOptionPane.showMessageDialog(null, "Your cards total " + player_value + "\nYou WON\nYour bet is now worth " + df.format(player_bet), "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/won.png"));
				}
				else  //If their total is less than 21, tell them their cards' total value
				{
					JOptionPane.showMessageDialog(null, "Your cards total " + player_value, "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
			
			//Bot 1's Turn
			if(!bot1_game_over)  //Ensure Bot 1 is still participating in this round
			{
				//While Loop to continually generate random numbers
				while(true)
				{
					int num = rnd.nextInt(1, 6);  //Range is 1-5
					
					if (num % 2 != 0)  //Odd number = HIT
					{
						bot1.add(cards[rnd.nextInt(0, cards.length)]);	  //More chance for Bot 1 to HIT than STAY
					}
					else if (num % 2 == 0)  //Even number = STAY
					{
						break;
					}
				}
				
				bot1_value = value(bot1);  //Calculate the NEW total value of Bot 1's cards
				
				if (bot1_value > 21)  //Bot 1 BUSTS
				{
					dealer_bet += bot1_bet;  //Add Bot 1's bet to the dealer's bet
					bot1_bet = 0;
					bot1_game_over = true;
					
					JOptionPane.showMessageDialog(null, "Bot 1's cards total " + bot1_value + "\nBot 1 BUSTED\nBot 1's entire bet has been given to the Dealer", "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/lost.png"));
				}
				else if (bot1_value == 21)  //Bot 2 WINS
				{
					bot1_bet += bot1_bet * 2.5;	  //Bot 1 earns 2.5 times its original bet
					bot1_game_over = true;	
					bot1_round_won++;
					
					JOptionPane.showMessageDialog(null, "Bot 1's cards total " + bot1_value + "\nBot 1 WON\nBot 1's bet is now worth " + df.format(bot1_bet), "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/won.png"));
				}
				else  //If its total is less than 21, tell the user Bot 1's total value of its cards
				{
					JOptionPane.showMessageDialog(null, "Bot 1's cards total " + bot1_value, "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
			
			//Bot 2's Turn
			if(!bot2_game_over)  //Ensure Bot 2 is still participating in this round
			{
				//While Loop to continually generate random numbers
				while(true)
				{
					int num = rnd.nextInt(1, 6);  //Range is 1-5
					
					if (num % 2 != 0)  //Odd number = HIT
					{
						bot2.add(cards[rnd.nextInt(0, cards.length)]);		//More chance for Bot 2 to HIT than STAY
					}
					else if (num % 2 == 0)  //Even number = STAY
					{
						break;
					}
				}
				
				bot2_value = value(bot2);  //Calculate the NEW total value of Bot 2's cards
				
				if (bot2_value > 21)  //Bot 2 BUSTS
				{
					dealer_bet += bot2_bet;   //Add Bot 2's bet to the dealer's bet
					bot2_bet = 0;
					bot2_game_over = true;
					
					JOptionPane.showMessageDialog(null, "Bot 2's cards total " + bot2_value + "\nBot 2 BUSTED\nBot 2's entire bet has been given to the Dealer", "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/lost.png"));
				}
				else if (bot2_value == 21)  //Bot 2 WINS
				{
					bot2_bet += bot2_bet * 2.5;   //Bot 2 earns 2.5 times its original bet
					bot2_game_over = true;	
					bot2_round_won++;
					
					JOptionPane.showMessageDialog(null, "Bot 2's cards total " + bot2_value + "\nBot 2 WON\nBot 2's bet is now worth " + df.format(bot2_bet), "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/won.png"));
				}
				else  //If its total is less than 21, tell the user Bot 2's total value of its cards
				{
					JOptionPane.showMessageDialog(null, "Bot 2's cards total " + bot2_value, "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
			//Tell the user how much money the dealer now has
			JOptionPane.showMessageDialog(null, "The dealer's bet is now worth " + df.format(dealer_bet), "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
			
			//Show the dealer's second card
			dealer_cards++;  //Increase to 2
			timer2.start();
		}
		
		if (e.getSource() == timer2)
		{
			timer2.stop();
			
			//Dealer's Turn
			dealer_value = value(dealer);  //Calculate the total value of the Dealer's cards
			
			if (dealer_value <= 16)  //If the dealer's cards add up to 16 or below, pick up another card
			{
				JOptionPane.showMessageDialog(null, "The dealer's total card value is " + dealer_value + "\nThe dealer will pick up another card", "Message Dialog", JOptionPane.INFORMATION_MESSAGE);

				dealer.add(cards[rnd.nextInt(0, cards.length)]);
				dealer_value = value(dealer);  //Calculate the NEW total value of the Dealer's cards
				
				dealer_cards++;  //Increase to 3
			}
			
			timer3.start();
		}
		
		if (e.getSource() == timer3)
		{
			timer3.stop();
			
			//Tell the user the Dealer's new total card value
			JOptionPane.showMessageDialog(null, "The dealer's total card value is " + dealer_value, "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
			
			if (dealer_value > 21)  //The dealer BUSTS
			{
				//All players still in the round will receive twice their original bet
				if (!player_game_over)      
				{
					player_bet += player_bet * 2.0;
					player_round_won++;
				}
				
				else if (!bot1_game_over)
				{
					bot1_bet += bot1_bet * 2.0;
					bot1_round_won++;
				}
				
				else if (!bot2_game_over)
				{
					bot2_bet += bot2_bet * 2.0;
					bot2_round_won++;
				}
				
				JOptionPane.showMessageDialog(null, "The dealer BUSTS\nAll players who are still in the round will receive twice their original bet", "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/won.png"));
			}
			
			else  //If the dealer does NOT BUST (The Dealer's total card value is less than or equal to 21)
			{
				if (!player_game_over)
				{
					if (player_value > dealer_value)
					{
						//Player wins twice their bet
						player_bet += player_bet * 2.0;
						player_round_won++;

						JOptionPane.showMessageDialog(null, "The player's total card value is " + player_value + "\nThe dealer's total card value is " + dealer_value + "\nYou win twice your bet!", "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/won.png"));
					}
					else if (player_value < dealer_value)
					{
						//Player loses all their bet and it goes to the Dealer
						dealer_bet += player_bet;
						player_bet = 0;

						JOptionPane.showMessageDialog(null, "The player's total card value is " + player_value + "\nThe dealer's total card value is " + dealer_value + "\nYou lost your entire bet", "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/lost.png"));
					}
					else  //If they tie
					{
						JOptionPane.showMessageDialog(null, "The player's total card value is " + player_value + "\nThe dealer's total card value is " + dealer_value + "\nIt's a TIE", "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				
				if (!bot1_game_over)
				{
					if (bot1_value > dealer_value)
					{
						//Bot 1 wins twice their bet
						bot1_bet += bot1_bet * 2.0;
						bot1_round_won++;

						JOptionPane.showMessageDialog(null, "Bot 1's total card value is " + bot1_value + "\nThe dealer's total card value is " + dealer_value + "\nBot 1 won twice its bet!", "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/won.png"));
					}
					else if (bot1_value < dealer_value)
					{
						//Bot 1 loses all their bet and it goes to the Dealer
						dealer_bet += bot1_bet;
						bot1_bet = 0;
						
						JOptionPane.showMessageDialog(null, "Bot 1's total card value is " + bot1_value + "\nThe dealer's total card value is " + dealer_value + "\nBot 1 lost its entire bet", "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/lost.png"));
					}
					else  //If they tie
					{
						JOptionPane.showMessageDialog(null, "Bot 1's total card value is " + bot1_value + "\nThe dealer's total card value is " + dealer_value + "\nIt's a TIE", "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				
				if (!bot2_game_over)
				{
					if (bot2_value > dealer_value)
					{
						//Bot 2 wins twice their bet
						bot2_bet += bot2_bet * 2.0;
						bot2_round_won++;
						
						JOptionPane.showMessageDialog(null, "Bot 2's total card value is " + bot2_value + "\nThe dealer's total card value is " + dealer_value + "\nBot 2 won twice its bet!", "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/won.png"));
					}
					else if (bot2_value < dealer_value)
					{
						//Bot 2 loses all their bet and it goes to the Dealer
						dealer_bet += bot2_bet;
						bot2_bet = 0;
						
						JOptionPane.showMessageDialog(null, "Bot 2's total card value is " + bot2_value + "\nThe dealer's total card value is " + dealer_value + "\nBot 2 lost its entire bet", "Message Dialog", JOptionPane.PLAIN_MESSAGE, new ImageIcon("Images/lost.png"));
					}
					else  //If they tie
					{
						JOptionPane.showMessageDialog(null, "Bot 2's total card value is " + bot2_value + "\nThe dealer's total card value is " + dealer_value + "\nIt's a TIE", "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			
			//Show how much money each person/player has earned from the round
			JOptionPane.showMessageDialog(null, "Your bet is now worth " + df.format(player_bet) + "\nBot 1's bet is now worth " + df.format(bot1_bet) + "\nBot 2's bet is now worth " + df.format(bot2_bet), "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
			JOptionPane.showMessageDialog(null, "The dealer accumulated " + df.format(dealer_bet) + " from this round", "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
		
			//Show how many rounds each player has won
			JOptionPane.showMessageDialog(null, "You have won " + player_round_won + " round(s)\nBot 1 has won " + bot1_round_won + " round(s)\nBot 2 has won " + bot2_round_won + " round(s)", "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
			
			
			//While Loop to continually ask the user if they want to do another round until a correct input has been entered
			while(true)
			{
				int option = JOptionPane.showConfirmDialog(null, "Do you want to play another round?", "Confirm Dialog", JOptionPane.YES_NO_OPTION);

				if (option == JOptionPane.YES_OPTION)  //If the user clicks YES, set everything to their default values
				{
					reset();  
					break; 
				}
				else  //If the user clicks NO or the "X" button in the top right corner, ask if they want to exit the game
				{
					int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Dialog", JOptionPane.YES_NO_OPTION);

					if (choice == JOptionPane.YES_OPTION)
					{
						System.exit(0);  //Kill the program
					}
					
					//** If the user clicks NO or the "X" button in the top right corner, the user will be asked if they want to play another round again, due to the While Loop
				}
			}
		}
		
		repaint();  //This wipes everything from the JFrame/window and calls on the "paint" method to quickly draw everything back
	}
	
	
	public static int value(ArrayList<String> person)
	{
		int value = 0;  //The total value of all the cards in the ArrayList
		int a = 0;  //The number of A's there are
		
		//Check if the person's cards add up to 21
		for (int i = 0; i < person.size(); i++)
		{
			//If the card is a Letter, its value is 10
			if (person.get(i).equals("J") || person.get(i).equals("Q") || person.get(i).equals("K"))
			{
				value += 10;
			}
			
			//If the card is a number, its value is the number of the card
			else if (Character.isDigit(person.get(i).charAt(0)))  //Gets the first character (Index 0) in the String and checks if it's a digit/number
			{
				if (person.get(i).charAt(0) == '1')  //If the first digit is a 1, the card is a 10
				{
					value += 10;
				}
				else
				{
					value += Integer.parseInt(person.get(i));
				}
			}
			
			//If the card is an "A", don't add its value to the total value yet, as it has 2 different values
			else if (person.get(i).equals("A"))
			{
				a++;  //Increase the number of A's by 1
			}
		}
		
		//For Loop that will run the same number of times as there are A's
		for (int i = 1; i <= a; i++)
		{
			if (value + 11 > 21)  //If the current total value exceeds 21 when you add 11, only add 1 to the total value (can't go above 21 or else it's a BUST)
			{
				value += 1;
			}
			else	//If the current total value is less than or equal to 21 when you add 11, then add 11 to the total value (as long as the total value is NOT above 21)
			{
				value += 11;
			}
		}
		
		return value;
	}
	
	
	public static void reset()
	{
		//Reset everything to their default values
		dealer.clear();
		player.clear();
		bot1.clear();
		bot2.clear();
		
		//Add 2 cards, at random, to each person's/player's hands
		for (int i = 1; i <= 2; i++)
		{
			player.add(cards[rnd.nextInt(0, cards.length)]);
			bot1.add(cards[rnd.nextInt(0, cards.length)]);
			bot2.add(cards[rnd.nextInt(0, cards.length)]);
			dealer.add(cards[rnd.nextInt(0, cards.length)]);
		}
		
		player_game_over = false;	//Set them to False because in the beginning, all the people/players didn't win or lose yet, so it's not Game Over for them
		bot1_game_over = false;
		bot2_game_over = false;
		
		player_value = 0;
		bot1_value = 0;
		bot2_value = 0;
		
		player_bet = 0;
		bot1_bet = 0;
		bot2_bet = 0;
		
		dealer_bet = 0;  //The dealer starts with $0.00
		dealer_cards = 0;
		
		showTitle = true;
		
		frame.setSize(bg.getIconWidth(), bg.getIconHeight());  //Set the size to the new background image
		frame.setLocationRelativeTo(null);  //Centered on screen
		
		//Start the timer, which will start the game
		bgSwitch.start();
	}

}
