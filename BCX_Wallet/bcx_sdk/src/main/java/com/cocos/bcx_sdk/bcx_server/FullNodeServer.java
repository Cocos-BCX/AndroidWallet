package com.cocos.bcx_sdk.bcx_server;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_log.LogUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * @author ningkang.guo
 * @Date 2019/3/11
 */
public class FullNodeServer {

    /**
     * 获取链接成功的节点
     *
     * @return
     */
    public static String getConnectServers() {

        try {

            List<WebSocket> listWebSocket = new ArrayList<>();

            final Object objectSync = new Object();

            final int nTotalCount = CocosBcxApiWrapper.nodeUrls.size();

            final List<String> listSelectedServer = new ArrayList<>();

            for (final String strServer : CocosBcxApiWrapper.nodeUrls) {

                Request request = new Request.Builder().url(strServer).build();
                OkHttpClient okHttpClient = new OkHttpClient();

                WebSocket webSocket = okHttpClient.newWebSocket(request, new WebSocketListener() {
                    @Override
                    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                        super.onFailure(webSocket, t, response);
                        LogUtils.i("onFailure", t.getMessage());
                        synchronized (objectSync) {
                            listSelectedServer.add("");
                            if (listSelectedServer.size() == nTotalCount) {
                                objectSync.notify();
                            }
                        }
                    }

                    @Override
                    public void onOpen(WebSocket webSocket, Response response) {
                        super.onOpen(webSocket, response);
                        LogUtils.i("onOpen", response.message());
                        synchronized (objectSync) {
                            listSelectedServer.add(strServer);
                            objectSync.notify();
                        }
                    }
                });
                listWebSocket.add(webSocket);
            }

            String strResultServer = "";
            synchronized (objectSync) {
                if (!listSelectedServer.isEmpty() && listSelectedServer.size() < nTotalCount) {
                    for (String strServer : listSelectedServer) {
                        if (!strServer.isEmpty()) {
                            strResultServer = strServer;
                            break;
                        }
                    }
                }

                if (strResultServer.isEmpty()) {
                    try {
                        objectSync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!listSelectedServer.isEmpty() && listSelectedServer.size() < nTotalCount) {
                        for (String strServer : listSelectedServer) {
                            if (!strServer.isEmpty()) {
                                strResultServer = strServer;
                                break;
                            }
                        }
                    }
                }
            }
            for (WebSocket webSocket : listWebSocket) {
                webSocket.close(1000, "close");
            }
            return strResultServer;
        } catch (Exception e) {
            return "";
        }
    }

}
