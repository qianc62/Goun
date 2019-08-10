
import com.cogentex.real.api.RealProMgr;   //RealPro Manager
import org.w3c.dom.*;                      //Definition of DOM classes



/**
 * RealPro application sample loading an XML file and realizing
 * the Deep-Syntactic Structures (DSyntSs) it contains.
 */
public class RealPro_XML_File_App
{
	/* RealPro Manager to be instantiated once during the initialization of the application.*/
	private RealProMgr realproMgr = null;


	/**
 	 * Main function called from command line.
 	 *
 	 * Usage:   java -classpath ... RealPro_XML_File_App  XML-filename
 	 */
	public static void main(String args[])
	{
		if (args.length != 1)
		{
			System.out.println("\nUsage: java -classpath ... RealPro_XML_File_App  XML-filename\n");
			return;
		}


		RealPro_XML_File_App app = new RealPro_XML_File_App();

		String xml_filename = args[0];
       	app.processXMLfile(xml_filename);
    }


	/**
 	 * Class constructor.
 	 */
	public RealPro_XML_File_App()
	{
		//Create an instance of RealPro Manager with its default configuration
		System.out.println("\nInitializing RealPro (this will take few seconds) ...");

		realproMgr = new RealProMgr();

		//Verify if the RealPro Manager has been initialized correctly.
		if (!realproMgr.isInitialized())
		{
            System.out.println("Initializing RealPro ... failed; " + realproMgr.getErrorMsg() + "\n");
		}
		else
		{
			System.out.println("Initializing RealPro ... completed.");
		}
	}




	/**
 	 * Method used for loading a given XML file and processing the
 	 * Deep-Syntactic Structures (DSyntSs) it contains.
 	 */
	public void processXMLfile( String xml_filename )
	{
	   try
	   {
			//Verify if the RealPro Manager has been initialized correctly.
			if (!realproMgr.isInitialized())
			{
            	System.out.println("Unable to process XML file; RealPro Manager not initialized; " + realproMgr.getErrorMsg() + "\n");
				return;
			}


			//Load a XML file containing some DSyntSs to realize.
			System.out.println("\nLoading XML File: " + xml_filename);
			org.w3c.dom.Document doc1 = realproMgr.loadXMLFile(xml_filename);


			//Verify if the loading is successfully
			if (doc1 == null)
			{
				System.out.println("\nLoading failed; " + realproMgr.getErrorMsg());
				return;
			}


			//Set the tabulation for printing tree structures and sentences
			String tabulation = "   ";


			//Print the document
			System.out.println("\nPrinting document (XML format): \n");
			System.out.println(realproMgr.toXMLString(doc1,true,tabulation));


			//Retreive all DSyntSs contained in the document.
			//This list will be modified dynamically
			//while the document will be modified below
			org.w3c.dom.NodeList list =  doc1.getElementsByTagName(RealProMgr.TAG_DSYNTS);


			//Verify if there are DSyntS to process
			int numberOfDSyntS = list.getLength();
			if (numberOfDSyntS == 0)
			{
				System.out.println("\nNo DSyntS to realize in the document.");
				return;
			}


			//Process all DSyntSs in the list from last to the first.
			//Backward processing is needed because the document associated
			//with the list used in the loop is modified dynamically
			int nb = list.getLength();
			for (int i = nb-1; i >= 0; i--)
			{
					//Process the ith DSyntS
					org.w3c.dom.Element dsynts = (Element)list.item(i);

					System.out.println("\nPrinting DSyntS #" + (i+1) + " (XML format): \n");
					System.out.println(realproMgr.toXMLString(dsynts,true,tabulation));

					System.out.println("\nRealizing DSyntS...");
					org.w3c.dom.DocumentFragment fragment = realproMgr.realize(dsynts);

					//If the realization is successful, print the resulting sentence structure
					//contained in the fragment and replace the DSyntS with the realized DSyntS
					//in the document. Otherwise, print the realization error message.
					if (fragment.hasChildNodes())
					{
						 System.out.println("\nResulting sentence structure:\n");
						 System.out.println(realproMgr.toXMLString(fragment,true,tabulation));

						 System.out.println("\nResulting sentence string:\n");
						 System.out.println(tabulation + realproMgr.getSentenceString());

						 System.out.println("\nReplacing DSyntS with realized DSyntS in document...\n");
						 org.w3c.dom.Node dsyntsParent = dsynts.getParentNode();
						 dsyntsParent.replaceChild(fragment,dsynts);
					}
					else
					{
			         	 System.out.println("\nRealization failed:\n");
			         	 System.out.println(tabulation + realproMgr.getErrorMsg());
					}
					System.out.println();
			}


			//Print the modified document after replacing all DSyntSs with
			//realized DSyntSs
			System.out.println("\nPrinting document with realized DSyntSs (XML format): \n");
			System.out.println(realproMgr.toXMLString(doc1,true,tabulation));


	      	return;
    	 }
    	 catch (Exception e)
    	 {
      		System.out.println("\nException encountered: " + e.toString() + "\n");
      		return;
    	 }
      }


}