

import com.cogentex.real.api.RealProMgr;   //RealPro Manager
import org.w3c.dom.*;                      //Definition of DOM classes



/**
 * RealPro application sample loading a DSS file and realizing
 * the Deep-Syntactic Structure (DSyntS) it contains.
 */
public class RealPro_DSS_File_App
{
	/* RealPro Manager to be instantiated once during the initialization of the application.*/
	private RealProMgr realproMgr = null;


	/**
 	 * Main function called from command line.
 	 *
 	 * Usage:   java -classpath ... RealPro_DSS_File_App  DSS-filename
 	 */
	public static void main(String args[])
	{
		if (args.length != 1)
		{
			System.out.println("\nUsage: java -classpath ... RealPro_DSS_File_App  DSS-filename\n");
			return;
		}


		RealPro_DSS_File_App app = new RealPro_DSS_File_App();

		String dss_filename = args[0];
       	app.processDSSfile(dss_filename);
    }


	/**
 	 * Class constructor.
 	 */
	public RealPro_DSS_File_App()
	{
		//Create an instance of RealPro Manager with its default configuration
		System.out.println("\nInitializing RealPro (this will take few seconds) ......");

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
 	 * Method used for loading a given DSS file and processing the
 	 * Deep-Syntactic Structure (DSyntS) it contains.
 	 */
	public void processDSSfile( String dss_filename )
	{
	   try
	   {
			//Verify if the RealPro Manager has been initialized correctly.
			if (!realproMgr.isInitialized())
			{
		        System.out.println("Unable to process DSS file; RealPro Manager not initialized; " + realproMgr.getErrorMsg() + "\n");
				return;
			}

			//Load a DSS file containing the DSyntS to realize.
			System.out.println("\nLoading DSS File: " + dss_filename);
			org.w3c.dom.Document doc1 = realproMgr.loadDSSFile(dss_filename);

			//Verify if the loading is successfully
			if (doc1 == null)
			{
				System.out.println("\nLoading failed; " + realproMgr.getErrorMsg());
				return;
			}
			else
			{
				System.out.println("\nLoading completed.\n");
			}

			//Set the tabulation for printing tree structures and sentences
			String tabulation = "   ";

			//Print the document
			System.out.println("\nPrinting document (XML format): \n");
			System.out.println(realproMgr.toXMLString(doc1,true,tabulation));

			System.out.println("\nPrinting document (DSS format): \n");
			System.out.println(realproMgr.toDSSString(doc1,true,tabulation));

			//Retreive all DSyntSs contained in the document
			NodeList list =  doc1.getElementsByTagName(RealProMgr.TAG_DSYNTS);
			System.out.println("\nNumber of DSyntS in document: " + list.getLength());

			//Process all DSyntSs
			for (int i = 0; i < list.getLength(); i++)
			{
					//Process the ith DSyntS
					Element dsynts = (Element)list.item(i);

					System.out.println("\nPrinting DSyntS #" + (i+1) + " (XML format): \n");
					System.out.println(realproMgr.toXMLString(dsynts,true,tabulation));

					System.out.println("\nPrinting DSyntS #" + (i+1) + " (DSS format): \n");
					System.out.println(realproMgr.toDSSString(dsynts,true,tabulation));

					System.out.println("\nRealizing DSyntS...");
					DocumentFragment fragment = realproMgr.realize(dsynts);

					//If the realization is successful, print the resulting sentence structure
					//contained in the fragment, otherwise, print the realization error message
					if (fragment.hasChildNodes())
					{
						 System.out.println("\nResulting sentence structure (XML format):\n");
						 System.out.println(realproMgr.toXMLString(fragment,true,tabulation));

						 System.out.println("\nResulting sentence string:\n");
						 System.out.println(tabulation + realproMgr.getSentenceString());
					}
					else
					{
			         	 System.out.println("\nRealization failed:\n");
			         	 System.out.println(tabulation + realproMgr.getErrorMsg());
					}
					System.out.println();
			}


	      	return;
    	 }
    	 catch (Exception e)
    	 {
      		System.out.println("\nException encountered: " + e.toString() + "\n");
      		return;
    	 }
      }


}