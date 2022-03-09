
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

public class Main  {

	public static Web3j web3j;// = Web3j.build(new HttpService("https://mainnet.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161"));
	
	public static String address;
	
	public static String name;// = "Ethereum";
	public static String symbol;// = "ETH";
	public static long chainID;// = 1;
	public static String explorer;// = "https://etherscan.io/";
	
	public static Console console = System.console();
	
	public static Scanner sc = new Scanner(System.in);
	
	public static String currentWorkingDir = System.getProperty("user.dir");
	
	
	public static void main(String[] args) throws Exception {
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            System.exit(0);
        }
        
		System.out.println("\n"
				+ "==================================================================================="
				+ "\nOrcania Wallet - Version 1.0.0"
				+ "\nWebsite: orcania.io/wallet"
				+ "\n===================================================================================\n");
		
		File keyFile = new File(".//key.txt");
		
		if(!keyFile.exists()) {createWallet();}
		else {System.out.println("Wallet available...Launching command line\n");}
		
		File addressFile = new File("address.txt");
		address = file.readAll(addressFile).trim();
		
		while(true) {
			try {
			System.out.print("\n" + name + "-> ");
			String[] argument = sc.nextLine().split(" ");
			
			if(argument[0].equalsIgnoreCase("open")) {
				long id = Long.parseLong(argument[1]);
				loadChain(id);
			}
			
			else if(argument[0].equalsIgnoreCase("add")) {
				
				if(argument[1].equalsIgnoreCase("token")) {
					addToken(argument[2]);
				}
				else if(argument[1].equalsIgnoreCase("chain")) {
					addChain(argument[2], argument[3], Long.parseLong(argument[4]), argument[5], argument[6]);
				}
				
			}
			
			else if(argument[0].equalsIgnoreCase("remove")) {
				
				if(argument[1].equalsIgnoreCase("token")) {
					removeToken(argument[2]);
				}
				else if(argument[1].equalsIgnoreCase("chain")) {
					removeChain(argument[2]);
				}
				
			}
			
			else if(argument[0].equalsIgnoreCase("transfer")) {
				if(argument[1].equalsIgnoreCase("token")) {
					String token = argument[2];
					
					BigInteger amount = new BigDecimal(args[4]).multiply(BigDecimal.valueOf(10).pow(Token.getDecimals(token).intValue())).toBigInteger();
					
					String txn = Write.transferToken(token,
							argument[3], //Recipient
							amount, 
							new BigInteger(argument[5]), //Gas limit
							new BigInteger(argument[6]).multiply(BI.GWEI)  //Gas Price
							);
					
					System.out.println("Transaction: " + explorer + "/tx/" + txn);
				}
				else if(argument[1].equalsIgnoreCase("coin")) {
					
					BigInteger amount = new BigDecimal(argument[3]).multiply(new BigDecimal(BI.ETH)).toBigInteger();
					
					String txn = Write.transferETH(
							argument[2], //Recipient
							amount, 
							new BigInteger(argument[4]).multiply(BI.GWEI)  //Gas Price
							);
					
					System.out.println("Transaction: " + explorer + "/tx/" + txn);
				}
			}
			
			else if(argument[0].equalsIgnoreCase("balance")) {
				getBalances();
			}
			
			else if(argument[0].equalsIgnoreCase("get")) {
				
				if(argument[1].equalsIgnoreCase("tokens")) {
					getAllTokens();
				}
				else if(argument[1].equalsIgnoreCase("chains")) {
					getAllChains();
				}
				
			}
			
			else if(argument[0].equalsIgnoreCase("address")) {
				File address_file = new File(".//address.txt");
				System.out.println("Your address: " + file.readAll(address_file));
			}
			
			else if(argument[0].equalsIgnoreCase("verify") && argument[1].equalsIgnoreCase("address")) {
				Main.console.printf("Input wallet's password to verify address (Password will not be visible for your own security)%n");
				String password = new String(Main.console.readPassword("Password: "));
				
				String address = null;
				try{ address = "0x" + keyManagement.addressFromKey(AES256.decrypt(file.readAll(keyFile).trim(), password)); } catch(Exception e) {
					System.out.println("Wrong password");	
					continue;
				}
				
				addressFile.delete();
				file.newFile(addressFile, address);
				
				System.out.println("Address reverified: " + address);
				
			}
			
			else {
				System.out.println("Invalid Command");
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Data missformat");
		}
			
		}
	}

	public static void createWallet() {
		System.out.println("You don't seem to have a wallet created yet, let's make one\n");
        console.printf("Input the password you would like to use for this wallet (Password will not be visible for your own security)%n");
        String password = new String(console.readPassword("Enter password: "));
        String password1 = new String(console.readPassword("Confirm password: "));
        
        if(!password.equals(password1)) {
        	System.out.println("Passwords do not match, try again \n");
        	createWallet();
        	return;
        }
        
		ECKeyPair ecKeyPair  = null;
		try {
			ecKeyPair = Keys.createEcKeyPair();
		} catch (InvalidAlgorithmParameterException e) {e.printStackTrace();} catch (NoSuchAlgorithmException e) {e.printStackTrace();} catch (NoSuchProviderException e) {e.printStackTrace();}

          WalletFile aWallet = null;
		try {
			aWallet = Wallet.createLight("n", ecKeyPair);
		} catch (CipherException e) {e.printStackTrace();}
		address = "0x" + aWallet.getAddress();
		
		File keyFile = new File(".//key.txt");
		file.newFile(keyFile, AES256.encrypt(ecKeyPair.getPrivateKey().toString(), password));
		keyFile.setReadOnly();
		
		File addressFile = new File(".//address.txt");
		file.newFile(addressFile, address);
		addressFile.setReadOnly();
		
	}

	public static void getBalances() {
		String output = "\nYour Balance: \n"
				+ name + ": " + getETHbalance() + " " + symbol + "\n";
		
		System.out.println("Fetching Tokens...");
		
		File tokens = new File(chainID + "\\tokens\\");
		File[] listOfTokens = tokens.listFiles();
		
		if(listOfTokens == null) {
			System.out.println(output);
			return;
		}
		
		String[] tokensAddresses = new String[listOfTokens.length];
		int index = 0;
		for(File token: listOfTokens) {
			tokensAddresses[index++] = token.getName();
		}
		
		for(String token: tokensAddresses) {
			BigDecimal balance = Token.getBalance(token, address);;
			output += Token.getName(token) + ": " + balance + " " + Token.getSymbol(token) + "\n";
		}
		
		System.out.println(output);
		
	}
	
	public static void loadChain(long id) {
		System.out.println("Loading chain " + id + "...");
		
    	JSONObject data = loadFile(id + "\\data.json");
    	if(data == null) {return;}
		
		web3j = Web3j.build(new HttpService(data.get("rpc").toString()));
		name = data.get("name").toString();
		symbol = data.get("symbol").toString();
		chainID = Long.parseLong(data.get("chainID").toString());
		explorer = data.get("explorer").toString();
		
		System.out.println("Loaded chain " + id);
	}
	
	public static void addToken(String tokenAddress) {
		File token = new File(chainID + "\\tokens\\" + tokenAddress);
		try {token.createNewFile();} catch (IOException e) {e.printStackTrace();}
		System.out.println("Token added");
	}
	
	public static void removeToken(String tokenAddress) {
		File token = new File(chainID + "\\tokens\\" + tokenAddress);
		if(token.delete()) {System.out.println("Token removed");}
		else {System.out.println("Token not found");}
	}
	
	public static void addChain(String name, String rpc, long id, String symbol, String explorer) {
		File chain = new File(id + "");
		if(!chain.mkdir()) {System.out.println("Chain already exists"); return;}
		
		JSONObject json = new JSONObject();
    	
		json.put("name", name);
		json.put("rpc", rpc);
		json.put("chainID", id);
		json.put("symbol", symbol);
		json.put("explorer", explorer);
		
		File data = new File(id + "\\data.json");
		file.newFile(data, json);
		
		System.out.println("Chain added");
	}
	
	public static void removeChain(String id) {
		File chain = new File(id);
		//chain.
		chain.delete();
		
		System.out.println("Chain removed");
	}
	
	public static BigDecimal getETHbalance() {
		BigDecimal decimals = new BigDecimal(BI.TEN.pow(18));
		BigDecimal balance = null;
		
		try {
			EthGetBalance ethGetBalance = web3j
					.ethGetBalance(address, DefaultBlockParameterName.LATEST)
					.sendAsync()
					.get();
			
			balance = new BigDecimal(ethGetBalance.getBalance());
			balance = balance.divide(decimals);
			
		} catch (Exception e) {System.out.println("Error");e.printStackTrace();}
		
		return balance;	
	}
	
	public static void getAllChains() {
		System.out.println("Loading chains...");
		
		String output = "\nYour imported chains: \n";
		File thisFile = new File(currentWorkingDir);
		File[] allFiles = thisFile.listFiles();
		
		for(File fl : allFiles) {
			if(fl.isDirectory()) {
				long id;
				try {id = Long.parseLong(fl.getName());} catch(Exception e) {continue;}
				
				JSONObject data = loadFile(id + "\\data.json");
				
				String rpc = data.get("rpc").toString();
				String name = data.get("name").toString();
				String symbol = data.get("symbol").toString();
				String chainID = data.get("chainID").toString();
				String explorer = data.get("explorer").toString();
				
				output += "\n" + name + " " + symbol
						+ "\nRPC: " + rpc
						+ "\nExplorer: " + explorer
						+ "\nChain ID: " + chainID + "\n";
			}
		}
		
		System.out.println(output);
	}
	
	public static void getAllTokens() {
		System.out.println("Loading tokens...");
		
		String output = "\nYour imported tokens: \n";
		File thisFile = new File(chainID + "\\tokens");
		File[] allFiles = thisFile.listFiles();
		
		if(allFiles == null) {System.out.println("You have no imported tokens on " + name); return;}
		
		for(File fl : allFiles) {
			if(!fl.isDirectory()) {
				String address;
				try {address = fl.getName();} catch(Exception e) {continue;}
				
				output += "\n" + Token.getName(address) + " " + Token.getSymbol(address)
						+ "\nAddress: " + address
						+ "\nBalance: " + Token.getBalance(address, Main.address)
						+ "\n";
			}
		}
		
		System.out.println(output);
	}
	
	
	public static JSONObject loadFile(String jsonFile) {
    	JSONParser parser = new JSONParser(); 
    	JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(new FileReader(jsonFile));
		} catch (FileNotFoundException e) {
			System.out.println("Invalid File");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return json;

    }


}
