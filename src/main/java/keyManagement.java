import java.math.BigInteger;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

public class keyManagement {

	
	  public static String addressFromKey(String key) {
		  BigInteger _key = new BigInteger(key.replace(" ", ""));
		  ECKeyPair ecKeyPair  = ECKeyPair.create(_key);

         WalletFile aWallet = null;
		try {
			aWallet = Wallet.createLight("n", ecKeyPair);
		} catch (CipherException e) {e.printStackTrace();}
          String sAddress = aWallet.getAddress();
          
          return sAddress;
	  }
	  
	  public static String addressFromKey(BigInteger key) {
		  ECKeyPair ecKeyPair  = ECKeyPair.create(key);

         WalletFile aWallet = null;
		try {
			aWallet = Wallet.createLight("n", ecKeyPair);
		} catch (CipherException e) {e.printStackTrace();}
          String sAddress = aWallet.getAddress();
          
          return sAddress;
	  }

}
