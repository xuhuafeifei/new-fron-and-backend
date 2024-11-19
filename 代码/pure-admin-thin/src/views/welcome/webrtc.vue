<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  RTCMessage,
  SIGNAL_TYPE_NEW_PEER,
  SIGNAL_TYPE_CANDIDATE,
  SIGNAL_TYPE_OFFER,
  SIGNAL_TYPE_JOIN,
  SIGNAL_TYPE_ANSWER
} from "@/api/webrtc.ts";

onMounted(() => {
  localPlayer.value = document.querySelector("#local-video");
  remotePlayer.value = document.querySelector("#remote-video");
});

const userId = ref(Math.random().toString(36).substr(2));
console.log("userId = " + userId.value);
// sessionStorage.setItem("userId", ); // 本地uid

// 本地播放器
const localPlayer = ref<HTMLVideoElement>();
// 本地视频流
const localStream = ref<MediaStream>();

/** 远程 */
// 远程播放器
const remotePlayer = ref<HTMLVideoElement>();
// 远程视频流
const remoteStream = ref<MediaStream>();

// rtc websocket
const rtcWS = new WebSocket("ws://localhost:8010/api_demo/ws3/" + userId.value);
// peerConnection
const peerConnection = ref<RTCPeerConnection>();

rtcWS.onmessage = ev => {
  // console.log(ev.data);
  const rtcMsg: RTCMessage = JSON.parse(ev.data);
  if (rtcMsg.cmd === SIGNAL_TYPE_NEW_PEER) {
    handleRemoteNewPeer();
  } else if (rtcMsg.cmd === SIGNAL_TYPE_OFFER) {
    handleRemoteOffer(rtcMsg);
  } else if (rtcMsg.cmd === SIGNAL_TYPE_ANSWER) {
    handleRemoteAnswer(rtcMsg);
  } else if (rtcMsg.cmd === SIGNAL_TYPE_CANDIDATE) {
    handleRemoteCandidate(rtcMsg);
  }
};

rtcWS.onopen = ev => {
  console.log(ev);
  console.log("建立websocket");
};

/**
 * 接收远端发送的candidate信息, 然后发送自己的candidate
 */
const handleRemoteCandidate = (rtcMsg: RTCMessage) => {
  // peerConnection.value.onicecandidate = handleIceCandidate;
  console.log("handleRemoteCandidate...");
  console.log(rtcMsg);

  const candidate = new RTCIceCandidate(rtcMsg.data);
  peerConnection.value
    .addIceCandidate(candidate)
    .then(e => {
      console.log("candidate添加成功");
      console.log(e);
    })
    .catch(error => {
      console.log("candidate添加失败");
      console.error(error);
    });
};

/**
 * 处理对端发送的answer
 */
const handleRemoteAnswer = (rtcMsg: RTCMessage) => {
  // peerConnection.value.onicecandidate = handleIceCandidate;
  console.log("handleRemoteAnswear");
  console.log(rtcMsg);

  // 设置对端的offer信息
  peerConnection.value
    .setRemoteDescription(rtcMsg.data)
    .then(e => {
      console.log("对端answer添加成功");
      console.log(e);
    })
    .catch(error => {
      console.error("对端answer添加失败");
      console.error(error);
    });
};

/**
 * 接收对端发送的offer, 处理并交换本地offer(answer)
 */
const handleRemoteOffer = async (message: RTCMessage) => {
  // debug
  console.log("handleRemoteOffer...");

  // 接收对端发送的offer
  const offer = message.data;
  console.log("远端offer");
  console.log(offer);
  peerConnection.value
    .setRemoteDescription(offer)
    .then(e => {
      console.log("远端offer添加成功");
      console.log(e);
    })
    .catch(error => {
      console.error("远端offer添加失败");
      console.error(error);
    });

  const answer = await peerConnection.value.createAnswer();
  createAnswerAndSendMessage(answer);
};

/**
 * 发送answer
 */
const createAnswerAndSendMessage = answer => {
  const rtcMsg = new RTCMessage();
  rtcMsg.userId = userId.value;
  rtcMsg.data = answer;
  rtcMsg.cmd = SIGNAL_TYPE_ANSWER;

  // debug
  console.log("发送answer");
  console.log(rtcMsg);

  rtcWS.send(JSON.stringify(rtcMsg));
};

/**
 * 创建peerConnection, 发送offer
 */
const handleRemoteNewPeer = async () => {
  console.log("handleRemoteNewPeer...");
  // 創建offer, 然后设置LocalDescription
  const offer = await peerConnection.value.createOffer();
  createOfferAndSendMessage(offer);
};

/**
 * 创建peerConnection
 */
const createPeerConnection = async () => {
  peerConnection.value = new RTCPeerConnection({
    iceServers: [
      { urls: "stun:www.xhf.icu:3478" }, // 谷歌的公共服务
      {
        urls: "turn:www.xhf.icu:3478",
        credential: "NDkc6880",
        username: "xhf"
      }
    ]
  });
  peerConnection.value.onicecandidate = handleIceCandidate;
  peerConnection.value.ontrack = handleRemoteStreamAdd;

  // 把本地流设置给RTCPeerConnection
  for (const track of localStream.value.getTracks()) {
    peerConnection.value.addTrack(track);
  }

  // debug
  console.log("创建pc成功");
  console.log(peerConnection.value);
};

/**
 * 回调函数, 发送Candidate
 */
const handleIceCandidate = event => {
  console.log("iceCandidate...");
  console.log(event.candidate);
  if (event.candidate) {
    const candidateJson = {
      sdpMLineIndex: event.candidate.sdpMLineIndex,
      sdpMid: event.candidate.sdpMid,
      candidate: event.candidate.candidate
    };
    const rtcMsg = new RTCMessage();
    rtcMsg.cmd = SIGNAL_TYPE_CANDIDATE;
    rtcMsg.data = candidateJson;
    rtcMsg.userId = userId.value;
    const message = JSON.stringify(rtcMsg);
    // debug
    console.log("发送candidate");
    console.log(message);

    // 发送candidate
    rtcWS.send(message);
  } else {
    console.log("没有候选...");
  }
};

/**
 * 回调函数, 自动添加远程的视频流
 */
function handleRemoteStreamAdd(event) {
  console.log("handleRemoteStreamAdd");
  console.log(event.streams);
  setTimeout(() => {
    remotePlayer.value.srcObject = event.streams[0];
  }, 500);
  remotePlayer.value.play();
}

/**
 * 发送offer
 */
async function createOfferAndSendMessage(offer) {
  await peerConnection.value.setLocalDescription(offer);

  const rtcMsg = new RTCMessage();
  rtcMsg.cmd = SIGNAL_TYPE_OFFER;
  rtcMsg.userId = userId.value;
  rtcMsg.data = offer;
  const message = JSON.stringify(rtcMsg);

  // debug
  console.log("发送offer");
  console.log(rtcMsg);

  rtcWS.send(message);
}

const constraints = (window.constraints = {
  audio: true,
  video: true
});

const join = async () => {
  console.log("join");
  // 获取本地摄像流
  const stream = await navigator.mediaDevices.getUserMedia(constraints);
  localPlayer.value.srcObject = stream;
  localStream.value = stream;
  createPeerConnection();

  // 创建消息
  const msg = new RTCMessage();
  msg.userId = userId.value;
  msg.cmd = SIGNAL_TYPE_JOIN;

  // 加入房间
  rtcWS.send(JSON.stringify(msg));
};
</script>

<template>
  <h1>WebRTC</h1>
  <el-button @click="join">加入</el-button>
  <!-- 本地播放 -->
  <video id="local-video" autoplay playsinline />
  <!-- 远程播放 -->
  <video id="remote-video" autoplay playsinline />
</template>
