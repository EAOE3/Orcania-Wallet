

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;


public class Write {

	  public static String functionExecute(String encodedFunction, String contractAddress, BigInteger GAS_LIMIT, BigInteger GAS_PRICE, BigInteger Value){
		    Credentials creds = getCredentials();
		    RawTransactionManager manager = new RawTransactionManager(Main.web3j, creds, Main.chainID);
		    //RawTransaction test = RawTransaction.createTransaction(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, Main.beaconContract, encodedFunction);
		    EthSendTransaction transaction = null;
			try {
				//transaction = manager.sendTransactionEIP1559(GAS_PRICE1, GAS_PRICE, GAS_LIMIT, Main.beaconContract, encodedFunction, null);
				transaction = manager.sendTransaction(GAS_PRICE, GAS_LIMIT, contractAddress, encodedFunction, Value);
				//transaction = manager.sendTransactionEIP1559(GAS_PRICE1, GAS_PRICE, GAS_LIMIT, Main.beaconContract, encodedFunction, Value);
				//Main.web3j.eth.getTransactionReceipt(hash [, callback]);
				if(transaction.getError() != null)
				    System.out.println(transaction.getError().getMessage());
				//transaction.get
				/*TransactionReceipt transactionReceipt = Transfer.sendFundsEIP1559(

				        Main.web3j, keyManagement.getCredentialsFromPrivateKey(),

				        "0x<address>|<ensName>", //toAddress

				        BigDecimal.ONE.valueOf(1), //value

				        Convert.Unit.ETHER, //unit

				        BigInteger.valueOf(8_000_000), //gasLimit

				        DefaultGasProvider.GAS_LIMIT,//maxPriorityFeePerGas

				        BigInteger.valueOf(3_100_000_000L)//maxFeePerGas

				  ).send();*/
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
		    try{return transaction.getTransactionHash();} catch(NullPointerException e) {return null;}
		}

	  
	  public static String transferToken(String contractAddress, String recipient, BigInteger amount, BigInteger GAS_LIMIT, BigInteger GAS_PRICE) {
		    Function function = new Function(
		            "transfer",  // function we're calling
		            Arrays.asList(new Address(recipient), new Uint256(amount)),   // Parameters to pass as Solidity Types
		            Arrays.asList());
		    String encodedFunction = FunctionEncoder.encode(function);
			return functionExecute(encodedFunction, contractAddress, GAS_LIMIT, GAS_PRICE, BI.ZERO);	
	  }
	  
	  public static String transferETH(String to_address, BigInteger amount_ether, BigInteger GAS_PRICE) throws Exception {
			TransactionManager tm = new RawTransactionManager(Main.web3j, getCredentials(), Main.chainID);
			EthSendTransaction tr = tm.sendTransaction(GAS_PRICE, BI.THOUSAND_21, to_address, "", amount_ether);
			
			if(tr.getError() != null) System.out.println(tr.getError().getMessage());

			return tr.getTransactionHash();
		}
	  
	  //Failed
	  /*public static String transferETHEIP1559(String to_address, BigInteger amount_ether, BigInteger GAS_PRICE, BigInteger PRIORITY_FEE) throws Exception {
		  TransactionManager tm = new RawTransactionManager(Main.web3j, keyManagement.getCredentialsFromPrivateKey(), 4);
		  EthSendTransaction tr = tm.sendTransactionEIP1559(PRIORITY_FEE, GAS_PRICE, BI.THOUSAND_21, to_address, "0x0000", amount_ether);

		  if(tr.getError() != null) System.out.println(tr.getError().getMessage());
		  
		  System.out.println("'" + tr.getTransactionHash() + "'");
		  return tr.getTransactionHash();
		}*/
	  
	  public static Credentials getCredentials() {
		  File keyFile = new File(".//key.txt");
		  Main.console.printf("Input wallet's password to sign transaction (Password will not be visible for your own security)%n");
		  String password = new String(Main.console.readPassword("Password: "));
		  
		  BigInteger key;
		  try{ key = new BigInteger(AES256.decrypt(file.readAll(keyFile).trim(), password)); } catch(Exception e) {
			  System.out.println("Wrong password, try again");
			  return getCredentials();
		  }
		  
		  ECKeyPair ecKeyPair = ECKeyPair.create(key);
		  return Credentials.create(ecKeyPair);
		}
	  
	 
	  
}
