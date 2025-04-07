export interface PartnerRequestDTO {
    alias: string;
    id: string;
    type: string;
    direction: 'INBOUND' | 'OUTBOUND';
    application: string;
    processedFlowType: 'MESSAGE' | 'ALERTING' | 'NOTIFICATION';
    description: string;
  
  }