export interface WebInfo {
  fiArea: string;
  compCode: string;
  ipAddress: string;
  paymentCenter: string;
  userWebOnline: string;

  authCompanyCode: string[];
  authCostCenter: string[];
  authFIArea: string[];
  authPaymentCenter: string[];

  defaultFiArea?: string;
  defaultCompCode?: string;
  defaultPaymentCenter?: string;
}
