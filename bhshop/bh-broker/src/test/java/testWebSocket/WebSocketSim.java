package testWebSocket;

import com.bh.utils.requestTool.RequestTool;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/26.
 */
public class WebSocketSim {
    private static Logger logger = LoggerFactory.getLogger(WebSocketSim.class);
    private static WebSocketClient client;
    private static String period = "bhshop-21536-118";
  

    public static void main(String[] args) throws URISyntaxException {

        URI uri = new URI("wss://bhmall.zhiyesoft.cn/bh-broker/websocket/auctionBidServer/" + period);
        // {"username":"13682220372","password":"yuanchun520"}

        Map<String, String> header = getLoginHeader("13682220372", "yuanchun520");
        client = new WebSocketClient(uri, new Draft_6455(), header) {
            @Override
            public void onOpen(ServerHandshake arg0) {
                System.out.println("打开链接");
                for (int i = 0; i < 1000000; i++) {
                    try {
                        Thread.currentThread().sleep(11);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String bidMsg = "{'tag':'" + period + "','key':'','msgBody':'{\"auctionPrice\":\"" + String.valueOf(100 + i) + "\"}'}";
                    logger.debug("bidMsg=" + bidMsg);
                    client.send(bidMsg);
                }
            }

            @Override
            public void onMessage(String arg0) {
                System.out.println("收到消息" + arg0);
            }

            @Override
            public void onError(Exception arg0) {
                arg0.printStackTrace();
                System.out.println("发生错误已关闭");
            }

            @Override
            public void onClose(int arg0, String arg1, boolean arg2) {
                System.out.println("链接已关闭");
            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                try {
                    System.out.println(new String(bytes.array(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };

        client.connect();
        while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            System.out.println("还没有打开");
        }
    }


    public static Map<String, String> getLoginHeader(String userName, String passwd) {
        Map<String, String> header = new HashMap<String, String>();
        RequestTool rtLogin = new RequestTool();
        rtLogin.setUrl("https://bhmall.zhiyesoft.cn/bh-user-api/login.json");
        rtLogin.getHeader().put("Content-Type", "application/json;charset=UTF-8");
        rtLogin.getParams().put("data", "{\"username\":\"" + userName + "\",\"password\":\"" + passwd + "\"}");
        String pageLogin = rtLogin.postSimulate("UTF-8");
        String cookie = "DTL_SESSION_ID=" + rtLogin.getOutCookies().get("DTL_SESSION_ID");
        header.put("Cookie", cookie);//set cookie
        return header;
    }
}
