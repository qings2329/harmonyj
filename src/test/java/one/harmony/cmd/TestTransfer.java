package one.harmony.cmd;

import one.harmony.common.Config;
import one.harmony.rpc.HmyResponse;
import one.harmony.rpc.RPC;
import one.harmony.transaction.ChainID;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class TestTransfer {
	private static final int LOCAL_NET = 2;

	public static void testImportPrivateKey() throws Exception {
		Keys.cleanKeyStore();
		String key = "fd416cb87dcf8ed187e85545d7734a192fc8e976f5b540e9e21e896ec2bc25c3";
		String accountName = "a1";
		Keys.importPrivateKey(key, accountName);
	}

	public static void main(String[] args) throws Exception {
//		testImportPrivateKey();

		String from = "one1pdv9lrdwl0rg5vglh4xtyrv3wjk3wsqket7zxy";
		String to = "one17qf920fn7f7pzl8ts7ypk8243kg6hzn8m4mne6";
		String amount = "1.234";
		int fromShard = 0;
		int toShard = 0;
		String memo = "0x5061796d656e7420666f722078797a";
		// Create transfer object
		String node = Config.node;
		RPC rpc = new RPC(node);
		HmyResponse response = rpc.getGasPrice().send();
		BigInteger gasPrice = Numeric.toBigInt(response.getResult());
		long gp = gasPrice.divide(BigInteger.TEN.pow(9)).longValue();
		Transfer t = new Transfer(from, to, amount, gp, fromShard, toShard);
		// Prepare transfer
		String passphrase = "harmony-one";
		t.prepare(passphrase, node); // offline, no need to connect to network
		// Execute transfer
		boolean dryRun = false;
		int waitToConfirmTime = 0;
		t.SetGas(50000);
		String txHash = t.execute(ChainID.DEV, dryRun, waitToConfirmTime); // needs connection to network
		// Keys.cleanKeyStore();
		System.out.println(txHash);
	}



}
