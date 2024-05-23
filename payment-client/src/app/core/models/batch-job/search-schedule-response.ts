import { PaymentAlias } from '@core/models/batch-job/payment-alias';

export interface SearchScheduleResponse {
  id: any;
  paymentAliasId: any;
  paymentAlias: PaymentAlias;
  paymentName: string;
  paymentType: number;
  state: string;
  status: string;
  triggerAtInMillis: any;
  updated: any;
  created: any;
  createdBy: string;
  jobDate: Date;
  jobType: string;
}
