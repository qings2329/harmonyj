package one.harmony.cmd;

import one.harmony.rpc.HmyResponse;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.http.HttpService;

import one.harmony.common.RequestTester;
import one.harmony.rpc.RPC;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BalanceRequestTest extends RequestTester {

    private RPC rpc;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        rpc = new RPC(httpService);
    }

    @Test
    public void testBalanceShard0Request() throws Exception {
        String oneAddress = "one1pdv9lrdwl0rg5vglh4xtyrv3wjk3wsqket7zxy";
//		String oneAddress = "one15vlc8yqstm9algcf6e94dxqx6y04jcsqjuc3gt";


        HmyResponse hmyResponse = rpc.getBalanceV2(oneAddress).send();
        if (hmyResponse.hasError()) {
            System.out.println(hmyResponse.getError().getMessage());
        } else {
            String hexStr = hmyResponse.getResult();
            System.out.println(convertBalance(hexStr));
        }

        HmyResponse hmyResponse1 = rpc.getBalance(oneAddress).send();
        if (hmyResponse1.hasError()) {
            System.out.println(hmyResponse1.getError().getMessage());
        } else {
            String hexStr = hmyResponse1.getResult();
            System.out.println(convertBalance(hexStr));
        }


//		verifyResult(
//				"{\"jsonrpc\":\"2.0\",\"method\":\"hmy_getBalance\",\"params\":[\"one1pdv9lrdwl0rg5vglh4xtyrv3wjk3wsqket7zxy\",\"latest\"],\"id\":0}");
    }

    BigDecimal multiple = new BigDecimal(BigInteger.TEN.pow(18));

    public BigDecimal convertBalance(String hexStr) {
        if (hexStr.startsWith("0x")) {
            hexStr = hexStr.substring(2);
        }
        BigInteger bigInteger = new BigInteger(hexStr, 16);
        BigDecimal balance = new BigDecimal(bigInteger).divide(multiple);
        return balance;
    }

}
