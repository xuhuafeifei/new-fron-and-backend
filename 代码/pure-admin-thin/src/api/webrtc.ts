export class RTCMessage {
  userId: string;
  roomId: string;
  data: any;
  cmd: string;
  remoteUserId: string;

  constructor() {
    this.roomId = "1";
  }
}

export const SIGNAL_TYPE_JOIN = "join";
export const SIGNAL_TYPE_RESP_JOIN = "resp-join"; // 告知加入者对方是谁
export const SIGNAL_TYPE_LEAVE = "leave";
export const SIGNAL_TYPE_NEW_PEER = "new-peer";
export const SIGNAL_TYPE_PEER_LEAVE = "peer-leave";
export const SIGNAL_TYPE_OFFER = "offer";
export const SIGNAL_TYPE_ANSWER = "answer";
export const SIGNAL_TYPE_CANDIDATE = "candidate";
