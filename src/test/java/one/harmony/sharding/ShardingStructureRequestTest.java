package one.harmony.sharding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.harmony.rpc.ShardingStructure;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.http.HttpService;

import one.harmony.common.RequestTester;
import one.harmony.rpc.RPC;

public class ShardingStructureRequestTest extends RequestTester {

	private RPC rpc;

	@Override
	protected void initWeb3Client(HttpService httpService) {
		rpc = new RPC(httpService);
	}

	@Test
	public void testShardingStructureRequest() throws Exception {
		ShardingStructure shardingStructure = rpc.getShardingStructure().send();
//		ObjectMapper objectMapper = new ObjectMapper();
//		System.out.println(objectMapper.writeValueAsString(result));

		System.out.println(shardingStructure.getResult());

//		verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"hmy_getShardingStructure\",\"params\":[],\"id\":0}");
	}

}
