package one.harmony.token;

import one.harmony.cmd.Contract;
import one.harmony.common.Config;
import one.harmony.transaction.ChainID;
import one.harmony.transaction.Handler;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;


public class Erc20Token extends Contract {

    public static final String FUNC_NAME = "name";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_TRANSFER = "transfer";

    protected Erc20Token(String contractBinary, String contractAddress, ContractGasProvider gasProvider, Handler handler) {
        super(contractBinary, contractAddress, handler, gasProvider);
    }


    public RemoteFunctionCall<String> name() {
        final Function function = new Function(FUNC_NAME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String account) {
        final Function function = new Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(new Address(account)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String recipient, BigInteger amount) {
        final Function function = new Function(
                FUNC_TRANSFER,
                Arrays.<Type>asList(new Address(recipient),
                        new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }


    public static void main(String args[]) throws Exception {
        String contractAddress = "one1ctppqspq688anm80uq29gyut04tpzjnfff04e2";
        String hexContractAddr = new one.harmony.account.Address(contractAddress, contractAddress.startsWith("0x")).getHexAddr();

        String accountAddress = "one18t4yj4fuutj83uwqckkvxp9gfa0568uc48ggj7";
        String hexAccountAddr = new one.harmony.account.Address(accountAddress, accountAddress.startsWith("0x")).getHexAddr();

        Handler handler = new Handler(Config.node, ChainID.TESTNET);
        StaticGasProvider gasProvider = new StaticGasProvider(new BigInteger("0"), new BigInteger("0"));
        Erc20Token erc20Token = new Erc20Token("", hexContractAddr, gasProvider, handler);
        BigInteger balance = erc20Token.balanceOf(hexAccountAddr).send();
        System.out.println(balance);
    }


}
