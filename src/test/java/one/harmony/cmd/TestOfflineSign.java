package one.harmony.cmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import one.harmony.account.Address;
import one.harmony.common.Config;
import one.harmony.keys.Store;
import one.harmony.rpc.HmyResponse;
import one.harmony.rpc.RPC;
import one.harmony.transaction.ChainID;
import one.harmony.transaction.Handler;
import one.harmony.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class TestOfflineSign {

    private static final Logger log = LoggerFactory.getLogger(TestOfflineSign.class);

    public static void main(String[] args) throws Exception {

        String node = Config.node;
        RPC rpc = new RPC(node);


//        String rawTx = "0x30786638366538303835303164636436353030303833303138366130383038303934353838316637383335373665653263303936386230623062323430313138363731343866636331613837353332323264306662653830303038303237613066363237353562376231303137363837666239393032323432613631356138373932656163613530636464323930333131626263636337656631626539313239613034333365363631323437333433353763326230373239656232663565323636386431366262333061386264643630626462336134643232306637393164393063";
//        HmyResponse hmyResponse = rpc.sendRawTransaction(rawTx).send();
//        if (hmyResponse.hasError()) {
//            System.out.println(hmyResponse.getError().getMessage());
//        }


        String from = "one1pdv9lrdwl0rg5vglh4xtyrv3wjk3wsqket7zxy";
        String to = "one1r49ggrfuawl3lnqqn2v40f8rk2fu3mrlv48uzg";
        String amount = "1.456";
        int fromShard = 0;
        int toShard = 0;

        HmyResponse response = rpc.getGasPrice().send();
        BigInteger gasPrice = Numeric.toBigInt(response.getResult());
        long gasLimit = 50000;
        BigInteger amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();

        HmyResponse nonceRes = rpc.getTransactionCount(from).send();
        if (nonceRes.hasError()) {
            System.out.println(nonceRes.getError());
            return;
        }
        long nonce = Long.parseLong(nonceRes.getResult().substring(2), 16);

        String fromAddressHex = new Address(from, false).getHexAddr();
        String toAddressHex = new Address(to, false).getHexAddr();
        Transaction transaction = new Transaction(fromAddressHex, nonce, toAddressHex, fromShard, toShard, amountWei, gasLimit, gasPrice, "".getBytes());

        WalletFile walletFile = Store.extractWalletFileFromAddress(from);
        String passphrase = "harmony-one";
        Credentials credentials = Credentials.create(Wallet.decrypt(passphrase, walletFile));

        int chainId = ChainID.TESTNET;
        Transaction signedTransaction = transaction.sign(chainId, credentials);
        log.info(String.format("signed transaction with chainId %d", chainId));
        ObjectMapper mapper = new ObjectMapper();
        log.info(mapper.writeValueAsString(signedTransaction));

        String txHexStr = new String(signedTransaction.getRawHash());
        byte[] txBytes = Numeric.hexStringToByteArray(txHexStr);
        String calTxId = Numeric.toHexString(Hash.sha3(txBytes));
        System.out.println("calTxId: " + calTxId);

        HmyResponse sendResponse = rpc.sendRawTransaction(new String(transaction.getRawHash())).send();
        if (sendResponse.hasError()) {
            throw new Exception(sendResponse.getError().getMessage());
        }
        System.out.println("txId: " + sendResponse.getResult());

    }

}
