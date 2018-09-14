/****************************************************************
 **  Copyright 1996 by DTAI Incorporated, All Rights Reserved  **
 ****************************************************************/

import java.util.*;
import java.awt.*;
import dtai.gwt.*;
import java.net.*;
import java11.awt.event.*;
//import dtai.print.*; // Comment out this import if you don't have the
                     // JustPrint developers license

public class TestGadget extends GadgetApplet
    implements ActionListener, AdjustmentListener, ItemListener,
    CellListener, MouseListener, TreeListener, FocusListener {

    TextFieldGadget textfield1;
    Hashtable helpHash = new Hashtable();
    PanelGadget textFieldCard;
    PanelGadget propertiesCard;
    PanelGadget coverImagePanel;

    ButtonGadget button2;
    ButtonGadget button3;
    ButtonGadget button4;

    MultiColumnListGadget grid;

    TabPaneGadget tabs;
    ComboBoxGadget combo;
    ChoiceGadget choice;
    int n = 0;

    PopupMenuGadget popup;

    ButtonGadget testButton;

    public static void main( String args[] ) {

		// this is only used if executing outside of a web browser

        AppletManager manager = new AppletManager( new TestGadget(), 100, 100, 600, 500, args ); // OLD WAY
    }

    public void init() {

        try { // just to catch image URL errors

            setLayout( new GadgetCardLayout() );

//////////////////
// press F1 with key focus or mouse over gadget with help
//////////////////

            setGadgetHelp( new GadgetDialogHelp( helpHash ) );
            helpHash.put( "#text", "You should try to enter some text here.  Use the keyboard." );

/* This is one way to give your app a background image...

            ImagePanelGadget coverImagePanel = new ImagePanelGadget(getImage(new URL( getDocumentBase(), "dtailogo.jpg" )));
            coverImagePanel.setLayout(new GadgetBorderLayout());
            add("Center",coverImagePanel); // assumes parent using GadgetBorderLayout

   BUT we'll do it this way, so that we can cover the image with a solid color... */

//////////////////
// Cover the image (below) unless background is transparent
//////////////////

            coverImagePanel = new PanelGadget();
            coverImagePanel.setLayout(new GadgetBorderLayout());
            coverImagePanel.setBackground(Color.cyan.darker());
            add("Panel",coverImagePanel);

//////////////////
// ImageGadget for background (if color is transparent)
//////////////////

            ImageGadget backgroundImage = new ImageGadget(
                getImage(new URL( getDocumentBase(), "dtailogo.jpg" )));
            backgroundImage.setStretched(true);
            //backgroundImage.setTiled(true);
            add("background",backgroundImage);

//////////////////
// MenuBarGadget
//////////////////

            MenuBarGadget menubar = new MenuBarGadget();
            coverImagePanel.add("North", menubar);
            MenuGadget menu = menubar.add("File");
            MenuItemGadget item = menu.add("New");
            menu.add("Open");
            menu.add("Save");
            menu.add("Close");
            menu.addSeparator();
            menu.add("Print").addActionListener(this);
            menu.addSeparator();
            menu.add("Quit").addActionListener(this);

            menu = menubar.add("Edit");
            menu.add("Cut");
            menu.add("Copy");
            menu.add("Clear");
            menu.add("Paste");

            menu = menubar.add("View");
            menu.add("Text Area");
            menu.add("Table");
            menu.add("Grid");
            menu.add("Radio Buttons");

//////////////////
// TabPaneGadget
//////////////////

            tabs = new TabPaneGadget();
            coverImagePanel.add("Center", tabs);


//////////////////
// TextFields, MaskedTextFields, etc.
//////////////////

            textFieldCard = new PanelGadget();
            textFieldCard.setLayout( new GadgetBorderLayout( 3, 3 ) );
            tabs.add( "Start", textFieldCard);

            textfield1 = new TextFieldGadget( "Enter some text here for now and on. " );
            textfield1.setTip( "Press F1 for more help" );
            textfield1.setHelpId( "#text" );
            textfield1.addActionListener( this );
            textfield1.setHorizAlign( TextFieldGadget.HORIZ_ALIGN_RIGHT );
            textFieldCard.add( "South", textfield1 );

            TextFieldGadget text = new MaskedTextFieldGadget( "999 99 99A", "120 00 00N" );
            text.setSelectedBackground( Color.yellow );
            text.addActionListener( this );
            text.setBackground(Color.red.darker());
            textFieldCard.add( "North", text );

//////////////////
// more TextFields, Choice, and Combo Gadgets
//////////////////

            PanelGadget fields = new PanelGadget();
            fields.setLayout( new GadgetGridLayout(0,2) );

            Gadget prevGadget;
            Gadget thisGadget;

            fields.add(new LabelGadget("Prompt 1:", LabelGadget.HORIZ_ALIGN_RIGHT) );
            fields.add(thisGadget = new TextFieldGadget("initial text 1"));
            thisGadget.setBackground(Color.yellow);
            prevGadget = thisGadget;
            fields.add(new LabelGadget("Prompt 2:", LabelGadget.HORIZ_ALIGN_RIGHT) );
            fields.add(thisGadget = new TextFieldGadget("initial text 2"));
            thisGadget.setBackground(Color.orange);
            thisGadget.setForeground(Color.red);
            prevGadget = thisGadget;
            fields.add(new LabelGadget("Prompt 3:", LabelGadget.HORIZ_ALIGN_RIGHT) );

            combo = new ComboBoxGadget(4);
            thisGadget = combo;
            prevGadget = thisGadget;
            combo.add("");
            combo.add("Apple");
            combo.add("Bean");
            combo.add("Berry");
            combo.add("Black Olive");
            combo.add("Cranberry");
            combo.add("Dill Pickle");
            combo.add("Egg");
            combo.add("Fish");
            combo.add("Gum");
            combo.add("Honey Mustard Shrimp");
            combo.add("Ice Cream");
            combo.setSorted(true);
            fields.add(combo);

            fields.add(new LabelGadget("Prompt 4:", LabelGadget.HORIZ_ALIGN_RIGHT) );
            fields.add(thisGadget = new TextFieldGadget("initial text 4"));
            prevGadget = thisGadget;
            fields.add(new LabelGadget("Prompt 5:", LabelGadget.HORIZ_ALIGN_RIGHT) );

            choice = new ChoiceGadget(3);
            thisGadget = choice;
            prevGadget = thisGadget;
            choice.setNextFocusGadget(combo); // You can specify keyboard focus order
            choice.add("Apple");
            choice.add("Bean");
            choice.add("Berry");
            choice.add("Black Olive");
            choice.add("Cranberry");
            choice.add("Dill Pickle");
            choice.add("Egg");
            choice.add("Fish");
            choice.add("Gum");
            choice.add("Honey Mustard Shrimp");
            choice.add("Ice Cream");
            choice.setSorted(true);
            choice.select(3);
            fields.add(choice);

            fields.add(new LabelGadget("Prompt 6:", LabelGadget.HORIZ_ALIGN_RIGHT) );
            fields.add(thisGadget = new TextFieldGadget("initial text 6"));
            prevGadget = thisGadget;

            PanelGadget holdStuff = new PanelGadget();
            holdStuff.setLayout( new GadgetBorderLayout() );
            textFieldCard.add( "Center", holdStuff );

            PanelGadget holdFields = new PanelGadget();
            holdFields.setLayout(new GadgetBorderLayout());
            holdFields.add("North", fields);

            holdStuff.add( "Center", holdFields );

//////////////////
// Simple ListGadget
//////////////////

            ListGadget list = new ListGadget();
            list.add("This");
            list.add("is");
            list.add("a");
            list.add("test",1); // insert at position 1
            for (int i = 0; i < 200; i++) {
                list.add("Element "+i);
            }
            holdStuff.add( "East", list );

//////////////////
// a ButtonGadget, with text and image, and it's "action" changes the font for the application
//////////////////

            testButton = new ButtonGadget( "Change\n\nFont", getImage(new URL( getDocumentBase(), "play.gif" )) );
            testButton.setAsDefault();
            testButton.addActionListener( this );
            holdStuff.add("West",testButton);

//////////////////
// A mouse press on this label pops up a PopupMenuGadget
//////////////////

            LabelGadget label = new LabelGadget( "Right click HERE (or hold meta key and click) for menu" );
            label.addMouseListener(this);
            holdStuff.add("South",label);

//////////////////
// PopupMenuGadget
//////////////////

            popup = new PopupMenuGadget();
            popup.add(new MenuItemGadget("Open..."));
            popup.add(new MenuItemGadget("Close..."));
            popup.add(new SeparatorGadget());
            popup.add(new MenuItemGadget("Lift..."));
            popup.add(new MenuItemGadget("Drop..."));

            PanelGadget buttonCard = new PanelGadget();
            buttonCard.setLayout( new GadgetBorderLayout( 3, 3 ) );

            PanelGadget colorCard = new PanelGadget();
            colorCard.setLayout( new GadgetBorderLayout( 3, 3 ) );
            tabs.add( "Color", colorCard);

            PanelGadget togglePanel = new PanelGadget();
            colorCard.add( "Center", togglePanel );
            togglePanel.setLayout( new GadgetGridLayout( 0, 1 ) );

            CheckboxGadgetGroup group = new CheckboxGadgetGroup();

            CheckboxGadget cb;
            togglePanel.add( cb = new CheckboxGadget( "Transparent", true, group ) );
            cb.setTip( "Make the coverImagePanel transparent" );
            cb.addItemListener( this );
            togglePanel.add( cb = new CheckboxGadget( "Red", true, group ) );
            cb.setTip( "Make the coverImagePanel red" );
            cb.addItemListener( this );
            togglePanel.add( cb = new CheckboxGadget( "Yellow", false, group ) );
            cb.setTip( "Make the coverImagePanel yellow" );
            cb.addItemListener( this );
            togglePanel.add( cb = new CheckboxGadget( "Green", false, group ) );
            cb.setTip( "Make the coverImagePanel green" );
            cb.addItemListener( this );
            cb.setEnabled(false);
            togglePanel.add( cb = new CheckboxGadget( "giant purple people eater right here now folks", true, group ) );
            cb.setTip( "Make the coverImagePanel purple" );
            cb.addItemListener( this );
            togglePanel.add( cb = new CheckboxGadget( "Black", true, group ) );
            cb.setTip( "Make the coverImagePanel black" );
            cb.addItemListener( this );
            togglePanel.add( cb = new CheckboxGadget( "White", true, group ) );
            cb.setTip( "Make the coverImagePanel white" );
            cb.addItemListener( this );
            togglePanel.add( cb = new CheckboxGadget( "Light Gray", true, group ) );
            cb.setTip( "Make the coverImagePanel light gray" );
            cb.addItemListener( this );
            cb.setState(true);

            ComponentShell compshell = new ComponentShell( new Button( "AWT" ) );
            compshell.setBackground( Color.pink.darker() );
            colorCard.add( "East", compshell );

            ScrollbarGadget scroll =
    			new ScrollbarGadget( ScrollbarGadget.HORIZONTAL,
    			                     10, 50, 0, 100 );
            scroll.addAdjustmentListener( this );
            colorCard.add( "South", scroll );

//////////////////
// More buttons
//////////////////

            tabs.add( "Buttons", buttonCard);

            button2 = new ButtonGadget( getImage(new URL( getDocumentBase(), "play.gif" )) );
            button2.setBlackToForeground(true);
            button2.addActionListener( this );

            buttonCard.add( "East", button2 );
            button2.setAsDefault(); // Displays with bold border, and most carriage
                                    // returns and double-clicks will invoke the default

            button3 =
    			new ButtonGadget( "You can't press me, I'm disabled." ,
    			                   getImage(new URL( getDocumentBase(), "pause.gif" )) );
            button3.setBlackToForeground(true);
            button3.addActionListener( this );
            button3.setEnabled( false );

            buttonCard.add( "Center", button3 );

//////////////////
// Multi-line text area
//////////////////

            PanelGadget textAreaCard = new PanelGadget();
            textAreaCard.setLayout( new GadgetBorderLayout( 3, 3 ) );
            tabs.add( "Text", textAreaCard);

            TextAreaGadget textArea = new TextAreaGadget("This is a really big line from the heart of what I call nothingness to me and"+
                "\n\n\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it."+
                "\nI will test it.");
            textAreaCard.add( "Center", textArea );
            textArea.addFocusListener(this);
            // Some options we could have set include...
            //textArea.setEditable(false);
            //textArea.setTextBackground(Color.blue);
            //textArea.setWordWrap(false);

//////////////////
// TableGadget...sort by pressing column headers,
// drag column header borders or double-click to resize
//////////////////

            PanelGadget tableCard = new PanelGadget();
            tableCard.setLayout( new GadgetBorderLayout( 3, 3 ) );
            tabs.add( "Table", tableCard);

            TableGadget table = new TableGadget( 10 );
            table.addActionListener( this );
            table.addItemListener( this );

            table.setMultipleMode( true );
            tableCard.add( "Center", table );
            table.addColumn( "First" );
            table.addColumn( "Last" );
            table.addColumn( "Home Phone" );
            table.addColumn( "Other" );
            table.addColumn( "Salary" );
            int row = 0;
            int column = 0;
            for ( int i = 0; i < 20; i++ ) {
                table.setCell( row, column++, "Rich" );
                table.setCell( row, column++, "Kadel" );
                table.setCell( row, column++, "555-1234" );
                table.setCell( row, column++, "555-9876" );
                table.setCell( row, column++, "$20,000", new Integer( 20000 ) );
                row++; column = 0;
                table.setCell( row, column++, "John" );
                table.setCell( row, column++, "Marsh" );
                table.setCell( row, column++, "555-2345" );
                table.setCell( row, column++, "555-8765" );
                table.setCell( row, column++, "$30,000", new Integer( 30000 ) );
                row++; column = 0;
                table.setCell( row, column++, "Mike" );
                table.setCell( row, column++, "Glasgow" );
                table.setCell( row, column++, "555-3456" );
                table.setCell( row, column++, "555-7654" );
                table.setCell( row, column++, "$100,000", new Integer( 100000 ) );
                row++; column = 0;
                table.setCell( row, column++, "Dale" );
                table.setCell( row, column++, "Anderson" );
                table.setCell( row, column++, "555-4567" );
                table.setCell( row, column++, "555-6543" );
                table.setCell( row, column++, "$150,000", new Integer( 150000 ) );
                row++; column = 0;
                table.setCell( row, column++, "Dave" );
                table.setCell( row, column++, "Meagher" );
                table.setCell( row, column++, "555-5678" );
                table.setCell( row, column++, "555-5432" );
                table.setCell( row, column++, "$1", new Integer( 1 ) );
                row++; column = 0;
                table.setCell( row, column++, "Rich" );
                table.setCell( row, column++, "Yumul" );
                table.setCell( row, column++, "555-6789" );
                table.setCell( row, column++, "555-4321" );
                table.setCell( row, column++, "$20,000", new Integer( 20000 ) );
                row++; column = 0;
                table.setCell( row, column++, "John" );
                table.setCell( row, column++, "Smith" );
                table.setCell( row, column++, "555-7890" );
                table.setCell( row, column++, "555-3210" );
                table.setCell( row, column++, "$20,000", new Integer( 20000 ) );
                row++; column = 0;
                table.setCell( row, column++, "Mike" );
                table.setCell( row, column++, "Douglas" );
                table.setCell( row, column++, "555-8901" );
                table.setCell( row, column++, "555-2109" );
                table.setCell( row, column++, "$20,000", new Integer( 20000 ) );
                row++; column = 0;
                table.setCell( row, column++, "Hubert" );
                table.setCell( row, column++, "Snuffelufegousenheimer" );
                table.setCell( row, column++, "555-9012" );
                table.setCell( row, column++, "555-1098" );
                table.setCell( row, column++, "$5", new Integer( 5 ) );
                row++; column = 0;
                table.setCell( row, column++, "Fryin" );
                table.setCell( row, column++, "Chicken" );
                table.setCell( row, column++, "555-0123" );
                table.setCell( row, column++, "555-0987" );
                table.setCell( row, column++, "$500", new Integer( 500 ) );
                row++; column = 0;
            }

//////////////////
// Multi-column list, or grid (e.g., for a spreadsheet)
//////////////////

            grid = new MultiColumnListGadget();
            tabs.add("Grid", grid);
            grid.setShowGrid(true);
            grid.addCellListener( this );
            grid.setMultipleMode( true );
            //grid.setSelectedBackground(Color.black); // this would be more like Excel
            grid.addColumn( "First" );
            grid.addColumn( "Last" );
            grid.addColumn( "Home Phone" );
            grid.addColumn( "Other" );
            grid.addColumn( "Salary" );
            row = 0;
            column = 0;
            for ( int i = 0; i < 1/*100*/; i++ ) {
                grid.setCell( row, column++, "Rich" );
                grid.setCell( row, column++, "Kadel" );
                grid.setCell( row, column++, "555-1234" );
                grid.setCell( row, column++, "555-9876" );
                grid.setCell( row, column++, "$20,000" );
                row++; column = 0;
                grid.setCell( row, column++, "John" );
                grid.setCell( row, column++, "Marsh" );
                grid.setCell( row, column++, "555-2345" );
                grid.setCell( row, column++, "555-9876" );
                grid.setCell( row, column++, "$30,000" );
                row++; column = 0;
                grid.setCell( row, column++, "Mike" );
                grid.setCell( row, column++, "Glasgow" );
                grid.setCell( row, column++, "555-3456" );
                grid.setCell( row, column++, "555-9876" );
                grid.setCell( row, column++, "$100,000" );
                row++; column = 0;
                grid.setCell( row, column++, "Dale" );
                grid.setCell( row, column++, "Anderson" );
                grid.setCell( row, column++, "555-4567" );
                grid.setCell( row, column++, "555-9876" );
                grid.setCell( row, column++, "$150,000" );
                row++; column = 0;
                grid.setCell( row, column++, "Dave" );
                grid.setCell( row, column++, "Meagher" );
                grid.setCell( row, column++, "555-6789" );
                grid.setCell( row, column++, "555-9876" );
                grid.setCell( row, column++, "$1" );
                row++; column = 0;
                grid.setCell( row, column++, "Rich" );
                grid.setCell( row, column++, "Yumul" );
                grid.setCell( row, column++, "555-7890" );
                grid.setCell( row, column++, "555-9876" );
                grid.setCell( row, column++, "$20,000" );
                row++; column = 0;
                grid.setCell( row, column++, "John" );
                grid.setCell( row, column++, "Smith" );
                grid.setCell( row, column++, "555-8901" );
                grid.setCell( row, column++, "555-9876" );
                grid.setCell( row, column++, "$20,000" );
                row++; column = 0;
                grid.setCell( row, column++, "Mike" );
                grid.setCell( row, column++, "Douglas" );
                grid.setCell( row, column++, "555-9012" );
                grid.setCell( row, column++, "555-9876" );
                grid.setCell( row, column++, "$20,000" );
                row++; column = 0;
                grid.setCell( row, column++, "Hubert" );
                grid.setCell( row, column++, "Snuffelufegousenheimer" );
                grid.setCell( row, column++, "555-0123" );
                grid.setCell( row, column++, "555-9876" );
                grid.setCell( row, column++, "$5" );
                row++; column = 0;
                grid.setCell( row, column++, "Fryin" );
                grid.setCell( row, column++, "Chicken" );
                grid.setCell( row, column++, "555-1234" );
                grid.setCell( row, column++, "555-9876" );
                grid.setCell( row, column++, "$500" );
                row++; column = 0;
            }

    //        tabs.setTab( "Second" ); // just for fun, and to make sure images are loaded right away

            TreeViewGadget tree = new TreeViewGadget();
            tree.addTreeListener(this);
            tree.addActionListener(this);
            tabs.add("Tree",tree);

            TreeFolder root = new TreeFolder("C:");
            TreeFolder folder = root.addFolder("Windows");
            folder.setExpanded(true);
            folder.addItem("config.sys");
            folder.addItem("autoexec.bat");
            folder = folder.addFolder("System");
            folder.addItem("regedit.exe");
            folder.addItem("registry.dat");
            for (int i=0; i < 100; i++) {
                folder.addItem("cache."+i);
            }
            folder = root.addFolder("kadel");
            folder.setExpanded(true);
            folder.add(new FolderItem("Mail",getImage(new URL( getDocumentBase(), "play.gif" ))));
            folder.add(new FolderItem("signature.txt",getImage(new URL( getDocumentBase(), "pause.gif" ))));
            folder = folder.addFolder("src");
            folder.setExpanded(true);
            folder.addItem("orgchart.java");
            folder.addItem("ecop.java");
            root.setExpanded(true);
            tree.setRoot(root);

//////////////////
// Properties Control
//////////////////
            propertiesCard = new PanelGadget();
            propertiesCard.setLayout( new GadgetBorderLayout( 3, 3 ) );
            tabs.add( "Props", propertiesCard);
            DataSheet sheet = new DataSheet();
            String testString[] = {"This is a test", "of the String array control"};
            sheet.add(new BooleanControl("Boolean Control"));
            sheet.add(new ColorControl("Color Control"));
            sheet.add(new DoubleControl("Double Control"));
            sheet.add(new FontControl("Font Control"));
            sheet.add(new IntegerControl("Integer Control"));
            sheet.add(new PasswordControl("Password Control"));
            sheet.add(new StringArrayControl("String Array Control", testString));
            sheet.add(new StringControl("String Control"));
            sheet.add(new URLControl("URL Control", new URL("http:")));
            propertiesCard.add("Center", sheet);

        }

        catch ( MalformedURLException mue ) {
            dtai.util.ShowUser.error( mue, "Bad URL for image" );
        }

    }

    private void print(Gadget printButton) {
        // Comment out the contents of this method
        // if you don't have a JustPrint developers license
        /*
	    GadgetShell shell = getMainPanel().getShell();
	    if (shell != null) {
	        Gadget gadget = shell.getGadget();
	        if (gadget != null) {
	            synchronized(gadget.getTreeLock()) {
            		try {
                        Printer printer = new LocalPrinter();
            			Dimension size = gadget.getSize();
            			int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
            			Dimension printerResolution = printer.getPrinterResolution();
            			double factor = printerResolution.width / (double) screenRes;
            			printer.setScaleFactor(factor);
            			printer.setFontScale(0.12667);
            			//using Toolkit, the screenresolution is given as 120 dpi, resulting
            			//in a default fontScale of 0.2  (120 / 600), which is too big.
            			//As the observable resolution of MY monitor is 76 or so, I have set
            			//the font scale factor to (76/600 =  0.12666666666).  ccp

            			Graphics g = printer.getGraphics();

            			shell.getGadgetTreeLock();
            			shell.setGraphicsOverride(g, (int)(size.width*factor), (int)(size.height*factor));
            			gadget.fontChanged();
            			shell.validateAll();

            			GadgetGraphics gg = gadget.getGadgetGraphics();
            			gg.setPaintingAll(true);
            			gadget.update(gg);
            			printer.endDoc();

            			shell.setGraphicsOverride(null, 0, 0);
            			gadget.setSize(size);
            			gadget.fontChanged();
            			gadget.validate();
                    }
                    catch (UnsatisfiedLinkError ule) {
                        dtai.util.ShowUser.error(ule,
                            "Sorry. The JustPrint printing software must "+
                            "be installed locally "+
                            "on your PC for printing to work.");
                        printButton.setEnabled(false);
                    }
                    catch (Exception e) {
                        dtai.util.ShowUser.error(e,
                            "Sorry. The JustPrint printing software that is "+
                            "installed on your PC "+
                            "does not work in this environment "+
                            "(browser or JDK problem?).");
                        printButton.setEnabled(false);
                    }
                }
            }
        }
        */
    }

    public void actionPerformed( ActionEvent action ) {

        if ( action.getSource() instanceof MenuItemGadget ) {
            if (((MenuItemGadget)action.getSource()).getLabel().equals("Quit")) {
                System.exit(0);
            } else if (((MenuItemGadget)action.getSource()).getLabel().equals("Print")) {
                print((MenuItemGadget)action.getSource());
            }
        }
        else if ( action.getSource() instanceof ButtonGadget &&
                  action.getSource().toString().equals("Change\n\nFont") ) {
            System.err.println( "got a button press" );
            choice.select(n);
            combo.select(n);
            n++;
            if ( n >= 4 ) { n = 0; }
            System.err.println("setting font");
            PanelGadget coverImagePanel = getMainPanel();
            if ( n % 2 == 0 ) {
                coverImagePanel.setFont( new Font("TimesRoman", Font.BOLD, 24 ) );
            }
            else {
                coverImagePanel.setFont( new Font("Helvetica", Font.BOLD, 14 ) );
            }
        }
        else if ( action.getSource() instanceof TextFieldGadget ) {
            textfield1.selectAll();
            testButton.setEnabled(!testButton.isEnabled());
            TextFieldGadget text = (TextFieldGadget)action.getSource();
            System.err.println( "accepted text \""+text.getText()+"\"");
        }
        else if ( action.getSource() instanceof TableGadget ) {
            TableGadget table = (TableGadget)action.getSource();
            System.err.println( "double-clicked \""+table.getSelectedItem()+"\"");
        }
        else if ( action.getSource() instanceof TreeViewGadget ) {
            TreeViewGadget tree = (TreeViewGadget)action.getSource();
            System.err.println( "double-clicked \""+tree.getSelectedTreeItem()+"\"");
        }
        else {
            System.err.println(action.getSource() );
        }
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        System.err.println(e.getValue() );
    }

    public void cellStateChanged(CellEvent e) {
        if ( e.getStateChange() == CellEvent.SELECTED ) {
            CellSelectable grid = e.getCellSelectable();
            int count = grid.getSelectedCount();
            GadgetCell cells[] = grid.getSelectedCells();
            System.err.println("Selected Grid Cells:");
            for ( int i = 0; i < count; i++ ) {
                System.err.println("  "+cells[i].getRow()+","+cells[i].getColumn()+": "+
                                   cells[i].getGadget());
            }
        }
    }

    public void treeStateChanged(TreeEvent e) {
        if ( e.getStateChange() == TreeEvent.SELECTED ) {
            TreeSelectable tree = e.getTreeSelectable();
            System.err.println("selected tree item "+tree.getSelectedTreeItem());
            System.err.println("meta="+e.isMetaDown()+" ctrl="+e.isControlDown()+" shift="+e.isShiftDown());
            if (e.isMetaDown()) {
                popup.showMenu(e.getX(),e.getY(),(Gadget)e.getSource());
            }
        }
    }

    public void treeNodeExpanded(TreeEvent e) {
        System.err.println("node expanded");
    }

    public void treeNodeCondensed(TreeEvent e) {
    }

    public void itemStateChanged(ItemEvent e) {
        if ( e.getStateChange() == ItemEvent.SELECTED ) {
            String colorName = e.getItemSelectable().getSelectedItems()[0];
            System.err.println("color \""+colorName+"\"");
            if ( colorName.equals( "Red" ) ) {
                setBackground( Color.red.darker() );
                grid.selectCell(0,0);
            }
            else if ( colorName.equals( "Green" ) ) {
                setBackground( Color.green.darker() );
                grid.selectCell(0,1);
            }
            else if ( colorName.equals( "Yellow" ) ) {
                setBackground( Color.yellow.darker() );
                grid.selectCell(1,0);
            }
            else if ( colorName.equals( "giant purple people eater right here now folks" ) ) {
                setBackground( Color.magenta.darker() );
                grid.selectCell(1,1);
            }
            else if ( colorName.equals( "Black" ) ) {
                setBackground( Color.black.darker() );
            }
            else if ( colorName.equals( "White" ) ) {
                setBackground( Color.white );
            }
            else if ( colorName.equals( "Cyan" ) ) {
                setBackground( Color.cyan.darker() );
            }
            else if ( colorName.equals( "Blue" ) ) {
                setBackground( Color.blue.darker() );
            }
            else if ( colorName.equals( "Black" ) ) {
                setBackground( Color.black );
            }
            else if ( colorName.equals( "Light Gray" ) ) {
                setBackground( Color.lightGray );
            }
            if ( colorName.equals( "Transparent" ) ) {
                setBackground( Color.white );
                coverImagePanel.setBackground( Gadget.transparent );
            }
            else {
                coverImagePanel.setBackground( getBackground() );
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (e.isMetaDown()) {
            popup.showMenu(e.getX(),e.getY(),(Gadget)e.getSource());
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void focusGained(FocusEvent e) {
        System.out.println("Focus Gained");
    }

    public void focusLost(FocusEvent e) {
        System.out.println("Focus Lost");
    }

}
