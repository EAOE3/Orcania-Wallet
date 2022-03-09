import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.util.FastHex;


public class Token {
	
	public static String getName(String tokenAddress) {
		com.esaulpaugh.headlong.abi.Function f = com.esaulpaugh.headlong.abi.Function.fromJson("{\"inputs\":[],\"name\":\"name\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"}");
		
		Tuple args = new Tuple();
		ByteBuffer one = f.encodeCall(args);

		EthCall ethCall = null;
		try {ethCall = Main.web3j.ethCall(Transaction.createEthCallTransaction(Main.address, tokenAddress, Hex.toHexString(one.array())),DefaultBlockParameterName.LATEST).send();} catch (IOException e) {e.printStackTrace(); }
		
		Tuple decoded = f.decodeReturn(
		        FastHex.decode(ethCall.getValue().substring(2, ethCall.getValue().length()))
		);

		return decoded.get(0).toString();
	}
	
	public static String getSymbol(String tokenAddress) {
		com.esaulpaugh.headlong.abi.Function f = com.esaulpaugh.headlong.abi.Function.fromJson("{\"inputs\":[],\"name\":\"symbol\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"}");
		
		Tuple args = new Tuple();
		ByteBuffer one = f.encodeCall(args);

		EthCall ethCall = null;
		try {ethCall = Main.web3j.ethCall(Transaction.createEthCallTransaction(Main.address, tokenAddress, Hex.toHexString(one.array())),DefaultBlockParameterName.LATEST).send();} catch (IOException e) {e.printStackTrace(); }
		
		Tuple decoded = f.decodeReturn(
		        FastHex.decode(ethCall.getValue().substring(2, ethCall.getValue().length()))
		);

		return decoded.get(0).toString();
	}
	
	public static BigInteger getDecimals(String tokenAddress) {
		Function function = new Function(
				"decimals",  // function we're calling
		         Arrays.asList(),   // Parameters to pass as Solidity Types
		         Arrays.asList(new TypeReference<Uint256>() {}));
		String encodedFunction = FunctionEncoder.encode(function);
		EthCall ethCall = null;
		try {ethCall = Main.web3j.ethCall(Transaction.createEthCallTransaction(Main.address, tokenAddress, encodedFunction),DefaultBlockParameterName.LATEST).send();} catch (IOException e) {e.printStackTrace(); }

		return  new BigInteger(ethCall.getValue().substring(2, 66), 16);	
	}
	
	public static BigDecimal getBalance(String tokenAddress, String userAddress) {
		Function function = new Function(
				"balanceOf",  // function we're calling
		         Arrays.asList(new Address(userAddress)),   // Parameters to pass as Solidity Types
		         Arrays.asList(new TypeReference<Uint256>() {}));
		String encodedFunction = FunctionEncoder.encode(function);
		EthCall ethCall = null;
		try {ethCall = Main.web3j.ethCall(Transaction.createEthCallTransaction(userAddress, tokenAddress, encodedFunction),DefaultBlockParameterName.LATEST).send();} catch (IOException e) {e.printStackTrace(); }

		return  new BigDecimal(new BigInteger(ethCall.getValue().substring(2, 66), 16)).divide(BigDecimal.valueOf(10).pow(getDecimals(tokenAddress).intValue()));	
	}
	  

	  
}
