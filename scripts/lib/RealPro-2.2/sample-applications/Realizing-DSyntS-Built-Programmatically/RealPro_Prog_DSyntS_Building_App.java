
import com.cogentex.real.api.RealProMgr;     //RealPro Manager
import org.w3c.dom.*;                        //Definition of DOM classes
import org.apache.xerces.dom.DocumentImpl;   //Implementation of DOM Document



/**
 * RealPro application for realizing DSyntSs built programmatically.
 */
public class RealPro_Prog_DSyntS_Building_App
{
	/* RealPro Manager to be instantiated once during the initialization of the application.*/
	private RealProMgr realproMgr = null;


	/**
 	 * Main function called from command line.  No paramater are required.
 	 *
 	 * Usage:   java -classpath ... RealPro_Prog_DSyntS_Building_App
 	 */
	public static void main(String args[])
	{
		RealPro_Prog_DSyntS_Building_App app = new RealPro_Prog_DSyntS_Building_App();
       	app.buildDocument();
    }


	/**
 	 * Class constructor.
 	 */
	public RealPro_Prog_DSyntS_Building_App()
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
 	 * Method used for building and processing Deep-Syntactic Structures (DSyntSs).
 	 */
	public void buildDocument( )
	{
	   try
	   {
			//Verify if the RealPro Manager has been initialized correctly.
			if (!realproMgr.isInitialized())
			{
	            System.out.println("\nRealPro Manager not initialized; " + realproMgr.getErrorMsg() + "\n");
				return;
			}

			//Create an instance of document used for creating the DSyntS*/
			System.out.println("\nCreating document instance ...");
			org.w3c.dom.Document document = (Document) new org.apache.xerces.dom.DocumentImpl();


			//Process DSyntS #1
			System.out.println("\n--------------------------------------------\n");
			System.out.println("\nBuilding DSyntS #1 ...");
			Element dsynts1 = buildDSyntS1(document);
			processDSyntS(dsynts1);


			//Process DSyntS #2
			System.out.println("\n--------------------------------------------\n");
			System.out.println("\nBuilding DSyntS #2 ...");
			Element dsynts2 = buildDSyntS2(document);
			processDSyntS(dsynts2);

	      	return;
    	 }
    	 catch (Exception e)
    	 {
      		System.out.println("\nException encountered: " + e.toString() + "\n");
      		return;
    	 }
      }




	/**
 	 * Method used for processing a Deep-Syntactic Structures (DSyntS).
 	 */
	public void processDSyntS( org.w3c.dom.Element dsynts )
	{
	   	try
	   	{
			System.out.println("\nProcessing DSyntS ...");

			//Set tabulation for printing resulting trees and sentences
			String tabulation = "  ";

			//Print DSyntS
			System.out.println("\nPrinting DSyntS (XML format) ...\n");
			System.out.println(realproMgr.toXMLString(dsynts,true,tabulation));

			System.out.println("\nPrinting DSyntS (DSS format) ...\n");
			System.out.println(realproMgr.toDSSString(dsynts,true,tabulation));


			//Check validity of DSyntS
			System.out.println("\nChecking validity ...");
			if (!realproMgr.validDSyntS(dsynts))
			{
				System.out.println("\nDSyntS invalid; " + realproMgr.getErrorMsg());
				return;
			}


			//Realize DSyntS
			System.out.println("\nRealizing DSyntS ...");
			org.w3c.dom.DocumentFragment fragmentSentence = realproMgr.realize(dsynts);

			//If the realization is successful, print the resulting sentence structure
			//contained in the fragment and replace the DSyntS with the realized DSyntS
			//in the document. Otherwise, print the realization error message.
			if (fragmentSentence.hasChildNodes())
			{
				System.out.println("\nResulting sentence structure:\n");
				System.out.println(realproMgr.toXMLString(fragmentSentence,true,tabulation));

				System.out.println("\nResulting sentence string:\n");
				System.out.println(tabulation + realproMgr.getSentenceString());
			}
			else
			{
				System.out.println("\nRealization failed:\n");
				System.out.println(tabulation + realproMgr.getErrorMsg());
			}
			System.out.println();

	      	return;
    	 }
    	 catch (Exception e)
    	 {
      		System.out.println("\nException encountered: " + e.toString() + "\n");
      		return;
    	 }
     }



	/**
 	 * Method used to build a DSyntS corresponding to:
 	 *
     * 		 John loves Mary.
     *
     *   <dsynts>
     *       <dsyntnode lexeme=love class=verb>
     *          <dsyntnode lexeme=John class=proper_noun rel=I/>
     *          <dsyntnode lexeme=Mary class=proper_noun rel=II/>
     *       </dsyntnode>
     *   </dsynts>
     *
     *
	 * The parameter <i>document</i> is used as a factory for creating the
	 * DSyntS elements.
 	 */
	private org.w3c.dom.Element buildDSyntS1( org.w3c.dom.Document document )
	{
		try
		{
			//Create DSyntS
			Element dsynts = document.createElement("dsynts");

			Element governor = document.createElement("dsyntnode");
			governor.setAttribute("lexeme", "love");

			governor.setAttribute("class", "verb");

			Element depI = document.createElement("dsyntnode");
			depI.setAttribute("lexeme", "John");
			depI.setAttribute("class", "proper_noun");

			Element depII = document.createElement("dsyntnode");
			depII.setAttribute("lexeme", "Mary");
			depII.setAttribute("class", "proper_noun");

			//Define Dependency Relationships
			dsynts.appendChild(governor);

			governor.appendChild(depI);
			depI.setAttribute("rel", "I");

			governor.appendChild(depII);
			depII.setAttribute("rel","II");

			//Return the DSyntS
			return dsynts;
		}
		catch ( Exception e )
		{
			return null;
		}
	}





	/**
 	 * Method used to build a DSyntS corresponding to:
 	 *
     * 		John loves <A HREF="url" target=new>Mary</A>.
     *
     *      <dsynts>
     *         <dsyntnode lexeme=love class=verb>
     *            <dsyntnode lexeme=John class=proper_noun rel=I/>
     *            <B>
     *            	 <A HREF="url" target=new>
     *                	<dsyntnode lexeme=Mary class=proper_noun rel=II/>
     *            	 </A>
     *            </B>
     *         </dsyntnode>
     *      </dsynts>
     *
     * The parameter <i>document</i> is used as a factory for creating the
	 * DSyntS elements.
 	 */
	private org.w3c.dom.Element buildDSyntS2( org.w3c.dom.Document document )
	{
		try
		{
			//Define Nodes
			Element dsynts   = document.createElement("dsynts");

			Element governor = document.createElement("dsyntnode");
			governor.setAttribute("lexeme", "love");
			governor.setAttribute("class", "verb");

			Element depI = document.createElement("dsyntnode");
			depI.setAttribute("lexeme", "John");
			depI.setAttribute("class", "proper_noun");

			//TAG B(old)
			Element sgmlB = document.createElement("B");

			//TAG A(nchor)
			Element sgmlA = document.createElement("A");
			sgmlA.setAttribute("HREF", "\"url\"");
			sgmlA.setAttribute("target", "new");


			Element depII = document.createElement("dsyntnode");
			depII.setAttribute("lexeme", "Mary");
			depII.setAttribute("class", "proper_noun");


			//Define Relationships
			dsynts.appendChild(governor);

			governor.appendChild(depI);
			depI.setAttribute("rel","I");

			governor.appendChild(sgmlB);

			sgmlB.appendChild(sgmlA);

			sgmlA.appendChild(depII);

			depII.setAttribute("rel","II");


			//Return the DSyntS
			return dsynts;
		}
		catch ( Exception e )
		{
			return null;
		}
	}



}