/*  
    Name:         Mary Y. Yu
    Email:        thom01@gmail.com
    Compilation:  javac Eliza.java
    Execution:    java Eliza
    Dependencies: No known dependencies. 
    Description:  This program is a version of the ELIZA, first created by Joseph    
                  Weizenbaum, to demonstratethe superficial communication between  
                  humans and machines. Eliza simulated response usees pattern matching 
                  and substitution methodology, giving the illustion of comprehension. 
                  This version of Eliza emulates a live person responding to questions  
                  regarding Mary Yu's resume.

*/

import java.awt.*;              // Needed for BorderLayout class
import javax.swing.*;           // Needed for Swing classes
import java.awt.event.*;        //Needed for Action Events
import javax.swing.border.*;    // Needed for borders
import java.util.regex.Matcher; // Needed for Regex pattern Matcher
import java.util.regex.Pattern; // Needed for Regex Pattern

public class Eliza extends JFrame
{
    private JPanel areaPanel, fieldPanel, buttonPanel; // area, fields, and button panels
    private JLabel label1, label2, label3;             // Fields and area labels
    private JTextArea conv;                            // Conversation area
    private JScrollPane scroll;                        // scroll area field
    private JTextField response, input;                // Eliza response and User input
    private JButton entButton, exitButton;              // entry accepts input, exit exits program


    // The Constructor. Create the GUI for user input.
    public Eliza()
    {
        //Set fonts and Borders.
        Font myFont = new Font("Veranda", Font.PLAIN, 15);
        Font myFont1 = new Font("Veranda", Font.PLAIN, 12);
        Font myFont3 = new Font("Veranda", Font.BOLD, 15);
        Border inner = BorderFactory.createEmptyBorder(4, 4, 4, 4);
        Border outer = BorderFactory.createLineBorder(Color.BLACK, 1);
        Border combined = BorderFactory.createCompoundBorder(outer, inner);
		
        // Set the title bar text.
        setTitle("Ask Eliza about Mary Yu's Resume.");
               
        //Specify an action for the close button.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add a BorderLayout manager to the content pane.
        setLayout(new BorderLayout(10,10));

        // Create Three panels.
        areaPanel = new JPanel();
        fieldPanel = new JPanel();
        buttonPanel = new JPanel();
        
        // Create and format 3 labels.
        label1 = new JLabel("   ELIZA replies:");
        label1.setFont(myFont);
        label2 = new JLabel("Your question:");
        label2.setFont(myFont3);
     
        // Create and format a text area.
        conv = new JTextArea(12, 69);
        conv.setBackground(Color.WHITE);
        conv.setBorder(combined);
        conv.setFont(myFont);
        conv.setEditable(false);
        scroll = new JScrollPane(conv);
        scroll.setBorder(BorderFactory.createTitledBorder(inner, "Your conversation with ELIZA: ", 
                                    TitledBorder.CENTER, TitledBorder.ABOVE_TOP, myFont, Color.BLACK));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        //Create and format two text fields. 
        response = new JTextField("Hello. I'm Eliza, Mary's digital resume assistant. What is you question?", 61);
        conv.append("Eliza: " + response.getText() + '\n');
        response.setEditable(false);
        response.setBackground(Color.WHITE);
        response.setBorder(combined);
        response.setFont(myFont);
        input = new JTextField("Click here to enter a question. " + 
                                 "Click 'Enter' button below to submit the question.", 61);
        input.setBackground(Color.WHITE);
        input.setBorder(combined);
        input.setFont(myFont);
        input.setEditable(true);     

        // Create and format two buttons.
        entButton = new JButton("Enter");
        entButton.setFont(myFont);
        exitButton = new JButton("Exit");
        exitButton.setFont(myFont);        

        // Add the components to the panels. 
        areaPanel.add(scroll);        
        fieldPanel.add(label1);
        fieldPanel.add(response);                
        fieldPanel.add(label2);
        fieldPanel.add(input);             
        buttonPanel.add(entButton);
        buttonPanel.add(exitButton);

        // Add the panels to the content pane.
        add(areaPanel, BorderLayout.NORTH);
        add(fieldPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
            
        // Pack and display the window with custom format.
        pack();
        setSize(970, 500);
        setResizable(false);
        setFont(myFont1);
        setVisible(true);
        
        // Register the action listeners.
        entButton.addActionListener(new EntButtonListener());
        exitButton.addActionListener(new ExitButtonListener());
        input.addMouseListener(new MyMouseListener());
    }
    
    /*
     Private inner class that handles mouse click events.
     When an event occurred, the input text field for 
     that event cleared and backgound is set to grey.
    */
    private class MyMouseListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
            input.setText (" ");
            input.setBackground(Color.lightGray);
            response.setText (" ");
        }
    }
    
    /* 
     Pivate inner class that handles Ent button click events.
     When an event occurs, "PatMat"method will be called.
     User response field will be filled with this method 
     returned string.
    */
    private class EntButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Calling method PatMat to get user response
            String instr, outstr;
            instr = input.getText();
            outstr = PatMat (instr);
 
            // Display the charges.
            response.setText (outstr);
            conv.append("You: " + input.getText() + '\n');
            conv.append("Eliza: " + response.getText() +'\n');
            input.setText ("Click here to enter next question."); 
            input.setBackground(Color.white); 

        }
    }

    /*
     Private inner class to handle the event when user clicks 
     the "Exit" button. When an event occurrs, program terminates.
    */
    private class ExitButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)    
        {
            System.exit(0);
        }
    }
    
  
    // The main method. 
    public static void main(String[] args)
    {
        // An instance of Eliza.
        new Eliza(); 
    }
    
    // The method for formulating and providing response to user's question.
    public static String PatMat(String in)
    {
        // Declaration and initialization. Note: Arrays simulate database for storing key subjects of a question.
        String[] dB_NA = {"University", "Universities", "college", "colleges", "School", "schools", "Certification", 
                           "certifications","certificate", "certificates", "degree", "degrees", "career"};
        String[] dB_NE = {"responsibilities", "resposibility", "employer[s]", "Job", "jobs", "experience",  
                           "work", "worked", "working","company","career"};
        String[] dB_V = {"start", "career", "change", "Changed", "find"}; 
        String[] dB_Q= {"Mary has B.S.EE (UTA, 1991), MBA (UD, 2002), M.S.EE (UTD, 2004). ",
                        "She's C++, Python, & Java certified (dcccd, 2018).", 
                        "Mary has 21+ yrs network lead engineer experience with Nortel, PrimeCo, & Verizon.", 
                        "Mary believes AI/Robotics is the future. She would be an assert to your comapany.",
                        "Your company is well known for AI/Robotics, Mary is aware because of her interests",
                        "Mary is available to start on Jan 2, 2019",
                        "I did not get that. Are you asking about Mary's eduction?",
                        "I did not get that. Are you asking about Mary's Career?"};
        int resultA = 0, resultE = 0, resultV = 0, response = 0;                        
        String output = "";
        String input = in.trim();
        
        //Call Pattern Matching the first word for the "Five Ws and How"  
        String question = PatMat5W(input);

        // Call Pattern Matching against words in dB_NA array.
        for (int indexA = 0; indexA < dB_NA.length; indexA++)
        {
            int result = PatMatN(input, dB_NA[indexA]);
            resultA = resultA + result; 
        }
     
        //Call Pattern matching against words in dB_NE array
        for (int indexE = 0; indexE < dB_NE.length; indexE++)
        {
            int result = PatMatN(input, dB_NE[indexE]);
            resultE = resultE + result; 
        }
         //Call Pattern matching against words in dB_V array
        for (int indexV = 0; indexV < dB_V.length; indexV++)
        {
            int result = PatMatN(input, dB_V[indexV]);
            resultV = resultV + result; 
        }
    
        //differentiate input keywords to support quick response
        //search.
        if (resultA !=0) response = 1;
        if (resultE !=0) response = 2;
		
        /* First word match ("Five Ws and How") determines the 
           outer the switch case.
           Arrays pattern match determine the inner switch cases.
           Matching response (from dB_Q) generated,           
        */
        
        switch (question.toLowerCase())
        {
            case "who":
                switch (response)
                {
                    case 2:
                        output = dB_Q[2];
                        break;
                    default:
                        output = dB_Q[6];
                        break;
                }
                break;
            case "what":
                switch (response)
                {          
                    case 1: 
                        output = dB_Q[0] + dB_Q[1];
                        break;
                    case 2:
                        if (resultV >0)
                        {
                            output = dB_Q[3];
                        }
                        else
                        {
                            output = dB_Q[2];
                        }
                        break;
                    default:
                        output = dB_Q[6];
                        break;
                }
                break;
            case "where":
                switch (response)
                {
                    case 1: 
                        output = dB_Q[0] + dB_Q[1];
                        break;
                    case 2:
                        output = dB_Q[2];
                        break;
                    default:
                        output = dB_Q[6];
                        break;                   
                }
                break;
            case "when":
                switch (response)
                {            
                    case 1: 
                        output = dB_Q[0] + dB_Q[1];
                        break;
                    case 2:
                        if (resultV >0)
                        {
                            output = dB_Q[5];
                        }
                        else
                        {
                            output = dB_Q[2];
                        }
                        break;
                    default:
                        output = dB_Q[6];
                        break;                   
                }
                break;          
            case "why":
                switch (response)
                {
                    case 2:
                        output = dB_Q[3];
                        break;
                    default:
                        output = dB_Q[7];
                        break;                   
                }
                break;
            case "how":
                switch (response)
                {
                    case 2:
                        output = dB_Q[4];
                        break;
                    default:
                        output = dB_Q[7];
                        break;                   

                }
                break;            
            default:
                output = "Let me make note to have Mary answer this question in person";
        }     
        return output;  // Return outout to calling function.
    }
        //This method uses Regext pattern matching for the first word (five Ws and one h).
    public static String PatMat5W(String input)
    {
        //Pattern Matching for "Five Ws and How".
        Pattern p = Pattern.compile("\\w+\\b");
        Matcher m1 = p.matcher(input);     		
		m1.find();
		String fiveW = (m1.group()).replaceAll("[^\\p{L}\\p{Nd}]+", ""); 
        return fiveW.toLowerCase();
    }
    
    //This method uses Regext Pattern matching for key words.
    public static int PatMatN(String input, String n)
    {
        int count = 0;
        Pattern pho = Pattern.compile(n, Pattern.CASE_INSENSITIVE);
        Matcher mho = pho.matcher(input);     		
		if (mho.find())
		{
            count = 1;
            
        }
        else
        {
            count = 0;
        }
        return (count);

    }            
}
