export interface Message {
  id: number;
  messageId: string;
  content: string;
  receivedAt: string;
  direction: 'INBOUND' | 'OUTBOUND';
}