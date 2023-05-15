package com.edgesdk.managers;

import android.util.Log;

import com.edgesdk.EdgeSdk;
import com.edgesdk.Utils.Constants;
import com.edgesdk.Utils.LogConstants;
import com.edgesdk.Utils.Urls;
import com.edgesdk.Utils.Utils;
import com.edgesdk.models.CreateMessage;
import com.edgesdk.models.Poll_Answer;
import com.edgesdk.models.Poll_Question;
import com.edgesdk.models.SetScreenMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.ws.client.ThreadType;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class LiveGamificationSocketManager implements Runnable{
    private Future threadHandler;
    private static EdgeSdk edgeSdk;
    private static WebSocket ws;
    private static Boolean isSelfDisconnected;
    private Map<String,Poll_Question> pollQuestionList;
    private Map<String,Poll_Answer> pollAnswerList;
    private static boolean isGameGoingOn;
    public LiveGamificationSocketManager(EdgeSdk edgeSdk) {
        this.edgeSdk = edgeSdk;
        this.setSelfDisconnected(false);
//        this.pollQuestionList = new HashMap<>();
//        this.pollAnswerList = new HashMap<>();
        this.pollQuestionList = new LinkedHashMap<>();
        this.pollAnswerList = new LinkedHashMap<>();
        this.isGameGoingOn=false;
    }

    public Future getThreadHandler() {
        return threadHandler;
    }

    public void setThreadHandler(Future threadHandler) {
        this.threadHandler = threadHandler;
    }

    public WebSocket getWs() {
        return ws;
    }

    public Boolean getSelfDisconnected() {
        return isSelfDisconnected;
    }

    public void setSelfDisconnected(Boolean selfDisconnected) {
        isSelfDisconnected = selfDisconnected;
    }

    public  boolean isIsGameGoingOn() {
        return isGameGoingOn;
    }

    public  void setIsGameGoingOn(boolean isGameGoingOn) {
        LiveGamificationSocketManager.isGameGoingOn = isGameGoingOn;
    }

    public Map<String, Poll_Question> getPollQuestionList() {
        return pollQuestionList;
    }

    public void setPollQuestionList(Map<String, Poll_Question> pollQuestionList) {
        this.pollQuestionList = pollQuestionList;
    }

    public Map<String, Poll_Answer> getPollAnswerList() {
        return pollAnswerList;
    }

    public void setPollAnswerList(Map<String, Poll_Answer> pollAnswerList) {
        this.pollAnswerList = pollAnswerList;
    }

    public void initWebSocket(){
        try {
            this.ws=null;
            this.ws = new WebSocketFactory().createSocket(Urls.LIVE_GAMIFICATION_SOCKET_SERVER,Integer.MAX_VALUE);
            this.ws.setPingInterval(3000);
            this.ws.addListener(new WebSocketListener() {
                @Override
                public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
                    Log.i(LogConstants.Live_Gamification,"Live gamification socket state:"+newState);
                }

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    Log.i(LogConstants.Live_Gamification,"Live gamification socket opened");
                }

                @Override
                public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {

                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    Log.i(LogConstants.Live_Gamification,"Unfortunately Live gamification socket server is disconnected");
                    if(!isSelfDisconnected){
                        //it is disconnected automatically so restart.
                        edgeSdk.startLiveGamificationManager();
                    }else{
                        Log.i(LogConstants.Live_Gamification,"Not retrying to Live gamification socket server bcz its self closed");
                    }
                }

                @Override
                public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    if(!isSelfDisconnected){
                        edgeSdk.stopLiveGamificationManager();
                        edgeSdk.startLiveGamificationManager();
                    }
                }

                @Override
                public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onTextMessage(WebSocket websocket, String socketResponse) throws Exception {
                    //We will socket messages here.
                    Log.i(LogConstants.Live_Gamification,"Live gamification socket response : "+socketResponse);
                    String responseType = Utils.trimStartingAndEndingCommas(Utils.parser(socketResponse).get("type").toString());
                    setIsGameGoingOn(true);
                    if(responseType.equals("poll")){
                        try {
                            Log.i(LogConstants.Live_Gamification, "Adding new question");
                            Poll_Question poll_question = new Poll_Question();
                            String poll = Utils.parser(socketResponse).get("poll").toString();
                            String type = responseType;
                            int mode = Utils.parser(socketResponse).get("mode").intValue();
                            JsonNode correct=null;
                            int correctAnswer[] = new int[1];
                            if(mode==3 || mode==2){
                                correct = Utils.parser(socketResponse).get("correct");
                                ObjectMapper objectMapper = new ObjectMapper();
                                correctAnswer = objectMapper.treeToValue(correct, int[].class);
                            }
                            String explanation = Utils.parser(socketResponse).get("explanation").toString();
                            long id = Utils.parser(socketResponse).get("id").longValue();
                            JsonNode choices = Utils.parser(socketResponse).get("choices");
                            ObjectMapper objectMapper = new ObjectMapper();
                            String[] choicesArray = objectMapper.treeToValue(choices, String[].class);
                            poll_question.setChoices(choicesArray);
                            poll_question.setCreated(explanation);
                            poll_question.setPoll(poll);
                            poll_question.setMode(mode);
                            poll_question.setType(type);
                            poll_question.setId(id);
                            poll_question.setCorrect(correctAnswer);
                            pollQuestionList.put(id+"",poll_question);
                            Log.i(LogConstants.Live_Gamification,"Adding question with id : "+id);
                            Log.i(LogConstants.Live_Gamification,"Question : "+pollQuestionList.get(id+"").getPoll());

                        }catch (Exception e){
                            Log.i(LogConstants.Live_Gamification,e.getMessage());
                        }
                        }else if(responseType.equals("resolve")){
                        Poll_Answer poll_answer = new Poll_Answer();
                        String type = responseType;
                        String explanation = Utils.parser(socketResponse).get("explanation").toString();
                        long id = Utils.parser(socketResponse).get("id").longValue();
                        JsonNode correct = Utils.parser(socketResponse).get("correct");
                        ObjectMapper objectMapper = new ObjectMapper();
                        int[] correctAnswer = objectMapper.treeToValue(correct, int[].class);
                        poll_answer.setCorrect(correctAnswer);
                        poll_answer.setExplanation(explanation);
                        poll_answer.setId(id);
                        poll_answer.setType(type);
                        pollAnswerList.put(id+"",poll_answer);
                        Log.i(LogConstants.Live_Gamification,"Poll Answer:"+id);

                    }
                }

                @Override
                public void onTextMessage(WebSocket websocket, byte[] data) throws Exception {

                }

                @Override
                public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

                }

                @Override
                public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onThreadCreated(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

                }

                @Override
                public void onThreadStarted(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

                }

                @Override
                public void onThreadStopping(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

                }

                @Override
                public void onError(WebSocket websocket, WebSocketException cause) throws Exception {

                }

                @Override
                public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {

                }

                @Override
                public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {

                }

                @Override
                public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {

                }

                @Override
                public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {

                }

                @Override
                public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {

                }

                @Override
                public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ws.setPingInterval(3000);

    }

    public void removePollFromPollQuestionList(int id){
        pollQuestionList.remove(id+"");
    }
    public void removePollFromPollAnswerList(int id){
        pollAnswerList.remove(id+"");
    }

    @Override
    public void run() {
        if(!this.ws.isOpen()) {
            try {
                this.ws.connect();
            } catch (WebSocketException e) {
                e.printStackTrace();
                Log.e(LogConstants.Live_Gamification, "New- Could not open Live gamification socket because :" + e.getMessage());
            }
            Log.i(LogConstants.Live_Gamification,"New- Successfully opened Live gamification socket");
            while (true) {
                if (threadHandler != null) {
                    if (threadHandler.isCancelled() || threadHandler.isDone() || !ws.isOpen()) {
                        break;
                    }
                }
                try {Thread.sleep(2000);} catch (Exception e) {}
            }

        }else {
            Log.e(LogConstants.Live_Gamification,"Could not open Live gamification socket because it is already open");
        }
    }
}
